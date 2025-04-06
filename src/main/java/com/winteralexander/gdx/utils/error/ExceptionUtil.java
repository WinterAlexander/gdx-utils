package com.winteralexander.gdx.utils.error;

import com.winteralexander.gdx.utils.ReflectionUtil;
import com.winteralexander.gdx.utils.Validation;

import java.io.PrintWriter;
import java.io.StringWriter;

import static com.winteralexander.gdx.utils.Validation.ensureNotNull;

/**
 * Utility class to do operations with Exceptions or Throwables
 * <p>
 * Created on 2017-11-02.
 *
 * @author Alexander Winter
 */
public class ExceptionUtil {
	private ExceptionUtil() {}

	public static int getDepth(Throwable throwable) {
		ensureNotNull(throwable, "throwable");
		int depth = 0;
		while(throwable.getCause() != null) {
			depth++;
			throwable = throwable.getCause();
		}
		return depth;
	}

	@SuppressWarnings({"ReassignedVariable", "SynchronizationOnLocalVariableOrMethodParameter"})
	public static void appendCause(Throwable exception, Throwable cause) {
		Throwable emptyCause = exception;

		while(true) {
			synchronized(emptyCause) {
				if(emptyCause.getCause() != null)
					emptyCause = emptyCause.getCause();
				else {
					try {
						if(emptyCause != cause)
							emptyCause.initCause(cause);
					} catch(IllegalStateException illegalState) {
						// this means we've got an exception with a null cause
						// but with a cause it's tedious and the only way to fix
						// it is to use reflection to assign the cause

						ReflectionUtil.set(emptyCause, "cause", cause);
					}

					break;
				}
			}
		}
	}

	public static String printStackTrace(Throwable exception) {
		if(exception == null)
			return "null";

		StringWriter sw = new StringWriter();
		exception.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}

	public static boolean doesThrow(ThrowingRunnable runnable) {
		try {
			runnable.run();
			return false;
		} catch(Throwable ex) {
			return true;
		}
	}

	/**
	 * Throws the specified exception while bypassing Java's checked system. Allows for throwing
	 * checked exceptions in a function that is not marked as throwing those exceptions.
	 *
	 * @param ex throwable to throw
	 * @param <E> implementation detail needed for bypass to work
	 * @throws E implementation detail needed for bypass to work
	 */
	@SuppressWarnings("unchecked")
	public static <E extends Throwable> void bypassThrow(Throwable ex) throws E {
		throw (E)ex;
	}

	/**
	 * Runs the provided {@link ThrowingRunnable} bypassing any exception check to make it unchecked
	 * @param runnable runnable to run
	 */
	public static void unchecked(ThrowingRunnable runnable) {
		try {
			runnable.run();
		} catch(Throwable ex) {
			bypassThrow(ex);
			throw new Error("Dead code");
		}
	}

	/**
	 * Runs the provided {@link ThrowingSupplier} bypassing any exception check to make it unchecked
	 * @param supplier supplier to run
	 */
	public static <T> T unchecked(ThrowingSupplier<T> supplier) {
		try {
			return supplier.get();
		} catch(Throwable ex) {
			bypassThrow(ex);
			throw new Error("Dead code");
		}
	}

	@FunctionalInterface
	public interface ThrowingRunnable {
		void run() throws Throwable;
	}

	@FunctionalInterface
	public interface ThrowingSupplier<T> {
		T get() throws Throwable;
	}
}
