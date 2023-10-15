package com.winteralexander.gdx.utils.async;

import com.badlogic.gdx.utils.LongMap;
import com.winteralexander.gdx.utils.Validation;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.function.Supplier;

/**
 * Wrapper around a {@link LongMap} for mapping of keys with elements that do not
 * support remapping of existing keys but allow all of its methods to be
 * executed over many threads effectively using a specified
 * {@link ReadWriteLock}
 * <p>
 * Created on 2023-07-31.
 *
 * @author Alexander Winter
 */
public class ThreadSafeLongMap<V> {
	private final LongMap<V> map;
	private final ReadWriteLock lock;

	public ThreadSafeLongMap(LongMap<V> map, ReadWriteLock lock) {
		Validation.ensureNotNull(map, "map");
		Validation.ensureNotNull(lock, "lock");
		this.map = map;
		this.lock = lock;
	}

	public V getOrInit(long key, V initValue) {
		return getOrInit(key, () -> initValue);
	}

	public V getOrInit(long key, Supplier<V> initValue) {
		lock.readLock().lock();

		if(map.containsKey(key)) {
			V val = map.get(key);
			lock.readLock().unlock();
			return val;
		}

		lock.readLock().unlock();
		lock.writeLock().lock();

		if(map.containsKey(key)) {
			V val = map.get(key);
			lock.writeLock().unlock();
			return val;
		}

		V val = initValue.get();
		map.put(key, val);
		lock.writeLock().unlock();
		return val;
	}

	public V getOrNull(long key) {
		lock.readLock().lock();
		V val = map.get(key);
		lock.readLock().unlock();
		return val;
	}

	public void clear() {
		lock.writeLock().lock();
		map.clear();
		lock.writeLock().unlock();
	}
}
