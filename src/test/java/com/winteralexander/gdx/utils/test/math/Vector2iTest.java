package com.winteralexander.gdx.utils.test.math;

import com.winteralexander.gdx.utils.math.vector.Vector2i;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link Vector2i}
 * <p>
 * Created on 2026-04-17.
 *
 * @author Alexander Winter
 */
public class Vector2iTest {
	@Test
	public void testMinMax() {
		Vector2i v1 = new Vector2i(0, 10);
		Vector2i v2 = new Vector2i(5, 4);
		v1.min(v2);
		assertEquals(new Vector2i(0, 4), v1);

		v1.max(v2);
		assertEquals(new Vector2i(5, 4), v1);
	}

	@Test
	public void testClamp() {
		Vector2i v1 = new Vector2i(0, 11);
		Vector2i v2 = new Vector2i(5, 6);
		Vector2i v3 = new Vector2i(10, 9);
		v1.clamp(v2, v3);
		assertEquals(new Vector2i(5, 9), v1);

		v1.set(1000, -1).clamp(v2, v3);
		assertEquals(new Vector2i(10, 6), v1);
	}
}
