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

import java.util.List;
import java.util.Scanner;

public class PptxAgent4 {

    public static void main(String[] args) {
        // 1. 初始化模型
        ChatModel chatModel = DashScopeChatModel.builder()
                .dashScopeApi(DashScopeApi.builder()
                        .apiKey("XXXX")
                        .build())
                .defaultOptions(DashScopeChatOptions.builder()
                        .model("qwen-max")
                        .temperature(0.3)
                        .build())
                .build();

        // 2. 配置 Skill 和 ShellTool
        var registry = FileSystemSkillRegistry.builder()
                .userSkillsDirectory("E:/worksapce/sts5.0/legend-smartmind/src/main/resources/skills")
                .build();

        var agent = ReactAgent.builder()
                .name("html-ppt-agent")
                .model(chatModel)
                .instruction("你是HTML演示文稿专家。\n\n" +
                        "使用 html-ppt skill 为用户创建演示文稿：\n" +
                        "1. 询问用户：主题、页数、风格、用途\n" +
                        "2. 使用 shell_tool 执行命令创建deck\n" +
                        "3. 编辑HTML内容并保存到 target/ 目录\n" +
                        "4. 告知用户文件路径")
                .saver(new MemorySaver())
                .hooks(List.of(
                        SkillsAgentHook.builder().skillRegistry(registry).build(),
                        ShellToolAgentHook.builder()
                                .shellTool2(ShellTool2.builder(System.getProperty("user.dir")).build())
                                .build()
                ))
                .build();

        // 3. 运行对话
        Scanner scanner = new Scanner(System.in);
        String threadId = "ppt-session-" + System.currentTimeMillis();

        System.out.println("===== HTML PPT 生成助手 =====\n");

        String userInput = "帮我做一个关于软件测试 Test Harness 的演示文稿，技术分享用，3页";
        System.out.println("用户: " + userInput);

        while (true) {
            try {
                var response = agent.call(userInput, RunnableConfig.builder().threadId(threadId).build());
                System.out.println("\n助手: " + response.getText());

                if (response.getText().contains("完成") || response.getText().contains("target/")) {
                    System.out.println("\n✅ 生成完成！");
                    break;
                }

                System.out.print("\n用户: ");
                userInput = scanner.nextLine();
                if ("exit".equalsIgnoreCase(userInput)) break;

            } catch (Exception e) {
                System.out.println("错误: " + e.getMessage());
                break;
            }
        }

        scanner.close();
    }
}
