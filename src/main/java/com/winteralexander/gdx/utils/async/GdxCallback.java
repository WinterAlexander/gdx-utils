package com.winteralexander.gdx.utils.async;

import com.winteralexander.gdx.utils.GdxUtil;
import com.winteralexander.gdx.utils.function.RunnableFromConsumer;

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
		try {
			GdxUtil.postRunnable(RunnableFromConsumer.make(this, value, true));
		} catch(IllegalArgumentException ignored) {}
	}

	void receive(T t);

	static <T> GdxCallback<T> fromConsumer(Consumer<T> consumer) {
		return consumer::accept;
	}
}
