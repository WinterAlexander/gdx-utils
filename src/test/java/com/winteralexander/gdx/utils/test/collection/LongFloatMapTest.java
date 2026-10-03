package com.winteralexander.gdx.utils.test.collection;

import com.badlogic.gdx.utils.Collections;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.LongArray;
import com.winteralexander.gdx.utils.collection.LongFloatMap;
import com.winteralexander.gdx.utils.collection.Vec2iMap;
import com.winteralexander.gdx.utils.collection.Vec2sMap;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link LongFloatMap}
 * <p>
 * Created on 2026-10-03.
 *
 * @author Alexander Winter
 */
public class LongFloatMapTest {
	@Test
	public void testConstructorsAndCapacityValidation() {
		LongFloatMap defaultMap = new LongFloatMap();
		assertTrue(defaultMap.isEmpty());
		assertFalse(defaultMap.notEmpty());

		LongFloatMap capacityMap = new LongFloatMap(4);
		assertTrue(capacityMap.isEmpty());
		LongFloatMap loadFactorMap = new LongFloatMap(4, 0.5f);
		assertTrue(loadFactorMap.isEmpty());

		assertThrows(IllegalArgumentException.class, () -> new LongFloatMap(-1));
		assertThrows(IllegalArgumentException.class, () -> new LongFloatMap(1, 0f));
		assertThrows(IllegalArgumentException.class, () -> new LongFloatMap(1, 1f));
		assertThrows(IllegalArgumentException.class, () -> new LongFloatMap(1, -0.5f));
	}

	@Test
	public void testPutGetAndZeroKey() {
		LongFloatMap map = new LongFloatMap(4);
		assertEquals(-1f, map.get(0, -1f), 0f);
		assertEquals(-1f, map.get(Long.MIN_VALUE, -1f), 0f);
		assertFalse(map.containsKey(0));
		assertFalse(map.containsKey(Long.MIN_VALUE));

		map.put(0, 12.5f);
		map.put(Long.MIN_VALUE, -3f);
		map.put(Long.MAX_VALUE, 7f);
		assertEquals(3, map.size);
		assertTrue(map.containsKey(0));
		assertEquals(12.5f, map.get(0, -1f), 0f);
		assertEquals(-3f, map.get(Long.MIN_VALUE, 0f), 0f);
		assertEquals(7f, map.get(Long.MAX_VALUE, 0f), 0f);

		map.put(0, 14f);
		map.put(Long.MIN_VALUE, 4f);
		assertEquals(3, map.size);
		assertEquals(14f, map.get(0, 0f), 0f);
		assertEquals(4f, map.get(Long.MIN_VALUE, 0f), 0f);
		assertTrue(map.notEmpty());
		assertFalse(map.isEmpty());
	}

	@Test
	public void testPutReturningOldValue() {
		LongFloatMap map = new LongFloatMap(1);
		assertEquals(-9f, map.put(0, 2f, -9f), 0f);
		assertEquals(2f, map.put(0, 3f, -9f), 0f);
		assertEquals(-9f, map.put(42, 5f, -9f), 0f);
		assertEquals(5f, map.put(42, 8f, -9f), 0f);
		assertEquals(2, map.size);
		assertEquals(3f, map.get(0, 0f), 0f);
		assertEquals(8f, map.get(42, 0f), 0f);
	}

	@Test
	public void testPutAllAndCopyConstructor() {
		LongFloatMap source = new LongFloatMap(2);
		source.put(0, 100f);
		source.put(Long.MIN_VALUE, -1f);
		source.put(3_000_000_000L, 2f);
		LongFloatMap copy = new LongFloatMap(source);
		assertEquals(source, copy);
		assertEquals(source.hashCode(), copy.hashCode());

		LongFloatMap destination = new LongFloatMap(1);
		destination.put(3_000_000_000L, 9f);
		destination.put(4, 4f);
		destination.putAll(source);
		assertEquals(4, destination.size);
		assertEquals(100f, destination.get(0, 0f), 0f);
		assertEquals(-1f, destination.get(Long.MIN_VALUE, 0f), 0f);
		assertEquals(2f, destination.get(3_000_000_000L, 0f), 0f);
		assertEquals(4f, destination.get(4, 0f), 0f);

		LongFloatMap empty = new LongFloatMap(source);
		empty.clear();
		assertTrue(empty.isEmpty());
		assertNotEquals(source, empty);
	}

