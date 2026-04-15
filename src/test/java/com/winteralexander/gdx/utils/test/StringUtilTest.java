package com.winteralexander.gdx.utils.test;

import com.winteralexander.gdx.utils.StringUtil;
import org.junit.Test;

import java.util.Random;

import static com.winteralexander.gdx.utils.StringUtil.randomAlphanumericalString;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link StringUtil}
 * <p>
 * Created on 2026-03-09.
 *
 * @author Alexander Winter
 */
public class StringUtilTest {
	@Test
	public void testQuote() {
		assertEquals("\"hello\"", StringUtil.quote("hello"));
		assertEquals("\"hel\\\"lo\"", StringUtil.quote("hel\"lo"));
		assertEquals("\"hel\\\\lo\"", StringUtil.quote("hel\\lo"));
	}

	@Test
	public void testAlphanumString() {
		Random random = new Random();
		String randomString = randomAlphanumericalString(random, 16);
		assertEquals(16, randomString.length());

		for(int i = 0; i < 100; i++) {
			randomString = randomAlphanumericalString(random, i);
			assertEquals(i, randomString.length());
		}
	}
}
