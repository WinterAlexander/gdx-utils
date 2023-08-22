package com.github.winteralexander.gdx.utils.test;

import com.badlogic.gdx.utils.StringBuilder;
import com.github.winteralexander.gdx.utils.TimeUtil;
import org.junit.Test;

import static com.github.winteralexander.gdx.utils.TimeUtil.*;
import static org.junit.Assert.*;

/**
 * Unit tests for TimeUtil methods
 * <p>
 * Created on 2018-01-13.
 *
 * @author Alexander Winter
 */
public class TimeUtilTest {
	@Test
	public void testDisplayIntoMillis() {
		StringBuilder sb = new StringBuilder();
		initTimeDisplay(sb);

		displayIntoFromMillis(sb, 1000);
		assertEquals("00:00:01.000", sb.toString());

		displayIntoFromMillis(sb, 22116 + 60000 * 10 + 3600000);
		assertEquals("01:10:22.116", sb.toString());

		displayIntoFromMillis(sb, 1);
		assertEquals("00:00:00.001", sb.toString());

		displayIntoFromMillis(sb, 33444 + 22 * 60000 + 11 * 3600000);
		assertEquals("11:22:33.444", sb.toString());
	}

	@Test
	public void testDisplayIntoFromTicks() {
		StringBuilder sb = new StringBuilder();
		initTimeDisplay(sb);

		displayIntoFromTicks(sb, 60, 0f);
		assertEquals("00:00:01.000", sb.toString());

		displayIntoFromTicks(sb, (22117 + 60000 * 10 + 3600000) * 60 / 1000, 0f);
		assertEquals("01:10:22.117", sb.toString());

		displayIntoFromTicks(sb, 1, 0f);
		assertEquals("00:00:00.017", sb.toString());
	}

	@Test
	public void testIsToday() {
		assertTrue(TimeUtil.isToday(System.currentTimeMillis()));
		assertFalse(TimeUtil.isToday(System.currentTimeMillis() - 24 * 60 * 60 * 1000));
		assertFalse(TimeUtil.isToday(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
	}

	@Test
	public void durationParseTest() {
		assertEquals(0, parseDuration("0"));
		assertEquals(0, parseDuration("0d"));
		assertEquals(0, parseDuration("0m"));
		assertEquals(0, parseDuration("0h"));
		assertEquals(0, parseDuration("0s"));

		for(int i = 0; i < 10; i++) {
			assertEquals(i * 1000, parseDuration(i + "s"));
			assertEquals(i * 1000 * 60, parseDuration(i + "m"));
			assertEquals(i * 1000 * 60 * 60, parseDuration(i + "h"));
			assertEquals(i * 1000 * 60 * 60 * 24, parseDuration(i + "d"));
		}

		assertEquals(2 * 60 * 1000 + 1000, parseDuration("2m1s"));
		assertEquals(20 * 60 * 1000 + 30 * 1000, parseDuration("20m30s"));
		assertEquals(20 * 60 * 1000 + 30 * 1000, parseDuration("20.5m"));
	}
}
