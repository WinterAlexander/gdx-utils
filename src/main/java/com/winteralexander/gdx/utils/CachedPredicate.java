package com.winteralexander.gdx.utils;

import com.badlogic.gdx.utils.ObjectMap;

import java.util.function.Predicate;

import static com.winteralexander.gdx.utils.Validation.ensureNotNull;

/**
 * {@link Predicate} that caches the return value of another predicate for performance optimization
 * <p>
 * Created on 2023-09-24.
 *
 * @author Alexander Winter
 */
public class CachedPredicate<T> implements Predicate<T> {
	private final Predicate<T> predicate;
	private final ObjectMap<T, Boolean> evaluated = new ObjectMap<>();

	public CachedPredicate(Predicate<T> predicate) {
		ensureNotNull(predicate, "predicate");
		this.predicate = predicate;
	}

	@Override
	public boolean test(T param) {
		if(!evaluated.containsKey(param))
			evaluated.put(param, predicate.test(param));

		return evaluated.get(param);
	}
}
