package com.winteralexander.gdx.utils.test.collection;

import com.badlogic.gdx.utils.Array;
import com.winteralexander.gdx.utils.collection.CollectionUtil;
import org.junit.Test;

import java.util.Comparator;

import static com.winteralexander.gdx.utils.collection.CollectionUtil.except;
import static com.winteralexander.gdx.utils.collection.CollectionUtil.greatest;
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

		String[] arr = new String[]{foo, bar, fizz, crunch};

		String[] withoutfoo = except(String.class, arr, foo);
		String[] withoutbar = except(String.class, arr, bar);
		String[] withoutfizz = except(String.class, arr, fizz);
		String[] withoutcrunch = except(String.class, arr, crunch);

		assertArrayEquals(withoutfoo, new String[]{bar, fizz, crunch});
		assertArrayEquals(withoutbar, new String[]{foo, fizz, crunch});
		assertArrayEquals(withoutfizz, new String[]{foo, bar, crunch});
		assertArrayEquals(withoutcrunch, new String[]{foo, bar, fizz});
	}

	@Test
	public void testIteratorCasting() {
		Array<Object> objs = new Array<>();
		objs.add("Heloo");
		objs.add("Henloo");
		objs.add("Halo");
		objs.add("Allo");
		assertTrue(CollectionUtil.allInstanceOf(objs, String.class));
		assertTrue(CollectionUtil.allInstanceOf(objs, CharSequence.class));
		Iterable<String> strs = CollectionUtil.castIterable(objs);
		Array<String> array = CollectionUtil.toGdxArray(strs);
		assertEquals(objs, array);
		objs.add(new Object());
		assertFalse(CollectionUtil.allInstanceOf(objs, String.class));
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
		String[] obs = new String[] {
				"22",
				"213",
				"23424"
		};

		String greatest = greatest(obs, Comparator.comparingInt(Integer::parseInt));
		assertEquals("23424", greatest);
	}
}
