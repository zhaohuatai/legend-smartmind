package com.smartmind.ai.agent;

import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.alibaba.cloud.ai.graph.streaming.OutputType;
import com.alibaba.cloud.ai.graph.streaming.StreamingOutput;

import com.smartmind.biz.bo.model.Course;
import com.smartmind.biz.bo.model.LearningUnit;
import com.smartmind.biz.bo.model.ExperimentGuide;
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
public class ExperimentGuideAgent {

    private final ReactAgent agent;

    @Autowired
    private ICourseService courseService;

    @Autowired
    private ILearningUnitService learningUnitService;

    @Autowired
    private com.smartmind.biz.service.IExperimentGuideService experimentGuideService;

    public ExperimentGuideAgent(ChatModel chatModel) {
        if (chatModel != null) {
            String systemPrompt = loadSystemPrompt();
            
            this.agent = ReactAgent.builder()
                    .name("ExperimentGuideAgent")
                    .model(chatModel)
                    .instruction(systemPrompt)
                    .saver(new MemorySaver())
                    .build();
        } else {
            this.agent = null;
        }
    }

    /**
     * 生成实验指导书 Markdown 内容（使用agent.stream实现真正的流式输出）
     *
     * @param guideId    指导书ID
     * @param courseId   课程ID
     * @param unitCode   单元编码
     * @param userPrompt 用户额外要求
     * @return Flux<String> 流式实验指导书 Markdown 内容
     * @throws GraphRunnerException 
     */
    public Flux<String> generateExperimentGuide(Long guideId, Long courseId, String unitCode, String userPrompt) throws GraphRunnerException {

        Course course = courseService.loadById(courseId).orElse(null);
        LearningUnit unit = learningUnitService.loadByUniqueColumn("unit_code", unitCode).orElse(null);
        
        ExperimentGuide guideData = null;
        if (guideId != null) {
            guideData = experimentGuideService.loadById(guideId).orElse(null);
        }
        
        String knowledgeBaseContent = queryKnowledgeBase(courseId, unitCode);

        String prompt = buildPrompt(course, unit, knowledgeBaseContent, userPrompt, guideData);

        RunnableConfig config = RunnableConfig.builder().threadId("guide-" + guideId).build();

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
    private String buildPrompt(Course course, LearningUnit unit, String knowledgeBaseContent, String userPrompt, ExperimentGuide guideData) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("请根据以下教学内容生成实验指导书（Markdown格式）：\n\n");

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

        if (guideData != null) {
            prompt.append("## 指导书已有信息\n");
            if (guideData.getGuideName() != null) {
                prompt.append("- 指导书名称: ").append(guideData.getGuideName()).append("\n");
            }
            if (guideData.getExperimentSession() != null) {
                prompt.append("- 第几次实验: ").append(guideData.getExperimentSession()).append("\n");
            }
           
            if (guideData.getEstimatedHours() != null) {
                prompt.append("- 预计课时数: ").append(guideData.getEstimatedHours()).append("\n");
            }
            if (guideData.getExperimentObjectives() != null) {
                prompt.append("- 实验目的: ").append(guideData.getExperimentObjectives()).append("\n");
            }
            if (guideData.getExperimentPrinciple() != null) {
                prompt.append("- 实验原理: ").append(guideData.getExperimentPrinciple()).append("\n");
            }
            if (guideData.getExperimentEquipment() != null) {
                prompt.append("- 实验器材/设备: ").append(guideData.getExperimentEquipment()).append("\n");
            }
            if (guideData.getExperimentSteps() != null) {
                prompt.append("- 实验步骤: ").append(guideData.getExperimentSteps()).append("\n");
            }
            if (guideData.getPrecautions() != null) {
                prompt.append("- 注意事项: ").append(guideData.getPrecautions()).append("\n");
            }
            if (guideData.getReportRequirements() != null) {
                prompt.append("- 实验报告要求: ").append(guideData.getReportRequirements()).append("\n");
            }
            if (guideData.getMainContent() != null) {
                prompt.append("- 实验指导书完整内容: ").append(guideData.getMainContent()).append("\n");
            }
            prompt.append("\n");
        }

        if (knowledgeBaseContent != null && !knowledgeBaseContent.isEmpty()) {
            prompt.append("## 知识库参考资料\n").append(knowledgeBaseContent).append("\n\n");
        }

        if (userPrompt != null && !userPrompt.isEmpty()) {
            prompt.append("## 用户额外要求\n").append(userPrompt).append("\n\n");
        }

        prompt.append("## 实验指导书格式要求\n");
        prompt.append("请按照以下结构生成完整的实验指导书内容：\n\n");
        prompt.append("1. **实验目的**：明确本次实验要达到的目标\n");
        prompt.append("2. **实验原理**：实验相关的理论基础和原理说明\n");
        prompt.append("3. **实验器材**：列出所需的实验设备和材料\n");
        prompt.append("4. **实验步骤**：详细的操作流程，分步骤说明\n");
        prompt.append("5. **注意事项**：实验过程中的安全事项和操作要点\n");
        prompt.append("6. **实验报告要求**：实验报告的格式和内容要求\n\n");
        prompt.append("请使用Markdown格式输出，结构清晰，层次分明。只输出实验指导书内容，不要添加任何额外说明。");

        return prompt.toString();
    }

    /**
     * 加载系统提示词
     */
    private String loadSystemPrompt() {
        try {
            ClassPathResource resource = new ClassPathResource("experiment-guide/system-prompt.md");
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
        return "你是一位经验丰富的实验指导教师，擅长编写规范、实用的实验指导书。" +
               "你的任务是将用户提供的实验内容转换为结构完整的实验指导书。" +
               "实验指导书必须包含以下环节：实验目的→实验原理→实验器材→实验步骤→注意事项→实验报告要求。" +
               "请使用Markdown格式输出，结构清晰，层次分明。" +
               "只输出实验指导书内容，不要添加任何解释性文字。";
    }
}
