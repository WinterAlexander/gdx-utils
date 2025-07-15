package com.winteralexander.gdx.utils.test.math;

import com.winteralexander.gdx.utils.math.direction.AxisDirection6;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for {@link AxisDirection6}
 * <p>
 * Created on 2025-07-14.
 *
 * @author Alexander Winter
 */
public class AxisDirectionTest {
	@Test
	public void testOppositesAreOpposites() {
		for(AxisDirection6 dir : AxisDirection6.values)
			assertEquals("AxisDirection6." + dir + " is not opposite to AxisDirection6." + dir.opposite(),
					-1f,
					dir.opposite().asVector().dot(dir.asVector()),
					0.0001f);
	}
}
