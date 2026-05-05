package com.smartmind.ai.agent.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.function.FunctionToolCallback;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.agent.hook.shelltool.ShellToolAgentHook;
import com.alibaba.cloud.ai.graph.agent.hook.skills.SkillsAgentHook;
import com.alibaba.cloud.ai.graph.agent.tools.ShellTool2;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.alibaba.cloud.ai.graph.skills.registry.SkillRegistry;
import com.alibaba.cloud.ai.graph.skills.registry.filesystem.FileSystemSkillRegistry;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import com.alibaba.cloud.ai.graph.agent.tools.PythonTool;
import com.alibaba.cloud.ai.graph.agent.tools.ShellTool;
public class AgentDemo5Skill {

	// 初始化 ChatModel
	static DashScopeApi dashScopeApi = DashScopeApi.builder().apiKey(" ").build();

	static ChatModel chatModel = DashScopeChatModel.builder()
		    .dashScopeApi(dashScopeApi)
		    .defaultOptions(DashScopeChatOptions.builder()
		        // Note: model must be set when use options build.
		        .model(DashScopeChatModel.DEFAULT_MODEL_NAME)
		        
		        .temperature(0.5)
		        .maxToken(1000)
		        .build())
		    .build();

	SkillRegistry registry = FileSystemSkillRegistry.builder()
			  .userSkillsDirectory("C:\\Users\\zhaohuatai\\.claude\\skills")
			  .build();
	SkillsAgentHook hook = SkillsAgentHook.builder()
			  .skillRegistry(registry)
			  .build();


	// 2. Skills Hook：注册 read_skill 工具并注入技能列表到系统提示
	SkillsAgentHook skillsHook = SkillsAgentHook.builder()
	  .skillRegistry(registry)
	  .build();

	// 3. Shell Hook：提供 Shell 命令执行（工作目录可指定，如当前工程目录）
	ShellToolAgentHook shellHook = ShellToolAgentHook.builder()
	  .shellTool2(ShellTool2.builder(System.getProperty("user.dir")).build())
	  .build();
	
	ReactAgent agent = ReactAgent.builder()
			  .name("skills-integration-agent")
			  .model(chatModel)
			  .saver(new MemorySaver())
			  .tools(PythonTool.createPythonToolCallback(PythonTool.DESCRIPTION))
			  .hooks(List.of(skillsHook, shellHook))
			  .enableLogging(true)
			  .build();
	
	public static void main(String[] args) {
		AgentDemo5Skill demo=new AgentDemo5Skill();
		try {
			String skillFilePath = "E:\\work\\test.pdf";  // 实际路径来自技能目录或 hook.listSkills()
			AssistantMessage response = demo.agent.call("帮我下载这个 YouTube 视频：https://www.youtube.com/shorts/w7xmAkx3JLg");
//			AssistantMessage response = demo.agent.call("请从 " + skillFilePath + " 文件中提取关键信息。");
			System.out.println("-----------------------");
			System.out.println(response.getText());
		} catch (GraphRunnerException e) {
			e.printStackTrace();
		}
	}
}
