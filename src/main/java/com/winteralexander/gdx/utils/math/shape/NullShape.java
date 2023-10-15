package com.winteralexander.gdx.utils.math.shape;

import com.badlogic.gdx.math.Vector2;

/**
 * {@link Shape} that never overlaps anything
 * <p>
 * Created on 2018-02-10.
 *
 * @author Alexander Winter
 */
public class NullShape implements Shape {
	public static final NullShape INSTANCE = new NullShape();

	@Override
	public boolean contains(float x, float y) {
		return false;
	}

	@Override
	public boolean crossesSegment(float x, float y, float x2, float y2) {
		return false;
	}

	@Override
	public void projectOnto(Vector2 point) {
		throw new UnsupportedOperationException("Cannot project onto null shape");
	}

	@Override
	public boolean fullyContains(Shape shape) {
		return false;
	}

	@Override
	public boolean overlaps(Shape shape) {
		return false;
	}

	@Override
	public Rectangle getBoundingRectangle() {
		return null;
	}
}
