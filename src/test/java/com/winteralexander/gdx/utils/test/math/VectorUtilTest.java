package com.winteralexander.gdx.utils.test.math;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Vector4;
import com.winteralexander.gdx.utils.math.vector.VectorUtil;
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

	@Test
	public void testRound() {
		Vector2 val = new Vector2(0.199f, 0.149f);
		VectorUtil.round(val, 2);
		assertEquals(new Vector2(0.2f, 0.15f), val);

		Vector3 val2 = new Vector3(0.199f, 0.149f, 2.51f);
		VectorUtil.round(val2, 1);
		assertEquals(new Vector3(0.2f, 0.1f, 2.5f), val2);

		Vector4 val3 = new Vector4(444900f, 902031f, 120921f, 0.0000013f);
		VectorUtil.round(val3, -2);
		assertEquals(new Vector4(444900f, 902000f, 120900f, 0f), val3);
	}

	@Test
	public void testGetComponent() {
		Vector2 val = new Vector2(0.199f, 0.149f);
		assertEquals(0.199f, VectorUtil.getComponent(val, 0), 1e-10f);
		assertEquals(0.149f, VectorUtil.getComponent(val, 1), 1e-10f);

		Vector3 val2 = new Vector3(0.199f, 0.149f, 2.51f);
		assertEquals(0.199f, VectorUtil.getComponent(val2, 0), 1e-10f);
		assertEquals(0.149f, VectorUtil.getComponent(val2, 1), 1e-10f);
		assertEquals(2.51f, VectorUtil.getComponent(val2, 2), 1e-10f);

		Vector4 val3 = new Vector4(444900f, 902031f, 120921f, 0.0000013f);
		assertEquals(444900f, VectorUtil.getComponent(val3, 0), 1e-10f);
		assertEquals(902031f, VectorUtil.getComponent(val3, 1), 1e-10f);
		assertEquals(120921f, VectorUtil.getComponent(val3, 2), 1e-10f);
		assertEquals(0.0000013f, VectorUtil.getComponent(val3, 3), 1e-10f);
	}

	@Test
	public void testSetFromArray() {
		Vector2 vec2 = new Vector2();
		Vector3 vec3 = new Vector3();
		Vector4 vec4 = new Vector4();
		float[] array = {
			1f, 2f, 3f, 4f, 5f, 6f, 7f
		};

		VectorUtil.setFromArray(vec2, array, 1);
		assertEquals(2f, vec2.x, 1e-10f);
		assertEquals(3f, vec2.y, 1e-10f);

		VectorUtil.setFromArray(vec3, array, 0);
		assertEquals(1f, vec3.x, 1e-10f);
		assertEquals(2f, vec3.y, 1e-10f);
		assertEquals(3f, vec3.z, 1e-10f);

		VectorUtil.setFromArray(vec4, array, 3);
		assertEquals(4f, vec4.x, 1e-10f);
		assertEquals(5f, vec4.y, 1e-10f);
		assertEquals(6f, vec4.z, 1e-10f);
		assertEquals(7f, vec4.w, 1e-10f);
	}
}
