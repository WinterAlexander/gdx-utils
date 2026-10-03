package com.winteralexander.gdx.utils.collection;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * {@link IntFloatMap} but for longs
 * <p>
 * Created on 2026-10-03.
 *
 * @author Alexander Winter
 */
public class LongFloatMap implements Iterable<LongFloatMap.Entry> {
	public int size;

	long[] keyTable;
	float[] valueTable;

	float zeroValue;
	boolean hasZeroValue;

	private final float loadFactor;
	private int threshold;

	protected int shift;
	protected int mask;

	private transient LongFloatMap.Entries entries1, entries2;
	private transient LongFloatMap.Values values1, values2;
	private transient LongFloatMap.Keys keys1, keys2;

	public LongFloatMap() {
		this(51, 0.8f);
	}

	public LongFloatMap(int initialCapacity) {
		this(initialCapacity, 0.8f);
	}

	public LongFloatMap(int initialCapacity, float loadFactor) {
		if(loadFactor <= 0f || loadFactor >= 1f)
			throw new IllegalArgumentException("loadFactor must be > 0 and < 1: " + loadFactor);
		this.loadFactor = loadFactor;

		int tableSize = tableSize(initialCapacity, loadFactor);
		threshold = (int)(tableSize * loadFactor);
		mask = tableSize - 1;
		shift = Long.numberOfLeadingZeros(mask);

		keyTable = new long[tableSize];
		valueTable = new float[tableSize];
	}

	public LongFloatMap(LongFloatMap map) {
		this((int)(map.keyTable.length * map.loadFactor), map.loadFactor);
		System.arraycopy(map.keyTable, 0, keyTable, 0, map.keyTable.length);
		System.arraycopy(map.valueTable, 0, valueTable, 0, map.valueTable.length);
		size = map.size;
		zeroValue = map.zeroValue;
		hasZeroValue = map.hasZeroValue;
	}

	protected int place(long item) {
		return (int)((item ^ item >>> 32) * 0x9E3779B97F4A7C15L >>> shift);
	}

	private int locateKey(long key) {
		long[] keyTable = this.keyTable;
		for(int i = place(key); ; i = i + 1 & mask) {
			long other = keyTable[i];
			if(other == 0)
				return -(i + 1); // Empty space is available.
			if(other == key)
				return i; // Same key was found.
		}
	}

	public void put(long key, float value) {
		if(key == 0) {
			zeroValue = value;
			if(!hasZeroValue) {
				hasZeroValue = true;
				size++;
			}
			return;
		}
		int i = locateKey(key);
		if(i >= 0) { // Existing key was found.
			valueTable[i] = value;
			return;
		}
		i = -(i + 1); // Empty space was found.
		keyTable[i] = key;
		valueTable[i] = value;
		if(++size >= threshold)
			resize(keyTable.length << 1);
	}

	public float put(long key, float value, float defaultValue) {
		if(key == 0) {
			float oldValue = zeroValue;
			zeroValue = value;
			if(!hasZeroValue) {
				hasZeroValue = true;
				size++;
				return defaultValue;
			}
			return oldValue;
		}
		int i = locateKey(key);
		if(i >= 0) { // Existing key was found.
			float oldValue = valueTable[i];
			valueTable[i] = value;
			return oldValue;
		}
		i = -(i + 1); // Empty space was found.
		keyTable[i] = key;
		valueTable[i] = value;
		if(++size >= threshold)
			resize(keyTable.length << 1);
		return defaultValue;
	}

	public void putAll(LongFloatMap map) {
		ensureCapacity(map.size);
		if(map.hasZeroValue)
			put(0, map.zeroValue);
		long[] keyTable = map.keyTable;
		float[] valueTable = map.valueTable;
		for(int i = 0, n = keyTable.length; i < n; i++) {
			long key = keyTable[i];
			if(key != 0)
				put(key, valueTable[i]);
		}
	}

	private void putResize(long key, float value) {
		long[] keyTable = this.keyTable;
		for(int i = place(key); ; i = (i + 1) & mask) {
			if(keyTable[i] == 0) {
				keyTable[i] = key;
				valueTable[i] = value;
				return;
			}
		}
	}

	public float get(long key, float defaultValue) {
		if(key == 0)
			return hasZeroValue ? zeroValue : defaultValue;
		int i = locateKey(key);
		return i >= 0 ? valueTable[i] : defaultValue;
	}

