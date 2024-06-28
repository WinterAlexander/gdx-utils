package com.winteralexander.gdx.utils.test;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.winteralexander.gdx.utils.math.VectorUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link VectorUtil}
 * <p>
 * Created on 2024-06-28.
 *
 * @author Alexander Winter
 */
public class VectorUtilTest {
	@Test
	public void testMinMaxVec2() {
		Vector2 val = new Vector2(0f, 2f);
		Vector2 other = new Vector2(-6f, 10f);
		assertEquals(new Vector2(0f, 10f), VectorUtil.max(val.cpy(), other));
		assertEquals(new Vector2(-6f, 2f), VectorUtil.min(val.cpy(), other));
	}

	@Test
	public void testMinMaxVec3() {
		Vector3 val = new Vector3(0f, 2f, 4f);
		Vector3 other = new Vector3(4f, 1f, 3f);
		assertEquals(new Vector3(4f, 2f, 4f), VectorUtil.max(val.cpy(), other));
		assertEquals(new Vector3(0f, 1f, 3f), VectorUtil.min(val.cpy(), other));
	}
}
