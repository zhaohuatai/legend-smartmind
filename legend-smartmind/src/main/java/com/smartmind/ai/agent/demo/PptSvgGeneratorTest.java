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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * PPT SVG生成器技能测试类
 * 测试将Markdown文稿转化为可导入PPT的SVG页面的功能
 */
public class PptSvgGeneratorTest {
    
    private static final String TEST_MODE = System.getProperty("test.mode", "false");
    private static final String LOG_DIR = "logs/ppt-svg-test";
    private static final String OUTPUT_DIR = "output/ppt-svg-output";
    
    public static void main(String[] args) {
        System.out.println("===== PPT SVG生成器技能测试启动 =====");
        System.out.println("测试模式: " + TEST_MODE);
        System.out.println("当前时间: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        try {
            // 创建输出目录
            createOutputDirectory();
            
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
    
    /**
     * 创建输出目录
     */
    private static void createOutputDirectory() throws Exception {
        Path outputPath = Paths.get(OUTPUT_DIR);
        if (!Files.exists(outputPath)) {
            Files.createDirectories(outputPath);
            System.out.println("创建输出目录: " + OUTPUT_DIR);
        }
    }
    
    /**
     * 测试模式 - 自动执行预设测试用例
     */
    private static void runTestMode() throws Exception {
        System.out.println("\n=== 执行PPT SVG生成器测试模式 ===");
        
        ChatModel chatModel = createChatModel();
        var registry = createSkillRegistry();
        var agent = createAgent(chatModel, registry);
        
        String threadId = "ppt-svg-test-session-" + System.currentTimeMillis();
        
        // 创建测试用的Markdown文件
        createTestMarkdownFiles();
        
        // PPT SVG生成器测试用例
        String[] testCases = {
            "/ppt-quick @" + OUTPUT_DIR + "/test-report.md --style=品牌蓝 --output=" + OUTPUT_DIR,
            "/ppt-analyze @" + OUTPUT_DIR + "/tech-presentation.md",
            "/ppt-design --style=科技暗黑",
            "/ppt-generate --output=" + OUTPUT_DIR + "/slides",
            "帮我把 @" + OUTPUT_DIR + "/business-plan.md 快速转成 PPT，用商务咨询风格"
        };
        
        for (int i = 0; i < testCases.length; i++) {
            System.out.println("\n--- 测试用例 " + (i + 1) + " ---");
            System.out.println("输入: " + testCases[i]);
            
            try {
                var response = agent.call(testCases[i], RunnableConfig.builder().threadId(threadId).build());
                
                logTestResult(i + 1, testCases[i], response.getText(), null);
                
                System.out.println("响应长度: " + response.getText().length() + " 字符");
                System.out.println("PPT SVG功能检测: " + detectPptSvgFeatures(response.getText()));
                System.out.println("测试结果: ✅ 成功");
                
                Thread.sleep(4000); // 给AI和文件操作更多时间
                
            } catch (Exception e) {
                logTestResult(i + 1, testCases[i], null, e.getMessage());
                System.out.println("测试结果: ❌ 失败 - " + e.getMessage());
            }
        }
        
        System.out.println("\n=== PPT SVG生成器测试完成 ===");
        printTestSummary();
    }
    
    /**
     * 交互模式 - 用户与AI交互测试PPT SVG生成器
     */
    private static void runInteractiveMode() {
        ChatModel chatModel = createChatModel();
        var registry = createSkillRegistry();
        var agent = createAgent(chatModel, registry);
        
        Scanner scanner = new Scanner(System.in);
        String threadId = "ppt-svg-session-" + System.currentTimeMillis();
        int interactionCount = 0;
        final int MAX_INTERACTIONS = 30;

        System.out.println("\n===== PPT SVG生成器助手 =====");
        System.out.println("输入 'exit' 退出，输入 'test' 进入测试模式");
        System.out.println("支持的命令: /ppt-quick, /ppt-analyze, /ppt-design, /ppt-generate, /ppt-export\n");

        // 创建测试文件
        try {
            createTestMarkdownFiles();
            System.out.println("✅ 已创建测试Markdown文件在 " + OUTPUT_DIR + " 目录");
        } catch (Exception e) {
            System.out.println("❌ 创建测试文件失败: " + e.getMessage());
        }

        String userInput = "/ppt-quick @" + OUTPUT_DIR + "/test-report.md --style=品牌蓝 --output=" + OUTPUT_DIR;
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
                    responseText.contains("SVG文件已生成") || 
                    responseText.contains("PPT页面已创建") ||
                    responseText.contains(".svg") && responseText.contains("1920×1080")) {
                    shouldExit = true;
                    System.out.println("\n✅ PPT SVG生成完成！");
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
            System.out.println("如果PPT SVG生成尚未完成，请重新运行程序继续。");
        }

        scanner.close();
    }
    
    /**
     * 创建测试用的Markdown文件
     */
    private static void createTestMarkdownFiles() throws Exception {
        Path outputPath = Paths.get(OUTPUT_DIR);
        
        // 测试报告文件
        String testReport = "# 2024年技术趋势报告\n\n" +
                          "## 人工智能发展现状\n\n" +
                          "### 主要技术突破\n" +
                          "- 大语言模型的广泛应用\n" +
                          "- 多模态AI技术的成熟\n" +
                          "- 边缘计算与AI结合\n\n" +
                          "## 云计算与边缘计算\n\n" +
                          "### 发展趋势\n" +
                          "- 混合云成为主流\n" +
                          "- 边缘AI设备普及\n" +
                          "- 安全与隐私保护增强\n\n" +
                          "## 总结与展望\n\n" +
                          "未来技术发展将更加注重实际应用场景。";
        
        Files.write(outputPath.resolve("test-report.md"), testReport.getBytes());
        
        // 技术演示文稿
        String techPresentation = "# 微服务架构设计与实践\n\n" +
                                 "## 什么是微服务\n\n" +
                                 "微服务是一种架构风格，将单一应用程序划分成一组小的服务。\n\n" +
                                 "## 核心特性\n\n" +
                                 "- 服务自治\n" +
                                 "- 技术多样性\n" +
                                 "- 独立部署\n" +
                                 "- 容错设计\n\n" +
                                 "## 实施步骤\n\n" +
                                 "1. 领域驱动设计\n" +
                                 "2. 服务拆分\n" +
                                 "3. 基础设施搭建\n" +
                                 "4. 监控与运维\n\n" +
                                 "## 最佳实践\n\n" +
                                 "选择合适的服务粒度是关键。";
        
        Files.write(outputPath.resolve("tech-presentation.md"), techPresentation.getBytes());
        
        // 商业计划书
        String businessPlan = "# AI智能客服平台商业计划书\n\n" +
                             "## 项目概述\n\n" +
                             "基于大语言模型的智能客服解决方案。\n\n" +
                             "## 市场分析\n\n" +
                             "### 市场规模\n" +
                             "- 全球客服软件市场年增长率15%\n" +
                             "- AI客服渗透率持续提升\n\n" +
                             "## 技术优势\n\n" +
                             "- 多轮对话理解\n" +
                             "- 情感分析能力\n" +
                             "- 知识库自动更新\n\n" +
                             "## 商业模式\n\n" +
                             "SaaS订阅制，按坐席数量收费。";
        
        Files.write(outputPath.resolve("business-plan.md"), businessPlan.getBytes());
        
        System.out.println("✅ 已创建3个测试Markdown文件");
    }
    
    /**
     * 创建AI模型
     */
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
    
    /**
     * 创建技能注册表
     */
    private static FileSystemSkillRegistry createSkillRegistry() {
        return FileSystemSkillRegistry.builder()
                .userSkillsDirectory("E:/worksapce/sts5.0/legend-smartmind/src/main/resources/skills")
                .build();
    }
    
    /**
     * 创建AI代理
     */
    private static ReactAgent createAgent(ChatModel chatModel, FileSystemSkillRegistry registry) {
        String instruction = SkillInstructionBuilder.buildPptSvgInstruction();
        
        return ReactAgent.builder()
                .name("ppt-svg-agent-test")
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
    
    /**
     * 检测PPT SVG生成器特定功能
     */
    private static String detectPptSvgFeatures(String response) {
        StringBuilder features = new StringBuilder();
        
        if (response.contains("/ppt-")) {
            features.append("命令识别 ");
        }
        if (response.contains("SVG") || response.contains("svg")) {
            features.append("SVG生成 ");
        }
        if (response.contains("1920×1080") || response.contains("16:9")) {
            features.append("PPT尺寸 ");
        }
        if (response.contains("风格") || response.contains("style")) {
            features.append("风格设计 ");
        }
        if (response.contains("分析") || response.contains("analyze")) {
            features.append("文稿分析 ");
        }
        if (response.contains("转换为形状") || response.contains("PPT兼容")) {
            features.append("PPT兼容性 ");
        }
        if (response.contains("极简主义") || response.contains("科技暗黑") || 
            response.contains("商务咨询") || response.contains("品牌蓝")) {
            features.append("预设风格 ");
        }
        
        return features.length() > 0 ? features.toString() : "基础功能";
    }
    
    /**
     * 记录测试结果
     */
    private static void logTestResult(int testCaseId, String input, String response, String error) {
        try {
            Path logDir = Paths.get(LOG_DIR);
            if (!Files.exists(logDir)) {
                Files.createDirectories(logDir);
            }
            
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String logFile = String.format("ppt_svg_test_result_%s.txt", timestamp);
            
            StringBuilder logContent = new StringBuilder();
            logContent.append("=== PPT SVG生成器测试用例 ").append(testCaseId).append(" ===\n");
            logContent.append("时间: ").append(LocalDateTime.now()).append("\n");
            logContent.append("输入: ").append(input).append("\n");
            
            if (error != null) {
                logContent.append("状态: 失败\n");
                logContent.append("错误: ").append(error).append("\n");
            } else {
                logContent.append("状态: 成功\n");
                logContent.append("响应长度: ").append(response.length()).append(" 字符\n");
                logContent.append("检测到的功能: ").append(detectPptSvgFeatures(response)).append("\n");
                logContent.append("响应预览: ").append(response.substring(0, Math.min(400, response.length()))).append("...\n");
            }
            
            logContent.append("\n");
            
            Files.write(logDir.resolve(logFile), logContent.toString().getBytes());
            
        } catch (Exception e) {
            System.err.println("日志记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 打印测试摘要
     */
    private static void printTestSummary() {
        System.out.println("\n=== PPT SVG生成器技能测试摘要 ===");
        System.out.println("PPT SVG生成器是一个功能强大的工具，特点包括：");
        System.out.println("• 将Markdown文稿转化为可导入PPT的SVG页面");
        System.out.println("• 支持5种预设风格：极简主义、商务咨询、科技暗黑、瑞士平面、品牌蓝");
        System.out.println("• 生成的SVG支持PPT「转换为形状」功能，可二次编辑");
        System.out.println("• 标准PPT尺寸：1920×1080 px (16:9)");
        System.out.println("• 支持一键生成和分步执行两种模式");
        System.out.println("\n测试日志保存在: " + LOG_DIR);
        System.out.println("输出文件保存在: " + OUTPUT_DIR);
    }
}