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
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tool for executing Python code using system Python interpreter.
 *
 * This tool executes Python code in the host system's Python environment,
 * allowing access to all installed packages (pip, conda, etc.).
 */
public class PythonTool3 implements BiFunction<PythonTool3.PythonRequest, ToolContext, String> {

	public static final String DESCRIPTION = "Execute a shell command inside a persistent session. Before running a command, "
					+ "confirm the working directory is correct (e.g., inspect with `ls` or `pwd`) and ensure "
					+ "any parent directories exist. Prefer absolute paths and quote paths containing spaces, "
					+ "such as `cd \"/path/with spaces\"`. Chain multiple commands with `&&` or `;` instead of "
					+ "embedding newlines. Avoid unnecessary `cd` usage unless explicitly required so the "
					+ "session remains stable. Outputs may be truncated when they become very large, and long "
					+ "running commands will be terminated once their configured timeout elapses.";

	private static final Logger log = LoggerFactory.getLogger(PythonTool3.class);

	private final String pythonCommand;

	public PythonTool3() {
		this("python");
	}

	public PythonTool3(String pythonCommand) {
		this.pythonCommand = pythonCommand;
	}

	/**
	 * Create a ToolCallback for the Python tool.
	 */
	public static ToolCallback createPythonToolCallback(String description) {
		return FunctionToolCallback.builder("python_tool", new PythonTool3())
				.description(description)
				.inputType(PythonRequest.class)
				.build();
	}

	@Override
	public String apply(PythonRequest request, ToolContext toolContext) {
		if (request.code == null || request.code.trim().isEmpty()) {
			return "Error: Python code cannot be empty";
		}

		try {
			log.debug("Executing Python code: {}", request.code);

			// Create temporary Python file
			Path tempFile = Files.createTempFile("python_script_", ".py");
			Files.writeString(tempFile, request.code);

			// Build process
			ProcessBuilder pb = new ProcessBuilder(pythonCommand, tempFile.toString());
			pb.redirectErrorStream(true);

			// Set working directory and environment
			pb.directory(new java.io.File(System.getProperty("user.dir")));

			// Start process
			Process process = pb.start();

			// Read output
			StringBuilder output = new StringBuilder();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
				String line;
				while ((line = reader.readLine()) != null) {
					output.append(line).append("\n");
				}
			}

			// Wait for completion (with timeout)
			boolean finished = process.waitFor(60, TimeUnit.SECONDS);
			if (!finished) {
				process.destroyForcibly();
				Files.deleteIfExists(tempFile);
				return "Error: Python execution timeout (60s)";
			}

			// Clean up temp file
			Files.deleteIfExists(tempFile);

			int exitCode = process.exitValue();
			String result = output.toString().trim();

			if (exitCode != 0) {
				return "Python execution failed (exit code: " + exitCode + "): " + result;
			}

			return result.isEmpty() ? "Execution completed successfully" : result;

		} catch (Exception e) {
			log.error("Error executing Python code", e);
			return "Error executing Python code: " + e.getMessage();
		}
	}

	/**
	 * Request structure for the Python tool.
	 */
	public static class PythonRequest {

		@JsonProperty(required = true)
		@JsonPropertyDescription("The Python code to execute")
		public String code;

		public PythonRequest() {
		}

		public PythonRequest(String code) {
			this.code = code;
		}
	}
}
