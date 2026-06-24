package com.winteralexander.gdx.utils.test.collection;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.LongArray;
import com.winteralexander.gdx.utils.collection.CollectionUtil;
import org.junit.Test;

import java.util.Comparator;

import static com.winteralexander.gdx.utils.collection.CollectionUtil.*;
import static org.junit.Assert.*;

/**
 * Tests methods in {@link CollectionUtilTest}
 * <p>
 * Created on 2019-07-11.
 *
 * @author Alexander Winter
 */
public class CollectionUtilTest {
	@Test
	public void testExcept() {
		String foo = "foo";
		String bar = "bar";
		String fizz = "fizz";
		String crunch = "crunch";

		String[] arr = new String[] {foo, bar, fizz, crunch};

		String[] withoutfoo = except(String.class, arr, foo);
		String[] withoutbar = except(String.class, arr, bar);
		String[] withoutfizz = except(String.class, arr, fizz);
		String[] withoutcrunch = except(String.class, arr, crunch);

		assertArrayEquals(withoutfoo, new String[] {bar, fizz, crunch});
		assertArrayEquals(withoutbar, new String[] {foo, fizz, crunch});
		assertArrayEquals(withoutfizz, new String[] {foo, bar, crunch});
		assertArrayEquals(withoutcrunch, new String[] {foo, bar, fizz});
	}

	@Test
	public void testIteratorCasting() {
		Array<Object> objs = new Array<>();
		objs.add("Heloo");
		objs.add("Henloo");
		objs.add("Halo");
		objs.add("Allo");
		assertTrue(CollectionUtil.allInstancesOf(objs, String.class));
		assertTrue(CollectionUtil.allInstancesOf(objs, CharSequence.class));
		Iterable<String> strs = CollectionUtil.castIterable(objs);
		Array<String> array = CollectionUtil.toGdxArray(strs);
		assertEquals(objs, array);
		objs.add(new Object());
		assertFalse(CollectionUtil.allInstancesOf(objs, String.class));
	}

	@Test
	public void testEmptyArray() {
		Array<Integer> empty = CollectionUtil.emptyArray();
		Array<CollectionUtil> empty2 = CollectionUtil.emptyArray();

		assertEquals(0, empty.size);
		assertEquals(0, empty2.size);

		for(Integer inte : empty) {
			fail("Stuff in the empty array");
		}

		for(CollectionUtil inte : empty2) {
			fail("Stuff in the empty array");
		}
	}

	@Test
	public void testGreatest() {
		String[] obs = new String[] {"22", "213", "23424"};

		String greatest = greatest(obs, Comparator.comparingInt(Integer::parseInt));
		assertEquals("23424", greatest);
	}

	@Test
	public void testAny() {
		assertTrue(any(new String[] {"aa", "bb", "cc"}, s -> s.startsWith("a")));
		assertTrue(any(new String[] {"aa", "bb", "cc"}, s -> s.endsWith("c")));
		assertTrue(any(new int[] {1, 2, 3, 4}, i -> i == 3));
		assertFalse(any(new double[] {1.123123, 2.34243, 3.231423, 4.324234}, i -> i == 3));
		assertFalse(any(new long[] {1L, 2L, 3L, 4L}, i -> i == 6L));
	}

	@Test
	public void testAll() {
		assertTrue(all(new String[] {"aa", "bb", "cc"}, s -> s.length() == 2));
		assertTrue(all(new String[] {"aa", "bb", "cc"}, s -> !s.startsWith("d")));
		assertTrue(all(new int[] {1, 2, 3, 4}, i -> i < 10));
		assertFalse(all(new double[] {1.123123, 2.34243, 3.231423, 4.324234}, i -> i > 2.0));
		assertFalse(all(new long[] {1L, 2L, 3L, 4L}, i -> i == 3L));
	}

	@Test
	public void testMinMax() {
		assertEquals(3, CollectionUtil.max(1, 2, 3, -1));
		assertEquals(-1, CollectionUtil.min(1, 2, 3, -1));
		assertEquals(32,
				CollectionUtil.max(new IntArray(new int[] {1, 2, 3, -1, 32, 15, 22, 11, 22})));
		assertEquals(-22,
				CollectionUtil.min(new LongArray(new long[] {1, 2, 3, -1, 32, 15, -22, -11, 22})));
	}

	@Test
	public void testFillRange() {
		IntArray array = new IntArray();
		CollectionUtil.fillFromRange(0, 10, array);
		CollectionUtil.fillFromRange(20, 30, array);
		CollectionUtil.fillFromRange(-30, -20, array);
		CollectionUtil.fillFromRange(18, 16, array);
		CollectionUtil.fillFromRange(-7, -9, array);
		assertEquals(34, array.size);
		assertEquals(0, array.get(0));
		assertEquals(9, array.get(9));
		assertEquals(20, array.get(10));
		assertEquals(29, array.get(19));
		assertEquals(-30, array.get(20));
		assertEquals(-21, array.get(29));
		assertEquals(18, array.get(30));
		assertEquals(17, array.get(31));
		assertEquals(-7, array.get(32));
		assertEquals(-8, array.get(33));
	}
}