	@Test
	public void testGetAndIncrement() {
		LongFloatMap map = new LongFloatMap(1);
		assertEquals(10f, map.getAndIncrement(0, 10f, 2.5f), 0f);
		assertEquals(12.5f, map.get(0, 0f), 0f);
		assertEquals(12.5f, map.getAndIncrement(0, 10f, 2.5f), 0f);
		assertEquals(15f, map.get(0, 0f), 0f);

		assertEquals(-2f, map.getAndIncrement(Long.MAX_VALUE, -2f, 3f), 0f);
		assertEquals(1f, map.get(Long.MAX_VALUE, 0f), 0f);
		assertEquals(1f, map.getAndIncrement(Long.MAX_VALUE, 100f, -4f), 0f);
		assertEquals(-3f, map.get(Long.MAX_VALUE, 0f), 0f);
		assertEquals(2, map.size);
	}

	@Test
	public void testRemoveAndBackwardShiftAfterCollisions() {
		LongFloatMap map = new CollidingLongFloatMap();
		for(long key = 1; key <= 6; key++)
			map.put(key, key * 2f);
		map.put(0, 99f);

		assertEquals(99f, map.remove(0, -1f), 0f);
		assertEquals(-1f, map.remove(0, -1f), 0f);
		assertEquals(6f, map.remove(3, -1f), 0f);
		assertFalse(map.containsKey(3));
		for(long key : new long[] {1, 2, 4, 5, 6}) {
			assertTrue("key " + key + " should survive cluster repair", map.containsKey(key));
			assertEquals(key * 2f, map.get(key, -1f), 0f);
		}
		assertEquals(-1f, map.remove(100, -1f), 0f);
		assertEquals(5, map.size);
	}

	@Test
	public void testContainmentAndFindKey() {
		LongFloatMap map = new LongFloatMap();
		map.put(0, 1.25f);
		map.put(Long.MIN_VALUE, 2.5f);
		map.put(1_234_567_890_123L, 3.75f);

		assertTrue(map.containsValue(1.25f));
		assertTrue(map.containsValue(2.5f));
		assertFalse(map.containsValue(2.6f));
		assertTrue(map.containsValue(3.751f, 0.002f));
		assertFalse(map.containsValue(3.751f, 0.0001f));
		assertTrue(map.containsKey(Long.MIN_VALUE));
		assertFalse(map.containsKey(Long.MAX_VALUE));
		assertEquals(0L, map.findKey(1.25f, -1L));
		assertEquals(Long.MIN_VALUE, map.findKey(2.5f, -1L));
		assertEquals(1_234_567_890_123L, map.findKey(3.75f, -1L));
		assertEquals(900L, map.findKey(4f, 900L));
		assertEquals(1_234_567_890_123L, map.findKey(3.751f, 0.002f, -1L));
		assertEquals(-8L, map.findKey(4f, 0.01f, -8L));

		map.remove(0, 0f);
		assertFalse(map.containsValue(1.25f));
	}

	@Test
	public void testEnsureCapacityShrinkAndClearWithCapacity() {
		LongFloatMap map = new LongFloatMap(1);
		map.ensureCapacity(100);
		map.put(0, 1f);
		map.put(Long.MIN_VALUE, 2f);
		map.shrink(2);
		assertEquals(1f, map.get(0, 0f), 0f);
		assertEquals(2f, map.get(Long.MIN_VALUE, 0f), 0f);
		map.shrink(100);
		assertEquals(2, map.size);
		assertThrows(IllegalArgumentException.class, () -> map.shrink(-1));

		map.clear(0);
		assertTrue(map.isEmpty());
		map.put(99, 4f);
		map.clear(100);
		assertTrue(map.isEmpty());
		assertThrows(IllegalArgumentException.class, () -> map.clear(-1));
	}

	@Test
	public void testClear() {
		LongFloatMap map = new LongFloatMap();
		map.clear();
		assertTrue(map.isEmpty());
		map.put(0, 5f);
		map.put(1, 6f);
		map.clear();
		assertEquals(0, map.size);
		assertFalse(map.containsKey(0));
		assertFalse(map.containsKey(1));
		assertFalse(map.notEmpty());
		assertTrue(map.isEmpty());
	}

