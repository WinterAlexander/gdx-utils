package com.winteralexander.gdx.utils.math;

import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Vector4;
import com.winteralexander.gdx.utils.math.vector.Vector2i;
import com.winteralexander.gdx.utils.math.vector.Vector3i;

import static com.winteralexander.gdx.utils.Validation.ensureNotNull;

/**
 * Offers useful methods and constants for LibGDX vectors
 * <p>
 * Created by on 2017-04-13.
 *
 * @author Alexander Winter
 */
public class VectorUtil {
	public static final Vector2 UP = new Vector2(0f, 1f);
	public static final Vector2 DOWN = new Vector2(0f, -1f);
	public static final Vector2 LEFT = new Vector2(-1f, 0f);
	public static final Vector2 RIGHT = new Vector2(1f, 0f);

	private VectorUtil() {}

	public static <V extends Vector<V>> V round(V vec, int digits) {
		ensureNotNull(vec, "vec");

		if(vec instanceof Vector2) {
			round((Vector2)vec, digits);
			return vec;
		}

		if(vec instanceof Vector3) {
			round((Vector3)vec, digits);
			return vec;
		}

		if(vec instanceof Vector4) {
			round((Vector4)vec, digits);
			return vec;
		}

		throw new UnsupportedOperationException("Type " + vec.getClass() + " not supported by " +
				"round");
	}

	public static Vector2 round(Vector2 vec, int digits) {
		vec.x = MathUtil.round(vec.x, digits);
		vec.y = MathUtil.round(vec.y, digits);
		return vec;
	}

	public static Vector3 round(Vector3 vec, int digits) {
		vec.x = MathUtil.round(vec.x, digits);
		vec.y = MathUtil.round(vec.y, digits);
		vec.z = MathUtil.round(vec.z, digits);
		return vec;
	}

	public static Vector4 round(Vector4 vec, int digits) {
		vec.x = MathUtil.round(vec.x, digits);
		vec.y = MathUtil.round(vec.y, digits);
		vec.z = MathUtil.round(vec.z, digits);
		vec.w = MathUtil.round(vec.w, digits);
		return vec;
	}

	public static Vector2 min(Vector2 vec, float x, float y) {
		vec.x = Math.min(vec.x, x);
		vec.y = Math.min(vec.y, y);
		return vec;
	}

	public static Vector2 min(Vector2 vec, Vector2 min) {
		return min(vec, min.x, min.y);
	}

	public static Vector2 max(Vector2 vec, float x, float y) {
		vec.x = Math.max(vec.x, x);
		vec.y = Math.max(vec.y, y);
		return vec;
	}

	public static Vector2 max(Vector2 vec, Vector2 max) {
		return max(vec, max.x, max.y);
	}

	public static Vector3 min(Vector3 vec, float x, float y, float z) {
		vec.x = Math.min(vec.x, x);
		vec.y = Math.min(vec.y, y);
		vec.z = Math.min(vec.z, z);
		return vec;
	}

	public static Vector3 min(Vector3 vec, Vector3 min) {
		return min(vec, min.x, min.y, min.z);
	}

	public static Vector3 max(Vector3 vec, float x, float y, float z) {
		vec.x = Math.max(vec.x, x);
		vec.y = Math.max(vec.y, y);
		vec.z = Math.max(vec.z, z);
		return vec;
	}

	public static Vector3 max(Vector3 vec, Vector3 max) {
		return max(vec, max.x, max.y, max.z);
	}
}
