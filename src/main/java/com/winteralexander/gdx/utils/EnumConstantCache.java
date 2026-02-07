package com.winteralexander.gdx.utils;

import com.badlogic.gdx.utils.ObjectMap;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Utility to cache the values of {@link Enum} constants because both the values() function and
 * {@link Class#getEnumConstants()} create a new array every time
 * <p>
 * Created on 2024-08-24.
 *
 * @author Alexander Winter
 */
public class EnumConstantCache {
	private static final ObjectMap<Class<?>, Object[]> map = new ObjectMap<>();
	private static final ReadWriteLock lock = new ReentrantReadWriteLock();

	public static <T extends Enum<T>> T[] store(T[] values) {
		if(values.length == 0)
			throw new IllegalArgumentException("Received empty array, to use store with an empty " +
					"array provide class type");

		lock.writeLock().lock();
		map.put(values[0].getClass(), values);
		lock.writeLock().unlock();
		return values;
	}

	public static <T extends Enum<T>> T[] store(Class<T> type, T[] values) {
		lock.writeLock().lock();
		map.put(type, values);
		lock.writeLock().unlock();
		return values;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Enum<T>> T[] get(Class<T> type) {
		lock.readLock().lock();
		T[] values = (T[])map.get(type);
		lock.readLock().unlock();

		if(values == null) {
			values = type.getEnumConstants();
			store(type, values);
		}

		return values;
	}

	public static <T extends Enum<T>> boolean isCached(Class<T> type) {
		lock.readLock().lock();
		Object[] values = map.get(type);
		lock.readLock().unlock();

		return values != null;
	}
}
