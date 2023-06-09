package me.winter.gdx.utils.test;

import org.junit.Test;

import static me.winter.gdx.utils.math.MathUtil.*;
import static org.junit.Assert.*;

/**
 * Unit test to test MathUtil's methods
 * <p>
 * Created on 2018-06-03.
 *
 * @author Alexander Winter
 */
public class MathUtilTest
{
	private static final float FLOAT_ERROR = 0.001f;

	@Test
	public void testProjectToRectEdge()
	{
		assertEquals(new TestVec2(5f, 0f), projectToRectEdge(0f, 10f, 20f, new TestVec2()));
		assertEquals(new TestVec2(5f, 5f), projectToRectEdge(45f, 10f, 10f, new TestVec2()));
		assertEquals(new TestVec2(0f, 10f), projectToRectEdge(90f, 10f, 20f, new TestVec2()));
		assertEquals(new TestVec2(-6f, 6f), projectToRectEdge(135f, 12f, 12f, new TestVec2()));
		assertEquals(new TestVec2(-6f, 6f), projectToRectEdge(-225f, 12f, 12f, new TestVec2()));
		assertEquals(new TestVec2(0f, -7f), projectToRectEdge(-90f, 10f, 14f, new TestVec2()));
		assertEquals(new TestVec2(0f, -7f), projectToRectEdge(270f, 10f, 14f, new TestVec2()));
		assertEquals(new TestVec2(-5f, 0f), projectToRectEdge(-180f, 10f, 14f, new TestVec2()));
		assertEquals(new TestVec2(-5f, 0f), projectToRectEdge(180f, 10f, 14f, new TestVec2()));
	}

	@Test
	public void testNegModFloat()
	{
		assertEquals(15f, negMod(375f, 360f), FLOAT_ERROR);
		assertEquals(345f, negMod(-15f, 360f), FLOAT_ERROR);
		assertEquals(90f, negMod(90f, 360f), FLOAT_ERROR);
		assertEquals(180f, negMod(-180f, 360f), FLOAT_ERROR);

		assertEquals(1f, negMod(-8f, 3f), FLOAT_ERROR);
	}

	@Test
	public void testNegModComparison()
	{
		for(int i = Integer.MIN_VALUE / 1000; i < Integer.MAX_VALUE / 1000; i += 461)
		{
			float val = Float.intBitsToFloat(i);
			assertEquals("Value: " + val, slowNegMod(val, 360f), negMod(val, 360f), FLOAT_ERROR);
		}
	}

	private static float slowNegMod(float val, float divider)
	{
		while(val < 0)
			val += divider;

		return val % divider;
	}

	@Test
	public void testLineIntersectsLine()
	{
		assertTrue(lineIntersectsLine(0f, 0f, 5f, 5f, 0f, 5f, 5f, 0f));
		assertTrue(lineIntersectsLine(0f, 5f, 5f, 0f, 0f, 0f, 5f, 5f));

		assertFalse(lineIntersectsLine(-1f, -1f, 1f, 1f, 0f, 2f, 2f, 0f));
		assertFalse(lineIntersectsLine(-1f, -1f, 1f, 1f, 0f, 0f, 1f, -1f));
	}
}
