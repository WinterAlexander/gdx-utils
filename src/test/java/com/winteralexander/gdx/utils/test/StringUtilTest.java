package com.winteralexander.gdx.utils.test;

import com.winteralexander.gdx.utils.StringUtil;
import org.junit.Test;
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
}
