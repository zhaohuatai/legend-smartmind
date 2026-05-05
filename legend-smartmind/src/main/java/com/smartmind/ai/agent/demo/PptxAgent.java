package com.smartmind.ai.agent.demo;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
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
import com.alibaba.cloud.ai.graph.agent.tools.PythonTool;
import com.alibaba.cloud.ai.graph.agent.tools.ShellTool;
/**
 * PPT生成智能体 - 调用pptx skill生成PowerPoint文件
 */
@Slf4j
public class PptxAgent {
	// 初始化 ChatModel
	static DashScopeApi dashScopeApi = DashScopeApi.builder().apiKey("XXXX").build();

	static ChatModel chatModel = DashScopeChatModel.builder()
		    .dashScopeApi(dashScopeApi)
		    .defaultOptions(DashScopeChatOptions.builder()
		        // Note: model must be set when use options build.
		        .model(DashScopeChatModel.DEFAULT_MODEL_NAME)
		        .temperature(0.5)
		        .maxToken(1000)
		        .build())
		    .build();
//	SkillRegistry registry2 = ClasspathSkillRegistry.builder()
//			  .classpathPath("skills")
//			  .build();
	SkillRegistry registry = FileSystemSkillRegistry.builder()
			  .userSkillsDirectory("C:\\Users\\zhaohuatai\\.claude\\skills")
			  .build();
	SkillsAgentHook hook = SkillsAgentHook.builder()
			  .skillRegistry(registry)
			  .build();
	// 3. Shell Hook：提供 Shell 命令执行（工作目录可指定，如当前工程目录）
	ShellToolAgentHook shellHook = ShellToolAgentHook.builder()
	  .shellTool2(ShellTool2.builder(System.getProperty("user.dir")).build())
	  .build();
	
	ReactAgent agent = ReactAgent.builder()
			  .name("skills-agent")
			  .model(chatModel)
			  .instruction("你是一个专业的PPT制作专家。目前你处于 Windows 环境。你要利用pptx技能制作ppt\n" +
		                "1. **严禁使用 python_tool**。即使遇到困难，也只能通过 Shell 命令解决。\n" +
		                "2. 你的当前工作目录是 E:\\worksapce\\sts5.0\\legend-smartmind。\n" +
		                "3. 执行 Shell 命令时，路径必须使用 Windows 格式（如 E:\\... 或 ./path），禁止使用 /e/ 这种格式。\n" +
		                "4. 优先使用你已有的 Skill（generate-slides.py）。\n" +
		                "5. 如果需要安装依赖，请先确认 npm 是否存在。")
			  .tools(PythonTool.createPythonToolCallback(PythonTool.DESCRIPTION))
			  .saver(new MemorySaver())
			  .hooks(List.of(hook,shellHook))
//			  .enableLogging(true)
			  .build();


	public static void main(String[] args) {
		System.setProperty("polyglot.engine.WarnInterpreterOnly", "true");
		PptxAgent demo = new PptxAgent();
		java.util.Scanner scanner = new java.util.Scanner(System.in);

		System.out.println("===== PPT生成助手 =====");
		System.out.println("输入 'exit' 退出对话\n");

		// 使用固定的 threadId 保持对话上下文
		String threadId = "pptx-session-" + System.currentTimeMillis();

		// 第一轮：用户初始请求
		String userInput = "帮我做一个关于harness的ppt";
		System.out.println("用户: " + userInput);

		while (true) {
			try {
				// 使用 RunnableConfig 保持对话历史
				com.alibaba.cloud.ai.graph.RunnableConfig config = com.alibaba.cloud.ai.graph.RunnableConfig.builder()
						.threadId(threadId)
						.build();

				// 调用Agent（传入配置以保持上下文）
				var response = demo.agent.call(userInput, config);
				System.out.println("\n助手: " + response.getText());

				// 检查是否已完成PPT生成（包含文件路径）
				if (response.getText().contains(".pptx") && response.getText().contains("生成")) {
					System.out.println("\n✅ PPT生成完成！");
					break;
				}

				// 继续对话
				System.out.print("\n用户: ");
				userInput = scanner.nextLine();

				if (userInput.equalsIgnoreCase("exit") || userInput.equalsIgnoreCase("quit")) {
					System.out.println("再见！");
					break;
				}

			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}

		scanner.close();
	}
}
