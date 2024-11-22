package com.winteralexander.gdx.utils.math.direction;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.winteralexander.gdx.utils.EnumConstantCache;

/**
 * One of 4 finite directions on a grid
 * <p>
 * Created on 2024-11-21.
 *
 * @author Alexander Winter
 */
public enum GridDirection4 {
	UP(0f, 1f),
	LEFT(-1f, 0f),
	RIGHT(1f, 0f),
	DOWN(0f, -1f);

	public static final GridDirection4[] values = EnumConstantCache.store(values());

	private final Vector2 direction;

	GridDirection4(float x, float y) {
		this.direction = new Vector2(x, y);
	}

	public GridDirection4 opposite() {
		return values[values.length - ordinal() - 1];
	}

	public GridDirection4 clockwiseOrthogonal() {
		switch(this) {
			case RIGHT: return DOWN;
			case UP: return RIGHT;
			case DOWN: return LEFT;
			case LEFT: return UP;
			default: throw new IllegalStateException();
		}
	}

	public GridDirection4 counterClockwiseOrthogonal() {
		switch(this) {
			case RIGHT: return UP;
			case UP: return LEFT;
			case DOWN: return RIGHT;
			case LEFT: return DOWN;
			default: throw new IllegalStateException();
		}
	}

	public float x() {
		return asVector().x;
	}

	public float y() {
		return asVector().y;
	}

	public Vector2 asVector() {
		return direction;
	}

	public float getSidePosition(Rectangle rectangle) {
		switch(this) {
			case UP:
				return rectangle.y + rectangle.height;

			case DOWN:
				return rectangle.y;

			case LEFT:
				return rectangle.x;

			case RIGHT:
				return rectangle.x + rectangle.width;

			default:
				throw new IllegalStateException("Invalid navigation direction");
		}
	}
}
