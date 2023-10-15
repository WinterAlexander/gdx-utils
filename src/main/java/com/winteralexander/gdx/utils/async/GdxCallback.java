package com.winteralexander.gdx.utils.async;

import com.winteralexander.gdx.utils.GdxUtil;

import java.util.function.Consumer;

/**
 * A callback functionnal interface meant to be called from
 * a thread and received in the main LibGDX thread
 * <p>
 * Created on 2017-01-17.
 *
 * @author Alexander Winter
 */
@FunctionalInterface
public interface GdxCallback<T> extends Consumer<T> {
	CallbackWrapper gdxWrapper = GdxCallback::fromConsumer;

	@Override
	default void accept(T value) {
		GdxUtil.postRunnable(() -> receive(value));
	}

	void receive(T t);

	static <T> GdxCallback<T> fromConsumer(Consumer<T> consumer) {
		return consumer::accept;
	}
}
