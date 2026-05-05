package com.smartmind.ai.agent.demo;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Windows Shell命令辅助工具类
 * 用于处理Windows环境下的Shell命令兼容性问题
 */
public class WindowsShellHelper {
    
    private static final String OS = System.getProperty("os.name").toLowerCase();
    private static final boolean IS_WINDOWS = OS.contains("windows");
    
    /**
     * 转换Unix路径为Windows路径
     */
    public static String convertUnixPathToWindows(String unixPath) {
        if (!IS_WINDOWS) {
            return unixPath;
        }
        
        // 处理 /workspace/ 路径
        if (unixPath.startsWith("/workspace/")) {
            String relativePath = unixPath.substring("/workspace/".length());
            Path workspacePath = Paths.get(System.getProperty("user.dir"));
            return workspacePath.resolve(relativePath).toString().replace("/", "\\");
        }
        
        // 处理其他Unix路径
        return unixPath.replace("/", "\\");
    }
    
    /**
     * 转换Unix命令为PowerShell命令
     */
    public static String convertUnixToPowerShell(String unixCommand) {
        if (!IS_WINDOWS) {
            return unixCommand;
        }
        
        String command = unixCommand;
        
        // 转换路径
        command = command.replace("/workspace/", "${PWD}\\");
        command = command.replace("/", "\\");
        
        // 转换常用命令
        command = command.replace("mkdir -p", "New-Item -ItemType Directory -Force");
        command = command.replace("mkdir", "New-Item -ItemType Directory");
        command = command.replace("cat", "Get-Content");
        command = command.replace("echo", "Write-Output");
        command = command.replace("pwd", "Get-Location");
        command = command.replace("cd", "Set-Location");
        
        // 处理分号分隔的命令
        if (command.contains(";")) {
            String[] parts = command.split(";");
            StringBuilder psCommand = new StringBuilder();
            for (String part : parts) {
                psCommand.append(part.trim()).append("; ");
            }
            command = psCommand.toString();
        }
        
        // 处理管道
        command = command.replace("|", " | ");
        
        return command;
    }
    
    /**
     * 检查是否为Windows兼容的命令
     */
    public static boolean isWindowsCompatibleCommand(String command) {
        if (!IS_WINDOWS) {
            return true;
        }
        
        // 检查是否包含Unix特定命令
        String[] unixCommands = {"mkdir -p", "cat", "pwd", "/workspace/"};
        for (String unixCmd : unixCommands) {
            if (command.contains(unixCmd)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 获取当前系统的Shell类型
     */
    public static String getShellType() {
        return IS_WINDOWS ? "PowerShell" : "Bash";
    }
    
    /**
     * 生成Windows兼容的目录创建命令
     */
    public static String createDirectoryCommand(String path) {
        if (IS_WINDOWS) {
            return "New-Item -ItemType Directory -Force -Path \"" + convertUnixPathToWindows(path) + "\"";
        } else {
            return "mkdir -p " + path;
        }
    }
    
    /**
     * 生成Windows兼容的文件写入命令
     */
    public static String writeFileCommand(String filePath, String content) {
        String windowsPath = convertUnixPathToWindows(filePath);
        
        if (IS_WINDOWS) {
            // 对内容进行转义
            String escapedContent = content.replace("\"", "`\"")
                                          .replace("'", "`'")
                                          .replace("\n", "`n");
            return "Set-Content -Path \"" + windowsPath + "\" -Value \"" + escapedContent + "\"";
        } else {
            // 对内容进行单引号转义
            String escapedContent = content.replace("'", "'\\''");
            return "echo '" + escapedContent + "' > " + filePath;
        }
    }
    
    /**
     * 生成Windows兼容的文件读取命令
     */
    public static String readFileCommand(String filePath) {
        String windowsPath = convertUnixPathToWindows(filePath);
        
        if (IS_WINDOWS) {
            return "Get-Content -Path \"" + windowsPath + "\"";
        } else {
            return "cat " + filePath;
        }
    }
}