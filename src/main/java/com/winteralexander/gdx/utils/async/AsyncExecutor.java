package com.winteralexander.gdx.utils.async;

import java.util.concurrent.Executor;

import static com.winteralexander.gdx.utils.Validation.ensureNotNull;

/**
 * Executor that uses an {@link AsyncCallManager} to execute its runnables
 * <p>
 * Created on 2018-12-23.
 *
 * @author Alexander Winter
 */
public class AsyncExecutor implements Executor {
	private final AsyncCallManager asyncManager;

	public AsyncExecutor(AsyncCallManager asyncManager) {
		ensureNotNull(asyncManager, "asyncManager");
		this.asyncManager = asyncManager;
	}

	@Override
	public void execute(Runnable command) {
		asyncManager.async(command::run)
		.except(Exception.class,
				ex -> asyncManager.getLogger().error("Exception in AsyncExecutor", ex))
		.execute();
	}
}
