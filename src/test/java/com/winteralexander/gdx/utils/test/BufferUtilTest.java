package com.winteralexander.gdx.utils.test;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.BufferUtils;
import com.winteralexander.gdx.utils.BufferUtil;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link BufferUtil}
 * <p>
 * Created on 2024-09-02.
 *
 * @author Alexander Winter
 */
public class BufferUtilTest {
	@Test
	public void testVec3() {
		Vector3 vec1 = new Vector3(1f, 2f, 3f);
		Vector3 vec2 = new Vector3(4f, 5f, 6f);
		Vector3 vec3 = new Vector3(7f, 8f, 9f);

		FloatBuffer buffer = ByteBuffer.allocateDirect(9 * 4).asFloatBuffer();

		BufferUtil.putVector3(buffer, vec1);
		BufferUtil.putVector3(buffer, vec2);
		BufferUtil.putVector3(buffer, vec3);

		Vector3 vec = BufferUtil.getVector3(buffer, 0);
		assertEquals(vec1, vec);

		vec = BufferUtil.getVector3(buffer, 2);
		assertEquals(new Vector3(3f, 4f, 5f), vec);

		vec = BufferUtil.getVector3(buffer, 6);
		assertEquals(vec3, vec);
	}
}
