package com.winteralexander.gdx.utils.test.math;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.winteralexander.gdx.utils.math.RasterizationUtil;
import com.winteralexander.gdx.utils.math.shape2d.ShapeUtil;
import com.winteralexander.gdx.utils.math.vector.Vector2i;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.fail;

/**
 * Unit tests for {@link RasterizationUtil}
 * <p>
 * Created on 2025-03-02.
 *
 * @author Alexander Winter
 */
public class RasterizationUtilTest {
	@Test
	public void testLineIntersectionEquivToBruteforce() {
		Random random = new Random();
		Array<Vector2i> actual = new Array<>();
		Array<Vector2i> expected = new Array<>();

		for(int i = 0; i < 10_000; i++) {
			float startX = random.nextFloat() * 100f - 50f;
			float startY = random.nextFloat() * 100f - 50f;
			float endX = startX + random.nextFloat() * 100f - 50f;
			float endY = startY + random.nextFloat() * 100f - 50f;

			RasterizationUtil.allIntersectingLine2D(startX, startY, endX, endY, actual);
			allIntersectingLine2DBruteforce(startX, startY, endX, endY, expected);
			assertSameTiles(expected, actual);
			actual.clear();
			expected.clear();
		}
	}

	private void assertSameTiles(Array<Vector2i> vec2iA, Array<Vector2i> vec2iB) {
		//for(Vector2i tile : vec2iA)
		//	if(!vec2iB.contains(tile, false))
		//		fail("Tile " + tile + " not in second array");

		for(Vector2i tile : vec2iB)
			if(!vec2iA.contains(tile, false))
				fail("Tile " + tile + " not in first array");
	}

	private void allIntersectingLine2DBruteforce(float startX, float startY,
	                                             float endX, float endY,
	                                             Array<Vector2i> out) {
		int x0 = MathUtils.floor(startX);
		int y0 = MathUtils.floor(startY);

		int x1 = MathUtils.floor(endX);
		int y1 = MathUtils.floor(endY);

		if(x0 == x1 && y0 == y1) {
			out.add(new Vector2i(x0, y0));
			return;
		}

		int sx = x0 < x1 ? 1 : -1;
		int sy = y0 < y1 ? 1 : -1;
		Vector2 p = new Vector2();

		for(int x = x0; x * sx <= x1 * sx; x += sx) {
			for(int y = y0; y * sy <= y1 * sy; y += sy) {
				boolean i1 = Intersector.intersectSegments(startX, startY,
						endX, endY, x, y, x + 1f, y, p) && p.dst2(x, y) > 0f && p.dst2(x + 1f, y) > 0f;
				boolean i2 = Intersector.intersectSegments(startX, startY,
						endX, endY, x, y, x, y + 1f, p) && p.dst2(x, y) > 0f && p.dst2(x, y + 1f) > 0f;
				boolean i3 = Intersector.intersectSegments(startX, startY,
						endX, endY, x, y + 1f, x + 1f, y + 1f, p) && p.dst2(x, y + 1f) > 0f && p.dst2(x + 1f, y + 1f) > 0f;
				boolean i4 = Intersector.intersectSegments(startX, startY,
						endX, endY, x + 1f, y, x + 1f, y + 1f, p) && p.dst2(x + 1f, y) > 0f && p.dst2(x + 1f, y + 1f) > 0f;

				if(i1 || i2 || i3 || i4) {
					out.add(new Vector2i(x, y));
				}
			}
		}
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
