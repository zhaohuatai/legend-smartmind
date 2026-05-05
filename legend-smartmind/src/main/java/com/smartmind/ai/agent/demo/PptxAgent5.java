package com.smartmind.ai.agent.demo;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.agent.hook.shelltool.ShellToolAgentHook;
import com.alibaba.cloud.ai.graph.agent.hook.skills.SkillsAgentHook;
import com.alibaba.cloud.ai.graph.agent.tools.PythonTool;
import com.alibaba.cloud.ai.graph.agent.tools.PythonTool3;
import com.alibaba.cloud.ai.graph.agent.tools.ShellTool2;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.alibaba.cloud.ai.graph.skills.registry.filesystem.FileSystemSkillRegistry;
import org.springframework.ai.chat.model.ChatModel;


import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

public class PptxAgent5 {
    
    /**
     * Windows命令适配器 - 将命令转换为PowerShell兼容格式
     */
    private static String adaptCommandForWindows(String command) {
        System.out.println("[DEBUG] 原始命令: " + command);
        
        // 如果已经是PowerShell命令，直接返回
        if (command.startsWith("powershell")) {
            System.out.println("[DEBUG] 已经是PowerShell命令，无需适配");
            return command;
        }
        
        // 1. 检查是否包含多个命令（分号分隔）
        if (command.contains(";")) {
            return adaptMultipleCommandsToPowerShell(command);
        }
        
        // 2. 转换路径分隔符（PowerShell支持/和\\）
        command = command.replace("/", "\\\\");
        
        // 3. 转换Unix命令到PowerShell命令
        if (command.startsWith("cp ")) {
            // cp file1 file2 -> Copy-Item file1 file2
            command = command.replace("cp ", "Copy-Item ");
        }
        if (command.startsWith("python3 ")) {
            command = command.replace("python3 ", "python ");
        }
        
        // 4. 处理echo命令（转换为PowerShell的Write-Output或Set-Content）
        if (command.contains("echo ") && command.contains(">")) {
            command = adaptEchoToPowerShell(command);
        }
        
        // 5. 处理重定向操作符
        if (command.contains(">")) {
            command = adaptRedirectionToPowerShell(command);
        }
        
        // 6. 确保使用PowerShell命令前缀
        if (!command.startsWith("powershell")) {
            command = "powershell -Command \"" + command + "\"";
        }
        
        System.out.println("[DEBUG] 适配后PowerShell命令: " + command);
        return command;
    }
    
    /**
     * 将分号分隔的多个命令转换为PowerShell格式
     */
    private static String adaptMultipleCommandsToPowerShell(String command) {
        System.out.println("[DEBUG] 检测到多个命令，进行特殊处理");
        
        // 分割命令
        String[] commands = command.split(";");
        StringBuilder adaptedCommands = new StringBuilder();
        
        for (int i = 0; i < commands.length; i++) {
            String cmd = commands[i].trim();
            if (!cmd.isEmpty()) {
                // 单独适配每个命令
                String adaptedCmd = adaptSingleCommandToPowerShell(cmd);
                adaptedCommands.append(adaptedCmd);
                
                // 如果不是最后一个命令，添加分号分隔符
                if (i < commands.length - 1) {
                    adaptedCommands.append("; ");
                }
            }
        }
        
        // 包装整个命令序列
        String result = "powershell -Command \"" + adaptedCommands.toString() + "\"";
        System.out.println("[DEBUG] 多个命令适配结果: " + result);
        return result;
    }
    
    /**
     * 适配单个命令到PowerShell格式
     */
    private static String adaptSingleCommandToPowerShell(String command) {
        String adaptedCommand = command;
        
        // 转换路径分隔符
        adaptedCommand = adaptedCommand.replace("/", "\\\\");
        
        // 转换Unix命令到PowerShell命令
        if (adaptedCommand.startsWith("cp ")) {
            adaptedCommand = adaptedCommand.replace("cp ", "Copy-Item ");
        }
        if (adaptedCommand.startsWith("python3 ")) {
            adaptedCommand = adaptedCommand.replace("python3 ", "python ");
        }
        
        // 处理echo命令
        if (adaptedCommand.contains("echo ") && adaptedCommand.contains(">")) {
            adaptedCommand = adaptEchoToPowerShell(adaptedCommand);
        }
        
        // 处理重定向操作符
        if (adaptedCommand.contains(">")) {
            adaptedCommand = adaptRedirectionToPowerShell(adaptedCommand);
        }
        
        return adaptedCommand;
    }
    
    /**
     * 将echo命令转换为PowerShell格式
     */
    private static String adaptEchoToPowerShell(String command) {
        // 提取echo内容和文件路径
        String[] parts = command.split(">");
        if (parts.length >= 2) {
            String echoPart = parts[0].replace("echo ", "").trim();
            String filePath = parts[1].trim();
            
            // 移除可能的引号
            echoPart = echoPart.replace("\"", "");
            filePath = filePath.replace("\"", "");
            
            // 转换为PowerShell的Set-Content
            return String.format("Set-Content -Path '%s' -Value '%s' -Encoding UTF8", 
                                filePath, echoPart.replace("'", "''"));
        }
        return command;
    }
    
