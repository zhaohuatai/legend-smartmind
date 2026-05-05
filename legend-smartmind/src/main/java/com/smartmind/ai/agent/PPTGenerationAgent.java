package com.smartmind.ai.agent;

import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.alibaba.cloud.ai.graph.streaming.OutputType;
import com.alibaba.cloud.ai.graph.streaming.StreamingOutput;

import com.smartmind.biz.bo.model.Course;
import com.smartmind.biz.bo.model.LearningUnit;
import com.smartmind.biz.service.ICourseService;
import com.smartmind.biz.service.ILearningUnitService;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * PPT 课件生成智能体
 *
 * <p>基于ReactAgent实现，使用agent.stream实现真正的流式输出
 * <p>根据课程信息、单元信息生成符合 remark-it 规范的 PPT Markdown 内容
 */
@Slf4j
@Component
public class PPTGenerationAgent {

    private final ReactAgent agent;
    private final ChatModel chatModel;

    private static final String SYSTEM_PROMPT_PATH = "remark-it/docs/remark-it-prompt.md";

    @Autowired
    private ICourseService courseService;

    @Autowired
    private ILearningUnitService learningUnitService;

    public PPTGenerationAgent(ChatModel chatModel) {
        this.chatModel = chatModel;

        if (chatModel != null) {
            String systemPrompt = loadSystemPrompt();
            
            this.agent = ReactAgent.builder()
                    .name("PPTGenerationAgent")
                    .model(chatModel)
                    .instruction(systemPrompt)
                    .saver(new MemorySaver())
                    .build();
        } else {
            this.agent = null;
        }
    }

    /**
     * 生成 PPT Markdown 内容（使用agent.stream实现真正的流式输出）
     *
     * @param courseId   课程ID
     * @param unitCode   单元编码
     * @param userPrompt 用户额外要求
     * @return Flux<String> 流式 PPT Markdown 内容
     * @throws GraphRunnerException 
     */
    public Flux<String> generatePPT(Long courseId, String unitCode, String userPrompt) throws GraphRunnerException {

        Course course = courseService.loadById(courseId).orElse(null);
        LearningUnit unit =learningUnitService.loadByUniqueColumn("unit_code", unitCode).orElse(null);
        

        String knowledgeBaseContent = queryKnowledgeBase(courseId, unitCode);

        String prompt = buildPrompt(course, unit, knowledgeBaseContent, userPrompt);

        RunnableConfig config = RunnableConfig.builder().threadId("ppt-" + System.currentTimeMillis()).build();

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
        // 当前返回空字符串
        return "";
    }

    /**
     * 构建完整 Prompt
     */
    private String buildPrompt(Course course, LearningUnit unit, String knowledgeBaseContent, String userPrompt) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("请根据以下教学内容生成 PPT 课件：\n\n");

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
            if (unit.getStandardMapping() != null) {
                prompt.append("- 课标对应: ").append(unit.getStandardMapping()).append("\n");
            }
        }
        prompt.append("\n");

        if (knowledgeBaseContent != null && !knowledgeBaseContent.isEmpty()) {
            prompt.append("## 知识库参考资料\n").append(knowledgeBaseContent).append("\n\n");
        }

        if (userPrompt != null && !userPrompt.isEmpty()) {
            prompt.append("## 用户额外要求\n").append(userPrompt).append("\n\n");
        }

        prompt.append("请严格按照 remark-it 规范生成完整的 PPT Markdown 内容，不要添加任何额外说明。");

        return prompt.toString();
    }

    /**
     * 加载系统提示词
     */
    private String loadSystemPrompt() {
        try {
            ClassPathResource resource = new ClassPathResource(SYSTEM_PROMPT_PATH);
            if (resource.exists()) {
                try (InputStream is = resource.getInputStream()) {
                    String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                    // 转义花括号，防止被 ReactAgent 当作模板变量解析
                    return content.replace("{", "{{").replace("}", "}}");
                }
            } else {
                log.warn("System prompt file not found: {}, using default prompt", SYSTEM_PROMPT_PATH);
                return getDefaultSystemPrompt();
            }
        } catch (Exception e) {
            log.error("Failed to load system prompt: {}", e.getMessage());
            return getDefaultSystemPrompt();
        }
    }

    /**
     * 默认系统提示词（当文件加载失败时使用）
     */
    private String getDefaultSystemPrompt() {
        return "你是一位专业的教育课件设计师，精通使用 remark-it 框架（基于 remarkjs）制作教学演示文稿。" +
               "你的任务是将用户提供的教学内容转换为符合 remark-it 规范的 Markdown 格式课件代码。\n\n" +
               "输出格式要求：\n" +
               "1. 用 --- 分隔不同幻灯片页面\n" +
               "2. 页面属性直接写在页面顶部，不需要 YAML 分隔符\n" +
               "3. 使用 nord-dark（深色）和 nord-light（浅色）主题\n" +
               "4. 使用 Nord 色系强调重点内容\n" +
               "5. 公式中的 & 必须转义为 &amp;\n" +
               "6. Mermaid 图表必须用 <pre> 标签包裹\n\n" +
               "只输出符合规范的 Markdown 代码，不要添加任何解释性文字。";
    }

   
}
