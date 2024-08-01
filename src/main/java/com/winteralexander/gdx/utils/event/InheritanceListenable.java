package com.winteralexander.gdx.utils.event;

import com.badlogic.gdx.utils.ObjectMap;

import java.util.function.Consumer;

import static com.winteralexander.gdx.utils.Validation.ensureNotNull;

/**
 * {@link Listenable} which allows having inheritance for the listener class, wrapping the parent
 * listeners into a children listener class when added. Trigger function allows triggering the
 * children listeners (and their wrapped parents).
 * <p>
 * Created on 2024-07-31.
 *
 * @author Alexander Winter
 */
public abstract class InheritanceListenable<L, C extends L> implements Listenable<L> {
	private final ListenableImpl<C> delegator = new ListenableImpl<>();

	private final Class<C> childrenType;

	private final ObjectMap<L, C> wrappers = new ObjectMap<>();

	public InheritanceListenable(Class<C> childrenType) {
		ensureNotNull(childrenType, "childrenType");
		this.childrenType = childrenType;
	}

	protected abstract C wrapListener(L listener);

	public void trigger(Consumer<C> event) {
		delegator.trigger(event);
	}

	@Override
	public void addListener(L listener) {
		ensureNotNull(listener, "listener");
		if(childrenType.isInstance(listener))
			delegator.addListener(childrenType.cast(listener));
		else {
			C wrap = wrapListener(listener);
			wrappers.put(listener, wrap);
			delegator.addListener(wrap);
		}
	}

	@Override
	public void removeListener(L listener) {
		if(childrenType.isInstance(listener))
			delegator.removeListener(childrenType.cast(listener));
		else {
			C wrapped = wrappers.remove(listener);
			if(wrapped == null)
				return;
			delegator.removeListener(wrapped);
		}
	}

	@Override
	public boolean hasListener(L listener) {
		if(childrenType.isInstance(listener))
			return delegator.hasListener(childrenType.cast(listener));
		else
			return delegator.hasListener(wrappers.get(listener));
	}

	@Override
	public void clearListeners() {
		delegator.clearListeners();
		wrappers.clear();
	}
}
