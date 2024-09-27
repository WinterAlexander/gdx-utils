package com.winteralexander.gdx.utils.collection;

import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.LongMap;
import com.badlogic.gdx.utils.Null;
import com.winteralexander.gdx.utils.math.vector.Vector2i;

import java.util.Iterator;

import static com.winteralexander.gdx.utils.Validation.ensureNotNull;

/**
 * A fast map that maps {@link Vector2i} to values by converting the {@link Vector2i} to longs,
 * useful to store anything with 2D spatial coordinates
 * <p>
 * Created on 2024-09-05.
 *
 * @author Alexander Winter
 */
public class Vec2iMap<V> implements Iterable<Vec2iMap.Entry<V>> {
	private final KeysRedirectIterator keysIt;
	private final EntriesRedirectIterator<V> entriesIt;

	private final LongMap<V> map;

	public Vec2iMap() {
		map = new LongMap<>();
		keysIt = new KeysRedirectIterator(map);
		entriesIt = new EntriesRedirectIterator<>(map);
	}

	public Vec2iMap(int initialCapacity) {
		map = new LongMap<>(initialCapacity);
		keysIt = new KeysRedirectIterator(map);
		entriesIt = new EntriesRedirectIterator<>(map);
	}

	public Vec2iMap(int initialCapacity, float loadFactor) {
		map = new LongMap<>(initialCapacity, loadFactor);
		keysIt = new KeysRedirectIterator(map);
		entriesIt = new EntriesRedirectIterator<>(map);
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

	public boolean containsKey(int x, int y) {
		return map.containsKey(key(x, y));
	}

	public boolean containsKey(Vector2i vec) {
		return containsKey(vec.x, vec.y);
	}

	public boolean containsValue(Object value, boolean identity) {
		return map.containsValue(value, identity);
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

	public V remove(int x, int y) {
		return map.remove(key(x, y));
	}

	public V remove(Vector2i key) {
		return remove(key.x, key.y);
	}

	public void clear() {
		map.clear();
	}

	public int size() {
		return map.size;
	}

	public KeysRedirectIterator keys() {
		keysIt.iterator();
		return keysIt;
	}

	public LongMap.Values<V> values() {
		return map.values();
	}

	@Override
	public EntriesRedirectIterator<V> iterator() {
		entriesIt.iterator();
		return entriesIt;
	}

	private long key(int x, int y) {
		return (x & 0xFFFFFFFFL) + ((long)y << 32);
	}

	public LongMap<V> getInnerMap() {
		return map;
	}

	public static class Entry<V> {
		public int x, y;
		public @Null V value;

		public String toString () {
			return "(" + x + ", " + y + ")" + "=" + value;
		}
	}

	public static class KeysRedirectIterator implements Iterable<Vector2i>, Iterator<Vector2i> {
		private final LongMap<?> map;
		private LongMap.Keys it = null;
		private final Vector2i tmpVec2 = new Vector2i();

		public KeysRedirectIterator(LongMap<?> map) {
			ensureNotNull(map, "map");
			this.map = map;
		}

		@Override
		public Iterator<Vector2i> iterator() {
			it = map.keys();
			return this;
		}

		@Override
		public boolean hasNext() {
			return it.hasNext;
		}

		@Override
		public Vector2i next() {
			long key = it.next();
			return tmpVec2.set((int)key, (int)(key >> 32));
		}

		public void remove() {
			it.remove();
		}
	}

	public static class EntriesRedirectIterator<V> implements Iterable<Vec2iMap.Entry<V>>, Iterator<Vec2iMap.Entry<V>> {
		private final LongMap<V> map;
		private LongMap.Entries<V> it = null;
		private final Vec2iMap.Entry<V> tmpEntry = new Vec2iMap.Entry<>();

		public EntriesRedirectIterator(LongMap<V> map) {
			ensureNotNull(map, "map");
			this.map = map;
		}

		@Override
		public Iterator<Vec2iMap.Entry<V>> iterator() {
			it = map.entries();
			return this;
		}

		@Override
		public boolean hasNext() {
			return it.hasNext;
		}

		@Override
		public Vec2iMap.Entry<V> next() {
			LongMap.Entry<V> entry = it.next();
			tmpEntry.x = (short)(entry.key & 0xFFFF);
			tmpEntry.y = (short)((entry.key >> 16) & 0xFFFF);
			tmpEntry.value = entry.value;
			return tmpEntry;
		}

		public void remove() {
			it.remove();
		}
	}
}
