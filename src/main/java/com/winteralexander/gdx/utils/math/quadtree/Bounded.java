package com.winteralexander.gdx.utils.math.quadtree;

import com.badlogic.gdx.math.Rectangle;

/**
 * Something that is bounded by a {@link Rectangle}
 * <p>
 * Created on 2019-09-16.
 *
 * @author Alexander Winter
 */
public interface Bounded {
	void getBounds(Rectangle out);
}
