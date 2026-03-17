package com.winteralexander.gdx.utils.function;

import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Utility class for manipulating functional interfaces
 * <p>
 * Created on 2026-03-16.
 *
 * @author Alexander Winter
 */
public class FunctionUtil {
	private static final Consumer<?> NOOP_CONSUMER = v -> {};

	private FunctionUtil() {}

	public static <T, U> BiPredicate<T, U> testFirst(Predicate<T> predicate) {
		return (t, u) -> predicate.test(t);
	}

	public static <T, U> BiPredicate<T, U> testSecond(Predicate<U> predicate) {
		return (t, u) -> predicate.test(u);
	}

	@SuppressWarnings("unchecked")
	public static <T> Consumer<T> noopConsumer() {
		return (Consumer<T>)NOOP_CONSUMER;
	}
}
