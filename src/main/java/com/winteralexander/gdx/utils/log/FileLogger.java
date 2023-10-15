package com.winteralexander.gdx.utils.log;

import com.winteralexander.gdx.utils.io.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Logs to file with time
 * <p>
 * Created on 2017-06-07.
 *
 * @author Alexander Winter
 */
public class FileLogger extends AbstractLogger {
	protected File file;

	public FileLogger(LogLevel logLevel, File file) {
		this(logLevel, file, true);
	}

	public FileLogger(LogLevel logLevel, File file, boolean append) {
		super(logLevel);

		try {
			FileUtil.ensureFile(file);
			if(!append)
				FileUtil.deleteFile(file);
		} catch(IOException ex) {
			throw new RuntimeException("Failed to initialize FileLogger", ex);
		}
		this.file = file;
	}

	@Override
	protected void write(String line) throws IOException {
		PrintStream fos = new PrintStream(new FileOutputStream(file, true));

		fos.println(line);

		fos.flush();
		fos.close();
	}
}
