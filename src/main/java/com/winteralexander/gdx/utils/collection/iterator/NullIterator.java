package com.winteralexander.gdx.utils.collection.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * {@link Iterator} that never contains anything
 * <p>
 * Created on 2023-10-20.
 *
 * @author Alexander Winter
 */
public class NullIterator<T> implements Iterable<T>, Iterator<T> {
	public static final NullIterator<?> INSTANCE = new NullIterator<>();

	@Override
	public NullIterator<T> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public T next() {
		throw new NoSuchElementException();
	}

	@SuppressWarnings("unchecked")
	public static <T> NullIterator<T> instance() {
		return (NullIterator<T>)INSTANCE;
	}
}
