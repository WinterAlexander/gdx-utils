package com.winteralexander.gdx.utils.math.quadtree;

import com.badlogic.gdx.math.Rectangle;

/**
 * {@link Bounded} wrapper for usage in a {@link QuadTree}
 * <p>
 * Created on 2021-10-08.
 *
 * @author Alexander Winter
 */
public class QuadTreeBound<T> implements Bounded {
	public T object = null;
	public final Rectangle bound = new Rectangle();

	public QuadTreeBound() {}

	@Override
	public void getBounds(Rectangle out) {
		out.set(bound);
	}
}
