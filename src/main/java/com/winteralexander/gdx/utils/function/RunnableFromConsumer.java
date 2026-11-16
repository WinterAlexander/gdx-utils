package com.winteralexander.gdx.utils.function;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

import java.util.function.Consumer;

/**
 * Poolable utility wrapper to get a runnable object from a consumer without creating small objects
 * into heap.
 * <p>
 * Created on 2026-11-15.
 *
 * @author Alexander Winter
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class RunnableFromConsumer implements Runnable, Pool.Poolable {
	static {
		Pools.set(RunnableFromConsumer::new);
	}

	private Consumer consumer = null;
	private Object value = null;
	private boolean autoFree = false;

	public <T> void set(Consumer<T> consumer, T value, boolean autoFree) {
		this.consumer = consumer;
		this.value = value;
		this.autoFree = autoFree;
	}

	@Override
	public void run() {
		consumer.accept(value);
		if(autoFree)
			Pools.free(this);
	}

	@Override
	public void reset() {
		this.value = null;
		this.consumer = null;
	}

	public static <T> RunnableFromConsumer make(Consumer<T> consumer, T value, boolean autoFree) {
		RunnableFromConsumer obj = Pools.obtain(RunnableFromConsumer.class);
		obj.set(consumer, value, autoFree);
		return obj;
	}
}
