package com.winteralexander.gdx.utils.collection;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Collections;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * {@link Array} which prints out debugging information when a concurrent
 * iteration happens to see which was the other iterator.
 *
 * @author Alexander Winter
 * @deprecated because it should only be used as a debugging tool and not in production code
 * <p>
 * Created on 2022-04-14.
 */
@Deprecated
public class DebugArray<T> extends Array<T> {
	private ArrayIterable<T> iterable;

	public ArrayIterator<T> iterator() {
		if(Collections.allocateIterators)
			return new ArrayIterator<>(this, true);

		if(iterable == null)
			iterable = new ArrayIterable<>(this);
		return iterable.iterator();
	}

	public static class ArrayIterator<T> extends Array.ArrayIterator<T> implements Iterator<T>, Iterable<T> {
		private final Array<T> array;
		private final boolean allowRemove;
		int index;
		boolean valid = true;

		ArrayIterable<T> iterable;

		public ArrayIterator(Array<T> array) {
			this(array, true);
		}

		public ArrayIterator(Array<T> array, boolean allowRemove) {
			super(array, allowRemove);
			this.array = array;
			this.allowRemove = allowRemove;
		}

		public boolean hasNext() {
			if(!valid) {
				System.out.println(iterable.lastAcquire);
				throw new GdxRuntimeException("#iterator() cannot be used nested.");
			}
			return index < array.size;
		}

		public T next() {
			if(index >= array.size)
				throw new NoSuchElementException(String.valueOf(index));

			if(!valid) {
				System.out.println(iterable.lastAcquire);
				throw new GdxRuntimeException("#iterator() cannot be used nested.");
			}
			return array.items[index++];
		}

		public void remove() {
			if(!allowRemove)
				throw new GdxRuntimeException("Remove not allowed.");

			index--;
			array.removeIndex(index);
		}

		public void reset() {
			index = 0;
		}

		public ArrayIterator<T> iterator() {
			return this;
		}
	}

	public static class ArrayIterable<T> implements Iterable<T> {
		private final Array<T> array;
		private final boolean allowRemove;
		private ArrayIterator<T> iterator1, iterator2;

		StringWriter lastAcquire = new StringWriter();

		public ArrayIterable(Array<T> array) {
			this(array, true);
		}

		public ArrayIterable(Array<T> array, boolean allowRemove) {
			this.array = array;
			this.allowRemove = allowRemove;
		}

		public ArrayIterator<T> iterator() {
			if(Collections.allocateIterators)
				return new ArrayIterator<>(array, allowRemove);

			lastAcquire.getBuffer().setLength(0);
			new Throwable().printStackTrace(new PrintWriter(lastAcquire));
			if(iterator1 == null) {
				iterator1 = new ArrayIterator<>(array, allowRemove);
				iterator2 = new ArrayIterator<>(array, allowRemove);
				iterator1.iterable = this;
				iterator2.iterable = this;
			}
			if(!iterator1.valid) {
				iterator1.index = 0;
				iterator1.valid = true;
				iterator2.valid = false;
				return iterator1;
			}
			iterator2.index = 0;
			iterator2.valid = true;
			iterator1.valid = false;
			return iterator2;
		}
	}
}
