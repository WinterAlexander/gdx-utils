package com.winteralexander.gdx.utils.async;

import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.badlogic.gdx.utils.OrderedMap;
import com.winteralexander.gdx.utils.SystemUtil;
import com.winteralexander.gdx.utils.error.StackTracker;
import com.winteralexander.gdx.utils.error.Tracker;
import com.winteralexander.gdx.utils.log.Logger;
import com.winteralexander.gdx.utils.log.NullLogger;

import java.util.function.Consumer;

import static com.winteralexander.gdx.utils.Validation.ensureNotNull;

/**
 * Utility class to make async calls with a callback and handle exceptions
 *
 * @param <R> return type of the async call
 */
public class AsyncCall<R> {
	private static final AsyncCallManager defaultManager = new AsyncCallManager(new NullLogger(),
			50L,
			new ThreadPerTaskExecutor("AsyncCall executor"));

	private final AsyncCallManager manager;
	private final Call<R> call;
	private Consumer<R> callback;
	private Consumer<Void> finallyCallback;
	private final OrderedMap<Class<? extends Exception>, ExceptionCallback> exCallbacks = new OrderedMap<>();
	private boolean called = false;
	private volatile boolean cancelled = false;

	private final Tracker tracker = StackTracker.cut("AsyncCaller");

	public AsyncCall(AsyncCallManager manager, Call<R> call) {
		ensureNotNull(manager, "manager");
		ensureNotNull(call, "call");
		this.manager = manager;
		this.call = call;
	}

	/**
	 * Sets the callback to be executed when async task is done without any errors
	 *
	 * @param callback callback to be executed
	 * @return the same AsyncCaller
	 */
	public AsyncCall<R> then(Consumer<R> callback) {
		if(this.callback != null)
			throw new IllegalStateException("A callback was already set for this call");

		this.callback = callback;
		return this;
	}

	/**
	 * Sets the callback to be executed when async task is completed without any errors.
	 * <p>
	 * This method wraps the callback using specified CallbackWrapper
	 *
	 * @param callback callback to be executed
	 * @param wrapper  wrapper of the callback
	 * @return the same AsyncCaller
	 */
	public AsyncCall<R> then(Consumer<R> callback, CallbackWrapper wrapper) {
		this.callback = wrapper.wrap(callback);
		return this;
	}

	/**
	 * Adds a callback to a specific exception type. Any exception matching
	 * specific type exactly or via inheritance will be called. If you add an
	 * exception callback to the same exception type twice, the first one will
	 * be overwritten.
	 *
	 * @param type type to match
	 * @param callback exception callback
	 * @param <T> type of the exception
	 * @return the same AsyncCaller
	 */
	public <T extends Exception> AsyncCall<R> except(Class<T> type,
	                                                 Consumer<T> callback) {
		exCallbacks.put(type, new ExceptionCallback(callback, -1L));
		return this;
	}

	/**
	 * Adds a callback to a specific exception type. Any exception matching
	 * specific type exactly or via inheritance will be called. If you add an
	 * exception callback to the same exception type twice, the first one will
	 * be overwritten.
	 * <p>
	 * This method wraps the callback using specified CallbackWrapper.
	 *
	 * @param type type to match
	 * @param callback exception callback
	 * @param <T> type of the exception
	 * @return the same AsyncCaller
	 */
	public <T extends Exception> AsyncCall<R> except(Class<T> type,
	                                                 Consumer<T> callback,
	                                                 CallbackWrapper wrapper) {
		return except(type, wrapper.wrap(callback));
	}

	/**
	 * Adds an automatic retrying behavior on an exception type with an empty callback. Any
	 * exception matching the specified type exactly or via inheritance will be set to retry.
	 * Overwrites any previously set callbacks or retry configuration for this exception type.
	 *
	 * @param type type to match
	 * @param <T> type of the exception
	 * @return the same AsyncCaller
	 */
	public <T extends Exception> AsyncCall<R> exceptRetry(Class<T> type) {
		return exceptRetry(type, manager.getDefaultRetryDelay(), ex -> {});
	}

