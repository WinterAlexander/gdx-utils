package com.winteralexander.gdx.utils.math.direction;

import com.badlogic.gdx.math.Vector2;
import com.winteralexander.gdx.utils.EnumConstantCache;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.ToDoubleFunction;

/**
 * One of 8 finite directions on a grid
 * <p>
 * Created on 2024-11-21.
 *
 * @author Alexander Winter
 */
public enum GridDirection8 {
	UP_LEFT(-1f, 1f), UP(0f, 1f), UP_RIGHT(1f, 1f),
	LEFT(-1f, 0f), RIGHT(1f, 0f),
	DOWN_LEFT(-1f, -1f), DOWN(0f, -1f), DOWN_RIGHT(1f, -1f);

	public static final GridDirection8[] values = EnumConstantCache.store(values());

	private final Vector2 direction, normal;

	GridDirection8(float x, float y) {
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

	public boolean isHorizontal() {
		return this == LEFT || this == RIGHT;
	}

	public boolean isVertical() {
		return this == UP || this == DOWN;
	}

	public boolean isDiagonal() {
		switch(this) {
			case UP_LEFT:
			case UP_RIGHT:
			case DOWN_LEFT:
			case DOWN_RIGHT:
				return true;
		}
		return false;
	}

	public GridDirection4 asGridDirection4() {
		switch(this) {
			case UP: return GridDirection4.UP;
			case LEFT: return GridDirection4.LEFT;
			case RIGHT: return GridDirection4.RIGHT;
			case DOWN: return GridDirection4.DOWN;
			default: throw new IllegalStateException("Not a GridDirection4: " + this);
		}
	}

	public GridCorner asGridCorner() {
		switch(this) {
			case UP_LEFT: return GridCorner.UP_LEFT;
			case UP_RIGHT: return GridCorner.UP_RIGHT;
			case DOWN_LEFT: return GridCorner.DOWN_LEFT;
			case DOWN_RIGHT: return GridCorner.DOWN_RIGHT;
			default: throw new IllegalStateException("Not a GridCorner: " + this);
		}
	}

	public GridDirection8 opposite() {
		return values[values.length - ordinal() - 1];
	}

	public GridDirection8 nextClockwise() {
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

	public GridDirection8 nextCounterClockwise() {
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

	public GridDirection8 nextClockwiseOrthogonal() {
		switch(this) {
			case UP_LEFT: return UP_RIGHT;
			case UP: return RIGHT;
			case UP_RIGHT: return DOWN_RIGHT;
			case LEFT: return UP;
			case RIGHT: return DOWN;
			case DOWN_LEFT: return UP_LEFT;
			case DOWN: return LEFT;
			case DOWN_RIGHT: return DOWN_LEFT;
			default: throw new IllegalStateException();
		}
	}

	public GridDirection8 nextCounterClockwiseOrthogonal() {
		switch(this) {
			case UP_RIGHT: return UP_LEFT;
			case RIGHT: return UP;
			case DOWN_RIGHT: return UP_RIGHT;
			case UP: return LEFT;
			case DOWN: return RIGHT;
			case UP_LEFT: return DOWN_LEFT;
			case LEFT: return DOWN;
			case DOWN_LEFT: return DOWN_RIGHT;
			default: throw new IllegalStateException();
		}
	}

	public static GridDirection8 closestDirection(float x, float y) {
		GridDirection8 closest = values[0];
		for(int i = 1; i < values.length; i++)
			if(values[i].normal.dot(x, y) > closest.normal.dot(x, y))
				closest = values[i];
		return closest;
	}

	public static GridDirection8 closestDirection(Vector2 vector) {
		return closestDirection(vector.x, vector.y);
	}
}
