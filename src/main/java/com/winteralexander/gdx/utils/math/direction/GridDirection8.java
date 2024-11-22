package com.winteralexander.gdx.utils.math.direction;

import com.winteralexander.gdx.utils.EnumConstantCache;

/**
 * One of 8 finite directions on a grid
 * <p>
 * Created on 2024-11-21.
 *
 * @author Alexander Winter
 */
public enum GridDirection8 {
	TOP_LEFT, TOP, TOP_RIGHT,
	LEFT, RIGHT,
	BOTTOM_LEFT, BOTTOM, BOTTOM_RIGHT;

	public static final GridDirection8[] values = EnumConstantCache.store(values());

	public GridDirection8 opposite() {
		return values[values.length - ordinal() - 1];
	}

	public GridDirection8 clockwiseOrthogonal() {
		switch(this) {
			case TOP_LEFT: return TOP;
			case TOP: return TOP_RIGHT;
			case TOP_RIGHT: return RIGHT;
			case LEFT: return TOP_LEFT;
			case RIGHT: return BOTTOM_RIGHT;
			case BOTTOM_LEFT: return LEFT;
			case BOTTOM: return BOTTOM_LEFT;
			case BOTTOM_RIGHT: return BOTTOM;
			default: throw new IllegalStateException();
		}
	}

	public GridDirection8 counterClockwiseOrthogonal() {
		switch(this) {
			case TOP_LEFT: return LEFT;
			case TOP: return TOP_LEFT;
			case TOP_RIGHT: return TOP;
			case LEFT: return BOTTOM_LEFT;
			case RIGHT: return TOP_RIGHT;
			case BOTTOM_LEFT: return BOTTOM;
			case BOTTOM: return BOTTOM_RIGHT;
			case BOTTOM_RIGHT: return RIGHT;
			default: throw new IllegalStateException();
		}
	}
}
