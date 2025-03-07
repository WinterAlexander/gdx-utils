package com.winteralexander.gdx.utils.system;

import static com.winteralexander.gdx.utils.Validation.ensureNotNull;

/**
 * Thrown when a process encounters an error (non zero exit code)
 * <p>
 * Created on 2025-03-06.
 *
 * @author Alexander Winter
 */
public class ProcessErrorException extends Exception {
	private final String command;
	private final int exitCode;
	private final String output, error;

	public ProcessErrorException(String command, int exitCode, String output, String error) {
		super("Process `" + command + "` encountered error exit code (" + exitCode + "), " +
				"Output: " + output + ", Error: " + error);
		ensureNotNull(command, "command");
		ensureNotNull(output, "output");
		ensureNotNull(error, "error");
		this.command = command;
		this.exitCode = exitCode;
		this.output = output;
		this.error = error;
	}

	public String getCommand() {
		return command;
	}

	public int getExitCode() {
		return exitCode;
	}

	public String getOutput() {
		return output;
	}

	public String getError() {
		return error;
	}
}
