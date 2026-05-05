package com.smartmind.ai.agent.demo;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.agent.hook.shelltool.ShellToolAgentHook;
import com.alibaba.cloud.ai.graph.agent.hook.skills.SkillsAgentHook;
import com.alibaba.cloud.ai.graph.agent.tools.ShellTool2;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.alibaba.cloud.ai.graph.skills.registry.filesystem.FileSystemSkillRegistry;
import org.springframework.ai.chat.model.ChatModel;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicLong;

public class PptxAgent6 {

    private static final String TEST_MODE = System.getProperty("test.mode", "false");
    private static final String LOG_DIR = "logs/ppt-test";
    private static final AtomicLong totalExecutionTime = new AtomicLong(0);
    private static final AtomicLong commandCount = new AtomicLong(0);
    
    public static void main(String[] args) {
        System.out.println("===== PPT Agent 测试模式启动 =====");
        System.out.println("测试模式: " + TEST_MODE);
        System.out.println("当前时间: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        try {
            if (Boolean.parseBoolean(TEST_MODE)) {
                runTestMode();
            } else {
                runInteractiveMode();
            }
        } catch (Exception e) {
            System.err.println("程序执行失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void runTestMode() throws Exception {
        System.out.println("\n=== 执行测试模式 ===");
        
        ChatModel chatModel = createChatModel();
        var registry = createSkillRegistry();
        var agent = createAgent(chatModel, registry);
        
        String threadId = "test-session-" + System.currentTimeMillis();
        
        String[] testCases = {
            "帮我做一个关于软件测试 Test Harness 的演示文稿，技术分享用，3页",
            "创建一个关于微服务架构的PPT，面向开发团队，5页",
            "制作一个AI应用开发的介绍PPT，商业展示用，4页"
        };
        
        for (int i = 0; i < testCases.length; i++) {
            System.out.println("\n--- 测试用例 " + (i + 1) + " ---");
            System.out.println("输入: " + testCases[i]);
            
            try {
                var response = agent.call(testCases[i], RunnableConfig.builder().threadId(threadId).build());
                
                logTestResult(i + 1, testCases[i], response.getText(), null);
                
                System.out.println("响应长度: " + response.getText().length() + " 字符");
                System.out.println("测试结果: ✅ 成功");
                
                Thread.sleep(2000);
                
            } catch (Exception e) {
                logTestResult(i + 1, testCases[i], null, e.getMessage());
                System.out.println("测试结果: ❌ 失败 - " + e.getMessage());
            }
        }
        
        System.out.println("\n=== 测试完成 ===");
    }
    
    private static void runInteractiveMode() {
        ChatModel chatModel = createChatModel();
        var registry = createSkillRegistry();
        var agent = createAgent(chatModel, registry);
        
        Scanner scanner = new Scanner(System.in);
        String threadId = "ppt-session-" + System.currentTimeMillis();
        int interactionCount = 0;
        final int MAX_INTERACTIONS = 20; // 防止无限循环

        System.out.println("\n===== HTML PPT 生成助手 =====");
        System.out.println("输入 'exit' 退出，输入 'test' 进入测试模式");
        System.out.println("注意：这是一个交互式流程，AI助手会逐步引导您完成PPT制作\n");

        String userInput = "帮我做一个关于软件测试 Test Harness 的演示文稿，技术分享用，3页";
        System.out.println("用户: " + userInput);

        while (interactionCount < MAX_INTERACTIONS) {
            interactionCount++;
            try {
                var response = agent.call(userInput, RunnableConfig.builder().threadId(threadId).build());
                System.out.println("\n助手: " + response.getText());

                // 更智能的完成条件判断
                String responseText = response.getText();
                boolean shouldExit = false;
                
                // 检查是否为明确的完成信号
                if (responseText.contains("✅ 生成完成") || 
                    responseText.contains("PPT已生成") || 
                    responseText.contains("文件已保存") ||
                    responseText.contains("target/") && responseText.contains(".pptx")) {
                    shouldExit = true;
                    System.out.println("\n✅ 生成完成！");
                }
                // 检查是否用户主动要求退出
                else if (responseText.contains("exit") || responseText.contains("退出")) {
                    shouldExit = true;
                    System.out.println("\n用户要求退出。");
                }
                // 检查是否遇到无法继续的错误
                else if (responseText.contains("错误") && responseText.contains("无法继续")) {
                    shouldExit = true;
                    System.out.println("\n遇到错误，程序终止。");
                }
                
                if (shouldExit) {
                    break;
                }

                System.out.print("\n用户: ");
                userInput = scanner.nextLine();
                
                if ("exit".equalsIgnoreCase(userInput)) break;
                if ("test".equalsIgnoreCase(userInput)) {
                    System.setProperty("test.mode", "true");
                    runTestMode();
                    break;
                }

            } catch (Exception e) {
                System.out.println("错误: " + e.getMessage());
                break;
            }
        }
        
        if (interactionCount >= MAX_INTERACTIONS) {
            System.out.println("\n⚠️ 已达到最大交互次数(" + MAX_INTERACTIONS + ")，程序自动退出。");
            System.out.println("如果PPT尚未完成，请重新运行程序继续。");
        }

        scanner.close();
    }
    
    private static ChatModel createChatModel() {
        return DashScopeChatModel.builder()
                .dashScopeApi(DashScopeApi.builder()
                        .apiKey("XXXX")
                        .build())
                .defaultOptions(DashScopeChatOptions.builder()
                        .model("qwen-max")
                        .temperature(0.3)
                        .build())
                .build();
    }
    
    private static FileSystemSkillRegistry createSkillRegistry() {
        return FileSystemSkillRegistry.builder()
                .userSkillsDirectory("E:/worksapce/sts5.0/legend-smartmind/src/main/resources/skills")
                .build();
    }
    
    private static ReactAgent createAgent(ChatModel chatModel, FileSystemSkillRegistry registry) {
        String instruction = SkillInstructionBuilder.buildPptWorkflowInstruction();
        
        return ReactAgent.builder()
                .name("html-ppt-agent-test")
                .model(chatModel)
                .instruction(instruction)
                .saver(new MemorySaver())
                .hooks(List.of(
                        SkillsAgentHook.builder().skillRegistry(registry).build(),
                        ShellToolAgentHook.builder()
                                .shellTool2(ShellTool2.builder(System.getProperty("user.dir")).build())
                                .build()
                ))
                .build();
    }
    
    private static void logTestResult(int testCaseId, String input, String response, String error) {
        try {
            Path logDir = Paths.get(LOG_DIR);
            if (!Files.exists(logDir)) {
                Files.createDirectories(logDir);
            }
            
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String logFile = String.format("test_result_%s.txt", timestamp);
            
            StringBuilder logContent = new StringBuilder();
            logContent.append("=== 测试用例 ").append(testCaseId).append(" ===\n");
            logContent.append("时间: ").append(LocalDateTime.now()).append("\n");
            logContent.append("输入: ").append(input).append("\n");
            
            if (error != null) {
                logContent.append("状态: 失败\n");
                logContent.append("错误: ").append(error).append("\n");
            } else {
                logContent.append("状态: 成功\n");
                logContent.append("响应长度: ").append(response.length()).append(" 字符\n");
                logContent.append("响应预览: ").append(response.substring(0, Math.min(200, response.length()))).append("...\n");
            }
            
            logContent.append("\n");
            
            Files.write(logDir.resolve(logFile), logContent.toString().getBytes());
            
        } catch (Exception e) {
            System.err.println("日志记录失败: " + e.getMessage());
        }
    }
}
