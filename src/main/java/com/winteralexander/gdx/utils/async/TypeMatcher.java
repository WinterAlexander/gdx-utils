package com.winteralexander.gdx.utils.async;

import com.winteralexander.gdx.utils.collection.CollectionUtil;

import java.util.Arrays;
import java.util.function.Predicate;

import static com.winteralexander.gdx.utils.Validation.ensureNotNull;

/**
 * TODO Undocumented :(
 * <p>
 * Created on 2023-10-19.
 *
 * @author Alexander Winter
 */
public class TypeMatcher implements Predicate<Class<? extends Exception>> {
	private final Class<? extends Exception>[] types;

	public TypeMatcher(Class<? extends Exception>[] types) {
		ensureNotNull(types, "types");
		this.types = types;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o)
			return true;
		if(o == null || getClass() != o.getClass())
			return false;
		TypeMatcher that = (TypeMatcher)o;
		return Arrays.equals(types, that.types);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(types);
	}

	@Override
	public boolean test(Class<? extends Exception> e) {
		return CollectionUtil.contains(types, e);
	}
}
