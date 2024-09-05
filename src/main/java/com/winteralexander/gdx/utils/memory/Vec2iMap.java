package com.winteralexander.gdx.utils.memory;

import com.badlogic.gdx.utils.LongMap;
import com.winteralexander.gdx.utils.math.vector.Vector2i;

import java.util.Iterator;

/**
 * A fast map that maps {@link Vector2i} to values by converting the {@link Vector2i} to longs,
 * useful to store anything with 2D spatial coordinates
 * <p>
 * Created on 2024-09-05.
 *
 * @author Alexander Winter
 */
public class Vec2iMap<V> implements Iterable<LongMap.Entry<V>> {
	private final LongMap<V> map;

	public Vec2iMap() {
		map = new LongMap<>();
	}

	public Vec2iMap(int initialCapacity) {
		map = new LongMap<>(initialCapacity);
	}

	public Vec2iMap(int initialCapacity, float loadFactor) {
		map = new LongMap<>(initialCapacity, loadFactor);
	}

	public V get(int x, int y) {
		return map.get(key(x, y));
	}

	public V get(int x, int y, V defaultValue) {
		return map.get(key(x, y), defaultValue);
	}

	public V get(Vector2i key) {
		return get(key.x, key.y);
	}

	public V get(Vector2i key, V defaultValue) {
		return get(key.x, key.y, defaultValue);
	}

	public void put(int x, int y, V value) {
		map.put(key(x, y), value);
	}

	public void put(Vector2i key, V value) {
		put(key.x, key.y, value);
	}

	public void putAll(Vec2iMap<V> otherMap) {
		map.putAll(otherMap.map);
	}

	public int size() {
		return map.size;
	}

	public LongMap.Keys keys() {
		return map.keys();
	}

	public LongMap.Values<V> values() {
		return map.values();
	}

	@Override
	public Iterator<LongMap.Entry<V>> iterator() {
		return map.iterator();
	}

	private long key(int x, int y) {
		return (long)x + (long)y << 32;
	}
}
