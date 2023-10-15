package com.winteralexander.gdx.utils.math.shape;

import com.badlogic.gdx.math.Vector2;

/**
 * Represents a 2d shape
 * <p>
 * Created on 2016-12-05.
 *
 * @author Alexander Winter
 */
public interface Shape {
	/**
	 * Checks if the specified point is inside this shape
	 *
	 * @param position point to check if inside
	 * @return true if inside, otherwise false
	 */
	default boolean contains(Vector2 position) {
		return contains(position.x, position.y);
	}

	/**
	 * Checks if the specified point is inside this shape
	 *
	 * @param x x coordinate of the point
	 * @param y y coordinate of the point
	 * @return true if inside, otherwise false
	 */
	boolean contains(float x, float y);

	/**
	 * Checks if this shape crosses the specified line segment
	 *
	 * @param x  x coordinate of starting point
	 * @param y  y coordinate of starting point
	 * @param x2 x coordinate of ending point
	 * @param y2 y coordinate of ending point
	 * @return true if this shape crosses this line segment, otherwise false
	 */
	boolean crossesSegment(float x, float y, float x2, float y2);

	/**
	 * Projects the specified point onto this shape, setting it to the closest
	 * point that is in this shape
	 *
	 * @param point vector to project
	 */
	void projectOnto(Vector2 point);

	/**
	 * Checks if this shape and the specified shape are overlapping
	 *
	 * @param shape shape to check if overlap
	 * @return true if overlaps, otherwise false
	 */
	boolean overlaps(Shape shape);

	/**
	 * Checks if the specified shape is fully contained inside this shape
	 *
	 * @param shape shape to check if fully inside
	 * @return true if fully inside, otherwise false
	 */
	boolean fullyContains(Shape shape);

	Rectangle getBoundingRectangle();
}
