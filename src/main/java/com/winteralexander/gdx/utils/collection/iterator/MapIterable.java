package com.winteralexander.gdx.utils.collection.iterator;

import java.util.Iterator;
import java.util.function.Function;

import static com.winteralexander.gdx.utils.Validation.ensureNotNull;

/**
 * {@link Iterable} wrapper that maps the elements returned to something else using a mapping
 * function
 * <p>
 * Created on 2023-10-20.
 *
 * @author Alexander Winter
 */
public class MapIterable<P, R> implements ReusableIterator<R>
{
	private final Iterable<P> iterable;
	private final Function<P, R> mapping;
	private Iterator<P> iterator;

	public MapIterable(Iterable<P> iterable, Function<P, R> mapping) {
		ensureNotNull(iterable, "iterable");
		ensureNotNull(mapping, "mapping");
		this.iterable = iterable;
		this.mapping = mapping;
		iterator = iterable.iterator();
	}

	@Override
	public void reset() {
		iterator = iterable.iterator();
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public R next() {
		return mapping.apply(iterator.next());
	}
}
