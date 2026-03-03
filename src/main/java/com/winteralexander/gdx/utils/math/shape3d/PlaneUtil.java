package com.winteralexander.gdx.utils.math.shape3d;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;

/**
 * Utility class for {@link Plane} operations, not thread safe.
 * <p>
 * Created on 2025-09-16.
 *
 * @author Alexander Winter
 */
public class PlaneUtil {
	private static final Vector3 tmpVec1 = new Vector3(),
			tmpVec2 = new Vector3(),
			tmpVec3 = new Vector3();

	private PlaneUtil() {}

	/**
	 * Multiplies the specified plane in-plane by the specified 4x4 matrix
	 *
	 * @param plane plane to multiply
	 * @param transform matrix to left-multiply
	 */
	public static void mul(Plane plane, Matrix4 transform) {
		tmpVec2.set(-plane.normal.y, plane.normal.z, plane.normal.x).crs(plane.normal);
		tmpVec3.set(tmpVec2).crs(plane.normal);

		tmpVec1.set(plane.normal).scl(-plane.d);
		tmpVec2.add(tmpVec1);
		tmpVec3.add(tmpVec1);
		tmpVec1.mul(transform);
		tmpVec2.mul(transform);
		tmpVec3.mul(transform);
		plane.set(tmpVec2, tmpVec1, tmpVec3);
	}
}
