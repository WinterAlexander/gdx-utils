package com.winteralexander.gdx.utils;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * {@link Supplier} that has an internal cache variable to prevent creation of
 * new instances in {@link #get()}
 * <p>
 * Created on 2020-11-30.
 *
 * @author Alexander Winter
 */
public class MutableSupplier<T> implements Supplier<T> {
	private final Consumer<T> supplier;
	private final T cacheValue;

	public MutableSupplier(T cacheValue, Consumer<T> cacheModifier) {
		Validation.ensureNotNull(cacheValue, "cacheValue");
		Validation.ensureNotNull(cacheModifier, "cacheModifier");
		this.cacheValue = cacheValue;
		this.supplier = cacheModifier;
	}

	@Override
	public T get() {
		supplier.accept(cacheValue);
		return cacheValue;
	}
}
