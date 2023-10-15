package com.winteralexander.gdx.utils.collection.iterator;

import com.winteralexander.gdx.utils.Validation;

import static com.winteralexander.gdx.utils.Validation.ensureInRange;

/**
 * {@link IndexIterator} for Java arrays
 * <p>
 * Created on 2021-10-01.
 *
 * @author Alexander Winter
 */
public class JavaArrayIndexIterator<T> implements IndexIterator<T> {
	private final T[] array;
	private final int startIndex, endIndex;

	private int index;

	public JavaArrayIndexIterator(T[] array, int startIndex, int endIndex) {
		Validation.ensureNotNull(array, "array");
		Validation.ensureInRange(startIndex, 0, endIndex + 1, "startIndex");
		Validation.ensureInRange(endIndex, startIndex, array.length + 1, "endIndex");
		this.array = array;
		this.startIndex = startIndex;
		this.endIndex = endIndex;

		index = startIndex;
	}

	@Override
	public int size() {
		return endIndex - startIndex;
	}

	@Override
	public T objectAt(int index) {
		return array[index - startIndex];
	}

	@Override
	public void reset() {
		index = startIndex;
	}

	@Override
	public boolean hasNext() {
		return index < endIndex;
	}

	@Override
	public T next() {
		return array[index++];
	}
}
