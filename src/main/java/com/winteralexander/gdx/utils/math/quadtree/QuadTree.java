package com.winteralexander.gdx.utils.math.quadtree;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.winteralexander.gdx.utils.math.shape2d.Shape;

import static com.winteralexander.gdx.utils.Validation.ensureStrictlyPositive;

/**
 * Data structure allowing the query of rectangles possibly colliding with
 * a specified rectangle in a fast manner. The quadtree pools its nodes.
 * <p>
 * Created on 2019-09-15.
 *
 * @author Alexander Winter
 */
public class QuadTree<T extends Bounded> {
	public static final int DEFAULT_MAX_ENTITIES = 10;
	public static final int DEFAULT_MAX_LEVELS = 10;

	final Rectangle tmpRectangle = new Rectangle();
	final Pool<QuadTreeNode<T>> pool = new Pool<QuadTreeNode<T>>() {
		@Override
		protected QuadTreeNode<T> newObject() {
			return new QuadTreeNode<>(QuadTree.this);
		}
	};

	private final QuadTreeNode<T> root;

	public final int maxEntities, maxLevels;

	public QuadTree() {
		this(DEFAULT_MAX_ENTITIES, DEFAULT_MAX_LEVELS);
	}

	public QuadTree(int maxEntities, int maxLevels) {
		ensureStrictlyPositive(maxEntities, "maxEntities");
		ensureStrictlyPositive(maxLevels, "maxLevels");
		this.maxEntities = maxEntities;
		this.maxLevels = maxLevels;

		root = new QuadTreeNode<>(this);
	}

	public void init(float x, float y, float width, float height) {
		root.init(0, x, y, width, height);
	}

	public void clear() {
		root.clear();
	}

	public void insert(T solid) {
		root.insert(solid);
	}

	public void retrieve(Array<T> out, float x, float y, float width, float height) {
		root.retrieve(out, tmpRectangle.set(x, y, width, height));
	}

	public void retrieve(Array<T> out, Rectangle bounds) {
		root.retrieve(out, bounds);
	}

	public void retrieve(Array<T> out, Shape bounds) {
		retrieve(out, bounds.getBoundingRectangle().asGdxRect());
	}

	public void debugRender(ShapeRenderer shapeRenderer) {
		root.debugRender(shapeRenderer);
	}
}
