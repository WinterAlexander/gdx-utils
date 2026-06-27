package com.winteralexander.gdx.utils.math.shape3d;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Segment;

/**
 * Extension of {@link Segment} with convenience null constructor and toString function
 * <p>
 * Created on 2024-08-09.
 *
 * @author Alexander Winter
 */
public class SegmentPlus extends Segment {
	public SegmentPlus() {
		this(0f, 0f, 0f, 0f, 0f, 0f);
	}

	public SegmentPlus(Vector3 a, Vector3 b) {
		super(a, b);
	}

	// clang-format off
	public SegmentPlus(float aX, float aY, float aZ,
	                   float bX, float bY, float bZ) {
		// clang-format on
		super(aX, aY, aZ, bX, bY, bZ);
	}

	public void set(Segment other) {
		a.set(other.a);
		b.set(other.b);
	}

	public boolean epsilonEquals(Segment other, float epsilon) {
		return a.epsilonEquals(other.a, epsilon) && b.epsilonEquals(other.b, epsilon)
				|| a.epsilonEquals(other.b, epsilon) && b.epsilonEquals(other.a, epsilon);
	}

	public float getParameter(Vector3 point) {
		return getParameter(a, b, point);
	}

	@Override
	public String toString() {
		return a + " -> " + b;
	}

	public SegmentPlus cpy() {
		return new SegmentPlus(a, b);
	}

	public static float getParameter(Vector3 a, Vector3 b, Vector3 point) {
		// ((b - a) dot (x - a)) / ||b - a||^2 but unrolled
		float relBx = b.x - a.x;
		float relBy = b.y - a.y;
		float relBz = b.z - a.z;

		float relPx = point.x - a.x;
		float relPy = point.y - a.y;
		float relPz = point.z - a.z;

		return (relBx * relPx + relBy * relPy + relBz * relPz)
				/ (relBx * relBx + relBy * relBy + relBz * relBz);
	}
}
