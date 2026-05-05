package com.alibaba.cloud.ai.graph.agent.tools;


import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 简单的 Node.js 执行工具
 */
public class NodeTool implements BiFunction<NodeTool.NodeRequest, ToolContext, String> {

    public static final String DESCRIPTION = "执行 JavaScript/Node.js 代码。用于生成 PPT 时，使用 pptxgenjs 库。";

    private final String nodeCommand;

    public NodeTool() {
        this("node");
    }

    public NodeTool(String nodeCommand) {
        this.nodeCommand = nodeCommand;
    }

    public static ToolCallback createNodeToolCallback() {
        return FunctionToolCallback.builder("node_tool", new NodeTool())
                .description(DESCRIPTION)
                .inputType(NodeRequest.class)
                .build();
    }

    @Override
    public String apply(NodeRequest request, ToolContext context) {
        if (request.code == null || request.code.trim().isEmpty()) {
            return "错误: 代码不能为空";
        }

        try {
            // 创建临时文件
            Path tempFile = Files.createTempFile("script_", ".js");
            Files.writeString(tempFile, request.code);

            // 执行 node
            ProcessBuilder pb = new ProcessBuilder(nodeCommand, tempFile.toString());
            pb.redirectErrorStream(true);
            pb.directory(new java.io.File(System.getProperty("user.dir")));

            Process process = pb.start();

            // 读取输出
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            // 等待完成
            boolean finished = process.waitFor(60, TimeUnit.SECONDS);
            Files.deleteIfExists(tempFile);

            if (!finished) {
                process.destroyForcibly();
                return "错误: 执行超时 (60秒)";
            }

            int exitCode = process.exitValue();
            String result = output.toString().trim();

            if (exitCode != 0) {
                return "执行失败 (exit code: " + exitCode + "):\n" + result;
            }

            return result.isEmpty() ? "执行成功" : result;

        } catch (Exception e) {
            return "执行错误: " + e.getMessage();
        }
    }

    public static class NodeRequest {
        @JsonProperty(required = true)
        public String code;

        public NodeRequest() {}
    }
}