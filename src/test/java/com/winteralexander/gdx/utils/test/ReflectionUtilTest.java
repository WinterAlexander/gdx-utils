package com.winteralexander.gdx.utils.test;

import com.winteralexander.gdx.utils.ReflectionUtil;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests {@link com.winteralexander.gdx.utils.ReflectionUtil}
 * <p>
 * Created on 2024-04-16.
 *
 * @author Alexander Winter
 */
public class ReflectionUtilTest {
	@SuppressWarnings("UnnecessaryBoxing")
	@Test
	public void testGetSet() {
		Integer dummy = new Integer(60);
		int value = ReflectionUtil.get(dummy, "value");

		assertEquals(60, value);

		ReflectionUtil.set(dummy, "value", 42);

		assertEquals(42, dummy.intValue());
	}

	@Test
	public void testPrettyString() {
		Object outer = new Object() {
			Object innerObjectAAAAA = new Object() {
				Object innerObjectBBBBB = new Object() {
					Object innerObjectCCCCC = new Object() {
						Object innerObjectDDDDD = new Object() {
							int innerValueEEEEE = 32;
						};
					};
				};
			};
		};

		String level3 = ReflectionUtil.toPrettyString(outer, 3, 0);
		assertTrue(level3.contains("innerObjectAAAAA"));
		assertTrue(level3.contains("innerObjectBBBBB"));
		assertTrue(level3.contains("innerObjectCCCCC"));
		assertFalse(level3.contains("innerObjectDDDDD"));
		assertFalse(level3.contains("innerValueEEEEE"));

		String level4 = ReflectionUtil.toPrettyString(outer, 4, 0);
		assertTrue(level4.contains("innerObjectAAAAA"));
		assertTrue(level4.contains("innerObjectBBBBB"));
		assertTrue(level4.contains("innerObjectCCCCC"));
		assertTrue(level4.contains("innerObjectDDDDD"));
		assertFalse(level4.contains("innerValueEEEEE"));

		String all = ReflectionUtil.toPrettyString(outer);
		assertTrue(all.contains("innerObjectAAAAA"));
		assertTrue(all.contains("innerObjectBBBBB"));
		assertTrue(all.contains("innerObjectCCCCC"));
		assertTrue(all.contains("innerObjectDDDDD"));
		assertTrue(all.contains("innerValueEEEEE"));
 	}
}
