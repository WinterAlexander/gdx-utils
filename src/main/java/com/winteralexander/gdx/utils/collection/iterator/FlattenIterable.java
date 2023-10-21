package com.winteralexander.gdx.utils.collection.iterator;

import java.util.Iterator;

import static com.winteralexander.gdx.utils.Validation.ensureNotNull;

/**
 * {@link Iterable} wrapper that flattens two layers of nested iterables into a single iterable
 * <p>
 * Created on 2023-10-20.
 *
 * @author Alexander Winter
 */
public class FlattenIterable<T> implements ReusableIterator<T> {
	private final Iterable<? extends Iterable<T>> iterable;
	private Iterator<? extends Iterable<T>> iterator;
	private Iterator<T> current;

	public FlattenIterable(Iterable<? extends Iterable<T>> iterable)
	{
		ensureNotNull(iterable, "iterable");
		this.iterable = iterable;
		reset();
	}

	@Override
	public void reset() {
		iterator = iterable.iterator();

		do
		{
			if(iterator.hasNext())
				current = iterator.next().iterator();
			else
				current = null;
		}
		while(current != null && !current.hasNext());
	}

	@Override
	public boolean hasNext() {
		return current != null;
	}

	@Override
	public T next() {
		T next = current.next();

		while(current != null && !current.hasNext())
		{
			if(iterator.hasNext())
				current = iterator.next().iterator();
			else
				current = null;
		}
		return next;
	}
}
