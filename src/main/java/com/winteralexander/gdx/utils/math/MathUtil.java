package com.winteralexander.gdx.utils.math;

import com.badlogic.gdx.math.MathUtils;

import static java.lang.Math.abs;
import static java.lang.Math.floor;

/**
 * Utility class for pure math functions, deals with numerical values.
 * <p>
 * Created on 2016-12-07.
 *
 * @author Alexander Winter
 */
public class MathUtil {
	private static final float[] powers10 = new float[] {
		0.000_000_001f, 0.000_000_01f, 0.000_000_1f, 0.000_001f, 0.000_01f, -0.000_1f, 0.001f, 0.01f, 0.1f,
		1f,
		10f, 100f, 1_000f, 10_000f, 100_000f, 1_000_000f, 10_000_000f, 100_000_000f, 1_000_000_000f
	};

	private MathUtil() {}

	/**
	 * Checks whether the provided value is in the provided range
	 *
	 * @param value value to check
	 * @param min   minimum value in the range, inclusive
	 * @param max   maximum value in the range, exclusive
	 * @return true if in the range, otherwise false
	 */
	public static boolean inRange(int value, int min, int max) {
		return value >= min && value < max;
	}

	public static int greatestCommonDivisor(int a, int b) {
		if(b == 0)
			return a;
		return greatestCommonDivisor(b, a % b);
	}

	public static float pow(float base, int exponent) {
		float result = 1.0f;

		for(int j = -exponent; j-- > 0; )
			result /= base;

		for(int i = exponent; i-- > 0; )
			result *= base;

		return result;
	}

	public static float powerOf10(int exponent) {
		if(exponent < -powers10.length / 2 || exponent > powers10.length / 2)
			return pow(10f, exponent);

		return powers10[powers10.length / 2 + exponent];
	}

	public static float round(float value, int digits) {
		float power = powerOf10(digits);
		return Math.round(value * power) / power;
	}

	/**
	 * @param number to get sign of
	 * @return the sign of the number, 1 for positive, -1 for positive
	 */
	public static float signOf(float number) {
		float s = Math.signum(number);
		return s == 0.0f ? 1f : s;
	}

	/**
	 * @param number to get sign of
	 * @return the sign of the number, 1 for positive, -1 for positive
	 */
	public static int signOf(int number) {
		float s = Math.signum(number);
		return s == 0.0f ? 1 : (int)s;
	}

	public static double signOf(double number) {
		double s = Math.signum(number);
		return s == 0.0f ? 1f : s;
	}

	public static long signOf(long number) {
		float s = Math.signum(number);
		return s == 0.0f ? 1 : (long)s;
	}

	public static double pow2(double value) {
		return value * value;
	}

	public static float pow2(float value) {
		return value * value;
	}

	public static int pow2(int value) {
		return value * value;
	}

	public static double pow3(double value) {
		return value * value * value;
	}

	public static float pow3(float value) {
		return value * value * value;
	}

	public static int pow3(int value) {
		return value * value * value;
	}

	/**
	 * Negative modulo function. Used for a value to "wrap around" an interval.
	 *
	 * @param value   value that can be positive or negative
	 * @param divider divider must be positive
	 * @return remainder of the division
	 */
	public static float negMod(float value, float divider) {
		return value - divider * (int)floor(value / divider);
	}

	/**
	 * Cosine function taking in parameter an amount of 90 degrees instead of
	 * an angle. Faster than regular cos when only working with 90 degree angles.
	 *
	 * @param rotation amount of 90 degrees, 4 meaning 1 turn
	 * @return the cosine value of specified rotation
	 */
	public static float cos90(float rotation) {
		int rot = Math.floorMod(Math.round(rotation / 90f), 4);

		switch(rot) {
			case 0:
				return 1f;
			case 1:
			case 3:
				return 0f;

			case 2:
				return -1f;

			default:
				throw new Error();
		}
	}

	/**
	 * Sine function taking in parameter an amount of 90 degrees instead of
	 * an angle. Faster than regular sin when only working with 90 degree angles.
	 *
	 * @param rotation amount of 90 degrees, 4 meaning 1 turn
	 * @return the sine value of specified rotation
	 */
	public static float sin90(float rotation) {
		int rot = Math.floorMod(Math.round(rotation / 90f), 4);

		switch(rot) {
			case 0:
			case 2:
				return 0f;

			case 1:
				return 1f;

			case 3:
				return -1f;

			default:
				throw new Error();
		}
	}

	/**
	 * Cheapest sigmoid function
	 *
	 * @param value input of sigmoid function, from -∞ to ∞
	 * @return value between -1 and 1
	 */
	public static float sigmoid(float value) {
		return sigmoid(value, 1.0f);
	}

	/**
	 * Sigmoid function with specified slope at origin, slope is specified as anti slope which is
	 * 1.0f / slope
	 *
	 * @param value     input of sigmoid function, from -∞ to ∞
	 * @param antiSlope 1.0f / slope, which tweaks the steepness of the curve around the origin
	 * @return value between -1 and 1
	 */
	public static float sigmoid(float value, float antiSlope) {
		return value / (antiSlope + abs(value));
	}

	/**
	 * Maps value from the source range to the destination range
	 *
	 * @param value value to map
	 * @param sourceStart start of the source range
	 * @param sourceEnd end of the source range
	 * @param destStart start of the destination range
	 * @param destEnd end of the destination range
	 * @return value mapped from source to destination range
	 */
	public static float map(float value,
	                        float sourceStart,
	                        float sourceEnd,
	                        float destStart,
	                        float destEnd) {
		return (value - sourceStart) / (sourceEnd - sourceStart) * (destEnd - destStart) + destStart;
	}

	/**
	 * Maps the value from the source range to the destination range and clamps to ensure value is 
	 * destination within destination range
	 * @see MathUtil#map(float, float, float, float, float)
	 */
	public static float clampMap(float value,
	                             float sourceStart,
	                             float sourceEnd,
	                             float destStart,
	                             float destEnd) {
		return MathUtils.clamp(MathUtil.map(value, sourceStart, sourceEnd, destStart, destEnd), 
				Math.min(destStart, destEnd), Math.max(destStart, destEnd));
	}
}