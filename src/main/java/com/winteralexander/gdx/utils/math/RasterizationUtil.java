package com.winteralexander.gdx.utils.math;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.winteralexander.gdx.utils.math.vector.Vector2i;

import java.util.function.Consumer;

import static java.lang.Math.abs;

/**
 * Utility class to perform rasterization
 * <p>
 * Created on 2025-03-01.
 *
 * @author Alexander Winter
 */
public class RasterizationUtil {

	/**
	 * Bresenham algorithm for 2D line rasterization. Works entirely using integer arithmetic.
	 * Given a line start and end, returns tiles that are part of the rasterization of the line.
	 * Not all tiles intersecting the line are necessary part of the rasterization, as Bresenham
	 * only returns a subset. See
	 * {@link #completeLine2D(float, float, float, float, Consumer, Vector2i)} for an alternative
	 * algorithm.
	 *
	 * @param startX start x tile of the line
	 * @param startY start y tile of the line
	 * @param endX end x tile of the line
	 * @param endY end y tile of the line
	 * @param tileConsumer consumer receiving the tiles processed by the algorithm
	 * @param tmpTile temporary object to avoid allocation
	 */
	public static void bresenhamLine2D(int startX, int startY,
	                                   int endX, int endY,
	                                   Consumer<Vector2i> tileConsumer,
	                                   Vector2i tmpTile) {

		int dx = abs(endX - startX);
		int dy = -abs(endY - startY);
		int error = dx + dy;
		int sx = startX < endX ? 1 : -1;
		int sy = startY < endY ? 1 : -1;

		while(true) {
			tileConsumer.accept(tmpTile.set(startX, startY));

			if(startX == endX && startY == endY)
				break;

			int e2 = error * 2;
			if(e2 >= dy) {
				error += dy;
				startX += sx;
			}

			if(e2 <= dx) {
				error += dx;
				startY += sy;
			}
		}
	}

	/**
	 * Overload allowing to specify an array in which tiles are collected
	 *
	 * @see #bresenhamLine2D(int, int, int, int, Consumer, Vector2i)
	 */
	public static void bresenhamLine2D(int startX, int startY,
	                                   int endX, int endY,
	                                   Array<Vector2i> tilesOut) {
		bresenhamLine2D(startX, startY, endX, endY, vector2i -> tilesOut.add(vector2i.cpy()));
	}


	/**
	 * @see #bresenhamLine2D(int, int, int, int, Consumer, Vector2i)
	 */
	public static void bresenhamLine2D(int startX, int startY,
	                                   int endX, int endY,
	                                   Consumer<Vector2i> tileConsumer) {
		bresenhamLine2D(startX, startY, endX, endY, tileConsumer, new Vector2i());
	}

	/**
	 * @see #bresenhamLine2D(int, int, int, int, Consumer, Vector2i)
	 */
	public static void bresenhamLine2D(Vector2i start,
	                                   Vector2i end,
	                                   Array<Vector2i> tilesOut) {
		bresenhamLine2D(start.x, start.y, end.x, end.y, tilesOut);
	}

	/**
	 * @see #bresenhamLine2D(int, int, int, int, Consumer, Vector2i)
	 */
	public static void bresenhamLine2D(Vector2i start,
	                                   Vector2i end,
	                                   Consumer<Vector2i> tileConsumer) {
		bresenhamLine2D(start.x, start.y, end.x, end.y, tileConsumer, new Vector2i());
	}

	/**
	 * @see #bresenhamLine2D(int, int, int, int, Consumer, Vector2i)
	 */
	public static void bresenhamLine2D(Vector2i start,
	                                   Vector2i end,
	                                   Consumer<Vector2i> tileConsumer,
	                                   Vector2i tmpTile) {
		bresenhamLine2D(start.x, start.y, end.x, end.y, tileConsumer, tmpTile);
	}

	/**
	 * Custom algorithm for 2D line rasterization that returns all tiles that intersect with the
	 * line. Results may not look great for rendering, but are great for drawing pixels on a grid
	 * with a brush. Algorithm is inspired from bresenham and operates in O(n) where n is the number
	 * of output tiles. This algorithm works with real inputs instead of integers and the line
	 * will be considered from it's real coordinates when checking for intersection. If the
	 * resolution need to be different, the input may be scaled.
	 *
	 * @param startX x coordinate of the start of the line
	 * @param startY y coordinate of the start of the line
	 * @param endX x coordinate of the end of the line
	 * @param endY y coordinate of the end of the line
	 * @param tileConsumer consumer for tiles as they are processed by the algorithm
	 * @param tmpTile temporary object to avoid allocation
	 */
	public static void completeLine2D(float startX, float startY,
	                                  float endX, float endY,
	                                  Consumer<Vector2i> tileConsumer,
	                                  Vector2i tmpTile) {
		int x = MathUtils.floor(startX);
		int y = MathUtils.floor(startY);

		int x1 = MathUtils.floor(endX);
		int y1 = MathUtils.floor(endY);

		int sx = startX < endX ? 1 : -1;
		int sy = startY < endY ? 1 : -1;

		int dx = sx == 1 ? 1 : 0;
		int dy = sy == 1 ? 1 : 0;

		float m = (endY - startY) / (endX - startX);

		while(true) {
			tileConsumer.accept(tmpTile.set(x, y));

			if(x == x1 && y == y1)
				break;

			float intersectY = m * (x + dx - startX) + startY;

			if(intersectY * sy < (y + dy) * sy) {
				x += sx;
			} else if(intersectY * sy > (y + dy) * sy) {
				y += sy;
			} else {
				x += sx;
				y += sy;
			}
		}
	}

	/**
	 * @see #completeLine2D(float, float, float, float, Consumer, Vector2i)
	 */
	public static void completeLine2D(float startX, float startY,
	                                  float endX, float endY,
	                                  Array<Vector2i> tilesOut) {
		completeLine2D(startX, startY, endX, endY, vector2i -> tilesOut.add(vector2i.cpy()));
	}

	/**
	 * @see #completeLine2D(float, float, float, float, Consumer, Vector2i)
	 */
	public static void completeLine2D(float startX, float startY,
	                                  float endX, float endY,
	                                  Consumer<Vector2i> tileConsumer) {
		completeLine2D(startX, startY, endX, endY, tileConsumer, new Vector2i());
	}

	/**
	 * @see #completeLine2D(float, float, float, float, Consumer, Vector2i)
	 */
	public static void completeLine2D(Vector2 start,
	                                  Vector2 end,
	                                  Array<Vector2i> tilesOut) {
		completeLine2D(start.x, start.y, end.x, end.y, tilesOut);
	}

	/**
	 * @see #completeLine2D(float, float, float, float, Consumer, Vector2i)
	 */
	public static void completeLine2D(Vector2 start,
	                                  Vector2 end,
	                                  Consumer<Vector2i> tileConsumer) {
		completeLine2D(start.x, start.y, end.x, end.y, tileConsumer, new Vector2i());
	}

	/**
	 * @see #completeLine2D(float, float, float, float, Consumer, Vector2i)
	 */
	public static void completeLine2D(Vector2 start,
	                                  Vector2 end,
	                                  Consumer<Vector2i> tileConsumer,
	                                  Vector2i tmpTile) {
		completeLine2D(start.x, start.y, end.x, end.y, tileConsumer, tmpTile);
	}
}
