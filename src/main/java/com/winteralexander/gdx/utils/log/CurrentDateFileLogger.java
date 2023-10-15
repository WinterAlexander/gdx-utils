package com.winteralexander.gdx.utils.log;

import com.winteralexander.gdx.utils.io.FileUtil;

import java.io.File;
import java.io.IOException;

/**
 * File logger that logs to a file with the current date.
 * <p>
 * Created on 2018-06-05.
 *
 * @author Cedric Martens
 */
public class CurrentDateFileLogger extends FileLogger {
	public CurrentDateFileLogger(LogLevel logLevel, File baseDir) {
		super(logLevel, FileUtil.getLogFile(baseDir));
	}

	public CurrentDateFileLogger(LogLevel logLevel, File baseDir, boolean append) {
		super(logLevel, FileUtil.getLogFile(baseDir), append);
	}

	@Override
	protected void write(String line) throws IOException {
		if(!file.getName().equals(FileUtil.getDailyLogfileName()))
			file = FileUtil.getLogFile(file.getParentFile());
		super.write(line);
	}
}