	@Test
	public void testEqualsHashCodeAndToString() {
		LongFloatMap first = new LongFloatMap();
		first.put(0, 1.5f);
		first.put(Long.MIN_VALUE, 2f);
		first.put(17L, -3f);
		LongFloatMap same = new LongFloatMap(100, 0.5f);
		same.put(17L, -3f);
		same.put(0, 1.5f);
		same.put(Long.MIN_VALUE, 2f);
		assertEquals(first, first);
		assertEquals(first, same);
		assertEquals(same, first);
		assertEquals(first.hashCode(), same.hashCode());
		LongFloatMap positiveZero = new LongFloatMap();
		positiveZero.put(9, 0f);
		LongFloatMap negativeZero = new LongFloatMap();
		negativeZero.put(9, -0f);
		assertEquals(positiveZero, negativeZero);
		assertEquals(positiveZero.hashCode(), negativeZero.hashCode());
		assertNotEquals(first, new Object());
		LongFloatMap differentValue = new LongFloatMap(first);
		differentValue.put(17L, 8f);
		assertNotEquals(first, differentValue);
		LongFloatMap differentKey = new LongFloatMap(first);
		differentKey.remove(Long.MIN_VALUE, 0f);
		differentKey.put(Long.MAX_VALUE, 2f);
		assertNotEquals(first, differentKey);

		assertEquals("[]", new LongFloatMap().toString());
		assertTrue(first.toString().startsWith("[0=1.5"));
		assertTrue(first.toString().contains("-9223372036854775808=2.0"));
		assertTrue(first.toString().contains("17=-3.0"));
		assertEquals("17=-3.0",
				new LongFloatMap
						.Entry() {
							{
								key = 17;
								value = -3f;
							}
						}
						.toString());
	}

	@Test
	public void testEntriesIteratorAndIterator() {
		LongFloatMap map = populatedMap();
		Map<Long, Float> found = new HashMap<>();
		LongFloatMap.Entries entries = map.entries();
		assertSame(entries, entries.iterator());
		assertThrows(IllegalStateException.class, entries::remove);
		while(entries.hasNext()) {
			LongFloatMap.Entry entry = entries.next();
			found.put(entry.key, entry.value);
		}
		assertEquals(expectedEntries(), found);
		assertThrows(NoSuchElementException.class, entries::next);
		entries.reset();
		assertTrue(entries.hasNext());

		Iterator<LongFloatMap.Entry> iterator = map.iterator();
		assertTrue(iterator.hasNext());
		assertNotNull(iterator.next());
	}

	@Test
	public void testKeysValuesAndArrayConversion() {
		LongFloatMap map = populatedMap();
		Set<Long> keys = new HashSet<>();
		LongFloatMap.Keys keyIterator = map.keys();
		assertThrows(IllegalStateException.class, keyIterator::remove);
		while(keyIterator.hasNext)
			keys.add(keyIterator.next());
		assertEquals(expectedEntries().keySet(), keys);
		assertThrows(NoSuchElementException.class, keyIterator::next);

		LongArray allKeys = map.keys().toArray();
		assertEquals(expectedEntries().keySet(), asLongSet(allKeys));
		LongArray appendedKeys = new LongArray();
		appendedKeys.add(-5L);
		map.keys().toArray(appendedKeys);
		assertEquals(expectedEntries().keySet().size() + 1, appendedKeys.size);
		assertEquals(-5L, appendedKeys.get(0));

		Set<Float> values = new HashSet<>();
		LongFloatMap.Values valueIterator = map.values();
		assertSame(valueIterator, valueIterator.iterator());
		assertThrows(IllegalStateException.class, valueIterator::remove);
		while(valueIterator.hasNext())
			values.add(valueIterator.next());
		assertEquals(new HashSet<>(expectedEntries().values()), values);
		assertThrows(NoSuchElementException.class, valueIterator::next);
		valueIterator.reset();
		assertTrue(valueIterator.hasNext());

		FloatArray allValues = map.values().toArray();
		assertEquals(expectedEntries().size(), allValues.size);
		assertEquals(new HashSet<>(expectedEntries().values()), asFloatSet(allValues));
		FloatArray appendedValues = new FloatArray();
		appendedValues.add(-5f);
		map.values().toArray(appendedValues);
		assertEquals(expectedEntries().size() + 1, appendedValues.size);
		assertEquals(-5f, appendedValues.get(0), 0f);
	}

	@Test
	public void testIteratorRemovalAndZeroKeyRemoval() {
		LongFloatMap map = new CollidingLongFloatMap();
		map.put(0, 0f);
		for(long i = 1; i <= 8; i++)
			map.put(i, i);
		LongFloatMap.Keys repeatedRemove = map.keys();
		repeatedRemove.next();
		repeatedRemove.remove();
		assertThrows(IllegalStateException.class, repeatedRemove::remove);

		map = new CollidingLongFloatMap();
		map.put(0, 0f);
		for(long i = 1; i <= 8; i++)
			map.put(i, i);

		LongFloatMap.Keys keys = map.keys();
		assertThrows(IllegalStateException.class, keys::remove);
		Set<Long> removed = new HashSet<>();
		while(keys.hasNext) {
			long key = keys.next();
			removed.add(key);
			keys.remove();
		}
		assertEquals(9, removed.size());
		assertTrue(removed.contains(0L));
		assertEquals(0, map.size);
		assertTrue(map.isEmpty());

		map.put(0, 4f);
		map.put(1, 5f);
		LongFloatMap.Entries entries = map.entries();
		while(entries.hasNext()) {
			entries.next();
			entries.remove();
		}
		assertTrue(map.isEmpty());
	}

