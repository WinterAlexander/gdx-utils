package com.winteralexander.gdx.utils.collection;

import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Null;
import com.winteralexander.gdx.utils.math.vector.Vector2i;

import java.util.Iterator;

import static com.winteralexander.gdx.utils.Validation.ensureNotNull;

/**
 * A fast map that maps 2 shorts to values by converting the 2 shorts to ints, useful to store
 * anything with 2D spatial coordinates. Method signatures accept ints instead of shorts for
 * convenience, but those are truncated.
 * <p>
 * Created on 2024-09-05.
 *
 * @author Alexander Winter
 */
public class Vec2sMap<V> implements Iterable<Vec2sMap.Entry<V>> {
	private final KeysRedirectIterator keysIt;
	private final EntriesRedirectIterator<V> entriesIt;

	private final IntMap<V> map;

	public Vec2sMap() {
		map = new IntMap<>();
		keysIt = new KeysRedirectIterator(map);
		entriesIt = new EntriesRedirectIterator<>(map);
	}

	public Vec2sMap(int initialCapacity) {
		map = new IntMap<>(initialCapacity);
		keysIt = new KeysRedirectIterator(map);
		entriesIt = new EntriesRedirectIterator<>(map);
	}

	public Vec2sMap(int initialCapacity, float loadFactor) {
		map = new IntMap<>(initialCapacity, loadFactor);
		keysIt = new KeysRedirectIterator(map);
		entriesIt = new EntriesRedirectIterator<>(map);
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

	public V remove(int x, int y) {
		return map.remove(key((short)x, (short)y));
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

	public IntMap.Values<V> values() {
		return map.values();
	}

	@Override
	public EntriesRedirectIterator<V> iterator() {
		entriesIt.iterator();
		return entriesIt;
	}

	private int key(short x, short y) {
		return (x & 0xFFFF) + ((int)y << 16);
	}

	public static class Entry<V> {
		public short x, y;
		public @Null V value;

		public String toString () {
			return "(" + x + ", " + y + ")" + "=" + value;
		}
	}

	public static class KeysRedirectIterator implements Iterable<Vector2i>, Iterator<Vector2i> {
		private final IntMap<?> map;
		private IntMap.Keys it = null;
		private final Vector2i tmpVec2 = new Vector2i();

		public KeysRedirectIterator(IntMap<?> map) {
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
			int key = it.next();
			return tmpVec2.set((short)(key & 0xFFFF), (short)((key >> 16) & 0xFFFF));
		}

		public void remove() {
			it.remove();
		}
	}

	public static class EntriesRedirectIterator<V> implements Iterable<Entry<V>>, Iterator<Entry<V>> {
		private final IntMap<V> map;
		private IntMap.Entries<V> it = null;
		private final Entry<V> tmpEntry = new Entry<>();

		public EntriesRedirectIterator(IntMap<V> map) {
			ensureNotNull(map, "map");
			this.map = map;
		}

		@Override
		public Iterator<Entry<V>> iterator() {
			it = map.entries();
			return this;
		}

		@Override
		public boolean hasNext() {
			return it.hasNext;
		}

		@Override
		public Entry<V> next() {
			IntMap.Entry<V> entry = it.next();
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
