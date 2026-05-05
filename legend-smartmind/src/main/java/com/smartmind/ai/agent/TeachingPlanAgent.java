package com.smartmind.ai.agent;

import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.alibaba.cloud.ai.graph.streaming.OutputType;
import com.alibaba.cloud.ai.graph.streaming.StreamingOutput;

import com.smartmind.biz.bo.model.Course;
import com.smartmind.biz.bo.model.LearningUnit;
import com.smartmind.biz.bo.model.TeachingPlan;
import com.smartmind.biz.service.ICourseService;
import com.smartmind.biz.service.ILearningUnitService;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Slf4j
@Component
public class TeachingPlanAgent {

    private final ReactAgent agent;

    @Autowired
    private ICourseService courseService;

    @Autowired
    private ILearningUnitService learningUnitService;

    @Autowired
    private com.smartmind.biz.service.ITeachingPlanService teachingPlanService;

    public TeachingPlanAgent(ChatModel chatModel) {
        if (chatModel != null) {
            String systemPrompt = loadSystemPrompt();
            
            this.agent = ReactAgent.builder()
                    .name("TeachingPlanAgent")
                    .model(chatModel)
                    .instruction(systemPrompt)
                    .saver(new MemorySaver())
                    .build();
        } else {
            this.agent = null;
        }
    }

    /**
     * 生成教案 Markdown 内容（使用agent.stream实现真正的流式输出）
     *
     * @param planId     教案ID
     * @param courseId   课程ID
     * @param unitCode   单元编码
     * @param userPrompt 用户额外要求
     * @return Flux<String> 流式教案 Markdown 内容
     * @throws GraphRunnerException 
     */
    public Flux<String> generateTeachingPlan(Long planId, Long courseId, String unitCode, String userPrompt) throws GraphRunnerException {

        Course course = courseService.loadById(courseId).orElse(null);
        LearningUnit unit = learningUnitService.loadByUniqueColumn("unit_code", unitCode).orElse(null);
        
        TeachingPlan planData = null;
        if (planId != null) {
            planData = teachingPlanService.loadById(planId).orElse(null);
        }
        
        String knowledgeBaseContent = queryKnowledgeBase(courseId, unitCode);

        String prompt = buildPrompt(course, unit, knowledgeBaseContent, userPrompt, planData);

        RunnableConfig config = RunnableConfig.builder().threadId("plan-" + planId).build();

        return agent.stream(prompt, config)
                .flatMap(output -> {
                    if (output instanceof StreamingOutput streamingOutput 
                        && streamingOutput.getOutputType() == OutputType.AGENT_MODEL_STREAMING) {
                        String text = streamingOutput.message().getText();
                        return text != null && !text.isEmpty() ? Flux.just(text) : Flux.empty();
                    }
                    return Flux.empty();
                })
                .filter(chunk -> !chunk.isEmpty())
                .concatWith(Flux.just("[DONE]"))
                .onErrorResume(e -> Flux.just("生成失败: " + e.getMessage(), "[DONE]"));
    }

    /**
     * 查询知识库（空实现，待后续接入RAG或LLM Wiki）
     *
     * @param courseId 课程ID
     * @param unitCode 单元编码
     * @return 知识库查询结果
     */
    public String queryKnowledgeBase(Long courseId, String unitCode) {
        // TODO: 后续接入知识库查询（RAG/LLM Wiki）
        return "";
    }

    /**
     * 构建完整 Prompt
     */
    private String buildPrompt(Course course, LearningUnit unit, String knowledgeBaseContent, String userPrompt, TeachingPlan planData) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("请根据以下教学内容生成教案（Markdown格式）：\n\n");

        prompt.append("## 课程信息\n");
        if (course != null) {
            if (course.getCourseName() != null) {
                prompt.append("- 课程名称: ").append(course.getCourseName()).append("\n");
            }
            if (course.getCourseDesc() != null) {
                prompt.append("- 课程描述: ").append(course.getCourseDesc()).append("\n");
            }
        }
        prompt.append("\n");

