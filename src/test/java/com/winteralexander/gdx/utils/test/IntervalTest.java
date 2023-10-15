package com.winteralexander.gdx.utils.test;

import com.winteralexander.gdx.utils.math.interval.Interval;
import com.winteralexander.gdx.utils.math.interval.SimpleInterval;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link Interval}
 * <p>
 * Created on 2018-08-30.
 *
 * @author Alexander Winter
 */
public class IntervalTest {
	@Test
	public void testSelfSimplification() {
		Interval a = new SimpleInterval(0, 5);
		a = a.add(new SimpleInterval(4, 8));

		assertTrue(a instanceof SimpleInterval);
		assertEquals(0, ((SimpleInterval)a).start, 0);
		assertEquals(8, ((SimpleInterval)a).end, 0);

		Interval b = new SimpleInterval(0, 5);
		b = b.add(new SimpleInterval(7, 9));
		b = b.add(new SimpleInterval(4, 8));

		assertTrue(b instanceof SimpleInterval);
		assertEquals(0, ((SimpleInterval)b).start, 0);
		assertEquals(9, ((SimpleInterval)b).end, 0);

		Interval c = new SimpleInterval(-4, 5);
		c = c.add(new SimpleInterval(7, 9));
		c = c.add(new SimpleInterval(4, 8).add(new SimpleInterval(9, 11)));

		assertTrue(c instanceof SimpleInterval);
		assertEquals(-4, ((SimpleInterval)c).start, 0);
		assertEquals(11, ((SimpleInterval)c).end, 0);
	}
}
