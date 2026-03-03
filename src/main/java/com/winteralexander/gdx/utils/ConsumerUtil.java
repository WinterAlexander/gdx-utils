package com.winteralexander.gdx.utils;

import java.util.function.Consumer;

/**
 * Utility class for {@link Consumer}
 * <p>
 * Created on 2024-07-21.
 *
 * @author Alexander Winter
 */
public class ConsumerUtil {
	private static final Consumer<?> NOOP_CONSUMER = v -> {};

	private ConsumerUtil() {}

	@SuppressWarnings("unchecked")
	public static <T> Consumer<T> noopConsumer() {
		return (Consumer<T>)NOOP_CONSUMER;
	}
}
