package com.smartmind.ai.agent.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Shell命令适配器 - 统一的Shell命令兼容性解决方案
 * 自动检测和转换Unix/Linux命令为Windows PowerShell命令
 */
public class ShellCommandAdapter {
    
    private static final String OS = System.getProperty("os.name").toLowerCase();
    private static final boolean IS_WINDOWS = OS.contains("windows");
    
    // Unix到Windows命令映射
    private static final Map<String, String> COMMAND_MAPPING = new HashMap<>();
    
    static {
        // 文件操作命令
        COMMAND_MAPPING.put("ls", "Get-ChildItem");
        COMMAND_MAPPING.put("cat", "Get-Content");
        COMMAND_MAPPING.put("mkdir -p", "New-Item -ItemType Directory -Force");
        COMMAND_MAPPING.put("mkdir", "New-Item -ItemType Directory");
        COMMAND_MAPPING.put("rm -rf", "Remove-Item -Recurse -Force");
        COMMAND_MAPPING.put("rm", "Remove-Item");
        COMMAND_MAPPING.put("cp", "Copy-Item");
        COMMAND_MAPPING.put("mv", "Move-Item");
        COMMAND_MAPPING.put("touch", "New-Item -ItemType File");
        
        // 目录操作命令
        COMMAND_MAPPING.put("pwd", "Get-Location");
        COMMAND_MAPPING.put("cd", "Set-Location");
        
        // 文本处理命令
        COMMAND_MAPPING.put("grep", "Select-String");
        COMMAND_MAPPING.put("echo", "Write-Output");
        COMMAND_MAPPING.put("sed", "ForEach-Object { $_ -replace");
        
        // 系统信息命令
        COMMAND_MAPPING.put("which", "Get-Command");
        COMMAND_MAPPING.put("uname", "$env:OS");
    }
    
    /**
     * 自动适配Shell命令
     */
    public static String adaptCommand(String originalCommand) {
        if (!IS_WINDOWS) {
            return originalCommand; // 非Windows系统直接返回原命令
        }
        
        String command = originalCommand.trim();
        
        // 检测命令类型
        if (isUnixCommand(command)) {
            return convertUnixToPowerShell(command);
        } else if (isPowerShellCommand(command)) {
            return command; // 已经是PowerShell命令
        } else {
            // 未知命令类型，尝试智能转换
            return smartConvert(command);
        }
    }
    
    /**
     * 检测是否为Unix命令
     */
    public static boolean isUnixCommand(String command) {
        String[] unixIndicators = {
            "mkdir -p", "cat ", "ls ", "pwd", "cd ", 
            "/workspace/", "/tmp/", "/home/", "/usr/",
            "&&", "||", "|", ";", "`"
        };
        
        for (String indicator : unixIndicators) {
            if (command.contains(indicator)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 检测是否为PowerShell命令
     */
    public static boolean isPowerShellCommand(String command) {
        String[] psIndicators = {
            "Get-", "Set-", "New-", "Remove-", "Copy-", "Move-",
            "Write-", "Select-", "ForEach-", "Where-", "$env:"
        };
        
        for (String indicator : psIndicators) {
            if (command.contains(indicator)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 转换Unix命令为PowerShell
     */
    private static String convertUnixToPowerShell(String unixCommand) {
        String command = unixCommand;
        
        // 1. 转换路径格式
        command = convertUnixPaths(command);
        
        // 2. 转换基本命令
        for (Map.Entry<String, String> entry : COMMAND_MAPPING.entrySet()) {
            if (command.contains(entry.getKey())) {
                command = command.replace(entry.getKey(), entry.getValue());
            }
        }
        
        // 3. 处理命令分隔符
        command = handleCommandSeparators(command);
        
        // 4. 处理管道和重定向
        command = handlePipesAndRedirects(command);
        
        return command;
    }
    
    /**
     * 转换Unix路径为Windows路径
     */
    private static String convertUnixPaths(String command) {
        // 处理 /workspace/ 路径
        if (command.contains("/workspace/")) {
            String relativePath = command.substring(command.indexOf("/workspace/") + "/workspace/".length());
            String windowsPath = System.getProperty("user.dir") + "\\" + relativePath.replace("/", "\\");
            command = command.replace("/workspace/" + relativePath, windowsPath);
        }
        
        // 转换通用Unix路径
        command = command.replace("/", "\\");
        
        return command;
    }
    
    /**
     * 处理命令分隔符
     */
    private static String handleCommandSeparators(String command) {
        // 处理 && 分隔符
        if (command.contains(" && ")) {
            String[] parts = command.split(" && ");
            StringBuilder psCommand = new StringBuilder();
            for (String part : parts) {
                psCommand.append(part.trim()).append("; ");
            }
            return psCommand.toString();
        }
        
        // 处理 ; 分隔符
        if (command.contains(";")) {
            return command.replace(";", "; ");
        }
        
        return command;
    }
    
    /**
     * 处理管道和重定向
     */
    private static String handlePipesAndRedirects(String command) {
        // 处理管道 |
        if (command.contains(" | ")) {
            command = command.replace(" | ", " | ");
        }
        
        // 处理重定向 >
        if (command.contains(" > ")) {
            command = command.replace(" > ", " > ");
        }
        
        // 处理重定向 >>
        if (command.contains(" >> ")) {
            command = command.replace(" >> ", " >> ");
        }
        
        return command;
    }
    
    /**
     * 智能转换未知命令
     */
    private static String smartConvert(String command) {
        // 简单的启发式转换
        if (command.startsWith("mkdir")) {
            return command.replace("mkdir", "New-Item -ItemType Directory");
        }
        if (command.startsWith("cat")) {
            return command.replace("cat", "Get-Content");
        }
        if (command.startsWith("ls")) {
            return command.replace("ls", "Get-ChildItem");
        }
        
        return command; // 无法转换，返回原命令
    }
    
    /**
     * 验证命令是否可执行
     */
    public static boolean isExecutable(String command) {
        if (!IS_WINDOWS) {
            return true; // 非Windows系统假设命令可执行
        }
        
        // 检查是否包含已知的不兼容模式
        String[] incompatiblePatterns = {
            "/workspace/", "/tmp/", "/home/",
            "mkdir -p", "cat ", "pwd"
        };
        
        for (String pattern : incompatiblePatterns) {
            if (command.contains(pattern)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 获取系统类型信息
     */
    public static String getSystemInfo() {
        StringBuilder info = new StringBuilder();
        info.append("操作系统: ").append(System.getProperty("os.name"));
        info.append("\n架构: ").append(System.getProperty("os.arch"));
        info.append("\n版本: ").append(System.getProperty("os.version"));
        info.append("\nShell类型: ").append(IS_WINDOWS ? "PowerShell" : "Bash");
        info.append("\n工作目录: ").append(System.getProperty("user.dir"));
        return info.toString();
    }
    
    /**
     * 生成兼容性提示信息
     */
    public static String getCompatibilityHint() {
        if (!IS_WINDOWS) {
            return "当前系统支持Unix/Linux命令";
        }
        
        return "当前运行在Windows系统上，请使用PowerShell命令：\n" +
               "• 使用 'New-Item -ItemType Directory -Force' 代替 'mkdir -p'\n" +
               "• 使用 'Get-Content' 代替 'cat'\n" +
               "• 使用 'Set-Content' 代替 'echo >'\n" +
               "• 使用 'Set-Location' 代替 'cd'\n" +
               "• 使用Windows路径分隔符 '\\\\' 而不是 '/'\n" +
               "• 工作目录是当前项目目录，不要使用 '/workspace/' 路径";
    }
}