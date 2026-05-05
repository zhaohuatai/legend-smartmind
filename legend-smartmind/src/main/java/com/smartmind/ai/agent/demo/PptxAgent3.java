package com.smartmind.ai.agent.demo;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.agent.hook.skills.SkillsAgentHook;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.alibaba.cloud.ai.graph.skills.registry.filesystem.FileSystemSkillRegistry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class PptxAgent3 {

    private static final String API_KEY = "XXXX";
    
    static DashScopeApi dashScopeApi = DashScopeApi.builder().apiKey(API_KEY).build();
    static ChatModel chatModel = DashScopeChatModel.builder()
            .dashScopeApi(dashScopeApi)
            .defaultOptions(DashScopeChatOptions.builder()
                    .model("qwen-max")
                    .temperature(0.3)
                    .build())
            .build();

    public static void main(String[] args) {
        // 1. 配置 Skill 注册表 - 指向 html-ppt-skill 所在目录
        FileSystemSkillRegistry registry = FileSystemSkillRegistry.builder()
                .userSkillsDirectory("E:/worksapce/sts5.0/legend-smartmind/src/main/resources/skills")
                .build();

        SkillsAgentHook skillsHook = SkillsAgentHook.builder()
                .skillRegistry(registry)
                .build();

        // 2. 构建 Agent - 不需要任何 Tool，只需要 Skill 指引
        ReactAgent agent = ReactAgent.builder()
                .name("html-ppt-expert")
                .model(chatModel)
                .instruction("""
                    你是一个专业的 HTML 演示文稿制作专家。
                    
                    你的任务是：根据用户的主题和要求，生成一个完整的、可直接在浏览器中运行的 HTML 演示文稿。
                    
                    请严格遵循以下工作流程：
                    1. 首先，参考 Skill 系统中 'html-ppt' 技能的指引
                    2. 从技能库中选择合适的主题（theme）和布局（layout）
                    3. 生成一个完整的 HTML 文件，包含以下结构：
                       - 正确的 <!DOCTYPE html>
                       - 引入所选主题的 CSS 文件（使用正确的相对路径）
                       - 引入 runtime.js 以支持键盘导航
                       - 每页内容使用独立的 <section> 标签
                       - 添加必要的 meta viewport 设置
                    
                    设计规范：
                    - 科技蓝风格：主色 #1E2761，强调色 #028090
                    - 3页内容：标题页、内容页（Test Harness 介绍）、总结页
                    - 每页添加合适的动画效果
                    - 确保内容专业、简洁、有视觉冲击力
                    
                    输出要求：
                    - 只输出完整的 HTML 代码，不要有任何解释文字
                    - 代码可以直接保存为 .html 文件并在浏览器中打开
                    """)
                .tools()  // 不添加任何工具
                .saver(new MemorySaver())
                .hooks(List.of(skillsHook))
                .enableLogging(false)  // 关闭详细日志
                .build();

        // 3. 运行对话
        Scanner scanner = new Scanner(System.in);
        String threadId = "html-ppt-session-" + System.currentTimeMillis();
        System.out.println("===== HTML PPT 生成助手 =====");
        System.out.println("生成后会自动保存为 output.html 并在浏览器中打开\n");
        
        String userInput = "帮我做一个关于软件测试 Test Harness 的 PPT，技术分享用，3页，科技蓝风格";

        while (true) {
            try {
                System.out.println("[用户]: " + userInput);

                RunnableConfig config = RunnableConfig.builder()
                        .threadId(threadId)
                        .build();

                var response = agent.call(userInput, config);
                String assistantText = response.getText();
                
                // 4. 从回复中提取 HTML 代码
                String htmlContent = extractHtmlCode(assistantText);
                
                if (htmlContent != null) {
                    // 5. 保存 HTML 文件
                    Path outputPath = Path.of(System.getProperty("user.dir"), "output.html");
                    Files.writeString(outputPath, htmlContent);
                    System.out.println("\n✅ HTML PPT 已生成: " + outputPath.toAbsolutePath());
                    
                    // 6. 尝试在浏览器中打开
                    openInBrowser(outputPath.toString());
                    break;
                } else {
                    // 如果没有提取到 HTML，打印完整回复
                    System.out.println("\n[助手]: " + assistantText);
                    
                    System.out.print("\n继续对话 (输入 exit 退出): ");
                    userInput = scanner.nextLine();
                    if ("exit".equalsIgnoreCase(userInput)) break;
                }

            } catch (Exception e) {
                log.error("发生错误: {}", e.getMessage());
                System.out.println("\n⚠️ 错误: " + e.getMessage());
                System.out.print("是否重试？(y/n): ");
                String retry = scanner.nextLine();
                if (!"y".equalsIgnoreCase(retry)) break;
            }
        }
        scanner.close();
    }
    
    /**
     * 从 AI 回复中提取 HTML 代码
     */
    private static String extractHtmlCode(String text) {
        if (text == null) return null;
        
        // 匹配 ```html ... ``` 或 ``` ... ```
        Pattern pattern = Pattern.compile("```(?:html)?\\s*([\\s\\S]*?)\\s*```", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        
        if (matcher.find()) {
            String code = matcher.group(1).trim();
            if (code.toLowerCase().startsWith("<!doctype") || code.toLowerCase().startsWith("<html")) {
                return code;
            }
        }
        
        // 如果没有代码块标记，检查整个文本是否就是 HTML
        String trimmed = text.trim();
        if (trimmed.toLowerCase().startsWith("<!doctype") || trimmed.toLowerCase().startsWith("<html")) {
            return trimmed;
        }
        
        return null;
    }
    
    /**
     * 在浏览器中打开文件
     */
    private static void openInBrowser(String filePath) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            ProcessBuilder pb;
            
            if (os.contains("win")) {
                pb = new ProcessBuilder("cmd", "/c", "start", filePath);
            } else if (os.contains("mac")) {
                pb = new ProcessBuilder("open", filePath);
            } else {
                pb = new ProcessBuilder("xdg-open", filePath);
            }
            
            pb.start();
            System.out.println("🌐 已在浏览器中打开");
        } catch (Exception e) {
            System.out.println("⚠️ 无法自动打开浏览器，请手动打开: " + filePath);
        }
    }
}