    /**
     * 将重定向操作符转换为PowerShell格式
     */
    private static String adaptRedirectionToPowerShell(String command) {
        // 简单的重定向处理
        if (command.contains(">>")) {
            // 追加重定向
            String[] parts = command.split(">>");
            if (parts.length >= 2) {
                return String.format("%s | Add-Content -Path '%s'", 
                                   parts[0].trim(), parts[1].trim());
            }
        } else if (command.contains(">")) {
            // 覆盖重定向
            String[] parts = command.split(">");
            if (parts.length >= 2) {
                return String.format("%s | Set-Content -Path '%s'", 
                                   parts[0].trim(), parts[1].trim());
            }
        }
        return command;
    }
    
    /**
     * 创建Windows兼容的ShellTool配置 - 通过指令强制使用PowerShell
     */
    private static ShellTool2 createWindowsCompatibleShellTool() {
        // 获取工作目录
        String workingDir = System.getProperty("user.dir");
        
        // 创建ShellTool2构建器
        ShellTool2.Builder builder = ShellTool2.builder(workingDir);
        
        // 由于ShellTool2.Builder没有直接的shellCommand配置方法，
        // 我们通过修改instruction来强制AI生成PowerShell命令
        
        return builder.build();
    }

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
                .instruction("""
使用 ppt-master skill 完整生成PPT。当前是Windows环境。

**重要：所有Shell命令必须使用PowerShell格式！**

PowerShell命令格式要求：
1. 使用 `powershell -Command` 前缀
2. 使用 `\\` 或 `/` 作为路径分隔符（PowerShell都支持）
3. 使用 `Copy-Item` 命令而不是 `cp`
4. 使用 `;` 连接多个命令，而不是 `&&`
5. 使用 `python` 命令而不是 `python3`
6. 使用 `Set-Content` 而不是 `echo >` 进行文件写入
7. 使用UTF-8编码：`-Encoding UTF8`

示例正确的PowerShell命令：
- ❌ 错误: `echo "content" > file.md`
- ✅ 正确: `powershell -Command "Set-Content -Path 'file.md' -Value 'content' -Encoding UTF8"`

- ❌ 错误: `cp file1 file2`
- ✅ 正确: `powershell -Command "Copy-Item -Path 'file1' -Destination 'file2'"`

- ❌ 错误: `python script.py; cp file1 file2`
- ✅ 正确: `powershell -Command "python script.py; Copy-Item file1 file2"`

请确保生成的所有命令都符合PowerShell格式要求。
""")
                .saver(new MemorySaver())
//                .tools(PythonTool.createPythonToolCallback(PythonTool.DESCRIPTION))
                .tools(PythonTool3.createPythonToolCallback(PythonTool3.DESCRIPTION)) // 替换为
                .hooks(List.of(
                        SkillsAgentHook.builder().skillRegistry(registry).build(),
                        ShellToolAgentHook.builder()
                                .shellTool2(createWindowsCompatibleShellTool())
                                .build()
                ))
                .build();

        // 3. 运行对话
        Scanner scanner = new Scanner(System.in);
        String threadId = "ppt-session-" + System.currentTimeMillis();

        String userInput = "帮我做一个关于TestHarness 的ppt,文件名为：TestHarness.pptx，技术分享用，3页";
        System.out.println("用户: " + userInput);

        while (true) {
            try {
                var response = agent.call(userInput, RunnableConfig.builder().threadId(threadId).build());
                String responseText = response.getText();
                
                // 检查响应中是否包含Shell命令，并进行适配
                if (responseText.contains("Executing shell command:")) {
                    System.out.println("\n[DEBUG] 检测到Shell命令，进行Windows适配...");
                    responseText = adaptShellCommandsInResponse(responseText);
                }
                
                System.out.println("\n助手: " + responseText);

                if (responseText.contains("完成") || responseText.contains("target/")) {
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
    }
    
    /**
     * 适配响应中的Shell命令
     */
    private static String adaptShellCommandsInResponse(String response) {
        // 查找并适配所有Shell命令
        String[] lines = response.split("\n");
        StringBuilder adaptedResponse = new StringBuilder();
        
        for (String line : lines) {
            if (line.contains("Executing shell command:")) {
                // 提取命令部分
                int startIndex = line.indexOf(":") + 2;
                if (startIndex > 1 && startIndex < line.length()) {
                    String originalCommand = line.substring(startIndex);
                    String adaptedCommand = adaptCommandForWindows(originalCommand);
                    
                    adaptedResponse.append("[ADAPTED] Executing shell command: ").append(adaptedCommand).append("\n");
                    adaptedResponse.append("[INFO] 原始命令已适配为Windows格式\n");
                    continue;
                }
            }
            adaptedResponse.append(line).append("\n");
        }
        
        return adaptedResponse.toString();
    }
}
