package com.winteralexander.gdx.utils.function;

import java.util.function.BiConsumer;
import java.util.function.Function;

import static com.winteralexander.gdx.utils.Validation.ensureNotNull;

/**
 * {@link Function} that has an internal cache variable for the result to prevent the creation of
 * new instances with {@link #apply}
 * <p>
 * Created on 2026-11-26.
 *
 * @author Alexander Winter
 */
public class MutableFunction<P, R> implements Function<P, R> {
	private final R cacheValue;
	private final BiConsumer<P, R> cacheModifier;

	public MutableFunction(R cacheValue, BiConsumer<P, R> cacheModifier) {
		ensureNotNull(cacheValue, "cacheValue");
		ensureNotNull(cacheModifier, "cacheModifier");
		this.cacheValue = cacheValue;
		this.cacheModifier = cacheModifier;
	}

	@Override
	public R apply(P param) {
		cacheModifier.accept(param, cacheValue);
		return cacheValue;
	}
}