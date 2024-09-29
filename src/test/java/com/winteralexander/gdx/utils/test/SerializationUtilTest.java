package com.winteralexander.gdx.utils.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Vector4;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.LongMap;
import com.winteralexander.gdx.utils.collection.Vec2iMap;
import com.winteralexander.gdx.utils.collection.Vec2sMap;
import com.winteralexander.gdx.utils.gfx.UVTransform;
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

	@Test
	public void testIntLongMapSerialization() throws IOException {
		ensureProperSerialization(new IntMap<>(), Vector2i.class);
		ensureProperSerialization(new LongMap<>(), Vector3i.class);
		ensureProperSerialization(new Vec2sMap<>(), Void.class);
		ensureProperSerialization(new Vec2iMap<>(), Boolean.class);

		IntMap<Vector2i> vecMap = new IntMap<>();
		LongMap<Integer> vecLongMap = new LongMap<>();
		Vec2sMap<UVTransform> vec2sMap = new Vec2sMap<>();
		Vec2iMap<Color> vec2iMap = new Vec2iMap<>();

		vecMap.put(1, new Vector2i(2, 3));
		vecMap.put(3, new Vector2i(-2, 3));
		vecMap.put(10, new Vector2i(-3, 3));

		vecLongMap.put(-10, 3);
		vecLongMap.put(-5, 111);

		vec2sMap.put(1, 1, UVTransform.CLOCKWISE);
		vec2sMap.put(1, -1, UVTransform.COUNTER_CLOCKWISE);
		vec2sMap.put(1, -11, null);

		vec2iMap.put(0, 0, new Color(1f, 0.5f, 0.5f, 0.2f));
		vec2iMap.put(0, 0, new Color(1f, 0.6f, 0.5f, 0.1f));
		vec2iMap.put(0, 0, new Color(0.7f, 0.8f, 0.2f, 0.553f));

		ensureProperSerialization(vecMap, Vector2i.class);
		ensureProperSerialization(vecLongMap, Integer.class);
		ensureProperSerialization(vec2sMap, UVTransform.class);
		ensureProperSerialization(vec2iMap, Color.class);
	}

	public <T> void ensureProperSerialization(IntMap<T> map, Class<T> type) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		writeIntMap(outputStream, map);

		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
		IntMap<T> other = readIntMap(inputStream, type);
		assertEquals(map.size, other.size);
		IntMap.Keys it = map.keys();

		while(it.hasNext) {
			int key = it.next();
			assertEquals(map.get(key), other.get(key));
		}
	}

	public <T> void ensureProperSerialization(LongMap<T> map, Class<T> type) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		writeLongMap(outputStream, map);

		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
		LongMap<T> other = readLongMap(inputStream, type);
		assertEquals(map.size, other.size);
		LongMap.Keys it = map.keys();

		while(it.hasNext) {
			long key = it.next();
			assertEquals(map.get(key), other.get(key));
		}
	}

	public <T> void ensureProperSerialization(Vec2sMap<T> map, Class<T> type) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		writeVec2sMap(outputStream, map);

		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
		Vec2sMap<T> other = readVec2sMap(inputStream, type);
		assertEquals(map.size(), other.size());

		for(Vector2i key : map.keys()) {
			assertEquals(map.get(key), other.get(key));
		}
	}

	public <T> void ensureProperSerialization(Vec2iMap<T> map, Class<T> type) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		writeVec2iMap(outputStream, map);

		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
		Vec2iMap<T> other = readVec2iMap(inputStream, type);
		assertEquals(map.size(), other.size());

		for(Vector2i key : map.keys()) {
			assertEquals(map.get(key), other.get(key));
		}
	}
}
