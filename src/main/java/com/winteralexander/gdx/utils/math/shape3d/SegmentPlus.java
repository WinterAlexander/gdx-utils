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
		return (b.dot(point) - b.dot(a) - a.dot(point) + a.dot(a)) / a.dst2(b);
	}
}