	/**
	 * Adds an automatic retrying behavior on an exception type with a provided callback. The
	 * callback is called everytime the async caller is about to retry the call. Any exception
	 * matching the specified type exactly or via inheritance will be set to retry. Overwrites
	 * any previously set callbacks or retry configuration for this exception type.
	 *
	 * @param type type to match
	 * @param <T> type of the exception
	 * @return the same AsyncCaller
	 */
	public <T extends Exception> AsyncCall<R> exceptRetry(Class<T> type,
	                                                      Consumer<T> retryCallback) {
		return exceptRetry(type, manager.getDefaultRetryDelay(), retryCallback);
	}

	/**
	 * Adds an automatic retrying behavior on an exception type with an empty callback. Any
	 * exception matching the specified type exactly or via inheritance will be set to retry.
	 * Overwrites any previously set callbacks or retry configuration for this exception type.
	 *
	 * @param type type to match
	 * @param retryDelay delay before retrying
	 * @param <T> type of the exception
	 * @return the same AsyncCaller
	 */
	public <T extends Exception> AsyncCall<R> exceptRetry(Class<T> type, long retryDelay) {
		return exceptRetry(type, retryDelay, ex -> {});
	}

	/**
	 * Adds an automatic retrying behavior on an exception type with a provided callback. The
	 * callback is called everytime the async caller is about to retry the call. Any exception
	 * matching the specified type exactly or via inheritance will be set to retry. Overwrites
	 * any previously set callbacks or retry configuration for this exception type.
	 *
	 * @param type type to match
	 * @param retryDelay delay before retrying
	 * @param <T> type of the exception
	 * @return the same AsyncCaller
	 */
	public <T extends Exception> AsyncCall<R> exceptRetry(Class<T> type,
	                                                      long retryDelay,
	                                                      Consumer<T> retryCallback) {
		exCallbacks.put(type, new ExceptionCallback(retryCallback, retryDelay));
		return this;
	}

	/**
	 * Adds an automatic retrying behavior on an exception type with a provided callback. The
	 * callback is called everytime the async caller is about to retry the call. Any exception
	 * matching the specified type exactly or via inheritance will be set to retry. Overwrites
	 * any previously set callbacks or retry configuration for this exception type.
	 * <p>
	 * This method wraps the callback using specified CallbackWrapper. Note that since the callback
	 * is wrapped, the code executed for callback may be run in another thread and end up executing
	 * after the async caller has restarted for its retry attempt.
	 *
	 * @param type type to match
	 * @param <T> type of the exception
	 * @return the same AsyncCaller
	 */
	public <T extends Exception> AsyncCall<R> exceptRetry(Class<T> type,
	                                                      Consumer<T> callback,
	                                                      CallbackWrapper wrapper) {
		return exceptRetry(type, wrapper.wrap(callback));
	}

	/**
	 * Adds a callback to an array of exception types which are all inheriting a base exception
	 * type which is the type passed to the callback. If one of the provided exception type was
	 * already added as a callback it will be overwritten with the new callback.
	 *
	 * @param type1    first type to match
	 * @param type2    second type to match
	 * @param callback exception callback
	 * @param <T>      base type of the exceptions
	 * @return this AsyncCaller, for chaining
	 */
	public <T extends Exception> AsyncCall<R> except(Class<? extends T> type1,
	                                                 Class<? extends T> type2,
	                                                 Consumer<T> callback) {
		//noinspection unchecked
		except((Class<T>)type1, callback);
		//noinspection unchecked
		except((Class<T>)type2, callback);
		return this;
	}

	/**
	 * @see #except(Class, Class, Consumer)
	 * <p>
	 * This method wraps the callback using specified CallbackWrapper.
	 */
	public <T extends Exception> AsyncCall<R> except(Class<? extends T> type1,
	                                                 Class<? extends T> type2,
	                                                 Consumer<T> callback,
	                                                 CallbackWrapper wrapper) {
		return except(type1, type2, wrapper.wrap(callback));
	}

	/**
	 * @see #except(Class, Class, Consumer)
	 */
	public <T extends Exception> AsyncCall<R> except(Class<? extends T> type1,
	                                                 Class<? extends T> type2,
	                                                 Class<? extends T> type3,
	                                                 Consumer<T> callback) {
		//noinspection unchecked
		except((Class<T>)type1, callback);
		//noinspection unchecked
		except((Class<T>)type2, callback);
		//noinspection unchecked
		except((Class<T>)type3, callback);
		return this;
	}