	public float getAndIncrement(long key, float defaultValue, float increment) {
		if(key == 0) {
			if(!hasZeroValue) {
				hasZeroValue = true;
				zeroValue = defaultValue + increment;
				size++;
				return defaultValue;
			}
			float oldValue = zeroValue;
			zeroValue += increment;
			return oldValue;
		}
		int i = locateKey(key);
		if(i >= 0) { // Existing key was found.
			float oldValue = valueTable[i];
			valueTable[i] += increment;
			return oldValue;
		}
		i = -(i + 1); // Empty space was found.
		keyTable[i] = key;
		valueTable[i] = defaultValue + increment;
		if(++size >= threshold)
			resize(keyTable.length << 1);
		return defaultValue;
	}

	public float remove(long key, float defaultValue) {
		if(key == 0) {
			if(!hasZeroValue)
				return defaultValue;
			hasZeroValue = false;
			size--;
			return zeroValue;
		}

		int i = locateKey(key);
		if(i < 0)
			return defaultValue;
		long[] keyTable = this.keyTable;
		float[] valueTable = this.valueTable;
		float oldValue = valueTable[i];
		int mask = this.mask, next = i + 1 & mask;
		while((key = keyTable[next]) != 0) {
			long placement = place(key);
			if((next - placement & mask) > (i - placement & mask)) {
				keyTable[i] = key;
				valueTable[i] = valueTable[next];
				i = next;
			}
			next = next + 1 & mask;
		}
		keyTable[i] = 0;
		size--;
		return oldValue;
	}

