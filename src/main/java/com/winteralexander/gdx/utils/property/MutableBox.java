package com.winteralexander.gdx.utils.property;

/**
 * Boxes a value that can be changed
 * <p>
 * Created on 2017-11-13.
 *
 * @author Alexander Winter
 */
public class MutableBox<T> implements Property<T> {
	private T value;

	public MutableBox() {
		this.value = null;
	}

	public MutableBox(T value) {
		this.value = value;
	}

	public boolean hasValue() {
		return value != null;
	}

	public T get() {
		return value;
	}

	public void set(T value) {
		this.value = value;
	}
}
