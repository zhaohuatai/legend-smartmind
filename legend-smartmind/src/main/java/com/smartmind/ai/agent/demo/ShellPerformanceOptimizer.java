package com.smartmind.ai.agent.demo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Shell命令性能优化器
 */
public class ShellPerformanceOptimizer {
    
    /**
     * 优化文件写入命令
     */
    public static String optimizeWriteCommand(String filePath, String content) {
        // 对于大文件内容，考虑直接使用Java文件操作
        if (content.length() > 500) { // 超过500字符的大文件
            return createDirectFileWriteCommand(filePath, content);
        }
        
        // 小文件使用优化的PowerShell命令
        return createOptimizedPowerShellWriteCommand(filePath, content);
    }
    
    /**
     * 创建优化的PowerShell写入命令
     */
    private static String createOptimizedPowerShellWriteCommand(String filePath, String content) {
        // 优化：避免使用多行字符串语法，使用单行转义
        String escapedContent = content.replace("\"", "`\"")
                                      .replace("'", "`'")
                                      .replace("\n", "`n")
                                      .replace("\r", "");
        
        // 确保目录存在
        String dirPath = getDirectoryPath(filePath);
        String ensureDirCommand = "";
        if (!dirPath.isEmpty()) {
            ensureDirCommand = String.format("New-Item -ItemType Directory -Force -Path '%s'; ", dirPath);
        }
        
        return String.format("%sSet-Content -Path '%s' -Value \"%s\"", 
                           ensureDirCommand, filePath, escapedContent);
    }
    
    /**
     * 创建直接文件写入命令（备用方案）
     */
    private static String createDirectFileWriteCommand(String filePath, String content) {
        try {
            // 直接使用Java文件操作
            Path path = Paths.get(filePath);
            Path parentDir = path.getParent();
            
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }
            
            Files.write(path, content.getBytes());
            return "echo '文件已通过Java直接写入'" + filePath;
        } catch (Exception e) {
            // 如果直接写入失败，回退到PowerShell
            return createOptimizedPowerShellWriteCommand(filePath, content);
        }
    }
    
    /**
     * 获取文件路径的目录部分
     */
    private static String getDirectoryPath(String filePath) {
        int lastSlash = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));
        if (lastSlash > 0) {
            return filePath.substring(0, lastSlash);
        }
        return "";
    }
    
    /**
     * 批量命令优化
     */
    public static String optimizeBatchCommands(String[] commands) {
        StringBuilder optimized = new StringBuilder();
        
        for (String command : commands) {
            if (command.startsWith("Set-Content") && command.contains("-Value")) {
                // 提取文件路径和内容进行优化
                String filePath = extractFilePath(command);
                String content = extractContent(command);
                if (filePath != null && content != null) {
                    optimized.append(optimizeWriteCommand(filePath, content)).append("; ");
                } else {
                    optimized.append(command).append("; ");
                }
            } else {
                optimized.append(command).append("; ");
            }
        }
        
        return optimized.toString();
    }
    
    /**
     * 从命令中提取文件路径
     */
    private static String extractFilePath(String command) {
        try {
            int pathIndex = command.indexOf("-Path");
            if (pathIndex > 0) {
                String afterPath = command.substring(pathIndex + 5).trim();
                int spaceIndex = afterPath.indexOf(' ');
                int quoteIndex = afterPath.indexOf('\'');
                
                if (quoteIndex == 0) {
                    int endQuote = afterPath.indexOf('\'', 1);
                    if (endQuote > 0) {
                        return afterPath.substring(1, endQuote);
                    }
                } else if (spaceIndex > 0) {
                    return afterPath.substring(0, spaceIndex);
                }
            }
        } catch (Exception e) {
            // 解析失败，返回null
        }
        return null;
    }
    
    /**
     * 从命令中提取内容
     */
    private static String extractContent(String command) {
        try {
            int valueIndex = command.indexOf("-Value");
            if (valueIndex > 0) {
                String afterValue = command.substring(valueIndex + 6).trim();
                
                // 处理 @'...'@ 语法
                if (afterValue.startsWith("@'")) {
                    int endIndex = afterValue.indexOf("'@", 2);
                    if (endIndex > 0) {
                        return afterValue.substring(2, endIndex);
                    }
                }
                
                // 处理单引号
                if (afterValue.startsWith("'")) {
                    int endQuote = afterValue.indexOf("'", 1);
                    if (endQuote > 0) {
                        return afterValue.substring(1, endQuote);
                    }
                }
                
                // 处理双引号
                if (afterValue.startsWith("\"")) {
                    int endQuote = afterValue.indexOf("\"", 1);
                    if (endQuote > 0) {
                        return afterValue.substring(1, endQuote);
                    }
                }
            }
        } catch (Exception e) {
            // 解析失败，返回null
        }
        return null;
    }
    
    /**
     * 性能监控
     */
    public static void logPerformance(String command, long startTime, long endTime) {
        long duration = endTime - startTime;
        System.out.println("[性能监控] 命令执行耗时: " + duration + "ms - " + 
                          (command.length() > 100 ? command.substring(0, 100) + "..." : command));
        
        if (duration > 5000) { // 超过5秒
            System.out.println("[性能警告] 命令执行较慢，建议优化");
        }
    }
}