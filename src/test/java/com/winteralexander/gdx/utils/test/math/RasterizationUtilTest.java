package com.winteralexander.gdx.utils.test.math;

import com.badlogic.gdx.utils.Array;
import com.winteralexander.gdx.utils.math.RasterizationUtil;
import com.winteralexander.gdx.utils.math.vector.Vector2i;
import org.junit.Test;

import static com.winteralexander.gdx.utils.math.RasterizationUtil.LineRasterizationMode.ALL_INTERSECTING;
import static com.winteralexander.gdx.utils.math.RasterizationUtil.LineRasterizationMode.SIMPLE;

/**
 * Unit tests for {@link RasterizationUtil}
 * <p>
 * Created on 2025-03-02.
 *
 * @author Alexander Winter
 */
public class RasterizationUtilTest {
	@Test
	public void testLineRasterization2D() {
		Array<Vector2i> out = new Array<>();

		RasterizationUtil.rasterizeLine2D(0f, 1f, 6f, 4f, SIMPLE, out);
		printRaster(out);


		RasterizationUtil.rasterizeLine2D(0.111f, 0.3123f, 100.123f, 35.213f, ALL_INTERSECTING, out);
		//System.out.println(ReflectionUtil.toPrettyString(out));
		printRaster(out);

		out.clear();
		RasterizationUtil.rasterizeLine2D(0.5f, 0.5f, 1.5f, 2.5f, ALL_INTERSECTING, out);
		//System.out.println(ReflectionUtil.toPrettyString(out));
		printRaster(out);
	}

	private void printRaster(Array<Vector2i> tiles) {
		int minX = tiles.get(0).x;
		int minY = tiles.get(0).y;
		int maxX = tiles.get(0).x;
		int maxY = tiles.get(0).y;

		for(Vector2i tile : tiles) {
			minX = Math.min(minX, tile.x);
			minY = Math.min(minY, tile.y);
			maxX = Math.max(maxX, tile.x);
			maxY = Math.max(maxY, tile.y);
		}

		for(int j = minY; j <= maxY; j++) {
			for(int i = minX; i <= maxX; i++) {
				if(tiles.contains(new Vector2i(i, j), false))
					System.out.print('X');
				else
					System.out.print('_');
			}
			System.out.print('\n');
		}
	}
}
