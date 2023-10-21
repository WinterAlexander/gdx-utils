package com.winteralexander.gdx.utils.test.collection;

import com.badlogic.gdx.utils.Array;
import com.winteralexander.gdx.utils.collection.iterator.FlattenIterable;
import com.winteralexander.gdx.utils.collection.iterator.MapIterable;
import com.winteralexander.gdx.utils.collection.iterator.NullIterator;
import com.winteralexander.gdx.utils.collection.iterator.UnitIterator;
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.winteralexander.gdx.utils.collection.CollectionUtil.toArray;
import static com.winteralexander.gdx.utils.collection.CollectionUtil.toGdxArray;
import static org.junit.Assert.*;

/**
 * Test case for iterators provided with gdx-utils
 * <p>
 * Created on 2023-10-20.
 *
 * @author Alexander Winter
 */
public class IteratorTest {
	@Test
	public void testNullIterator() {
		NullIterator<String> it = NullIterator.instance();

		assertNullIterator(it);
		assertNullIterator(it.iterator());

		NullIterator<Integer> it3 = new NullIterator<>();
		assertNullIterator(it3);
		assertNullIterator(it3.iterator());
	}

	private void assertNullIterator(NullIterator<?> it)
	{
		assertFalse(it.hasNext());
		assertThrows(NoSuchElementException.class, it::next);

		for(Object obj : it)
			fail("Unexpected element in NullIterator: " + obj);
	}

	@Test
	public void testUnitIterator() {
		UnitIterator<String> it = new UnitIterator<>("hello");

		for(int j = 0; j < 2; j++)
		{
			int i = 0;
			for(String hello : it) {
				assertEquals(i, 0);
				assertEquals(hello, "hello");
				i++;
			}

			Iterator<String> newIt = it.iterator();
			assertTrue(newIt.hasNext());
			assertEquals(newIt.next(), "hello");
			assertFalse(newIt.hasNext());
			assertThrows(NoSuchElementException.class, newIt::next);
		}
	}

	@Test
	public void testMapIterable() {
		Array<String> array = toGdxArray("1", "2", "3", "4");
		Iterable<Integer> ints = new MapIterable<>(array, Integer::parseInt);

		Integer[] intArray = toArray(Integer.class, ints);
		assertArrayEquals(intArray, new Integer[]{ 1, 2, 3, 4 });
	}

	@Test
	public void testFlattenIterable() {
		Array<String> array1 = toGdxArray("1", "2", "3");
		Array<String> array2 = toGdxArray("4", "5");
		Array<String> array3 = new Array<>();
		Array<String> array4 = toGdxArray("6", "7", "8");

		Array<Array<String>> arrs = toGdxArray(array1, array2, array3, array4);
		Iterable<String> flatIt = new FlattenIterable<>(arrs);
		String[] flat = toArray(String.class, flatIt);
		assertArrayEquals(new String[] { "1", "2", "3", "4", "5", "6", "7", "8" }, flat);
	}
}
