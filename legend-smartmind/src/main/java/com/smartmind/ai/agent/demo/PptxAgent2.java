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
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.alibaba.cloud.ai.graph.skills.registry.SkillRegistry;
import com.alibaba.cloud.ai.graph.skills.registry.classpath.ClasspathSkillRegistry;
import com.alibaba.cloud.ai.graph.skills.registry.filesystem.FileSystemSkillRegistry;

import lombok.extern.slf4j.Slf4j;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.alibaba.cloud.ai.graph.agent.tools.NodeTool;
import com.alibaba.cloud.ai.graph.agent.tools.PythonTool;
import com.alibaba.cloud.ai.graph.agent.tools.PythonTool2;
import com.alibaba.cloud.ai.graph.agent.tools.PythonTool3;
import com.alibaba.cloud.ai.graph.agent.tools.ShellTool;

import java.util.List;
import java.util.Scanner;

@Slf4j
public class PptxAgent2 {

    // 1. 初始化模型 (建议使用 qwen-max 提升代码生成质量)
    private static final String API_KEY = "XXXX"; // 替换为你的Key
    
    static DashScopeApi dashScopeApi = DashScopeApi.builder().apiKey(API_KEY).build();
    static ChatModel chatModel = DashScopeChatModel.builder()
            .dashScopeApi(dashScopeApi)
            .defaultOptions(DashScopeChatOptions.builder()
                    .model("qwen-max") // 强烈建议换成 max 减少 JSON 格式错误
                    .temperature(0.1)  // 降低随机性，使输出格式更稳定
                    .build())
            .build();

    public static void main(String[] args) {
        // 2. 配置 Skill 注册表
        FileSystemSkillRegistry registry = FileSystemSkillRegistry.builder()
//                .userSkillsDirectory("C:/Users/zhaohuatai/.claude/skills") // 使用正斜杠减少转义麻烦
        		.userSkillsDirectory("E:/worksapce/sts5.0/legend-smartmind/src/main/resources/skills") 
        		
                .build();

        SkillsAgentHook skillsHook = SkillsAgentHook.builder()
                .skillRegistry(registry)
                .build();

        ShellToolAgentHook shellHook = ShellToolAgentHook.builder()
                .shellTool2(ShellTool2.builder(System.getProperty("user.dir")).build())
                .build();

        
        // 3. 构建 Agent，强化对 JSON 格式的约束
        ReactAgent agent = ReactAgent.builder()
                .name("pptx-expert-agent")
                .model(chatModel)
                .instruction("你是一个专业的PPT制作专家。特别注意：\n" +
                        "1. 当你调用 python_tool 时，输入参数必须是合法的 JSON 格式。\n" +
                        "2. 代码中的所有双引号必须转义为 \\\"，换行符转义为 \\n。\n" +
                        "3. 涉及文件路径时，统一使用正斜杠 '/' (例如 C:/data/ppt)，不要使用反斜杠，以防 JSON 解析失败。\n" +
                        "4. 必须确保生成的 Python 代码是完整的，可以直接运行。")
//                .tools(PythonTool3.createPythonToolCallback(PythonTool.DESCRIPTION))
                .tools(NodeTool.createNodeToolCallback())
                .saver(new MemorySaver()) // 开启记忆保存
//                .hooks(List.of(skillsHook, shellHook))
                .hooks(List.of(skillsHook))
                .enableLogging(true)
                .build();

        // 4. 运行对话循环
        Scanner scanner = new Scanner(System.in);
        String threadId = "pptx-session-" + System.currentTimeMillis();
        System.out.println("===== PPT生成助手 (已强化格式校验) =====");
        
        String userInput = "帮我做一个关于软件测试 Test Harness 的 PPT，技术分享用，3页，科技蓝风格";

        while (true) {
            try {
                System.out.println("\n[用户]: " + userInput);

                RunnableConfig config = RunnableConfig.builder()
                        .threadId(threadId)
                        .build();

                // 执行 Agent 调用
                var response = agent.call(userInput, config);
                String assistantText = response.getText();
                System.out.println("\n[助手]: " + assistantText);

                if (assistantText.contains(".pptx") && assistantText.contains("生成")) {
                    System.out.println("\n✅ 任务完成！");
                    break;
                }

                System.out.print("\n[回复助手] (或输入 exit): ");
                userInput = scanner.nextLine();
                if ("exit".equalsIgnoreCase(userInput)) break;

            } catch (Exception e) {
                log.error("发生错误: {}", e.getMessage());
                // 【核心技巧】：发生异常时，自动将错误反馈给模型，让其修正
                System.out.println("\n⚠️ 检测到格式错误，正在尝试自动修复并重试...");
                userInput = "你在调用工具时发生了错误: " + e.getMessage() + 
                            "。请检查你的 JSON 格式，确保代码中的引号和换行符已正确转义，并重新尝试调用。";
            }
        }
        scanner.close();
    }
}

