package com.winteralexander.gdx.utils.math.vector;

import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Vector4;
import com.winteralexander.gdx.utils.math.MathUtil;

import static com.winteralexander.gdx.utils.Validation.ensureInRange;
import static com.winteralexander.gdx.utils.Validation.ensureNotNull;
import static java.lang.Math.abs;

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

	public static float getComponent(Vector2 vec, int component) {
		ensureInRange(component, 0, 2, "component");
		return component == 0 ? vec.x : vec.y;
	}

	public static float getComponent(Vector3 vec, int component) {
		ensureInRange(component, 0, 3, "component");

		return component == 0
				? vec.x
				: component == 1
					? vec.y
					: vec.z;
	}

	public static float getComponent(Vector4 vec, int component) {
		ensureInRange(component, 0, 4, "component");

		return component == 0
				? vec.x
				: component == 1
					? vec.y
					: component == 2
						? vec.z
						: vec.w;
	}

	public static void setFromArray(Vector2 vec, float[] array, int offset) {
		if(array.length - offset < 2)
			throw new IllegalArgumentException("Provided array doesn't contain enough values");

		vec.set(array[offset], array[offset + 1]);
	}

	public static void setFromArray(Vector3 vec, float[] array, int offset) {
		if(array.length - offset < 3)
			throw new IllegalArgumentException("Provided array doesn't contain enough values");

		vec.set(array[offset], array[offset + 1], array[offset + 2]);
	}

	public static void setFromArray(Vector4 vec, float[] array, int offset) {
		if(array.length - offset < 4)
			throw new IllegalArgumentException("Provided array doesn't contain enough values");

		vec.set(array[offset], array[offset + 1], array[offset + 2], array[offset + 3]);
	}

	public static float maxComp(Vector2 vec) {
		return Math.max(vec.x, vec.y);
	}

	public static float maxComp(Vector3 vec) {
		return Math.max(vec.x, Math.max(vec.y, vec.z));
	}

	public static float minComp(Vector2 vec) {
		return Math.min(vec.x, vec.y);
	}

	public static float minComp(Vector3 vec) {
		return Math.min(vec.x, Math.min(vec.y, vec.z));
	}

	public static Vector2 align(Vector2 vec, float gridStep) {
		vec.x = Math.round(vec.x / gridStep) * gridStep;
		vec.y = Math.round(vec.y / gridStep) * gridStep;
		return vec;
	}

	public static Vector2 inv(Vector2 vector) {
		vector.x = 1f / vector.x;
		vector.y = 1f / vector.y;
		return vector;
	}

	/**
	 * Returns the length of an axis aligned vector
	 *
	 * @param aaVector vector to get the length from
	 * @return length of that axis aligned vector
	 */
	public static float aaLength(Vector2 aaVector) {
		return abs(aaVector.x + aaVector.y);
	}

	@SuppressWarnings("SuspiciousNameCombination")
	public static Vector2 rotate90(Vector2 vec, float rotation) {
		rotation = MathUtil.negMod(rotation, 360f);

		if(rotation > -45f && rotation <= 45f)
			return vec;

		if(rotation > 45f && rotation <= 135f)
			return vec.set(-vec.y, vec.x);

		if(rotation > 135f && rotation <= 225f)
			return vec.scl(-1f);

		return vec.set(vec.y, -vec.x);
	}
}
