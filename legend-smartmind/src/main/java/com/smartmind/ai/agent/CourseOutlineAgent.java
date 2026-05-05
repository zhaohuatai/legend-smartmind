package com.smartmind.ai.agent;

import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.alibaba.cloud.ai.graph.streaming.OutputType;
import com.alibaba.cloud.ai.graph.streaming.StreamingOutput;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

import org.springframework.ai.chat.model.ChatModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * 课程大纲生成智能体
 *
 * <p>基于ReactAgent实现，使用agent.stream实现真正的流式输出
 */
@Slf4j
public class CourseOutlineAgent {

    private final ReactAgent agent;
    private final ChatModel chatModel;

    public CourseOutlineAgent(ChatModel chatModel) {
        this.chatModel = chatModel;

        if (chatModel != null) {
            this.agent = ReactAgent.builder()
                    .name("CourseOutlineAgent")
                    .model(chatModel)
                    .instruction("你是一位专业的课程设计专家。根据提供的课程信息，生成结构化的知识框架大纲。\n\n" +
                            "【严格的标题层级要求】\n" +
                            "1. 一级标题(#)：课程名称（只能有1个）\n" +
                            "2. 二级标题(##)：大单元/章（必须在一级标题下）\n" +
                            "3. 三级标题(###)：子单元/节（必须在二级标题下）\n" +
                            "4. 四级标题(####)：小节（必须在三级标题下）\n" +
                            "5. 五级标题(#####)：禁用，不允许使用\n\n" +
                            "【层级规范】\n" +
                            "- 禁止标题层级跳跃（如一级标题后直接接三级标题）\n" +
                            "- 每个层级必须按顺序递增（# -> ## -> ### -> ####）\n" +
                            "- 同级标题可以并列存在\n" +
                            "【格式示例】\n" +
                            "# 课程名称\n" +
                            "## 第一章 标题\n" +
                            "### 1.1 节标题\n" +
                            "#### 1.1.1 小节标题1\n" +
                            "#### 1.1.2 小节标题2\n" +
                            "请直接输出Markdown格式的课程大纲，不要添加额外说明。")
                    .saver(new MemorySaver())
                    .build();
        } else {
            this.agent = null;
        }
    }

    /**
     * 生成课程大纲（使用agent.stream实现真正的流式输出）
     *
     * @param courseName  课程名称
     * @param subjectType 学科类型
     * @param gradeLevel  面向年级
     * @param courseDesc  课程描述
     * @param userPrompt  用户额外要求
     * @return Flux<String> 流式大纲内容
     * @throws GraphRunnerException 
     */
    public Flux<String> generateOutline(String courseName, String subjectType,
                                        String gradeLevel, String courseDesc,
                                        String userPrompt) throws GraphRunnerException {

        if (chatModel == null || agent == null) {
            // 未配置模型，返回模拟数据
            return generateMockOutline();
        }

        // 构建提示词
        String prompt = buildPrompt(courseName, subjectType, gradeLevel, courseDesc, userPrompt);

        // 创建配置
        RunnableConfig config = RunnableConfig.builder()
                .threadId("outline-" + System.currentTimeMillis())
                .build();

        // 使用agent.stream实现真正的流式输出
        return agent.stream(prompt, config)
                .flatMap(output -> {
                    if (output instanceof StreamingOutput streamingOutput 
                        && streamingOutput.getOutputType() == OutputType.AGENT_MODEL_STREAMING) {
                        String text = streamingOutput.message().getText();
                        // 过滤掉空字符串，减少无用的 SSE 消息
                        return text != null && !text.isEmpty() ? Flux.just(text) : Flux.empty();
                    }
                    return Flux.empty();
                })
//                .buffer(Duration.ofMillis(100), 10)// 后端缓冲：每 100ms 或积累到 10 个字符再发送
//                .map(chunk -> {
//                    String merged = String.join("", chunk);
//                    // 可选：检测完整句子后再发送
//                    return merged;
//                })
                .filter(chunk -> !chunk.isEmpty())
                .concatWith(Flux.just("[DONE]"))
                .onErrorResume(e -> Flux.just("生成失败: " + e.getMessage(), "[DONE]"));
    }

    /**
     * 构建提示词
     */
    private String buildPrompt(String courseName, String subjectType,
                               String gradeLevel, String courseDesc, String userPrompt) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("请为以下课程生成知识框架大纲：\n\n");

        prompt.append("课程信息：\n");
        if (courseName != null) {
            prompt.append("- 课程名称: ").append(courseName).append("\n");
        }
        if (subjectType != null) {
            prompt.append("- 学科类型: ").append(subjectType).append("\n");
        }
        if (gradeLevel != null) {
            prompt.append("- 面向年级: ").append(gradeLevel).append("\n");
        }
        if (courseDesc != null && !courseDesc.isEmpty()) {
            prompt.append("- 课程描述: ").append(courseDesc).append("\n");
        }
        prompt.append("\n");

        if (userPrompt != null && !userPrompt.isEmpty()) {
            prompt.append("用户要求：\n").append(userPrompt).append("\n\n");
        }

        prompt.append("请生成完整的Markdown格式课程大纲。");

        return prompt.toString();
    }

    /**
     * 生成模拟大纲（用于测试）
     */
    private Flux<String> generateMockOutline() {
        String[] lines = {
                "# 课程知识框架\n",
                "## 第一章 基础知识\n",
                "### 1.1 基础概念\n",
                "- 学科定义与发展历程\n",
                "- 基本概念与术语\n",
                "### 1.2 基本原理\n",
                "- 核心理论框架\n",
                "- 基本原理与应用\n",
                "## 第二章 核心内容\n",
                "### 2.1 主要知识点\n",
                "- 知识点一详解\n",
                "- 知识点二详解\n",
                "### 2.2 实践应用\n",
                "- 案例分析\n",
                "- 动手实践\n",
                "## 第三章 拓展提升\n",
                "### 3.1 进阶内容\n",
                "- 深入理解\n",
                "- 拓展阅读\n",
                "---\n",
                "*以上大纲由AI生成，可根据实际需求进行调整*",
                "[DONE]"
        };

        Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();

        // 异步逐行发送
        new Thread(() -> {
            for (String line : lines) {
                sink.tryEmitNext(line);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            sink.tryEmitComplete();
        }).start();

        return sink.asFlux();
    }
}
