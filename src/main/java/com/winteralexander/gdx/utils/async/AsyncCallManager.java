package com.winteralexander.gdx.utils.async;

import com.winteralexander.gdx.utils.log.Logger;
import com.winteralexander.gdx.utils.log.NullLogger;

import java.util.concurrent.Executor;

import static com.winteralexander.gdx.utils.ObjectUtil.firstNonNull;
import static com.winteralexander.gdx.utils.Validation.ensureNotNull;
import static com.winteralexander.gdx.utils.Validation.ensurePositive;

/**
 * Manages async calls
 * <p>
 * Created on 2023-10-24.
 *
 * @author Alexander Winter
 */
public class AsyncCallManager {
	private Logger logger;
	private final Executor executor;

	/**
	 * Default delay before retrying a request, in milliseconds
	 */
	private final long defaultRetryDelay;

	public AsyncCallManager(Logger logger, long defaultRetryDelay, Executor executor) {
		ensureNotNull(executor, "executor");
		ensurePositive(defaultRetryDelay, "defaultRetryDelay");
		setLogger(logger);
		this.executor = executor;
		this.defaultRetryDelay = defaultRetryDelay;
	}
	/**
	 * Async call to a function without any parameters that returns void
	 *
	 * @param function function with no params
	 * @return AsyncCaller of the function
	 */
	public AsyncCall<Void> async(AsyncCall.CheckedVoidAction function) {
		return new AsyncCall<>(this, () -> {
			function.call();
			return null;
		});
	}

	/**
	 * Async call to a function without any parameters
	 *
	 * @param function function with no params
	 * @param <R>      return type of function
	 * @return AsyncCaller of the function
	 */
	public <R> AsyncCall<R> async(AsyncCall.CheckedSupplier<R> function) {
		return new AsyncCall<>(this, function::call);
	}

	/**
	 * Async call to a function with 1 parameter
	 *
	 * @param function function with 1 parameter
	 * @param param    parameter to give to the function
	 * @param <P>      param type
	 * @param <R>      result type of the function
	 * @return AsyncCaller of the function
	 */
	public <P, R> AsyncCall<R> async(AsyncCall.CheckedFunction<P, R> function, P param) {
		return new AsyncCall<>(this, () -> function.call(param));
	}

	public <P1, P2, R> AsyncCall<R> async(AsyncCall.CheckedBiFunction<P1, P2, R> function,
	                                      P1 param1, P2 param2) {
		return new AsyncCall<>(this, () -> function.call(param1, param2));
	}

	public <P1, P2, P3, R> AsyncCall<R> async(AsyncCall.CheckedTriFunction<P1, P2, P3, R> function,
	                                          P1 param1, P2 param2, P3 param3) {
		return new AsyncCall<>(this, () -> function.call(param1, param2, param3));
	}

	public <P1, P2, P3, P4, R> AsyncCall<R> async(AsyncCall.CheckedQuadriFunction<P1, P2, P3, P4, R> function,
	                                              P1 param1, P2 param2, P3 param3, P4 param4) {
		return new AsyncCall<>(this, () -> function.call(param1, param2, param3, param4));
	}

	public <P1, P2, P3, P4, P5, R> AsyncCall<R> async(AsyncCall.CheckedPentaFunction<P1, P2, P3, P4, P5, R> function,
	                                                  P1 param1, P2 param2, P3 param3, P4 param4, P5 param5) {
		return new AsyncCall<>(this, () -> function.call(param1, param2, param3, param4, param5));
	}

	public <P> AsyncCall<Void> async(AsyncCall.CheckedVoidFunction<P> function, P param) {
		return new AsyncCall<>(this, () -> {
			function.call(param);
			return null;
		});
	}

	public <P1, P2> AsyncCall<Void> async(AsyncCall.CheckedBiVoidFunction<P1, P2> function,
	                                      P1 param1,
	                                      P2 param2) {
		return new AsyncCall<>(this, () -> {
			function.call(param1, param2);
			return null;
		});
	}

	public <P1, P2, P3> AsyncCall<Void> async(AsyncCall.CheckedTriVoidFunction<P1, P2, P3> function,
	                                          P1 param1,
	                                          P2 param2,
	                                          P3 param3) {
		return new AsyncCall<>(this, () -> {
			function.call(param1, param2, param3);
			return null;
		});
	}

	public <P1, P2, P3, P4> AsyncCall<Void> async(AsyncCall.CheckedQuadriVoidFunction<P1, P2, P3, P4> function,
	                                              P1 param1, P2 param2, P3 param3, P4 param4) {
		return new AsyncCall<>(this, () -> {
			function.call(param1, param2, param3, param4);
			return null;
		});
	}

	public <P1, P2, P3, P4, P5> AsyncCall<Void> async(AsyncCall.CheckedPentaVoidFunction<P1, P2, P3, P4, P5> function,
	                                                  P1 param1, P2 param2, P3 param3, P4 param4, P5 param5) {
		return new AsyncCall<>(this, () -> {
			function.call(param1, param2, param3, param4, param5);
			return null;
		});
	}

	public Logger getLogger() {
		return logger;
	}

	public Executor getExecutor() {
		return executor;
	}

	public long getDefaultRetryDelay() {
		return defaultRetryDelay;
	}

	public void setLogger(Logger logger) {
		this.logger = firstNonNull(logger, new NullLogger());
	}
}
