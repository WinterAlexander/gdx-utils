package com.winteralexander.gdx.utils.test;

import org.junit.Test;

import static com.winteralexander.gdx.utils.collection.CollectionUtil.except;
import static org.junit.Assert.assertArrayEquals;

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
}
