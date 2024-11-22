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
	UP_LEFT, UP, UP_RIGHT,
	LEFT, RIGHT,
	DOWN_LEFT, DOWN, DOWN_RIGHT;

	public static final GridDirection8[] values = EnumConstantCache.store(values());

	public GridDirection8 opposite() {
		return values[values.length - ordinal() - 1];
	}

	public GridDirection8 clockwiseOrthogonal() {
		switch(this) {
			case UP_LEFT: return UP;
			case UP: return UP_RIGHT;
			case UP_RIGHT: return RIGHT;
			case LEFT: return UP_LEFT;
			case RIGHT: return DOWN_RIGHT;
			case DOWN_LEFT: return LEFT;
			case DOWN: return DOWN_LEFT;
			case DOWN_RIGHT: return DOWN;
			default: throw new IllegalStateException();
		}
	}

	public GridDirection8 counterClockwiseOrthogonal() {
		switch(this) {
			case UP_LEFT: return LEFT;
			case UP: return UP_LEFT;
			case UP_RIGHT: return UP;
			case LEFT: return DOWN_LEFT;
			case RIGHT: return UP_RIGHT;
			case DOWN_LEFT: return DOWN;
			case DOWN: return DOWN_RIGHT;
			case DOWN_RIGHT: return RIGHT;
			default: throw new IllegalStateException();
		}
	}
}
