package com.winteralexander.gdx.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Vector4;

import java.nio.FloatBuffer;

/**
 * Utility class for using various buffers
 * <p>
 * Created on 2024-09-02.
 *
 * @author Alexander Winter
 */
public class BufferUtil {
	private BufferUtil() {}

	public static Vector2 getVector2(FloatBuffer buffer) {
		return getVector2(buffer, new Vector2());
	}

	public static Vector2 getVector2(FloatBuffer buffer, Vector2 out) {
		out.x = buffer.get();
		out.y = buffer.get();
		return out;
	}

	public static Vector2 getVector2(FloatBuffer buffer, int offset) {
		return getVector2(buffer, offset, new Vector2());
	}

	public static Vector2 getVector2(FloatBuffer buffer, int offset, Vector2 out) {
		out.x = buffer.get(offset);
		out.y = buffer.get(offset + 1);
		return out;
	}

	public static Vector3 getVector3(FloatBuffer buffer) {
		return getVector3(buffer, new Vector3());
	}

	public static Vector3 getVector3(FloatBuffer buffer, Vector3 out) {
		out.x = buffer.get();
		out.y = buffer.get();
		out.z = buffer.get();
		return out;
	}

	public static Vector3 getVector3(FloatBuffer buffer, int offset) {
		return getVector3(buffer, offset, new Vector3());
	}

	public static Vector3 getVector3(FloatBuffer buffer, int offset, Vector3 out) {
		out.x = buffer.get(offset);
		out.y = buffer.get(offset + 1);
		out.z = buffer.get(offset + 2);
		return out;
	}

	public static Vector4 getVector4(FloatBuffer buffer) {
		return getVector4(buffer, new Vector4());
	}

	public static Vector4 getVector4(FloatBuffer buffer, Vector4 out) {
		out.x = buffer.get();
		out.y = buffer.get();
		out.z = buffer.get();
		out.w = buffer.get();
		return out;
	}

	public static Vector4 getVector4(FloatBuffer buffer, int offset) {
		return getVector4(buffer, offset, new Vector4());
	}

	public static Vector4 getVector4(FloatBuffer buffer, int offset, Vector4 out) {
		out.x = buffer.get(offset);
		out.y = buffer.get(offset + 1);
		out.z = buffer.get(offset + 2);
		out.w = buffer.get(offset + 3);
		return out;
	}

	public static void putVector2(FloatBuffer buffer, Vector2 vec) {
		buffer.put(vec.x);
		buffer.put(vec.y);
	}

	public static void putVector2(FloatBuffer buffer, int offset, Vector2 vec) {
		buffer.put(offset, vec.x);
		buffer.put(offset + 1, vec.y);
	}

	public static void putVector3(FloatBuffer buffer, Vector3 vec) {
		buffer.put(vec.x);
		buffer.put(vec.y);
		buffer.put(vec.z);
	}

	public static void putVector3(FloatBuffer buffer, int offset, Vector3 vec) {
		buffer.put(offset, vec.x);
		buffer.put(offset + 1, vec.y);
		buffer.put(offset + 2, vec.z);
	}

	public static void putVector4(FloatBuffer buffer, Vector4 vec) {
		buffer.put(vec.x);
		buffer.put(vec.y);
		buffer.put(vec.z);
		buffer.put(vec.w);
	}

	public static void putVector4(FloatBuffer buffer, int offset, Vector4 vec) {
		buffer.put(offset, vec.x);
		buffer.put(offset + 1, vec.y);
		buffer.put(offset + 2, vec.z);
		buffer.put(offset + 3, vec.w);
	}
}
