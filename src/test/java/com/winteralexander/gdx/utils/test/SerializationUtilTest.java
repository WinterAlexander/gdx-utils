package com.winteralexander.gdx.utils.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Vector4;
import com.badlogic.gdx.utils.*;
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
import static org.junit.Assert.*;

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
	public void testMapSerialization() throws IOException {
		ensureProperSerialization(new ObjectMap<>(), Short.class, Vector2.class);
		ensureProperSerialization(new IntMap<>(), Vector2i.class);
		ensureProperSerialization(new LongMap<>(), Vector3i.class);
		ensureProperSerialization(new IntIntMap());
		ensureProperSerialization(new IntFloatMap());
		ensureProperSerialization(new Vec2sMap<>(), Void.class);
		ensureProperSerialization(new Vec2iMap<>(), Boolean.class);

		ObjectMap<Short, Long> objectMap = new ObjectMap<>();
		IntMap<Vector2i> intMap = new IntMap<>();
		LongMap<Integer> longMap = new LongMap<>();
		Vec2sMap<UVTransform> vec2sMap = new Vec2sMap<>();
		Vec2iMap<Color> vec2iMap = new Vec2iMap<>();
		IntIntMap intIntMap = new IntIntMap();
		IntFloatMap intFloatMap = new IntFloatMap();

		objectMap.put((short)5, 6L);
		objectMap.put((short)-3, -13L);
		objectMap.put((short)32, 32L);

		intMap.put(1, new Vector2i(2, 3));
		intMap.put(3, new Vector2i(-2, 3));
		intMap.put(10, new Vector2i(-3, 3));

		longMap.put(-10, 3);
		longMap.put(-5, 111);

		vec2sMap.put(1, 1, UVTransform.CLOCKWISE);
		vec2sMap.put(1, -1, UVTransform.COUNTER_CLOCKWISE);
		vec2sMap.put(1, -11, null);

		vec2iMap.put(0, 0, new Color(1f, 0.5f, 0.5f, 0.2f));
		vec2iMap.put(0, 0, new Color(1f, 0.6f, 0.5f, 0.1f));
		vec2iMap.put(0, 0, new Color(0.7f, 0.8f, 0.2f, 0.553f));

		intIntMap.put(3, 5);
		intIntMap.put(-1, -32);
		intIntMap.put(-2323, 1231234);

		intFloatMap.put(1, 23f);
		intFloatMap.put(13, 12309123f);
		intFloatMap.put(123, -23f);

		ensureProperSerialization(objectMap, Short.class, Long.class);
		ensureProperSerialization(intMap, Vector2i.class);
		ensureProperSerialization(longMap, Integer.class);
		ensureProperSerialization(intIntMap);
		ensureProperSerialization(intFloatMap);
		ensureProperSerialization(vec2sMap, UVTransform.class);
		ensureProperSerialization(vec2iMap, Color.class);
	}

	@Test
	public void testPrimitiveCollectionsSerialization() throws IOException {
		IntArray intArray = new IntArray();
		LongArray longArray = new LongArray();
		IntSet intSet = new IntSet();
		IntFloatMap intFloatMap = new IntFloatMap();
		IntIntMap intIntMap = new IntIntMap();

		ensureProperSerialization(intArray);
		ensureProperSerialization(longArray);
		ensureProperSerialization(intSet);
		ensureProperSerialization(intFloatMap);
		ensureProperSerialization(intIntMap);

		intArray.add(1, 2, 3);
		intArray.add(4, 21, -190);

		longArray.add(203923L, 130291093L, -1201920912901L);
		intSet.addAll(1, 1, 2, 3, 4);
		intFloatMap.put(1, 203.34f);
		intFloatMap.put(2, 303.34f);
		intFloatMap.put(-90, -10903.34f);
		intFloatMap.put(180, 2311203.3234f);

		intIntMap.put(1, 1);
		intIntMap.put(20, 0);
		intIntMap.put(21, 0);
		intIntMap.put(22, 4);
		intIntMap.put(22, 5);

		ensureProperSerialization(intArray);
		ensureProperSerialization(longArray);
		ensureProperSerialization(intSet);
		ensureProperSerialization(intFloatMap);
		ensureProperSerialization(intIntMap);
	}

	private <K, V> void ensureProperSerialization(ObjectMap<K, V> map,
	                                              Class<K> keyType,
	                                              Class<V> valueType) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		writeObjectMap(outputStream, map);

		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
		ObjectMap<K, V> other = readObjectMap(inputStream, keyType, valueType);
		assertEquals(map.size, other.size);

		for(K key : map.keys()) {
			assertEquals(map.get(key), other.get(key));
		}
	}

	private <T> void ensureProperSerialization(IntMap<T> map, Class<T> type) throws IOException {
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

	private <T> void ensureProperSerialization(LongMap<T> map, Class<T> type) throws IOException {
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

	private void ensureProperSerialization(IntIntMap map) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		writeIntIntMap(outputStream, map);

		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
		IntIntMap other = readIntIntMap(inputStream);
		assertEquals(map.size, other.size);
		IntIntMap.Keys it = map.keys();

		while(it.hasNext) {
			int key = it.next();
			int val1 = map.get(key, -1);
			int val2 = other.get(key, -1);
			assertNotEquals(-1, val1);
			assertNotEquals(-1, val2);
			assertEquals(val1, val2);
		}
	}

	private void ensureProperSerialization(IntFloatMap map) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		writeIntFloatMap(outputStream, map);

		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
		IntFloatMap other = readIntFloatMap(inputStream);
		assertEquals(map.size, other.size);
		IntFloatMap.Keys it = map.keys();

		while(it.hasNext) {
			int key = it.next();
			float val1 = map.get(key, -1);
			float val2 = other.get(key, -1);

			assertNotEquals(-1f, val1, 0f);
			assertNotEquals(-1f, val2, 0f);
			assertEquals(val1, val2, 0f);
		}
	}

	private <T> void ensureProperSerialization(Vec2sMap<T> map, Class<T> type) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		writeVec2sMap(outputStream, map);

		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
		Vec2sMap<T> other = readVec2sMap(inputStream, type);
		assertEquals(map.size(), other.size());

		for(Vector2i key : map.keys()) {
			assertEquals(map.get(key), other.get(key));
		}
	}

	private <T> void ensureProperSerialization(Vec2iMap<T> map, Class<T> type) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		writeVec2iMap(outputStream, map);

		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
		Vec2iMap<T> other = readVec2iMap(inputStream, type);
		assertEquals(map.size(), other.size());

		for(Vector2i key : map.keys()) {
			assertEquals(map.get(key), other.get(key));
		}
	}
	private void ensureProperSerialization(Object any) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		writeAny(outputStream, any);

		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
		Object other = readAny(inputStream, any.getClass());
		assertEquals(any, other);
	}

	@Test
	public void testSetSerialization() throws IOException {
		ensureProperSerialization(new ObjectSet<>(), UVTransform.class);
		ensureProperSerialization(new IntSet());

		ObjectSet<Vector3> objectSet = new ObjectSet<>();
		IntSet intSet = new IntSet();

		objectSet.add(new Vector3(3f, -2f, -10f));
		objectSet.add(new Vector3(3.1f, 2.23f, -0.1f));
		objectSet.add(new Vector3(-3.1f, -2.2f, -4f));

		intSet.add(2);
		intSet.add(3);
		intSet.add(-99);

		ensureProperSerialization(objectSet, Vector3.class);
		ensureProperSerialization(intSet);
	}

	private <T> void ensureProperSerialization(ObjectSet<T> set, Class<T> type) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		writeObjectSet(outputStream, set);

		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
		ObjectSet<T> other = readObjectSet(inputStream, type);
		assertEquals(set.size, other.size);

		for(T val : set) {
			assertTrue(other.contains(val));
		}

		for(T val : other) {
			assertTrue(set.contains(val));
		}
	}

	private void ensureProperSerialization(IntSet set) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		writeIntSet(outputStream, set);

		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
		IntSet other = readIntSet(inputStream);
		assertEquals(set.size, other.size);

		IntSet.IntSetIterator it = set.iterator();
		while(it.hasNext) {
			int val = it.next();
			assertTrue(other.contains(val));
		}


		it = other.iterator();
		while(it.hasNext) {
			int val = it.next();
			assertTrue(set.contains(val));
		}
	}
}
