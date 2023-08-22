package com.github.winteralexander.gdx.utils.async;

import java.util.function.Consumer;

/**
 * Object that is used to wrap a callback object into a callback of another type
 * <p>
 * Created on 2017-10-26.
 *
 * @author Alexander Winter
 * @see GdxCallback#gdxWrapper
 */
@FunctionalInterface
public interface CallbackWrapper {
	<T> Consumer<T> wrap(Consumer<T> consumer);
}
