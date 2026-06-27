package com.winteralexander.gdx.utils.test.math.shape3d;

import com.badlogic.gdx.math.Vector3;
import com.winteralexander.gdx.utils.math.shape3d.SegmentPlus;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link SegmentPlus}
 * <p>
 * Created on 2026-06-27.
 *
 * @author Alexander Winter
 */
public class SegmentPlusTest {
	@Test
	public void testGetParameterPrecision() {
		assertEquals(1f,
				SegmentPlus.getParameter(new Vector3(-0.1f, 0.01f, -0.5f),
						new Vector3(-0.075f, 0.01f, -0.5f),
						new Vector3(-0.075f, 0.01f, -0.5f)),
				0f);
	}
}
