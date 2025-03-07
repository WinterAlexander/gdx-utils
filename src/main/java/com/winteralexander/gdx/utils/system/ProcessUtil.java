package com.winteralexander.gdx.utils.system;

import com.winteralexander.gdx.utils.StringUtil;

import java.io.IOException;

import static com.winteralexander.gdx.utils.StringUtil.join;
import static sun.misc.IOUtils.readAllBytes;

/**
 * Utility class to manipulate or launch system processes
 * <p>
 * Created on 2025-03-06.
 *
 * @author Alexander Winter
 */
public class ProcessUtil {
	public static String execute(String... command) throws ProcessErrorException, IOException, InterruptedException {
		Process process = new ProcessBuilder(command).start();
		int exitCode = process.waitFor();

		String output = new String(readAllBytes(process.getInputStream()));

		if(exitCode != 0) {
			String error = new String(readAllBytes(process.getErrorStream()));
			throw new ProcessErrorException(join(command, " "), exitCode, output, error);
		}

		return output;
	}
}
