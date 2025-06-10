package com.winteralexander.gdx.utils.math;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Vector4;

/**
 * Utility to produce noise values
 * <p>
 * Created on 2025-02-04.
 *
 * @author Alexander Winter
 */
public class NoiseUtil {
	private static final Vector2 tmpVec2 = new Vector2();
	private static final Vector3 tmpVec3 = new Vector3();

	private static final int M = 0x5bd1e995;
	private static final int A = 1190494759;
	private static final int B = 2147483647;
	private static final int C = -735179117;
	private static final int D = 179424673;

	public static Vector2 murmurHash22(float srcX, float srcY) {
		return murmurHash22(srcX, srcY, tmpVec2);
	}

	public static Vector2 murmurHash22(float srcX, float srcY, Vector2 out) {
		int x = Float.floatToRawIntBits(srcX);
		int y = Float.floatToRawIntBits(srcY);
		int outX = A;
		int outY = B;

		x *= M;
		y *= M;

		x ^= x >>> 24;
		y ^= y >>> 24;

		x *= M;
		y *= M;

		outX *= M;
		outY *= M;

		outX ^= x;
		outY ^= x;

		outX *= M;
		outY *= M;

		outX ^= y;
		outY ^= y;

		outX ^= outX >>> 13;
		outY ^= outY >>> 13;

		outX *= M;
		outY *= M;

		outX ^= outX >>> 15;
		outY ^= outY >>> 15;

		out.x = Float.intBitsToFloat(outX & 0x007FFFFF | 0x3F800000) - 1.0f;
		out.y = Float.intBitsToFloat(outY & 0x007FFFFF | 0x3F800000) - 1.0f;
		return out;
	}

	public static Vector3 murmurHash33(float srcX, float srcY, float srcZ) {
		return murmurHash33(srcX, srcY, srcZ, tmpVec3);
	}

	public static Vector3 murmurHash33(float srcX, float srcY, float srcZ, Vector3 out) {
		int x = Float.floatToRawIntBits(srcX);
		int y = Float.floatToRawIntBits(srcY);
		int z = Float.floatToRawIntBits(srcZ);
		int outX = A;
		int outY = B;
		int outZ = C;

		x *= M;
		y *= M;
		z *= M;

		x ^= x >>> 24;
		y ^= y >>> 24;
		z ^= z >>> 24;

		x *= M;
		y *= M;
		z *= M;

		outX *= M;
		outY *= M;
		outZ *= M;

		outX ^= x;
		outY ^= x;
		outZ ^= x;

		outX *= M;
		outY *= M;
		outZ *= M;

		outX ^= y;
		outY ^= y;
		outZ ^= y;

		outX *= M;
		outY *= M;
		outZ *= M;

		outX ^= z;
		outY ^= z;
		outZ ^= z;

		outX ^= outX >>> 13;
		outY ^= outY >>> 13;
		outZ ^= outZ >>> 13;

		outX *= M;
		outY *= M;
		outZ *= M;

		outX ^= outX >>> 15;
		outY ^= outY >>> 15;
		outZ ^= outZ >>> 15;

		out.x = Float.intBitsToFloat(outX & 0x007FFFFF | 0x3F800000) - 1.0f;
		out.y = Float.intBitsToFloat(outY & 0x007FFFFF | 0x3F800000) - 1.0f;
		out.z = Float.intBitsToFloat(outZ & 0x007FFFFF | 0x3F800000) - 1.0f;
		return out;
	}

	public static Vector4 murmurHash43(float srcX, float srcY, float srcZ, Vector4 out) {
		int x = Float.floatToRawIntBits(srcX);
		int y = Float.floatToRawIntBits(srcY);
		int z = Float.floatToRawIntBits(srcZ);
		int outX = A;
		int outY = B;
		int outZ = C;
		int outW = D;

		x *= M;
		y *= M;
		z *= M;

		x ^= x >>> 24;
		y ^= y >>> 24;
		z ^= z >>> 24;

		x *= M;
		y *= M;
		z *= M;

		outX *= M;
		outY *= M;
		outZ *= M;
		outW *= M;

		outX ^= x;
		outY ^= x;
		outZ ^= x;
		outW ^= x;

		outX *= M;
		outY *= M;
		outZ *= M;
		outW *= M;

		outX ^= y;
		outY ^= y;
		outZ ^= y;
		outW ^= y;

		outX *= M;
		outY *= M;
		outZ *= M;
		outW *= M;

		outX ^= z;
		outY ^= z;
		outZ ^= z;
		outW ^= z;

		outX ^= outX >>> 13;
		outY ^= outY >>> 13;
		outZ ^= outZ >>> 13;
		outW ^= outW >>> 13;

		outX *= M;
		outY *= M;
		outZ *= M;
		outW *= M;

		outX ^= outX >>> 15;
		outY ^= outY >>> 15;
		outZ ^= outZ >>> 15;
		outW ^= outW >>> 15;

		out.x = Float.intBitsToFloat(outX & 0x007FFFFF | 0x3F800000) - 1.0f;
		out.y = Float.intBitsToFloat(outY & 0x007FFFFF | 0x3F800000) - 1.0f;
		out.z = Float.intBitsToFloat(outZ & 0x007FFFFF | 0x3F800000) - 1.0f;
		out.w = Float.intBitsToFloat(outW & 0x007FFFFF | 0x3F800000) - 1.0f;
		return out;
	}
}
