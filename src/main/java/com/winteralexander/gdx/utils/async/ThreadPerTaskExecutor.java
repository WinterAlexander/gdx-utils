package com.winteralexander.gdx.utils.async;

import java.util.concurrent.Executor;

import static com.winteralexander.gdx.utils.Validation.ensureNotNull;

/**
 * {@link Executor} that opens a new thread for every task
 * <p>
 * Created on 2023-10-24.
 *
 * @author Alexander Winter
 */
public class ThreadPerTaskExecutor implements Executor {
	private final String name;

	public ThreadPerTaskExecutor(String name) {
		ensureNotNull(name, "name");
		this.name = name;
	}

	@Override
	public void execute(Runnable command) {
		Thread thread = new Thread(command, name);
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.start();
	}
}