	/**
	 * @see #except(Class, Class, Class, Consumer)
	 * <p>
	 * This method wraps the callback using specified CallbackWrapper.
	 */
	public <T extends Exception> AsyncCall<R> except(Class<? extends T> type1,
	                                                 Class<? extends T> type2,
	                                                 Class<? extends T> type3,
	                                                 Consumer<T> callback,
	                                                 CallbackWrapper wrapper) {
		return except(type1, type2, type3, wrapper.wrap(callback));
	}

	public <T extends Exception> AsyncCall<R> except(Class<? extends T>[] types,
	                                                 Consumer<T> callback) {
		for(Class<? extends T> type : types) {
			//noinspection unchecked
			except((Class<T>)type, callback);
		}
		return this;
	}

	public <T extends Exception> AsyncCall<R> except(Class<? extends T>[] types,
	                                                 Consumer<T> callback,
	                                                 CallbackWrapper wrapper) {
		return except(types, wrapper.wrap(callback));
	}

	/**
	 * Adds a callback to be always executed regardless of the async call failing
	 *
	 * @param callback callback to always call
	 * @return the same AsyncCaller
	 */
	public AsyncCall<R> always(Runnable callback) {
		this.finallyCallback = v -> callback.run();
		return this;
	}

	/**
	 * Adds a callback to be always executed regardless of the async call failing
	 * <p>
	 * This method wraps the callback using specified CallbackWrapper
	 *
	 * @param callback callback to always call
	 * @return the same AsyncCaller
	 */
	public AsyncCall<R> always(Runnable callback, CallbackWrapper wrapper) {
		this.finallyCallback = wrapper.wrap(v -> callback.run());
		return this;
	}

	public void cancel() {
		cancelled = true;
	}

	private void run() {
		StackTracker.enter(tracker);
		boolean retry;
		do {
			retry = false;
			try {
				R value = call.execute();
				if(callback != null && !cancelled)
					callback.accept(value);
			} catch(Exception ex) {
				if(!cancelled) {
					StackTracker.appendFullStack(ex);
					long delay = dispatch(ex);
					retry = delay >= 0;
					if(retry)
						SystemUtil.sleepIfRequired(delay);
				}
			} finally {
				if(!retry && !cancelled)
				{
					if(finallyCallback != null)
						finallyCallback.accept(null);
					StackTracker.exit(tracker);
				}
			}
		}
		while(retry && !cancelled);
	}

