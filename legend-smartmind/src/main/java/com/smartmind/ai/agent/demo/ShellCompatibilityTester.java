package com.smartmind.ai.agent.demo;

/**
 * Shell命令兼容性测试工具
 */
public class ShellCompatibilityTester {
    
    public static void main(String[] args) {
        System.out.println("=== Shell命令兼容性测试 ===\n");
        
        // 显示系统信息
        System.out.println("系统信息：");
        System.out.println(ShellCommandAdapter.getSystemInfo());
        System.out.println();
        
        // 测试命令转换
        String[] testCommands = {
            "mkdir -p /workspace/ppt-preview && cd /workspace/ppt-preview && pwd",
            "cat /workspace/ppt-preview/outline.md",
            "ls -la /workspace/",
            "echo 'Hello World' > /workspace/test.txt",
            "rm -rf /workspace/temp",
            "New-Item -ItemType Directory -Force -Path 'ppt-preview'",
            "Get-Content 'references/prompts.md'",
            "Set-Location 'E:\\worksapce\\sts5.0\\legend-smartmind' && Get-ChildItem"
        };
        
        System.out.println("命令转换测试：");
        for (String command : testCommands) {
            System.out.println("\n原命令: " + command);
            System.out.println("转换后: " + ShellCommandAdapter.adaptCommand(command));
            System.out.println("可执行: " + ShellCommandAdapter.isExecutable(command));
            System.out.println("是Unix命令: " + ShellCommandAdapter.isUnixCommand(command));
            System.out.println("是PowerShell命令: " + ShellCommandAdapter.isPowerShellCommand(command));
        }
        
        // 测试技能指令生成
        System.out.println("\n=== 技能指令生成测试 ===");
        System.out.println("\nPPT工作流指令：");
        System.out.println(SkillInstructionBuilder.buildPptWorkflowInstruction());
        
        System.out.println("\nHTML转PPT指令：");
        System.out.println(SkillInstructionBuilder.buildHtmlToPptxInstruction());
        
        System.out.println("\n文件操作指令：");
        System.out.println(SkillInstructionBuilder.buildFileOperationInstruction());
    }
}