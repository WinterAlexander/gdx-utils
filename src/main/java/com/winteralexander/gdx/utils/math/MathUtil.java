package com.winteralexander.gdx.utils.math;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import static java.lang.Math.*;

/**
 * Utility class for math functions
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

	private static final Vector2 tmpVec2 = new Vector2();

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

	public static boolean lineIntersectsLine(float l1StartX, float l1StartY,
	                                         float l1EndX, float l1EndY,
	                                         float l2StartX, float l2StartY,
	                                         float l2EndX, float l2EndY) {
		tmpVec2.set(l1StartX, l1StartY).sub(l1EndX, l1EndY).rotate90(1);

		float dot1 = tmpVec2.dot(l1StartX - l2StartX, l1StartY - l2StartY);
		float dot2 = tmpVec2.dot(l1StartX - l2EndX, l1StartY - l2EndY);

		if(dot1 > 0 == dot2 > 0)
			return false;

		tmpVec2.set(l2StartX, l2StartY).sub(l2EndX, l2EndY).rotate90(1);

		dot1 = tmpVec2.dot(l2StartX - l1StartX, l2StartY - l1StartY);
		dot2 = tmpVec2.dot(l2StartX - l1EndX, l2StartY - l1EndY);

		return dot1 > 0 != dot2 > 0;
	}

	/**
	 * Checks if the two provided Oriented Bounding Boxes (OBB) intersect with each other.
	 * @param r1X x position of the center of the first rectangle
	 * @param r1Y y position of the center of the first rectangle
	 * @param r1Width width of the first rectangle
	 * @param r1Height height of the first rectangle
	 * @param r1Angle angle of the first rectangle
	 * @param r2X x position of the center of the second rectangle
	 * @param r2Y y position of the center of the second rectangle
	 * @param r2Width width of the second rectangle
	 * @param r2Height height of the second rectangle
	 * @param r2Angle angle of the second rectangle
	 * @return true if the OBBs intersect, otherwise false
	 */
	public static boolean doOBBIntersectWithOBB(float r1X, float r1Y,
	                                            float r1Width, float r1Height,
	                                            float r1Angle,
	                                            float r2X, float r2Y,
	                                            float r2Width, float r2Height,
	                                            float r2Angle) {
		tmpVec2.set(r1Width, r1Height).scl(-0.5f, -0.5f).rotateDeg(r1Angle).add(r1X, r1Y);
		float r1X1 = tmpVec2.x;
		float r1Y1 = tmpVec2.y;

		tmpVec2.set(r1Width, r1Height).scl(-0.5f, 0.5f).rotateDeg(r1Angle).add(r1X, r1Y);
		float r1X2 = tmpVec2.x;
		float r1Y2 = tmpVec2.y;

		tmpVec2.set(r1Width, r1Height).scl(0.5f, 0.5f).rotateDeg(r1Angle).add(r1X, r1Y);
		float r1X3 = tmpVec2.x;
		float r1Y3 = tmpVec2.y;

		tmpVec2.set(r1Width, r1Height).scl(0.5f, -0.5f).rotateDeg(r1Angle).add(r1X, r1Y);
		float r1X4 = tmpVec2.x;
		float r1Y4 = tmpVec2.y;

		tmpVec2.set(r2Width, r2Height).scl(-0.5f, -0.5f).rotateDeg(r2Angle).add(r2X, r2Y);
		float r2X1 = tmpVec2.x;
		float r2Y1 = tmpVec2.y;

		tmpVec2.set(r2Width, r2Height).scl(-0.5f, 0.5f).rotateDeg(r2Angle).add(r2X, r2Y);
		float r2X2 = tmpVec2.x;
		float r2Y2 = tmpVec2.y;

		tmpVec2.set(r2Width, r2Height).scl(0.5f, 0.5f).rotateDeg(r2Angle).add(r2X, r2Y);
		float r2X3 = tmpVec2.x;
		float r2Y3 = tmpVec2.y;

		tmpVec2.set(r2Width, r2Height).scl(0.5f, -0.5f).rotateDeg(r2Angle).add(r2X, r2Y);
		float r2X4 = tmpVec2.x;
		float r2Y4 = tmpVec2.y;

		if(lineIntersectsLine(r1X1, r1Y1, r1X2, r1Y2, r2X1, r2Y1, r2X2, r2Y2)
				|| lineIntersectsLine(r1X1, r1Y1, r1X2, r1Y2, r2X2, r2Y2, r2X3, r2Y3)
				|| lineIntersectsLine(r1X1, r1Y1, r1X2, r1Y2, r2X3, r2Y3, r2X4, r2Y4)
				|| lineIntersectsLine(r1X1, r1Y1, r1X2, r1Y2, r2X4, r2Y4, r2X1, r2Y1)
				|| lineIntersectsLine(r1X2, r1Y2, r1X3, r1Y3, r2X1, r2Y1, r2X2, r2Y2)
				|| lineIntersectsLine(r1X2, r1Y2, r1X3, r1Y3, r2X2, r2Y2, r2X3, r2Y3)
				|| lineIntersectsLine(r1X2, r1Y2, r1X3, r1Y3, r2X3, r2Y3, r2X4, r2Y4)
				|| lineIntersectsLine(r1X2, r1Y2, r1X3, r1Y3, r2X4, r2Y4, r2X1, r2Y1)
				|| lineIntersectsLine(r1X3, r1Y3, r1X4, r1Y4, r2X1, r2Y1, r2X2, r2Y2)
				|| lineIntersectsLine(r1X3, r1Y3, r1X4, r1Y4, r2X2, r2Y2, r2X3, r2Y3)
				|| lineIntersectsLine(r1X3, r1Y3, r1X4, r1Y4, r2X3, r2Y3, r2X4, r2Y4)
				|| lineIntersectsLine(r1X3, r1Y3, r1X4, r1Y4, r2X4, r2Y4, r2X1, r2Y1)
				|| lineIntersectsLine(r1X4, r1Y4, r1X1, r1Y1, r2X1, r2Y1, r2X2, r2Y2)
				|| lineIntersectsLine(r1X4, r1Y4, r1X1, r1Y1, r2X2, r2Y2, r2X3, r2Y3)
				|| lineIntersectsLine(r1X4, r1Y4, r1X1, r1Y1, r2X3, r2Y3, r2X4, r2Y4)
				|| lineIntersectsLine(r1X4, r1Y4, r1X1, r1Y1, r2X4, r2Y4, r2X1, r2Y1))
			return true;

		return inOBB(r2X, r2Y, r1X, r1Y, r1Width, r1Height, r1Angle)
				|| inOBB(r1X, r1Y, r2X, r2Y, r2Width, r2Height, r2Angle);
	}

	public static boolean inOBB(float x, float y,
	                            float rx, float ry, float width, float height, float angle) {
		tmpVec2.set(x, y);

		tmpVec2.sub(rx, ry).rotateDeg(-angle);

		return tmpVec2.x <= width / 2f
				&& tmpVec2.x >= -width / 2f
				&& tmpVec2.y <= height / 2f
				&& tmpVec2.y >= -height / 2f;
	}

	public static boolean lineCrossesAABB(float lineX1, float lineY1, float lineX2, float lineY2, // line
	                                      float rectX, float rectY, float width, float height) // rect
	{
		float rectX1 = min(rectX, rectX + width);
		float rectX2 = max(rectX, rectX + width);

		float rectY1 = min(rectY, rectY + height);
		float rectY2 = max(rectY, rectY + height);

		float lineMinX = min(lineX1, lineX2);
		float lineMinY = min(lineY1, lineY2);
		float lineMaxX = max(lineX1, lineX2);
		float lineMaxY = max(lineY1, lineY2);

		if(inAABB(lineX1, lineY1, rectX, rectY, width, height)
		|| inAABB(lineX2, lineY2, rectX, rectY, width, height))
			return true;

		if(lineMinX < rectX1 && lineMaxX > rectX1) { // if it crosses the left limit
			float lineYAtCollision = lineY1 + (lineY2 - lineY1) * (rectX1 - lineX1) / (lineX2 - lineX1);

			if(lineYAtCollision > rectY1 && lineYAtCollision < rectY2)
				return true;
		}

		if(lineMinX < rectX2 && lineMaxX > rectX2) { // if it crosses the right limit
			float lineYAtCollision = lineY1 + (lineY2 - lineY1) * (rectX2 - lineX1) / (lineX2 - lineX1);

			if(lineYAtCollision > rectY1 && lineYAtCollision < rectY2)
				return true;
		}

		if(lineMinY < rectY1 && lineMaxY > rectY1) { // if it crosses the bottom limit
			float lineXAtCollision = lineX1 + (lineX2 - lineX1) * (rectY1 - lineY1) / (lineY2 - lineY1);

			if(lineXAtCollision > rectX1 && lineXAtCollision < rectX2)
				return true;
		}

		if(lineMinY < rectY2 && lineMaxY > rectY2) { // if it crosses the top limit
			float lineXAtCollision = lineX1 + (lineX2 - lineX1) * (rectY2 - lineY1) / (lineY2 - lineY1);

			return lineXAtCollision > rectX1 && lineXAtCollision < rectX2;
		}

		return false;
	}

	public static boolean inAABB(float px, float py,
	                             float x, float y,
	                             float size) {
		return inAABB(px, py, x, y, size, size);
	}

	public static boolean inAABB(float px, float py,
	                             float x, float y,
	                             float width, float height) {
		if(px > x + width)
			return false;

		if(px < x)
			return false;

		if(py > y + height)
			return false;

		return py >= y;
	}

	public static boolean inAABB(Vector2 point,
	                             Vector2 startPos,
	                             Vector2 size) {
		return inAABB(point.x, point.y,
				startPos.x, startPos.y,
				size.x, size.y);
	}

	public static boolean inAABB(Vector2 point,
	                             Vector2 startPos,
	                             Vector2 size,
	                             Vector2 origin) {
		return inAABB(point.x, point.y,
				startPos.x + origin.x, startPos.y + origin.y,
				size.x, size.y);
	}

	public static float distanceSquaredToAABB(float px, float py,
	                                          float x, float y,
	                                          float size) {
		return distanceSquaredToAABB(px, py, x, y, size, size);
	}

	public static float distanceSquaredToAABB(float px, float py,
	                                          float x, float y,
	                                          float width, float height) {
		float closestX = px;
		float closestY = py;

		if(px > x + width)
			closestX = x + width;

		if(px < x)
			closestX = x;

		if(py > y + height)
			closestY = y + height;

		if(py < y)
			closestY = y;

		return pow2(px - closestX) + pow2(py - closestY);
	}

	public static float distanceSquaredToAABB(Vector2 point,
	                                          Vector2 startPos,
	                                          Vector2 size) {
		return distanceSquaredToAABB(point.x, point.y,
				startPos.x, startPos.y,
				size.x, size.y);
	}

	public static float distanceSquaredToAABB(Vector2 point,
	                                          Vector2 startPos,
	                                          Vector2 size,
	                                          Vector2 origin) {
		return distanceSquaredToAABB(point.x, point.y,
				startPos.x + origin.x, startPos.y + origin.y,
				size.x, size.y);
	}

	public static boolean doAABBIntersectWithAABB(Vector2 start1, Vector2 size1,
	                                              Vector2 start2, Vector2 size2) {
		return doAABBIntersectWithAABB(start1.x, start1.y,
				size1.x, size1.y,
				start2.x, start2.y,
				size2.x, size2.y);
	}

	/**
	 * Checks if the first provided Axis Aligned Bounding Box (AABB) intersects with the second one.
	 * Edge touching does not count as intersection.
	 * @param x1 x of bottom left point of the first AABB
	 * @param y1 y of bottom left point of the first AABB
	 * @param w1 width of the first AABB
	 * @param h1 height of the first AABB
	 * @param x2 x of bottom left point of the second AABB
	 * @param y2 y of bottom left point of the second AABB
	 * @param w2 width of the second AABB
	 * @param h2 height of the second AABB
	 * @return true if there is intersection, otherwise false
	 */
	public static boolean doAABBIntersectWithAABB(float x1, float y1,
	                                              float w1, float h1,
	                                              float x2, float y2,
	                                              float w2, float h2) {
		if(x1 + w1 <= x2)
			return false;

		if(x1 >= x2 + w2)
			return false;

		if(y1 + h1 <= y2)
			return false;

		return y1 < y2 + h2;
	}

	/**
	 * Checks if box 1 is fully inside box 2
	 *
	 * @param x1 x position of box 1
	 * @param y1 y position of box 1
	 * @param w1 width of box 1
	 * @param h1 height of box 1
	 * @param x2 x position of box 2
	 * @param y2 y position of box 2
	 * @param w2 width of box 2
	 * @param h2 height of box 2
	 * @return true if box 1 is fully inside box 2, otherwise false
	 */
	public static boolean isAABBfullyInAABB(float x1, float y1,
	                                        float w1, float h1,
	                                        float x2, float y2,
	                                        float w2, float h2) {
		return x1 + w1 <= x2 + w2 && x1 >= x2 && y1 + h1 <= y2 + h2 && y1 >= y2;
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

	public static Vector2 push(Vector2 input, Vector2 direction, float scale, float cap) {
		return push(input, direction, scale, cap, input);
	}

	public static Vector2 push(Vector2 input, Vector2 direction, float scale, float cap, Vector2 vel) {
		vel.set(input);

		if(vel.x * direction.x < cap) {
			vel.x += direction.x * scale;

			if(vel.x * direction.x > cap)
				vel.x = cap * direction.x;
		}

		if(vel.y * direction.y < cap) {
			vel.y += direction.y * scale;

			if(vel.y * direction.y > cap)
				vel.y = cap * direction.y;
		}

		return vel;
	}

	public static float decelerate(float velocity, float deceleration, float delta) {
		float dir = signum(velocity);

		velocity -= deceleration * delta * dir;

		if(velocity * dir < 0f) // if velocity switched it's sign by deceleration
			velocity = 0f; // set it to 0, deceleration don't make you switch side

		return velocity;
	}

	public static Vector2 decelerate(Vector2 velocity, float deceleration, float delta) {
		float xDir = signum(velocity.x);
		float yDir = signum(velocity.y);

		velocity.x -= deceleration * delta * xDir;

		if(velocity.x * xDir < 0)
			velocity.x = 0;

		velocity.y -= deceleration * delta * yDir;

		if(velocity.y * yDir < 0)
			velocity.y = 0;

		return velocity;
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

	@SuppressWarnings("SuspiciousNameCombination")
	public static Vector2 rotate90(Vector2 vec, float rotation) {
		rotation = negMod(rotation, 360f);

		if(rotation > -45f && rotation <= 45f)
			return vec;

		if(rotation > 45f && rotation <= 135f)
			return vec.set(-vec.y, vec.x);

		if(rotation > 135f && rotation <= 225f)
			return vec.scl(-1f);

		return vec.set(vec.y, -vec.x);
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
	 * Returns the length of an axis aligned vector
	 *
	 * @param aaVector vector to get the length from
	 * @return length of that axis aligned vector
	 */
	public static float aaLength(Vector2 aaVector) {
		return abs(aaVector.x + aaVector.y);
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
	 * Projects the specified angle to the edge of a rectangle of specified size
	 *
	 * @param angle  angle to project at, in degrees
	 * @param width  width of the rectangle to project on
	 * @param height height of the rectangle to project on
	 * @return displacement from the center of the rectangle to the projection on the edge
	 */
	public static Vector2 projectToRectEdge(double angle, float width, float height, Vector2 out) {
		return projectToRectEdgeRad(Math.toRadians(angle), width, height, out);
	}

	/**
	 * Projects the specified angle to the edge of a rectangle of specified size
	 *
	 * @param angle  angle to project at, in radians
	 * @param width  width of the rectangle to project on
	 * @param height height of the rectangle to project on
	 * @return displacement from the center of the rectangle to the projection on the edge
	 */
	public static Vector2 projectToRectEdgeRad(double angle, float width, float height, Vector2 out) {
		float theta = negMod((float)angle + MathUtils.PI, MathUtils.PI2) - MathUtils.PI;

		float diag = MathUtils.atan2(height, width);
		float tangent = (float)Math.tan(angle);

		if(theta > -diag && theta <= diag) {
			out.x = width / 2f;
			out.y = width / 2f * tangent;
		} else if(theta > diag && theta <= MathUtils.PI - diag) {
			out.x = height / 2f / tangent;
			out.y = height / 2f;
		} else if(theta > MathUtils.PI - diag && theta <= MathUtils.PI + diag
				|| theta < -MathUtils.PI + diag && theta >= -MathUtils.PI - diag) {
			out.x = -width / 2f;
			out.y = -width / 2f * tangent;
		} else {
			out.x = -height / 2f / tangent;
			out.y = -height / 2f;
		}

		return out;
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