	public boolean notEmpty() {
		return size > 0;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public void shrink(int maximumCapacity) {
		if(maximumCapacity < 0)
			throw new IllegalArgumentException("maximumCapacity must be >= 0: " + maximumCapacity);
		int tableSize = tableSize(maximumCapacity, loadFactor);
		if(keyTable.length > tableSize)
			resize(tableSize);
	}

	public void clear(int maximumCapacity) {
		int tableSize = tableSize(maximumCapacity, loadFactor);
		if(keyTable.length <= tableSize) {
			clear();
			return;
		}
		size = 0;
		hasZeroValue = false;
		resize(tableSize);
	}

	public void clear() {
		if(size == 0)
			return;
		Arrays.fill(keyTable, 0);
		size = 0;
		hasZeroValue = false;
	}

	public boolean containsValue(float value) {
		if(hasZeroValue && zeroValue == value)
			return true;
		long[] keyTable = this.keyTable;
		float[] valueTable = this.valueTable;
		for(int i = valueTable.length - 1; i >= 0; i--)
			if(keyTable[i] != 0 && valueTable[i] == value)
				return true;
		return false;
	}

	public boolean containsValue(float value, float epsilon) {
		if(hasZeroValue && Math.abs(zeroValue - value) <= epsilon)
			return true;
		long[] keyTable = this.keyTable;
		float[] valueTable = this.valueTable;
		for(int i = valueTable.length - 1; i >= 0; i--)
			if(keyTable[i] != 0 && Math.abs(valueTable[i] - value) <= epsilon)
				return true;
		return false;
	}

	public boolean containsKey(long key) {
		if(key == 0)
			return hasZeroValue;
		return locateKey(key) >= 0;
	}

	public long findKey(float value, long notFound) {
		if(hasZeroValue && zeroValue == value)
			return 0;
		long[] keyTable = this.keyTable;
		float[] valueTable = this.valueTable;
		for(int i = valueTable.length - 1; i >= 0; i--)
			if(keyTable[i] != 0 && valueTable[i] == value)
				return keyTable[i];
		return notFound;
	}

	public long findKey(float value, float epsilon, long notFound) {
		if(hasZeroValue && Math.abs(zeroValue - value) <= epsilon)
			return 0;
		long[] keyTable = this.keyTable;
		float[] valueTable = this.valueTable;
		for(int i = valueTable.length - 1; i >= 0; i--)
			if(keyTable[i] != 0 && Math.abs(valueTable[i] - value) <= epsilon)
				return keyTable[i];
		return notFound;
	}

	public void ensureCapacity(int additionalCapacity) {
		int tableSize = tableSize(size + additionalCapacity, loadFactor);
		if(keyTable.length < tableSize)
			resize(tableSize);
	}

	private void resize(int newSize) {
		int oldCapacity = keyTable.length;
		threshold = (int)(newSize * loadFactor);
		mask = newSize - 1;
		shift = Long.numberOfLeadingZeros(mask);

		long[] oldKeyTable = keyTable;
		float[] oldValueTable = valueTable;

		keyTable = new long[newSize];
		valueTable = new float[newSize];

		if(size > 0) {
			for(int i = 0; i < oldCapacity; i++) {
				long key = oldKeyTable[i];
				if(key != 0)
					putResize(key, oldValueTable[i]);
			}
		}
	}

	public int hashCode() {
		int h = size;
		if(hasZeroValue)
			h += floatHashCode(zeroValue);
		long[] keyTable = this.keyTable;
		float[] valueTable = this.valueTable;
		for(int i = 0, n = keyTable.length; i < n; i++) {
			long key = keyTable[i];
			if(key != 0)
				h += key * 31 + floatHashCode(valueTable[i]);
		}
		return h;
	}

	private static int floatHashCode(float value) {
		// equals uses float ==, which considers positive and negative zero equal.
		return value == 0f ? 0 : NumberUtils.floatToRawIntBits(value);
	}

	public boolean equals(Object obj) {
		if(obj == this)
			return true;
		if(!(obj instanceof LongFloatMap))
			return false;
		LongFloatMap other = (LongFloatMap)obj;
		if(other.size != size)
			return false;
		if(other.hasZeroValue != hasZeroValue)
			return false;
		if(hasZeroValue) {
			if(other.zeroValue != zeroValue)
				return false;
		}
		long[] keyTable = this.keyTable;
		float[] valueTable = this.valueTable;
		for(int i = 0, n = keyTable.length; i < n; i++) {
			long key = keyTable[i];
			if(key != 0) {
				float otherValue = other.get(key, 0f);
				if(otherValue == 0f && !other.containsKey(key))
					return false;
				if(otherValue != valueTable[i])
					return false;
			}
		}
		return true;
	}

	public String toString() {
		if(size == 0)
			return "[]";
		java.lang.StringBuilder buffer = new java.lang.StringBuilder(32);
		buffer.append('[');
		long[] keyTable = this.keyTable;
		float[] valueTable = this.valueTable;
		int i = keyTable.length;
		if(hasZeroValue) {
			buffer.append("0=");
			buffer.append(zeroValue);
		} else {
			while(i-- > 0) {
				long key = keyTable[i];
				if(key == 0)
					continue;
				buffer.append(key);
				buffer.append('=');
				buffer.append(valueTable[i]);
				break;
			}
		}
		while(i-- > 0) {
			long key = keyTable[i];
			if(key == 0)
				continue;
			buffer.append(", ");
			buffer.append(key);
			buffer.append('=');
			buffer.append(valueTable[i]);
		}
		buffer.append(']');
		return buffer.toString();
	}

	public Iterator<LongFloatMap.Entry> iterator() {
		return entries();
	}

	public LongFloatMap.Entries entries() {
		if(Collections.allocateIterators)
			return new LongFloatMap.Entries(this);
		if(entries1 == null) {
			entries1 = new LongFloatMap.Entries(this);
			entries2 = new LongFloatMap.Entries(this);
		}
		if(!entries1.valid) {
			entries1.reset();
			entries1.valid = true;
			entries2.valid = false;
			return entries1;
		}
		entries2.reset();
		entries2.valid = true;
		entries1.valid = false;
		return entries2;
	}

	public LongFloatMap.Values values() {
		if(Collections.allocateIterators)
			return new LongFloatMap.Values(this);
		if(values1 == null) {
			values1 = new LongFloatMap.Values(this);
			values2 = new LongFloatMap.Values(this);
		}
		if(!values1.valid) {
			values1.reset();
			values1.valid = true;
			values2.valid = false;
			return values1;
		}
		values2.reset();
		values2.valid = true;
		values1.valid = false;
		return values2;
	}

	public LongFloatMap.Keys keys() {
		if(Collections.allocateIterators)
			return new LongFloatMap.Keys(this);
		if(keys1 == null) {
			keys1 = new LongFloatMap.Keys(this);
			keys2 = new LongFloatMap.Keys(this);
		}
		if(!keys1.valid) {
			keys1.reset();
			keys1.valid = true;
			keys2.valid = false;
			return keys1;
		}
		keys2.reset();
		keys2.valid = true;
		keys1.valid = false;
		return keys2;
	}

	static public class Entry {
		public long key;
		public float value;

		public String toString() {
			return key + "=" + value;
		}
	}

	static private class MapIterator {
		static private final int INDEX_ILLEGAL = -2;
		static final int INDEX_ZERO = -1;

		public boolean hasNext;

		final LongFloatMap map;
		int nextIndex, currentIndex;
		boolean valid = true;

		public MapIterator(LongFloatMap map) {
			this.map = map;
			reset();
		}

		public void reset() {
			currentIndex = INDEX_ILLEGAL;
			nextIndex = INDEX_ZERO;
			if(map.hasZeroValue)
				hasNext = true;
			else
				findNextIndex();
		}

		void findNextIndex() {
			long[] keyTable = map.keyTable;
			for(int n = keyTable.length; ++nextIndex < n; ) {
				if(keyTable[nextIndex] != 0) {
					hasNext = true;
					return;
				}
			}
			hasNext = false;
		}

		public void remove() {
			int i = currentIndex;
			if(i == INDEX_ZERO && map.hasZeroValue) {
				map.hasZeroValue = false;
			} else if(i < 0) {
				throw new IllegalStateException("next must be called before remove.");
			} else {
				long[] keyTable = map.keyTable;
				float[] valueTable = map.valueTable;
				int mask = map.mask, next = i + 1 & mask;
				long key;
				while((key = keyTable[next]) != 0) {
					long placement = map.place(key);
					if((next - placement & mask) > (i - placement & mask)) {
						keyTable[i] = key;
						valueTable[i] = valueTable[next];
						i = next;
					}
					next = next + 1 & mask;
				}
				keyTable[i] = 0;
				if(i != currentIndex)
					--nextIndex;
			}
			currentIndex = INDEX_ILLEGAL;
			map.size--;
		}
	}

	static public class Entries extends LongFloatMap.MapIterator implements Iterable<LongFloatMap.Entry>, Iterator<LongFloatMap.Entry> {
		private final LongFloatMap.Entry entry = new LongFloatMap.Entry();

		public Entries(LongFloatMap map) {
			super(map);
		}

		public LongFloatMap.Entry next() {
			if(!hasNext)
				throw new NoSuchElementException();
			if(!valid)
				throw new GdxRuntimeException("#iterator() cannot be used nested.");
			long[] keyTable = map.keyTable;
			if(nextIndex == INDEX_ZERO) {
				entry.key = 0;
				entry.value = map.zeroValue;
			} else {
				entry.key = keyTable[nextIndex];
				entry.value = map.valueTable[nextIndex];
			}
			currentIndex = nextIndex;
			findNextIndex();
			return entry;
		}

		public boolean hasNext() {
			if(!valid)
				throw new GdxRuntimeException("#iterator() cannot be used nested.");
			return hasNext;
		}

		public Iterator<LongFloatMap.Entry> iterator() {
			return this;
		}

		public void remove() {
			super.remove();
		}
	}

	static public class Values extends LongFloatMap.MapIterator {
		public Values(LongFloatMap map) {
			super(map);
		}

		public boolean hasNext() {
			if(!valid)
				throw new GdxRuntimeException("#iterator() cannot be used nested.");
			return hasNext;
		}

		public float next() {
			if(!hasNext)
				throw new NoSuchElementException();
			if(!valid)
				throw new GdxRuntimeException("#iterator() cannot be used nested.");
			float value = nextIndex == INDEX_ZERO ? map.zeroValue : map.valueTable[nextIndex];
			currentIndex = nextIndex;
			findNextIndex();
			return value;
		}

		public LongFloatMap.Values iterator() {
			return this;
		}

		public FloatArray toArray() {
			FloatArray array = new FloatArray(true, map.size);
			while(hasNext)
				array.add(next());
			return array;
		}

		public FloatArray toArray(FloatArray array) {
			while(hasNext)
				array.add(next());
			return array;
		}
	}

	static public class Keys extends LongFloatMap.MapIterator {
		public Keys(LongFloatMap map) {
			super(map);
		}

		public long next() {
			if(!hasNext)
				throw new NoSuchElementException();
			if(!valid)
				throw new GdxRuntimeException("#iterator() cannot be used nested.");
			long key = nextIndex == INDEX_ZERO ? 0 : map.keyTable[nextIndex];
			currentIndex = nextIndex;
			findNextIndex();
			return key;
		}

		public LongArray toArray() {
			LongArray array = new LongArray(true, map.size);
			while(hasNext)
				array.add(next());
			return array;
		}

		public LongArray toArray(LongArray array) {
			while(hasNext)
				array.add(next());
			return array;
		}
	}

	private static int tableSize(int capacity, float loadFactor) {
		if(capacity < 0)
			throw new IllegalArgumentException("capacity must be >= 0: " + capacity);
		int tableSize = MathUtils.nextPowerOfTwo(Math.max(2,
				(int)Math.ceil(capacity / loadFactor)));
		if(tableSize > 1 << 30)
			throw new IllegalArgumentException("The required capacity is too large: " + capacity);
		return tableSize;
	}
}
