package com.winteralexander.gdx.utils.collection.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.winteralexander.gdx.utils.Validation.ensureNotNull;

/**
 * {@link Iterator} with always only one object in it
 * <p>
 * Created on 2023-10-20.
 *
 * @author Alexander Winter
 */
public class UnitIterator<T> implements ReusableIterator<T> {
	private T object;
	private boolean clean = true;

	public UnitIterator(T object) {
		ensureNotNull(object, "object");
		this.object = object;
	}

	@Override
	public void reset() {
		clean = true;
	}

	@Override
	public boolean hasNext() {
		return clean;
	}

	@Override
	public T next() {
		if(!clean)
			throw new NoSuchElementException();
		clean = false;
		return object;
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}
}
