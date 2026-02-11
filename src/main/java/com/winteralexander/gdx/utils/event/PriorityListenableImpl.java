package com.winteralexander.gdx.utils.event;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.winteralexander.gdx.utils.Validation;

import java.util.function.Consumer;
import java.util.function.Function;

import static com.winteralexander.gdx.utils.Validation.ensureNotNull;

/**
 * PriorityListenable implementation with ObjectMap and enum values as priority
 * levels. Supports multiple listeners per levels.
 * <p>
 * Created on 2018-04-24.
 *
 * @author Alexander Winter
 */
public class PriorityListenableImpl<L, P extends Enum<P>> implements PriorityListenable<L, P> {
	private final ObjectMap<P, Array<L>> listeners = new ObjectMap<>();
	private final P[] priorities;

	private final P defaultPriority;

	private boolean canEditListeners = true;
	private final ObjectMap<P, Array<L>> toAdd = new ObjectMap<>(),
			toRemove = new ObjectMap<>();

	@SuppressWarnings("unchecked")
	public PriorityListenableImpl(P defaultPriority) {
		this.defaultPriority = defaultPriority;

		priorities = ((Class<P>)defaultPriority.getClass()).getEnumConstants();
		for(P priority : priorities) {
			listeners.put(priority, new Array<>());
			toAdd.put(priority, new Array<>());
			toRemove.put(priority, new Array<>());
		}
	}

	/**
	 * Triggers all listener with specified event, levels by levels and quit
	 * when a listener handles the event at a level. Will finish a level before
	 * quitting in the case of a handled event.
	 *
	 * @param event event to dispatch
	 * @return true if any listener handled the event, otherwise false
	 */
	protected boolean trigger(Function<L, Boolean> event) {
		canEditListeners = false;
		boolean result = false;
		for(P priority : priorities) {
			Array<L> level = listeners.get(priority);

			boolean handled = false;

			int size = level.size;
			for(int i = 0; i < size; i++) {
				L listener = level.get(i);
				if(event.apply(listener))
					handled = true;
			}

			if(handled) {
				result = true;
				break;
			}
		}
		canEditListeners = true;
		for(P priority : priorities) {
			listeners.get(priority).removeAll(toRemove.get(priority), true);
			listeners.get(priority).addAll(toAdd.get(priority));
			toAdd.get(priority).clear();
			toRemove.get(priority).clear();
		}

		return result;
	}

	/**
	 * Triggers all listener with specified event, levels by levels until the
	 * event becomes handled. When that happen, the handledEvent is used instead
	 * to trigger the remaining levels.
	 *
	 * @param event        event to dispatch
	 * @param handledEvent event to dispatch when handled
	 * @return true if any listener handled the event, otherwise false
	 */
	protected boolean trigger(Function<L, Boolean> event, Consumer<L> handledEvent) {
		canEditListeners = false;
		boolean handled = false;

		for(P priority : priorities) {
			Array<L> level = listeners.get(priority);

			if(handled) {
				for(L listener : level)
					handledEvent.accept(listener);
				continue;
			}

			for(L listener : level)
				if(event.apply(listener))
					handled = true;
		}
		canEditListeners = true;
		for(P priority : priorities) {
			listeners.get(priority).removeAll(toRemove.get(priority), true);
			listeners.get(priority).addAll(toAdd.get(priority));
			toAdd.get(priority).clear();
			toRemove.get(priority).clear();
		}

		return handled;
	}

	@Override
	public void addListener(L listener) {
		addListener(listener, defaultPriority);
	}

	@Override
	public void addListener(L listener, P priority) {
		ensureNotNull(listener, "listener");
		if(canEditListeners)
			listeners.get(priority).add(listener);
		else
			toAdd.get(priority).add(listener);
	}

	@Override
	public void removeListener(L listener) {
		ensureNotNull(listener, "listener");
		if(canEditListeners)
			for(Array<L> array : listeners.values())
				array.removeValue(listener, true);
		else
			for(Array<L> array : toRemove.values())
				array.add(listener);
	}

	@Override
	public boolean hasListener(L listener) {
		for(Array<L> array : listeners.values())
			if(array.contains(listener, true))
				return true;
		return false;
	}

	@Override
	public void clearListeners() {
		if(canEditListeners)
			for(Array<L> array : listeners.values())
				array.clear();
		else
			for(P priority : priorities)
				toRemove.get(priority).addAll(listeners.get(priority));
	}
}
