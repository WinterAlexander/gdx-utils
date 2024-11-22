package com.winteralexander.gdx.utils.math.direction;

import com.badlogic.gdx.math.Vector2;
import com.winteralexander.gdx.utils.EnumConstantCache;

/**
 * One of 4 corners of an element in a quad grid
 * <p>
 * Created on 2024-11-21.
 *
 * @author Alexander Winter
 */
public enum GridCorner {
	UP_LEFT(-1f, 1f),
	UP_RIGHT(1f, 1f),
	DOWN_LEFT(-1f, -1f),
	DOWN_RIGHT(1f, -1f);

	public static final GridCorner[] values = EnumConstantCache.store(values());

	private final Vector2 direction, normal;

	GridCorner(float x, float y) {
		this.direction = new Vector2(x, y);
		this.normal = direction.cpy().nor();
	}

	public int x() {
		return (int)asVector().x;
	}

	public int y() {
		return (int)asVector().y;
	}

	public float normalX() {
		return asNormal().x;
	}

	public float normalY() {
		return asNormal().y;
	}

	public Vector2 asVector() {
		return direction;
	}

	public Vector2 asNormal() {
		return normal;
	}

	public GridDirection8 asGridDirection8() {
		switch(this) {
			case UP_LEFT: return GridDirection8.UP_LEFT;
			case UP_RIGHT: return GridDirection8.UP_RIGHT;
			case DOWN_LEFT: return GridDirection8.DOWN_LEFT;
			case DOWN_RIGHT: return GridDirection8.DOWN_RIGHT;
			default: throw new IllegalStateException();
		}
	}

	public GridCorner opposite() {
		return values[values.length - ordinal() - 1];
	}

	public GridCorner clockwiseOrthogonal() {
		switch(this) {
			case DOWN_LEFT: return UP_LEFT;
			case UP_LEFT: return UP_RIGHT;
			case DOWN_RIGHT: return DOWN_LEFT;
			case UP_RIGHT: return DOWN_RIGHT;
			default: throw new IllegalStateException();
		}
	}

	public GridCorner counterClockwiseOrthogonal() {
		switch(this) {
			case UP_LEFT: return DOWN_LEFT;
			case UP_RIGHT: return UP_LEFT;
			case DOWN_LEFT: return DOWN_RIGHT;
			case DOWN_RIGHT: return UP_RIGHT;
			default: throw new IllegalStateException();
		}
	}
}
