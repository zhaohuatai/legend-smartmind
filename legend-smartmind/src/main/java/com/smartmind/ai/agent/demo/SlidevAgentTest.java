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
 * Slidev技能测试类
 * 用于测试Slidev技能的功能和兼容性
 */
public class SlidevAgentTest {
    
    private static final String TEST_MODE = System.getProperty("test.mode", "false");
    private static final String LOG_DIR = "logs/slidev-test";
    
    public static void main(String[] args) {
        System.out.println("===== Slidev技能测试启动 =====");
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
    
    /**
     * 测试模式 - 自动执行预设测试用例
     */
    private static void runTestMode() throws Exception {
        System.out.println("\n=== 执行Slidev技能测试模式 ===");
        
        ChatModel chatModel = createChatModel();
        var registry = createSkillRegistry();
        var agent = createAgent(chatModel, registry);
        
        String threadId = "slidev-test-session-" + System.currentTimeMillis();
        
        // Slidev技能测试用例 - 明确指定正确的命令格式
        String[] testCases = {
            "使用正确的Slidev命令格式：pnpm create slidev@latest vue3-composition-api -- 创建一个关于Vue 3 Composition API的Slidev演示文稿，包含代码示例，3页",
            "使用正确的Slidev命令格式：pnpm create slidev@latest typescript-generics -- 制作一个TypeScript泛型教程的Slidev演示，面向中级开发者，4页",
            "使用正确的Slidev命令格式：pnpm create slidev@latest micro-frontend -- 创建一个微前端架构介绍的Slidev演示，包含Mermaid图表，5页",
            "使用正确的Slidev命令格式：pnpm create slidev@latest react-hooks -- 使用Slidev创建一个React Hooks教程，包含交互式代码示例"
        };
        
        for (int i = 0; i < testCases.length; i++) {
            System.out.println("\n--- 测试用例 " + (i + 1) + " ---");
            System.out.println("输入: " + testCases[i]);
            
            try {
                var response = agent.call(testCases[i], RunnableConfig.builder().threadId(threadId).build());
                
                logTestResult(i + 1, testCases[i], response.getText(), null);
                
                System.out.println("响应长度: " + response.getText().length() + " 字符");
                System.out.println("Slidev功能检测: " + detectSlidevFeatures(response.getText()));
                
                // 验证项目结构
                boolean structureValid = validateSlidevProjectStructure(i + 1);
                if (structureValid) {
                    System.out.println("项目结构: ✅ 正常");
                } else {
                    System.out.println("项目结构: ⚠️ 异常（可能存在exit目录问题）");
                    
                    // 尝试自动修复exit目录问题
                    String projectName = getProjectNameByTestCase(i + 1);
                    fixExitDirectoryIssue(projectName);
                    
                    // 重新验证修复后的结构
                    boolean fixed = validateSlidevProjectStructure(i + 1);
                    if (fixed) {
                        System.out.println("修复后项目结构: ✅ 正常");
                    } else {
                        System.out.println("修复后项目结构: ❌ 仍然异常");
                    }
                }
                
                System.out.println("测试结果: ✅ 成功");
                
                Thread.sleep(3000); // 给AI一些处理时间
                
            } catch (Exception e) {
                logTestResult(i + 1, testCases[i], null, e.getMessage());
                System.out.println("测试结果: ❌ 失败 - " + e.getMessage());
            }
        }
        
        System.out.println("\n=== Slidev技能测试完成 ===");
        printTestSummary();
    }
    
    /**
     * 交互模式 - 用户与AI交互测试Slidev技能
     */
    private static void runInteractiveMode() {
        System.out.println("\n=== Slidev技能交互测试模式 ===");
        System.out.println("重要提示：请使用正确的Slidev命令格式");
        System.out.println("✅ 正确格式: pnpm create slidev@latest 项目名称");
        System.out.println("❌ 错误格式: pnpm create slidev（可能导致exit目录问题）");
        System.out.println("\n输入'exit'退出，输入'test'进入测试模式");
        
        ChatModel chatModel = createChatModel();
        var registry = createSkillRegistry();
        var agent = createAgent(chatModel, registry);
        
        Scanner scanner = new Scanner(System.in);
        String threadId = "slidev-session-" + System.currentTimeMillis();
        int interactionCount = 0;
        final int MAX_INTERACTIONS = 25;

        System.out.println("\n===== Slidev演示文稿生成助手 =====");
        System.out.println("输入 'exit' 退出，输入 'test' 进入测试模式");
        System.out.println("注意：Slidev是基于Web的开发者演示文稿工具\n");

        String userInput = "创建一个关于Vue 3 Composition API的Slidev演示文稿，包含代码示例，3页";
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
                    responseText.contains("Slidev项目已创建") || 
                    responseText.contains("演示文稿已保存") ||
                    responseText.contains("slides.md") && responseText.contains("---")) {
                    shouldExit = true;
                    System.out.println("\n✅ Slidev演示文稿生成完成！");
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
            System.out.println("如果Slidev项目尚未完成，请重新运行程序继续。");
        }

        scanner.close();
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
        String instruction = SkillInstructionBuilder.buildSlidevInstruction();
        
        return ReactAgent.builder()
                .name("slidev-agent-test")
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
     * 检测Slidev特定功能
     */
    private static String detectSlidevFeatures(String response) {
        // 简化的特征检测逻辑
        if (response.contains("pnpm create slidev@latest") || response.contains("slidev")) {
            return "检测到正确的Slidev命令格式";
        }
        if (response.contains("pnpm create slidev")) {
            return "检测到Slidev命令（可能存在exit目录风险）";
        }
        if (response.contains("slides.md") || response.contains("---")) {
            return "检测到Slidev内容格式";
        }
        
        // 保留原有的详细特征检测作为备选
        StringBuilder features = new StringBuilder();
        
        if (response.contains("---")) {
            features.append("幻灯片分隔符 ");
        }
        if (response.contains("theme:") || response.contains("layout:")) {
            features.append("Frontmatter配置 ");
        }
        if (response.contains("```")) {
            features.append("代码块 ");
        }
        if (response.contains("v-click") || response.contains("v-clicks")) {
            features.append("动画效果 ");
        }
        if (response.contains("mermaid") || response.contains("plantuml")) {
            features.append("图表 ");
        }
        if (response.contains("pnpm") || response.contains("slidev")) {
            features.append("CLI命令 ");
        }
        
        return features.length() > 0 ? features.toString() : "未检测到明显特征";
    }
    
    /**
     * 验证Slidev项目结构
     */
    private static boolean validateSlidevProjectStructure(int testCaseId) {
        try {
            String projectName = getProjectNameByTestCase(testCaseId);
            Path projectPath = Paths.get(projectName);
            
            if (!Files.exists(projectPath)) {
                System.out.println("项目目录不存在: " + projectPath);
                return false;
            }
            
            // 检查是否存在异常的exit目录
            Path exitPath = projectPath.resolve("exit");
            if (Files.exists(exitPath)) {
                System.out.println("⚠️ 检测到异常exit目录: " + exitPath);
                
                // 检查exit目录是否是Slidev项目
                Path exitPackageJson = exitPath.resolve("package.json");
                if (Files.exists(exitPackageJson)) {
                    System.out.println("exit目录包含Slidev项目文件，需要修复目录结构");
                    return false;
                }
            }
            
            // 检查正常的Slidev项目文件
            Path slidesMd = projectPath.resolve("slides.md");
            Path packageJson = projectPath.resolve("package.json");
            
            boolean hasSlidesMd = Files.exists(slidesMd);
            boolean hasPackageJson = Files.exists(packageJson);
            
            if (hasSlidesMd && hasPackageJson) {
                return true;
            } else {
                System.out.println("项目结构不完整 - slides.md: " + hasSlidesMd + ", package.json: " + hasPackageJson);
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("验证项目结构时出错: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 根据测试用例ID获取项目名称
     */
    private static String getProjectNameByTestCase(int testCaseId) {
        switch (testCaseId) {
            case 1: return "vue3-composition-api";
            case 2: return "typescript-generics";
            case 3: return "micro-frontend";
            case 4: return "react-hooks";
            default: return "slidev-test-" + testCaseId;
        }
    }
    
    /**
     * 修复exit目录问题
     */
    private static void fixExitDirectoryIssue(String projectName) {
        try {
            Path projectPath = Paths.get(projectName);
            Path exitPath = projectPath.resolve("exit");
            
            if (Files.exists(exitPath)) {
                System.out.println("\n🔧 检测到exit目录问题，正在修复...");
                
                // 检查exit目录是否包含Slidev项目文件
                Path exitPackageJson = exitPath.resolve("package.json");
                if (Files.exists(exitPackageJson)) {
                    System.out.println("exit目录包含Slidev项目文件，执行修复操作...");
                    
                    // 将exit目录的内容移动到项目根目录
                    try (var stream = Files.list(exitPath)) {
                        stream.forEach(source -> {
                            try {
                                Path target = projectPath.resolve(source.getFileName());
                                Files.move(source, target, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                                System.out.println("移动文件: " + source.getFileName());
                            } catch (Exception e) {
                                System.err.println("移动文件失败: " + source + " - " + e.getMessage());
                            }
                        });
                    }
                    
                    // 删除空的exit目录
                    Files.deleteIfExists(exitPath);
                    System.out.println("✅ exit目录问题修复完成");
                } else {
                    System.out.println("exit目录不包含Slidev项目文件，无需修复");
                }
            }
            
        } catch (Exception e) {
            System.err.println("修复exit目录问题时出错: " + e.getMessage());
        }
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
            String logFile = String.format("slidev_test_result_%s.txt", timestamp);
            
            StringBuilder logContent = new StringBuilder();
            logContent.append("=== Slidev测试用例 ").append(testCaseId).append(" ===\n");
            logContent.append("时间: ").append(LocalDateTime.now()).append("\n");
            logContent.append("输入: ").append(input).append("\n");
            
            if (error != null) {
                logContent.append("状态: 失败\n");
                logContent.append("错误: ").append(error).append("\n");
            } else {
                logContent.append("状态: 成功\n");
                logContent.append("响应长度: ").append(response.length()).append(" 字符\n");
                logContent.append("检测到的功能: ").append(detectSlidevFeatures(response)).append("\n");
                logContent.append("响应预览: ").append(response.substring(0, Math.min(300, response.length()))).append("...\n");
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
        System.out.println("\n=== Slidev技能测试摘要 ===");
        System.out.println("Slidev是一个基于Web的开发者演示文稿工具，特点包括：");
        System.out.println("• 基于Markdown和Vue组件");
        System.out.println("• 支持代码高亮和动画");
        System.out.println("• 包含Mermaid图表和LaTeX数学公式");
        System.out.println("• 可导出为PDF、PPTX等格式");
        System.out.println("• 支持交互式代码示例");
        System.out.println("\n测试日志保存在: " + LOG_DIR);
    }
}