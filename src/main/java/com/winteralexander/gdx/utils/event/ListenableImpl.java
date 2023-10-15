package com.winteralexander.gdx.utils.event;

import com.badlogic.gdx.utils.Array;
import com.winteralexander.gdx.utils.Validation;

import java.util.function.Consumer;

/**
 * Listenable implementation using a simple array to implement Listeners
 * <p>
 * Created on 2018-03-26.
 *
 * @author Alexander Winter
 */
public class ListenableImpl<L> implements Listenable<L> {
	private final Array<L> listeners = new Array<>(true, 4);

	private boolean canRemove = true;
	private final Array<L> toRemove = new Array<>(false, 4);

	public void trigger(Consumer<L> event) {
		Validation.ensureNotNull(event, "event");

		int size = listeners.size; // don't iterate over the listeners that have been added in the trigger call
		canRemove = false;
		for(int i = 0; i < size; i++)
			event.accept(listeners.get(i));
		canRemove = true;

		listeners.removeAll(toRemove, true);
		toRemove.clear();
	}

	@Override
	public void addListener(L listener) {
		Validation.ensureNotNull(listener, "listener");
		listeners.add(listener);
	}

	public void addPriorityListener(L listener) {
		Validation.ensureNotNull(listener, "listener");
		listeners.insert(0, listener);
	}

	@Override
	public void removeListener(L listener) {
		if(!canRemove) {
			toRemove.add(listener);
			return;
		}

		listeners.removeValue(listener, true);
	}

	@Override
	public boolean hasListener(L listener) {
		return listeners.contains(listener, true);
	}

	@Override
	public void clearListeners() {
		if(!canRemove) {
			toRemove.addAll(listeners);
			return;
		}

		listeners.clear();
	}
}
