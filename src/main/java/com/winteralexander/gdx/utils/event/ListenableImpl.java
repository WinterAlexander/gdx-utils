package com.winteralexander.gdx.utils.event;

import com.badlogic.gdx.utils.Array;

import java.util.function.Consumer;

import static com.winteralexander.gdx.utils.Validation.ensureNotNull;

/**
 * Listenable implementation using a simple array to implement Listeners
 * <p>
 * Created on 2018-03-26.
 *
 * @author Alexander Winter
 */
public class ListenableImpl<L> implements Listenable<L> {
	private final Array<L> listeners = new Array<>(true, 4);

	private boolean canEditListeners = true;
	private final Array<L> toAdd = new Array<>(true, 4),
			toAddWithPriority = new Array<>(true, 4),
			toRemove = new Array<>(false, 4);

	public void trigger(Consumer<L> event) {
		ensureNotNull(event, "event");

		int size = listeners.size; // don't iterate over the listeners that have been added in the trigger call
		canEditListeners = false;
		for(int i = 0; i < size; i++)
			event.accept(listeners.get(i));
		canEditListeners = true;

		listeners.removeAll(toRemove, true);
		listeners.addAll(toAdd);
		if(toAddWithPriority.size > 0) {
			listeners.insertRange(0, toAddWithPriority.size);
			for(int i = 0; i < toAddWithPriority.size; i++)
				listeners.set(i, toAddWithPriority.get(i));
		}
		toRemove.clear();
		toAdd.clear();
		toAddWithPriority.clear();
	}

	@Override
	public void addListener(L listener) {
		ensureNotNull(listener, "listener");
		if(!canEditListeners) {
			toAdd.add(listener);
			return;
		}
		listeners.add(listener);
	}

	public void addPriorityListener(L listener) {
		ensureNotNull(listener, "listener");
		if(!canEditListeners) {
			toAddWithPriority.add(listener);
			return;
		}
		listeners.insert(0, listener);
	}

	@Override
	public void removeListener(L listener) {
		ensureNotNull(listener, "listener");
		if(!canEditListeners) {
			toRemove.add(listener);
			return;
		}

		listeners.removeValue(listener, true);
	}

	@Override
	public boolean hasListener(L listener) {
		ensureNotNull(listener, "listener");
		return listeners.contains(listener, true);
	}

	@Override
	public void clearListeners() {
		if(!canEditListeners) {
			toRemove.addAll(listeners);
			return;
		}

		listeners.clear();
	}
}
