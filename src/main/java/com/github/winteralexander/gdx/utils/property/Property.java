package com.github.winteralexander.gdx.utils.property;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A property is something that stores a specific value in any particular way.
 * Has 2 method, one for get and one for set
 * <p>
 * Created on 2021-09-19.
 *
 * @author Alexander Winter
 */
public interface Property<T> extends Supplier<T>, Consumer<T> {
	@Override
	default void accept(T t) {
		set(t);
	}

	void set(T t);

	@Override
	default T get() {
		return null;
	}
}