        prompt.append("## 单元信息\n");
        if (unit != null) {
            if (unit.getUnitName() != null) {
                prompt.append("- 单元名称: ").append(unit.getUnitName()).append("\n");
            }
            if (unit.getUnitCode() != null) {
                prompt.append("- 单元编码: ").append(unit.getUnitCode()).append("\n");
            }
            if (unit.getUnitDesc() != null) {
                prompt.append("- 单元描述: ").append(unit.getUnitDesc()).append("\n");
            }
            if (unit.getUnitObjectives() != null) {
                prompt.append("- 单元目标: ").append(unit.getUnitObjectives()).append("\n");
            }
            if (unit.getKeyPoints() != null) {
                prompt.append("- 重难点: ").append(unit.getKeyPoints()).append("\n");
            }
            if (unit.getKnowledgeTags() != null) {
                prompt.append("- 知识点标签: ").append(unit.getKnowledgeTags()).append("\n");
            }
        }
        prompt.append("\n");

        if (planData != null) {
            prompt.append("## 教案已有信息\n");
            if (planData.getLessonTitle() != null) {
                prompt.append("- 课时主题: ").append(planData.getLessonTitle()).append("\n");
            }
            if (planData.getLessonSummary() != null) {
                prompt.append("- 授课内容简介: ").append(planData.getLessonSummary()).append("\n");
            }
            if (planData.getKnowledgeObjectives() != null) {
                prompt.append("- 知识目标: ").append(planData.getKnowledgeObjectives()).append("\n");
            }
            if (planData.getAbilityObjectives() != null) {
                prompt.append("- 能力目标: ").append(planData.getAbilityObjectives()).append("\n");
            }
            if (planData.getLiteracyObjectives() != null) {
                prompt.append("- 素养目标: ").append(planData.getLiteracyObjectives()).append("\n");
            }
            if (planData.getKeyPoints() != null) {
                prompt.append("- 教学重点: ").append(planData.getKeyPoints()).append("\n");
            }
            if (planData.getDifficultPoints() != null) {
                prompt.append("- 教学难点: ").append(planData.getDifficultPoints()).append("\n");
            }
            if (planData.getTeachingMethods() != null) {
                prompt.append("- 教学方法: ").append(planData.getTeachingMethods()).append("\n");
            }
            if (planData.getTeachingTools() != null) {
                prompt.append("- 教学工具: ").append(planData.getTeachingTools()).append("\n");
            }
            if (planData.getMainContent() != null) {
                prompt.append("- 教学主要内容: ").append(planData.getMainContent()).append("\n");
            }
            if (planData.getTeachingReflection() != null) {
                prompt.append("- 教学反思: ").append(planData.getTeachingReflection()).append("\n");
            }
            prompt.append("\n");
        }

        if (knowledgeBaseContent != null && !knowledgeBaseContent.isEmpty()) {
            prompt.append("## 知识库参考资料\n").append(knowledgeBaseContent).append("\n\n");
        }

        if (userPrompt != null && !userPrompt.isEmpty()) {
            prompt.append("## 用户额外要求\n").append(userPrompt).append("\n\n");
        }

        prompt.append("## 教案格式要求\n");
        prompt.append("请按照以下结构生成完整的教案内容：\n\n");
        prompt.append("1. **导入**：创设情境，激发兴趣，引出本节课主题\n");
        prompt.append("2. **新课讲授**：核心概念讲解、重点难点分析、例题演示\n");
        prompt.append("3. **互动**：课堂提问、小组讨论、学生展示等互动环节\n");
        prompt.append("4. **小结**：梳理本节课重点知识和方法\n");
        prompt.append("5. **作业**：课后巩固练习\n\n");
        prompt.append("请使用Markdown格式输出，结构清晰，层次分明。只输出教案内容，不要添加任何额外说明。");

        return prompt.toString();
    }

    /**
     * 加载系统提示词
     */
    private String loadSystemPrompt() {
        try {
            ClassPathResource resource = new ClassPathResource("teaching-plan/system-prompt.md");
            if (resource.exists()) {
                try (InputStream is = resource.getInputStream()) {
                    String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                    return content.replace("{", "{{").replace("}", "}}");
                }
            } else {
                log.warn("System prompt file not found, using default prompt");
                return getDefaultSystemPrompt();
            }
        } catch (Exception e) {
            log.error("Failed to load system prompt: {}", e.getMessage());
            return getDefaultSystemPrompt();
        }
    }

    /**
     * 默认系统提示词
     */
    private String getDefaultSystemPrompt() {
        return "你是一位经验丰富的教育专家，擅长编写规范、实用的教案。" +
               "你的任务是将用户提供的教学内容转换为结构完整的教案。" +
               "教案必须包含以下环节：导入→新课讲授→互动→小结→作业。" +
               "请使用Markdown格式输出，结构清晰，层次分明。" +
               "只输出教案内容，不要添加任何解释性文字。";
    }
}
