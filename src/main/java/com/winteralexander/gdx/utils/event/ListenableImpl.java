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

	private boolean listenersLocked = false;
	private final Array<L> toAdd = new Array<>(true, 4),
			toAddWithPriority = new Array<>(true, 4),
			toRemove = new Array<>(false, 4);

	public void trigger(Consumer<L> event) {
		ensureNotNull(event, "event");

		boolean wasLocked = listenersLocked;
		if(!wasLocked)
			lockListeners();
		for(int i = 0; i < listeners.size; i++)
			event.accept(listeners.get(i));
		if(!wasLocked)
			unlockListeners();
	}

	@Override
	public void addListener(L listener) {
		ensureNotNull(listener, "listener");
		if(listenersLocked) {
			toAdd.add(listener);
			return;
		}
		listeners.add(listener);
	}

	public void addPriorityListener(L listener) {
		ensureNotNull(listener, "listener");
		if(listenersLocked) {
			toAddWithPriority.add(listener);
			return;
		}
		listeners.insert(0, listener);
	}

	@Override
	public void removeListener(L listener) {
		ensureNotNull(listener, "listener");
		if(listenersLocked) {
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
		if(listenersLocked) {
			toRemove.addAll(listeners);
			return;
		}

		listeners.clear();
	}

	public void lockListeners() {
		listenersLocked = true;
	}

	public void unlockListeners() {
		listenersLocked = false;

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
}
