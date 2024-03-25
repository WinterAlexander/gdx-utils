package com.winteralexander.gdx.utils.math.quadtree;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.winteralexander.gdx.utils.math.shape.Shape;

import java.util.function.BiConsumer;

import static com.winteralexander.gdx.utils.Validation.ensureNotNull;

/**
 * {@link QuadTree} wrapper for usage with elements that are not {@link Bounded}
 * <p>
 * Created on 2021-10-14.
 *
 * @author Alexander Winter
 */
public class QuadTreeWrapper<T> {
	private final QuadTree<QuadTreeBound<T>> quadTree = new QuadTree<>(10, 5);

	private final Pool<QuadTreeBound<T>> boundPool = new Pool<QuadTreeBound<T>>() {
		@Override
		protected QuadTreeBound<T> newObject() {
			return new QuadTreeBound<>();
		}
	};

	private final Iterable<T> objects;
	private final BiConsumer<T, Rectangle> boundingRectProvider;
	private final Array<QuadTreeBound<T>> usedBounds = new Array<>();

	private final Array<QuadTreeBound<T>> tmpBounds = new Array<>();
	private final Array<T> tmpOut = new Array<>();

	private final Rectangle tmpRectangle = new Rectangle();

	public QuadTreeWrapper(Iterable<T> objects, BiConsumer<T, Rectangle> boundingRectProvider) {
		ensureNotNull(objects, "objects");
		ensureNotNull(boundingRectProvider, "boundingRectProvider");
		this.objects = objects;
		this.boundingRectProvider = boundingRectProvider;
	}

	public void init(float x, float y, float width, float height) {
		quadTree.init(x, y, width, height);

		for(T object : objects) {
			boundingRectProvider.accept(object, tmpRectangle);

			if(Float.isNaN(tmpRectangle.width) || Float.isNaN(tmpRectangle.height))
				continue;

			QuadTreeBound<T> bound = boundPool.obtain();
			usedBounds.add(bound);

			bound.object = object;
			bound.bound.set(tmpRectangle);

			quadTree.insert(bound);
		}
	}

	public void clear() {
		boundPool.freeAll(usedBounds);
		usedBounds.clear();
		tmpBounds.clear();
		tmpOut.clear();
		quadTree.clear();
	}

	public Array<T> retrieve(float x, float y, float width, float height) {
		return retrieve(tmpRectangle.set(x, y, width, height));
	}

	public Array<T> retrieve(com.badlogic.gdx.math.Rectangle bounds) {
		if(bounds == null) {
			tmpOut.clear();
			return tmpOut;
		}

		tmpOut.clear();
		tmpBounds.clear();
		quadTree.retrieve(tmpBounds, bounds);

		for(QuadTreeBound<T> bound : tmpBounds)
			tmpOut.add(bound.object);
		tmpBounds.clear();

		return tmpOut;
	}

	public Array<T> retrieve(Shape bounds) {
		if(bounds == null) {
			tmpOut.clear();
			return tmpOut;
		}

		return retrieve(bounds.getBoundingRectangle().asGdxRect());
	}
}