	@Test
	public void testIteratorAllocationAndNestedIteratorGuard() {
		boolean previous = Collections.allocateIterators;
		try {
			LongFloatMap map = populatedMap();
			Collections.allocateIterators = false;
			LongFloatMap.Entries outer = map.entries();
			LongFloatMap.Entries inner = map.entries();
			assertNotSame(outer, inner);
			assertThrows(GdxRuntimeException.class, outer::hasNext);
			assertThrows(GdxRuntimeException.class, outer::next);
			assertTrue(inner.hasNext());

			LongFloatMap.Values values = map.values();
			LongFloatMap.Values nextValues = map.values();
			assertThrows(GdxRuntimeException.class, values::hasNext);
			assertTrue(nextValues.hasNext());
			LongFloatMap.Keys keys = map.keys();
			LongFloatMap.Keys nextKeys = map.keys();
			assertThrows(GdxRuntimeException.class, keys::next);
			assertTrue(nextKeys.hasNext);

			Collections.allocateIterators = true;
			assertNotSame(map.entries(), map.entries());
			assertNotSame(map.values(), map.values());
			assertNotSame(map.keys(), map.keys());
		} finally {
			Collections.allocateIterators = previous;
		}
	}

	@Test
	public void testRandomizedOperationsAgainstHashMap() {
		LongFloatMap actual = new LongFloatMap(1);
		Map<Long, Float> expected = new HashMap<>();
		java.util.Random random = new java.util.Random(0x5eed);
		for(int i = 0; i < 2_000; i++) {
			long key;
			switch(random.nextInt(5)) {
				case 0:
					key = 0;
					break;
				case 1:
					key = random.nextInt(41) - 20;
					break;
				default:
					key = random.nextLong();
			}
			switch(random.nextInt(3)) {
				case 0:
					{
						float value = random.nextInt(1_000) / 10f;
						actual.put(key, value);
						expected.put(key, value);
						break;
					}
				case 1:
					{
						float increment = random.nextInt(20) / 10f;
						float defaultValue = random.nextInt(50);
						float before = expected.containsKey(key) ? expected.get(key) : defaultValue;
						assertEquals(before,
								actual.getAndIncrement(key, defaultValue, increment),
								0f);
						expected.put(key, before + increment);
						break;
					}
				default:
					{
						float defaultValue = -999f;
						Float removed = expected.remove(key);
						assertEquals(removed == null ? defaultValue : removed,
								actual.remove(key, defaultValue),
								0f);
					}
			}
			assertEquals(expected.size(), actual.size);
			for(Map.Entry<Long, Float> entry : expected.entrySet()) {
				assertTrue(actual.containsKey(entry.getKey()));
				assertEquals(entry.getValue(), actual.get(entry.getKey(), Float.NaN), 0f);
			}
		}
	}

	private static LongFloatMap populatedMap() {
		LongFloatMap map = new LongFloatMap(1);
		map.put(0, 1f);
		map.put(Long.MIN_VALUE, 2f);
		map.put(Long.MAX_VALUE, 3f);
		map.put(9_876_543_210_123L, 4f);
		return map;
	}

	private static Map<Long, Float> expectedEntries() {
		Map<Long, Float> expected = new HashMap<>();
		expected.put(0L, 1f);
		expected.put(Long.MIN_VALUE, 2f);
		expected.put(Long.MAX_VALUE, 3f);
		expected.put(9_876_543_210_123L, 4f);
		return expected;
	}

	private static Set<Long> asLongSet(LongArray array) {
		Set<Long> result = new HashSet<>();
		for(int i = 0; i < array.size; i++)
			result.add(array.get(i));
		return result;
	}

	private static Set<Float> asFloatSet(FloatArray array) {
		Set<Float> result = new HashSet<>();
		for(int i = 0; i < array.size; i++)
			result.add(array.get(i));
		return result;
	}

	private static class CollidingLongFloatMap extends LongFloatMap {
		CollidingLongFloatMap() {
			super(1);
		}

		@Override
		protected int place(long item) {
			return 0;
		}
	}
}
