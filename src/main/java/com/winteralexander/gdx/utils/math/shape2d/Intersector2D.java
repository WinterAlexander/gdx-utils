package com.winteralexander.gdx.utils.math.shape2d;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.winteralexander.gdx.utils.math.MathUtil;

import static com.winteralexander.gdx.utils.math.MathUtil.pow2;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Utility class for 2D intersection between 2D shapes, not thread safe.
 * <p>
 * Created on 2025-09-16.
 *
 * @author Alexander Winter
 */
public class Intersector2D {
	private static final Vector2 tmpVec2 = new Vector2();

	private Intersector2D() {}

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

	/**
	 * Checks if the provided a 2D segment crosses a provided 2D Axis-Aligned Bounding Box
	 * @param lineX1 x coordinate of the first point of the segment
	 * @param lineY1 y coordinate of the first point of the segment
	 * @param lineX2 x coordinate of the second point of the segment
	 * @param lineY2 y coordinate of the second point of the segment
	 * @param rectX x coordinate of the position of the rectangle (left side)
	 * @param rectY y coordinate of the position of the rectangle (bottom side)
	 * @param width width of the rectangle
	 * @param height height of the rectangle
	 * @return true if the segment crosses the rectangle, otherwise false
	 */
	public static boolean lineCrossesAABB(float lineX1, float lineY1, float lineX2, float lineY2,
	                                      float rectX, float rectY, float width, float height) {
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

	/**
	 * Computes the overlap between two rectangles and return it as a vector containing the overlap
	 * in X and Y
	 * @param rect1 first rectangle to compute overlap
	 * @param rect2 second rectangle to compute overlap
	 * @param outOverlap overlap amount in two dimension
	 */
	public static void computeOverlap(Rectangle rect1, Rectangle rect2, Vector2 outOverlap) {
		outOverlap.x = Math.min(Math.min(Math.max(0f, rect2.x + rect2.width - rect1.x),
						Math.max(0f, rect1.x + rect1.width - rect2.x)),
				Math.min(rect1.width, rect2.width));
		outOverlap.y = Math.min(Math.min(Math.max(0f, rect2.y + rect2.height - rect1.y),
						Math.max(0f, rect1.y + rect1.height - rect2.y)),
				Math.min(rect1.height, rect2.height));
		outOverlap.x *= Math.signum(outOverlap.y);
		outOverlap.y *= Math.signum(outOverlap.x);
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
		float theta = MathUtil.negMod((float)angle + MathUtils.PI, MathUtils.PI2) - MathUtils.PI;

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

	public static float AABBdistanceSquaredToAABB(Rectangle aabb1, Rectangle aabb2) {
		return AABBdistanceSquaredToAABB(aabb1.x, aabb1.y, aabb1.width, aabb1.height,
				aabb2.x, aabb2.y, aabb2.width, aabb2.height);
	}

	public static float AABBdistanceSquaredToAABB(Vector2 pos1, Vector2 size1,
	                                              Vector2 pos2, Vector2 size2) {
		return AABBdistanceSquaredToAABB(pos1.x, pos1.y, size1.x, size1.y,
				pos2.x, pos2.y, size2.x, size2.y);
	}

	public static float AABBdistanceSquaredToAABB(float x1, float y1, float width1, float height1,
	                                              float x2, float y2, float width2, float height2) {
		float points1DstTo2 = Math.min(
				Math.min(distanceSquaredToAABB(x1, y1, x2, y2, width2, height2),
						distanceSquaredToAABB(x1 + width1, y1 + height1, x2, y2, width2, height2)),
				Math.min(distanceSquaredToAABB(x1 + width1, y1, x2, y2, width2, height2),
						distanceSquaredToAABB(x1, y1 + height1, x2, y2, width2, height2)));

		float points2DstTo1 = Math.min(
				Math.min(distanceSquaredToAABB(x2, y2, x1, y1, width1, height1),
						distanceSquaredToAABB(x2 + width2, y2 + height2, x1, y1, width1, height1)),
				Math.min(distanceSquaredToAABB(x2 + width2, y2, x1, y1, width1, height1),
						distanceSquaredToAABB(x2, y2 + height2, x1, y1, width1, height1)));
		return Math.min(points1DstTo2, points2DstTo1);
	}
}
