package com.winteralexander.gdx.utils.async;

import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.OrderedMap;
import com.winteralexander.gdx.utils.error.StackTracker;
import com.winteralexander.gdx.utils.error.Tracker;
import com.winteralexander.gdx.utils.log.Logger;
import com.winteralexander.gdx.utils.log.NullLogger;

import java.util.function.Consumer;

import static com.winteralexander.gdx.utils.ObjectUtil.firstNonNull;
import static com.winteralexander.gdx.utils.Validation.ensureNotNull;

/**
 * Utility class to make async calls with a callback and handle exceptions
 *
 * @param <R> return type of the async call
 */
public class AsyncCaller<R> {
	private static Logger logger = new NullLogger();
	private static ObjectSet<AsyncCaller<?>> calls = new ObjectSet<>();

	private final Call<R> call;
	private Consumer<R> callback;
	private Consumer<Void> finallyCallback;
	private final OrderedMap<Class<? extends Exception>, ExceptionCallback> exCallbacks = new OrderedMap<>();
	private boolean called = false;
	private volatile boolean cancelled = false;

	private final Tracker tracker = StackTracker.cut("AsyncCaller");

	public AsyncCaller(Call<R> call) {
		ensureNotNull(call, "call");
		this.call = call;
	}

	/**
	 * Sets the callback to be executed when async task is done without any errors
	 *
	 * @param callback callback to be executed
	 * @return the same AsyncCaller
	 */
	public AsyncCaller<R> then(Consumer<R> callback) {
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
	public AsyncCaller<R> then(Consumer<R> callback, CallbackWrapper wrapper) {
		this.callback = wrapper.wrap(callback);
		return this;
	}

	/**
	 * Adds a callback to a specific exception type. Any exception matching
	 * specific type exactly or via inheritance will be called. If you add an
	 * exception callback to the same exception type twice, the first one will
	 * be overwritten.
	 *
	 * @param type     type to match
	 * @param callback exception callback
	 * @param <T>      type of the exception
	 * @return the same AsyncCaller
	 */
	public <T extends Exception> AsyncCaller<R> except(Class<T> type,
	                                                   Consumer<T> callback) {
		exCallbacks.put(type, new ExceptionCallback(callback, true));
		return this;
	}

	/**
	 * @see #except(Class, Consumer)
	 * <p>
	 * This method wraps the callback using specified CallbackWrapper.
	 */
	public <T extends Exception> AsyncCaller<R> except(Class<T> type,
	                                                   Consumer<T> callback,
	                                                   CallbackWrapper wrapper) {
		return except(type, wrapper.wrap(callback));
	}

	public <T extends Exception> AsyncCaller<R> exceptRetry(Class<T> type,
	                                                        Consumer<T> retryCallback) {
		exCallbacks.put(type, new ExceptionCallback(retryCallback, true));
		return this;
	}

	public <T extends Exception> AsyncCaller<R> exceptRetry(Class<T> type,
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
	public <T extends Exception> AsyncCaller<R> except(Class<? extends T> type1,
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
	public <T extends Exception> AsyncCaller<R> except(Class<? extends T> type1,
	                                                   Class<? extends T> type2,
	                                                   Consumer<T> callback,
	                                                   CallbackWrapper wrapper) {
		return except(type1, type2, wrapper.wrap(callback));
	}

	/**
	 * @see #except(Class, Class, Consumer)
	 */
	public <T extends Exception> AsyncCaller<R> except(Class<? extends T> type1,
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
	public <T extends Exception> AsyncCaller<R> except(Class<? extends T> type1,
	                                                   Class<? extends T> type2,
	                                                   Class<? extends T> type3,
	                                                   Consumer<T> callback,
	                                                   CallbackWrapper wrapper) {
		return except(type1, type2, type3, wrapper.wrap(callback));
	}

	public <T extends Exception> AsyncCaller<R> except(Class<? extends T>[] types,
	                                                   Consumer<T> callback) {
		for(Class<? extends T> type : types) {
			//noinspection unchecked
			except((Class<T>)type, callback);
		}
		return this;
	}

	public <T extends Exception> AsyncCaller<R> except(Class<? extends T>[] types,
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
	public AsyncCaller<R> always(Runnable callback) {
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
	public AsyncCaller<R> always(Runnable callback, CallbackWrapper wrapper) {
		this.finallyCallback = wrapper.wrap(v -> callback.run());
		return this;
	}

	public void cancel() {
		cancelled = true;
	}

	/**
	 * Executes the async call
	 */
	public void execute() {
		called = true;

		if(cancelled)
			return;

		new Thread(() -> {
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
						retry = dispatch(ex);
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
		}, "AsyncCaller execution").start();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void finalize() {
		if(!called)
			logger.error("AsyncCaller was destroyed without ever being executed !", tracker.get());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean dispatch(Exception exception) {
		for(Entry<Class<? extends Exception>,
				ExceptionCallback> entry : exCallbacks.entries()) {
			if(entry.key.isInstance(exception)) {
				((Consumer)entry.value.callback).accept(exception);
				return entry.value.retry;
			}
		}

		logger.error("Unhandled exception in AsyncCaller!", exception);
		return false;
	}

	public static void setLogger(Logger logger) {
		AsyncCaller.logger = firstNonNull(logger, new NullLogger());
	}

	/**
	 * Async call to a function without any parameters that returns void
	 *
	 * @param function function with no params
	 * @return AsyncCaller of the function
	 */
	public static AsyncCaller<Void> async(CheckedVoidAction function) {
		return new AsyncCaller<>(() -> {
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
	public static <R> AsyncCaller<R> async(CheckedSupplier<R> function) {
		return new AsyncCaller<>(function::call);
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
	public static <P, R> AsyncCaller<R> async(CheckedFunction<P, R> function, P param) {
		return new AsyncCaller<>(() -> function.call(param));
	}

	public static <P1, P2, R> AsyncCaller<R> async(CheckedBiFunction<P1, P2, R> function,
	                                               P1 param1, P2 param2) {
		return new AsyncCaller<>(() -> function.call(param1, param2));
	}

	public static <P1, P2, P3, R> AsyncCaller<R> async(CheckedTriFunction<P1, P2, P3, R> function,
	                                                   P1 param1, P2 param2, P3 param3) {
		return new AsyncCaller<>(() -> function.call(param1, param2, param3));
	}

	public static <P1, P2, P3, P4, R>
	AsyncCaller<R> async(CheckedQuadriFunction<P1, P2, P3, P4, R> function,
	                     P1 param1, P2 param2, P3 param3, P4 param4) {
		return new AsyncCaller<>(() -> function.call(param1, param2, param3, param4));
	}

	public static <P1, P2, P3, P4, P5, R>
	AsyncCaller<R> async(CheckedPentaFunction<P1, P2, P3, P4, P5, R> function,
	                     P1 param1, P2 param2, P3 param3, P4 param4, P5 param5) {
		return new AsyncCaller<>(() -> function.call(param1, param2, param3, param4, param5));
	}

	public static <P> AsyncCaller<Void> async(CheckedVoidFunction<P> function, P param) {
		return new AsyncCaller<>(() -> {
			function.call(param);
			return null;
		});
	}

	public static <P1, P2> AsyncCaller<Void> async(CheckedBiVoidFunction<P1, P2> function,
	                                               P1 param1,
	                                               P2 param2) {
		return new AsyncCaller<>(() -> {
			function.call(param1, param2);
			return null;
		});
	}

	public static <P1, P2, P3> AsyncCaller<Void> async(CheckedTriVoidFunction<P1, P2, P3> function,
	                                                   P1 param1,
	                                                   P2 param2,
	                                                   P3 param3) {
		return new AsyncCaller<>(() -> {
			function.call(param1, param2, param3);
			return null;
		});
	}

	public static <P1, P2, P3, P4>
	AsyncCaller<Void> async(CheckedQuadriVoidFunction<P1, P2, P3, P4> function,
	                        P1 param1, P2 param2, P3 param3, P4 param4) {
		return new AsyncCaller<>(() -> {
			function.call(param1, param2, param3, param4);
			return null;
		});
	}

	public static <P1, P2, P3, P4, P5>
	AsyncCaller<Void> async(CheckedPentaVoidFunction<P1, P2, P3, P4, P5> function,
	                        P1 param1, P2 param2, P3 param3, P4 param4, P5 param5) {
		return new AsyncCaller<>(() -> {
			function.call(param1, param2, param3, param4, param5);
			return null;
		});
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
		public final boolean retry;

		public ExceptionCallback(Consumer<? extends Exception> callback, boolean retry) {
			ensureNotNull(callback, "callback");
			this.callback = callback;
			this.retry = retry;
		}
	}
}
