package com.winteralexander.gdx.utils.memory;

import com.badlogic.gdx.utils.IntMap;
import com.winteralexander.gdx.utils.math.vector.Vector2i;

import java.util.Iterator;

/**
 * A fast map that maps 2 shorts to values by converting the 2 shorts to ints,
 * useful to store anything with 2D spatial coordinates. Method signatures accept ints instead of
 * shorts for convenience, but those are truncated.
 * <p>
 * Created on 2024-09-05.
 *
 * @author Alexander Winter
 */
public class Vec2sMap<V> implements Iterable<IntMap.Entry<V>> {
	private final IntMap<V> map;

	public Vec2sMap() {
		map = new IntMap<>();
	}

	public Vec2sMap(int initialCapacity) {
		map = new IntMap<>(initialCapacity);
	}

	public Vec2sMap(int initialCapacity, float loadFactor) {
		map = new IntMap<>(initialCapacity, loadFactor);
	}

	public V get(int x, int y) {
		return map.get(key((short)x, (short)y));
	}

	public V get(int x, int y, V defaultValue) {
		return map.get(key((short)x, (short)y), defaultValue);
	}

	public V get(Vector2i key) {
		return get(key.x, key.y);
	}

	public V get(Vector2i key, V defaultValue) {
		return get(key.x, key.y, defaultValue);
	}

	public void put(int x, int y, V value) {
		map.put(key((short)x, (short)y), value);
	}

	public void put(Vector2i key, V value) {
		put(key.x, key.y, value);
	}

	public void putAll(Vec2sMap<V> otherMap) {
		map.putAll(otherMap.map);
	}

	public int size() {
		return map.size;
	}

	public IntMap.Keys keys() {
		return map.keys();
	}

	public IntMap.Values<V> values() {
		return map.values();
	}

	@Override
	public Iterator<IntMap.Entry<V>> iterator() {
		return map.iterator();
	}

	private int key(short x, short y) {
		return (int)x + (int)y << 16;
	}
}