	/**
	 * Executes the async call
	 */
	public void execute() {
		called = true;

		if(cancelled)
			return;

		manager.getExecutor().execute(this::run);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void finalize() {
		if(!called)
			manager.getLogger().error("AsyncCaller was destroyed without ever being executed !", tracker.get());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private long dispatch(Exception exception) {
		for(Entry<Class<? extends Exception>,
				ExceptionCallback> entry : exCallbacks.entries()) {
			if(entry.key.isInstance(exception)) {
				((Consumer)entry.value.callback).accept(exception);
				return entry.value.retryDelay;
			}
		}

		manager.getLogger().error("Unhandled exception in AsyncCaller", exception);
		return -1;
	}

	public static void setLogger(Logger logger) {
		defaultManager.setLogger(logger);
	}

	/**
	 * Async call to a function without any parameters that returns void
	 *
	 * @param function function with no params
	 * @return AsyncCaller of the function
	 */
	public static AsyncCall<Void> async(CheckedVoidAction function) {
		return defaultManager.async(function);
	}

	/**
	 * Async call to a function without any parameters
	 *
	 * @param function function with no params
	 * @param <R>      return type of function
	 * @return AsyncCaller of the function
	 */
	public static <R> AsyncCall<R> async(CheckedSupplier<R> function) {
		return defaultManager.async(function);
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
	public static <P, R> AsyncCall<R> async(CheckedFunction<P, R> function, P param) {
		return defaultManager.async(function, param);
	}

	public static <P1, P2, R> AsyncCall<R> async(CheckedBiFunction<P1, P2, R> function,
	                                             P1 param1, P2 param2) {
		return defaultManager.async(function, param1, param2);
	}

	public static <P1, P2, P3, R> AsyncCall<R> async(CheckedTriFunction<P1, P2, P3, R> function,
	                                                 P1 param1, P2 param2, P3 param3) {
		return defaultManager.async(function, param1, param2, param3);
	}

	public static <P1, P2, P3, P4, R> AsyncCall<R> async(CheckedQuadriFunction<P1, P2, P3, P4, R> function,
	                                                     P1 param1, P2 param2, P3 param3, P4 param4) {
		return defaultManager.async(function, param1, param2, param3, param4);
	}

	public static <P1, P2, P3, P4, P5, R> AsyncCall<R> async(CheckedPentaFunction<P1, P2, P3, P4, P5, R> function,
	                                                         P1 param1, P2 param2, P3 param3, P4 param4, P5 param5) {
		return defaultManager.async(function, param1, param2, param3, param4, param5);
	}

	public static <P> AsyncCall<Void> async(CheckedVoidFunction<P> function, P param) {
		return defaultManager.async(function, param);
	}

	public static <P1, P2> AsyncCall<Void> async(CheckedBiVoidFunction<P1, P2> function,
	                                             P1 param1,
	                                             P2 param2) {
		return defaultManager.async(function, param1, param2);
	}

	public static <P1, P2, P3> AsyncCall<Void> async(CheckedTriVoidFunction<P1, P2, P3> function,
	                                                 P1 param1,
	                                                 P2 param2,
	                                                 P3 param3) {
		return defaultManager.async(function, param1, param2, param3);
	}

	public static <P1, P2, P3, P4> AsyncCall<Void> async(CheckedQuadriVoidFunction<P1, P2, P3, P4> function,
	                                                     P1 param1, P2 param2, P3 param3, P4 param4) {
		return defaultManager.async(function, param1, param2, param3, param4);
	}

	public static <P1, P2, P3, P4, P5> AsyncCall<Void> async(CheckedPentaVoidFunction<P1, P2, P3, P4, P5> function,
	                                                         P1 param1, P2 param2, P3 param3, P4 param4, P5 param5) {
		return defaultManager.async(function, param1, param2, param3, param4, param5);
	}

	/**
	 * Represents a function call
	 * <p>
	 * Created on 2017-08-26.
	 *
	 * @author Alexander Winter
	 */
	@FunctionalInterface
	public interface Call<R> {
		R execute() throws Exception;
	}

	@FunctionalInterface
	public interface CheckedSupplier<R> {
		R call() throws Exception;
	}

	@FunctionalInterface
	public interface CheckedFunction<P, R> {
		R call(P param1) throws Exception;
	}

	@FunctionalInterface
	public interface CheckedBiFunction<P1, P2, R> {
		R call(P1 param1, P2 param2) throws Exception;
	}

	@FunctionalInterface
	public interface CheckedTriFunction<P1, P2, P3, R> {
		R call(P1 param1, P2 param2, P3 param3) throws Exception;
	}

	@FunctionalInterface
	public interface CheckedQuadriFunction<P1, P2, P3, P4, R> {
		R call(P1 param1, P2 param2, P3 param3, P4 param4) throws Exception;
	}

	@FunctionalInterface
	public interface CheckedPentaFunction<P1, P2, P3, P4, P5, R> {
		R call(P1 param1, P2 param2, P3 param3, P4 param4, P5 param5) throws Exception;
	}

	@FunctionalInterface
	public interface CheckedVoidAction {
		void call() throws Exception;
	}

	@FunctionalInterface
	public interface CheckedVoidFunction<P> {
		void call(P param1) throws Exception;
	}

	@FunctionalInterface
	public interface CheckedBiVoidFunction<P1, P2> {
		void call(P1 param1, P2 param2) throws Exception;
	}

	@FunctionalInterface
	public interface CheckedTriVoidFunction<P1, P2, P3> {
		void call(P1 param1, P2 param2, P3 param3) throws Exception;
	}

	@FunctionalInterface
	public interface CheckedQuadriVoidFunction<P1, P2, P3, P4> {
		void call(P1 param1, P2 param2, P3 param3, P4 param4) throws Exception;
	}

	@FunctionalInterface
	public interface CheckedPentaVoidFunction<P1, P2, P3, P4, P5> {
		void call(P1 param1, P2 param2, P3 param3, P4 param4, P5 param5) throws Exception;
	}

	private static class ExceptionCallback {
		public final Consumer<? extends Exception> callback;
		/**
		 * Number of milliseconds before retrying the request, or -1 if not retrying
		 */
		public final long retryDelay;

		public ExceptionCallback(Consumer<? extends Exception> callback, long retryDelay) {
			ensureNotNull(callback, "callback");
			this.callback = callback;
			this.retryDelay = retryDelay;
		}
	}
}
