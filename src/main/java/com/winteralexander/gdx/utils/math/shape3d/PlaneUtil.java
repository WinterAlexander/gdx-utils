package com.winteralexander.gdx.utils.math.shape3d;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector4;
import com.winteralexander.gdx.utils.math.vector.VectorUtil;

/**
 * Utility class for {@link Plane} operations, not thread safe.
 * <p>
 * Created on 2025-09-16.
 *
 * @author Alexander Winter
 */
public class PlaneUtil {
	private static final Vector4 tmpVec4 = new Vector4();

	private PlaneUtil() {}

	/**
	 * Multiplies the plane equation by a specified 4x4 matrix. Transforming a plane equation by
	 * a coordinate transformation matrix should involve multiplying the plane equation by the
	 * inverse transpose of the transformation matrix.
	 *
	 * @param plane plane to multiply
	 * @param matrix4 matrix to left-multiply
	 */
	public static void mul(Plane plane, Matrix4 matrix4) {
		tmpVec4.set(plane.normal, plane.d);
		VectorUtil.mul(tmpVec4, matrix4);
		plane.normal.set(tmpVec4.x, tmpVec4.y, tmpVec4.z).nor();
		plane.d = tmpVec4.w;
	}
	/**
	 * Multiplies the plane equation by the transpose of a specified 4x4 matrix.
	 *
	 * @param plane plane to multiply
	 * @param matrix4 matrix to left-multiply the transpose of
	 */
	public static void traMul(Plane plane, Matrix4 matrix4) {
		tmpVec4.set(plane.normal, plane.d);
		VectorUtil.traMul(tmpVec4, matrix4);
		plane.normal.set(tmpVec4.x, tmpVec4.y, tmpVec4.z).nor();
		plane.d = tmpVec4.w;
	}
}
