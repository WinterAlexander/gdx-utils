package com.winteralexander.gdx.utils.math.shape2d;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedMap;
import com.winteralexander.gdx.utils.memory.ArrayPool;

import java.util.Comparator;
import java.util.function.Consumer;

/**
 * Utility for operations on libGDX's {@link com.badlogic.gdx.math.Shape2D} and its subclasses
 * <p>
 * Created on 2026-04-19.
 *
 * @author Alexander Winter
 */
public class GdxShapeUtil {
	private static final Rectangle outRect = new Rectangle();
	private static final OrderedMap<Float, Array<Rectangle>> rows = new OrderedMap<>();
	private static final Comparator<Rectangle> xRectangleComparator = (o1,
			o2) -> Float.compare(o1.x, o2.x);

	private static final ArrayPool<Rectangle> arrayPool = new ArrayPool<>(true, 0);

	private GdxShapeUtil() {}

	/**
	 * Computes the negative space of a provided array of rectangles. This function has been tested
	 * to work for rectangles not exceeding the bound and not overlapping each other. This function
	 * is not thread safe
	 *
	 * @param bound bound for the negative space
	 * @param rectangles array of rectangles to compute the negative space of
	 * @param outputConsumer consumer to get the output rectangles as they are created
	 */
	public static void computeNegativeSpace(Rectangle bound,
			Array<Rectangle> rectangles,
			Consumer<Rectangle> outputConsumer) {
		computeNegativeSpace(bound, rectangles.items, rectangles.size, outputConsumer);
	}

	public static void computeNegativeSpace(Rectangle bound,
			Rectangle[] rectangles,
			Consumer<Rectangle> outputConsumer) {
		computeNegativeSpace(bound, rectangles, rectangles.length, outputConsumer);
	}

	public static void computeNegativeSpace(Rectangle bound,
			Rectangle[] rectangles,
			int rectangleCount,
			Consumer<Rectangle> outputConsumer) {
		rows.put(bound.y, arrayPool.obtain());
		rows.put(bound.y + bound.height, null);

		for(int i = 0; i < rectangleCount; i++) {
			Rectangle rect = rectangles[i];

			if(!rows.containsKey(rect.y) || rows.get(rect.y) == null)
				rows.put(rect.y, arrayPool.obtain());
			rows.get(rect.y).add(rect);

			if(!rows.containsKey(rect.y + rect.height))
				rows.put(rect.y + rect.height, null);
		}

		Array<Float> keys = rows.orderedKeys();

		keys.sort();

		for(int i = 0; i < keys.size - 1; i++) {
			float y = keys.get(i);
			if(rows.get(y) == null)
				continue;

			for(Rectangle rectangle : rows.get(y)) {
				if(rectangle.y + rectangle.height <= y)
					continue;

				for(int j = i + 1; j < keys.size - 1; j++) {
					float otherY = keys.get(j);
					if(rectangle.y + rectangle.height > otherY) {
						if(rows.get(otherY) == null)
							rows.put(otherY, arrayPool.obtain());
						rows.get(otherY).add(rectangle);
					} else
						break;
				}
			}
		}

		for(Array<Rectangle> row : rows.values())
			if(row != null)
				row.sort(xRectangleComparator);

		for(int i = 0; i < keys.size - 1; i++) {
			float bottomY = keys.get(i);
			float topY = keys.get(i + 1);
			outRect.y = bottomY;
			outRect.height = topY - bottomY;
			outRect.x = bound.x;
			Array<Rectangle> rowRectangles = rows.get(bottomY);
			if(rowRectangles != null)
				for(Rectangle rectangle : rowRectangles) {
					outRect.width = rectangle.x - outRect.x;
					if(outRect.width > 0f)
						outputConsumer.accept(outRect);
					outRect.x = Math.max(outRect.x, rectangle.x + rectangle.width);
				}
			outRect.width = bound.x + bound.width - outRect.x;
			if(outRect.width > 0f)
				outputConsumer.accept(outRect);
		}

		for(Array<Rectangle> row : rows.values())
			if(row != null) {
				row.clear();
				arrayPool.free(row);
			}
		rows.clear();
	}
}
