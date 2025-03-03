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
	public static void bresenhamLine2D(float startX, float startY,
	                                   float endX, float endY,
	                                   Array<Vector2i> tilesOut) {
		bresenhamLine2D(startX, startY, endX, endY, vector2i -> tilesOut.add(vector2i.cpy()));
	}

	public static void bresenhamLine2D(float startX, float startY,
	                                   float endX, float endY,
	                                   Consumer<Vector2i> tileConsumer) {
		bresenhamLine2D(startX, startY, endX, endY, tileConsumer, new Vector2i());
	}

	public static void bresenhamLine2D(float startX, float startY,
	                                   float endX, float endY,
	                                   Consumer<Vector2i> tileConsumer,
	                                   Vector2i tmpTile) {
		int x0 = MathUtils.floor(startX);
		int y0 = MathUtils.floor(startY);

		int x1 = MathUtils.floor(endX);
		int y1 = MathUtils.floor(endY);

		int dx = abs(x1 - x0);
		int dy = -abs(y1 - y0);
		int error = dx + dy;
		int sx = x0 < x1 ? 1 : -1;
		int sy = y0 < y1 ? 1 : -1;

		while(true) {
			tileConsumer.accept(tmpTile.set(x0, y0));

			if(x0 == x1 && y0 == y1)
				break;

			int e2 = error * 2;
			if(e2 >= dy) {
				error += dy;
				x0 += sx;
			}

			if(e2 <= dx) {
				error += dx;
				y0 += sy;
			}
		}
	}

	public static void bresenhamLine2D(Vector2 start,
	                                   Vector2 end,
	                                   Array<Vector2i> tilesOut) {
		bresenhamLine2D(start.x, start.y, end.x, end.y, tilesOut);
	}

	public static void bresenhamLine2D(Vector2 start,
	                                   Vector2 end,
	                                   Consumer<Vector2i> tileConsumer) {
		bresenhamLine2D(start.x, start.y, end.x, end.y, tileConsumer, new Vector2i());
	}

	public static void bresenhamLine2D(Vector2 start,
	                                   Vector2 end,
	                                   Consumer<Vector2i> tileConsumer,
	                                   Vector2i tmpTile) {
		bresenhamLine2D(start.x, start.y, end.x, end.y, tileConsumer, tmpTile);
	}

	public static void allIntersectingLine2D(float startX, float startY,
	                                         float endX, float endY,
	                                         Array<Vector2i> tilesOut) {
		allIntersectingLine2D(startX, startY, endX, endY, vector2i -> tilesOut.add(vector2i.cpy()));
	}

	public static void allIntersectingLine2D(float startX, float startY,
	                                         float endX, float endY,
	                                         Consumer<Vector2i> tileConsumer) {
		allIntersectingLine2D(startX, startY, endX, endY, tileConsumer, new Vector2i());
	}

	public static void allIntersectingLine2D(float startX, float startY,
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

	public static void allIntersectingLine2D(Vector2 start,
	                                         Vector2 end,
	                                         Array<Vector2i> tilesOut) {
		allIntersectingLine2D(start.x, start.y, end.x, end.y, tilesOut);
	}

	public static void allIntersectingLine2D(Vector2 start,
	                                         Vector2 end,
	                                         Consumer<Vector2i> tileConsumer) {
		allIntersectingLine2D(start.x, start.y, end.x, end.y, tileConsumer, new Vector2i());
	}

	public static void allIntersectingLine2D(Vector2 start,
	                                         Vector2 end,
	                                         Consumer<Vector2i> tileConsumer,
	                                         Vector2i tmpTile) {
		allIntersectingLine2D(start.x, start.y, end.x, end.y, tileConsumer, tmpTile);
	}
}
