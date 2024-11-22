package com.winteralexander.gdx.utils.math.direction;

import com.winteralexander.gdx.utils.EnumConstantCache;

/**
 * One of 4 corners of an element in a quad grid
 * <p>
 * Created on 2024-11-21.
 *
 * @author Alexander Winter
 */
public enum GridCorner {
	TOP_LEFT,
	TOP_RIGHT,
	BOTTOM_LEFT,
	BOTTOM_RIGHT;

	public static final GridCorner[] values = EnumConstantCache.store(values());

	public GridCorner opposite() {
		switch(this) {
			case TOP_LEFT: return BOTTOM_RIGHT;
			case TOP_RIGHT: return BOTTOM_LEFT;
			case BOTTOM_LEFT: return TOP_RIGHT;
			case BOTTOM_RIGHT: return TOP_LEFT;
			default: throw new IllegalStateException();
		}
	}

	public GridCorner clockwiseOrthogonal() {
		switch(this) {
			case BOTTOM_LEFT: return TOP_LEFT;
			case TOP_LEFT: return TOP_RIGHT;
			case BOTTOM_RIGHT: return BOTTOM_LEFT;
			case TOP_RIGHT: return BOTTOM_RIGHT;
			default: throw new IllegalStateException();
		}
	}

	public GridCorner counterClockwiseOrthogonal() {
		switch(this) {
			case TOP_LEFT: return BOTTOM_LEFT;
			case TOP_RIGHT: return TOP_LEFT;
			case BOTTOM_LEFT: return BOTTOM_RIGHT;
			case BOTTOM_RIGHT: return TOP_RIGHT;
			default: throw new IllegalStateException();
		}
	}
}
