package com.winteralexander.gdx.utils.event;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;

/**
 * Listenable implementation with support for listener priority
 * <p>
 * Created on 2018-04-24.
 *
 * @author Alexander Winter
 */
public interface PriorityListenable<L, P> extends Listenable<L> {
	/**
	 * Adds a listener with a specific priority
	 *
	 * @param listener listener to add
	 * @param priority priority of the listener
	 */
	void addListener(L listener, P priority);

	/**
	 * Adds a collection of listeners with their priority to this Listenable
	 *
	 * @param listeners listeners with priority
	 */
	default void addListeners(ObjectMap<L, P> listeners) {
		for(Entry<L, P> entry : listeners)
			addListener(entry.key, entry.value);
	}
}
