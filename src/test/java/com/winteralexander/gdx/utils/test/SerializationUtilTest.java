package com.winteralexander.gdx.utils.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Vector4;
import com.winteralexander.gdx.utils.io.SerializationUtil;
import com.winteralexander.gdx.utils.math.vector.Vector2i;
import com.winteralexander.gdx.utils.math.vector.Vector3i;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

import static com.winteralexander.gdx.utils.io.SerializationUtil.*;
import static org.junit.Assert.assertEquals;

/**
 * Tests the {@link SerializationUtil} utility class
 * <p>
 * Created on 2020-10-13.
 *
 * @author Alexander Winter
 */
public class SerializationUtilTest {
	@Test
	public void testColorSerialization() throws IOException {
		Random random = new Random();
		for(int i = 0; i < 20000; i++) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Color color = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), random.nextFloat());

			writeColor(baos, color);

			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

			Color color2 = new Color(0f, 0f, 0f, 0f);

			readColor(bais, color2);

			assertEquals(color.r, color2.r, 1f / 255f);
			assertEquals(color.g, color2.g, 1f / 255f);
			assertEquals(color.b, color2.b, 1f / 255f);
			assertEquals(color.a, color2.a, 1f / 255f);
		}
	}

	@Test
	public void testRGBSerialization() throws IOException {
		Random random = new Random();
		for(int i = 0; i < 20000; i++) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Color color = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), random.nextFloat());

			writeRGB(baos, color);

			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

			Color color2 = new Color(0f, 0f, 0f, 0.5f);

			readRGB(bais, color2);

			assertEquals(color.r, color2.r, 1f / 255f);
			assertEquals(color.g, color2.g, 1f / 255f);
			assertEquals(color.b, color2.b, 1f / 255f);
			assertEquals(0.5f, color2.a, 0.0000001f);
		}
	}

	@Test
	public void testVectorSerialization() throws IOException {
		Random random = new Random();
		for(int i = 0; i < 100; i++) {
			Vector2 vec2 = new Vector2(random.nextFloat(), random.nextFloat());
			Vector3 vec3 = new Vector3(random.nextFloat(), random.nextFloat(), random.nextFloat());
			Vector4 vec4 = new Vector4(random.nextFloat(), random.nextFloat(),
					random.nextFloat(), random.nextFloat());

			Vector2i vec2i = new Vector2i(random.nextInt(), random.nextInt());
			Vector3i vec3i = new Vector3i(random.nextInt(), random.nextInt(), random.nextInt());

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			writeVec2(outputStream, vec2);
			writeVec3(outputStream, vec3);
			writeVec4(outputStream, vec4);
			vec2i.writeTo(outputStream);
			vec3i.writeTo(outputStream);

			ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

			Vector2 vec2Out = new Vector2();
			Vector3 vec3Out = new Vector3();
			Vector4 vec4Out = new Vector4();

			Vector2i vec2iOut = new Vector2i();
			Vector3i vec3iOut = new Vector3i();

			readVec2(inputStream, vec2Out);
			readVec3(inputStream, vec3Out);
			readVec4(inputStream, vec4Out);
			vec2iOut.readFrom(inputStream);
			vec3iOut.readFrom(inputStream);

			assertEquals(vec2.x, vec2Out.x, 0f);
			assertEquals(vec2.y, vec2Out.y, 0f);

			assertEquals(vec3.x, vec3Out.x, 0f);
			assertEquals(vec3.y, vec3Out.y, 0f);
			assertEquals(vec3.z, vec3Out.z, 0f);

			assertEquals(vec4.x, vec4Out.x, 0f);
			assertEquals(vec4.y, vec4Out.y, 0f);
			assertEquals(vec4.z, vec4Out.z, 0f);
			assertEquals(vec4.w, vec4Out.w, 0f);

			assertEquals(vec2i.x, vec2iOut.x);
			assertEquals(vec2i.y, vec2iOut.y);

			assertEquals(vec3i.x, vec3iOut.x);
			assertEquals(vec3i.y, vec3iOut.y);
			assertEquals(vec3i.z, vec3iOut.z);
		}
	}
}
