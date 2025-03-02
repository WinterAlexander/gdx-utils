package com.winteralexander.gdx.utils.math;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.winteralexander.gdx.utils.math.vector.Vector2i;

import java.util.function.Consumer;

import static com.winteralexander.gdx.utils.math.RasterizationUtil.LineRasterizationMode.ALL_INTERSECTING;
import static java.lang.Math.abs;

/**
 * Utility class to perform rasterization
 * <p>
 * Created on 2025-03-01.
 *
 * @author Alexander Winter
 */
public class RasterizationUtil {
	public static void rasterizeLine2D(float startX, float startY,
	                                   float endX, float endY,
	                                   LineRasterizationMode mode,
	                                   Array<Vector2i> tilesOut) {
		rasterizeLine2D(startX, startY, endX, endY, mode, vector2i -> tilesOut.add(vector2i.cpy()));
	}

	public static void rasterizeLine2D(float startX, float startY,
	                                   float endX, float endY,
	                                   LineRasterizationMode mode,
	                                   Consumer<Vector2i> tileConsumer) {
		rasterizeLine2D(startX, startY, endX, endY, mode, tileConsumer, new Vector2i());
	}

	public static void rasterizeLine2D(float startX, float startY,
	                                   float endX, float endY,
	                                   LineRasterizationMode mode,
	                                   Consumer<Vector2i> tileConsumer,
	                                   Vector2i tmpTile) {
		float dx = abs(endX - startX);
		float dy = -abs(endY - startY);
		float error = dx + dy;

		int x0 = MathUtils.floor(startX);
		int y0 = MathUtils.floor(startY);

		int x1 = MathUtils.floor(endX);
		int y1 = MathUtils.floor(endY);

		int sx = x0 < x1 ? 1 : -1;
		int sy = y0 < y1 ? 1 : -1;

		while(true) {
			tileConsumer.accept(tmpTile.set(x0, y0));
			if(error * 2f >= dy && (mode != ALL_INTERSECTING || error * 2f - dy > dx - error * 2f)) {
				if(x0 == x1)
					break;
				error += dy;
				x0 += sx;
				if(mode == ALL_INTERSECTING)
					continue;
			}
			if(error * 2f <= dx) {
				if(y0 == y1)
					break;
				error += dx;
				y0 += sy;
			}
		}
	}

	public static void rasterizeLine2D(Vector2 start,
	                                   Vector2 end,
	                                   LineRasterizationMode mode,
	                                   Array<Vector2i> tilesOut) {
		rasterizeLine2D(start.x, start.y, end.x, end.y, mode, tilesOut);
	}

	public static void rasterizeLine2D(Vector2 start,
	                                   Vector2 end,
	                                   LineRasterizationMode mode,
	                                   Consumer<Vector2i> tileConsumer) {
		rasterizeLine2D(start.x, start.y, end.x, end.y, mode, tileConsumer, new Vector2i());
	}

	public static void rasterizeLine2D(Vector2 start,
	                                   Vector2 end,
	                                   LineRasterizationMode mode,
	                                   Consumer<Vector2i> tileConsumer,
	                                   Vector2i tmpTile) {
		rasterizeLine2D(start.x, start.y, end.x, end.y, mode, tileConsumer, tmpTile);
	}

	public enum LineRasterizationMode {
		SIMPLE,
		ALL_INTERSECTING
	}
}
