package com.winteralexander.gdx.utils.test;

import com.winteralexander.gdx.utils.collection.iterator.NullIterator;
import com.winteralexander.gdx.utils.collection.iterator.UnitIterator;
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

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
}
