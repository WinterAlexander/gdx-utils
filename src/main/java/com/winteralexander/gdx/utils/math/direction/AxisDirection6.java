package com.winteralexander.gdx.utils.math.direction;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.winteralexander.gdx.utils.EnumConstantCache;

/**
 * One of 6 finite directions in a 3D environment
 * <p>
 * Created on 2025-07-14.
 *
 * @author Alexander Winter
 */
public enum AxisDirection6 {
	RIGHT(1f, 0f, 0f),
	UP(0f, 1f, 0f),
	BACK(0f, 0f, 1f),
	LEFT(-1f, 0f, 0f),
	DOWN(0f, -1f, 0f),
	FRONT(0f, 0f, -1f),
	;

	public static final AxisDirection6[] values = EnumConstantCache.store(values());

	private final Vector3 direction;

	AxisDirection6(float x, float y, float z) {
		direction = new Vector3(x, y, z);
	}

	public float getSidePosition(BoundingBox boundingBox) {
		switch(this) {
			case RIGHT:
				return boundingBox.getCenterX() + boundingBox.getWidth() / 2f;

			case LEFT:
				return boundingBox.getCenterX() - boundingBox.getWidth() / 2f;

			case UP:
				return boundingBox.getCenterY() + boundingBox.getHeight() / 2f;

			case DOWN:
				return boundingBox.getCenterY() - boundingBox.getHeight() / 2f;

			case BACK:
				return boundingBox.getCenterY() + boundingBox.getDepth() / 2f;

			case FRONT:
				return boundingBox.getCenterY() - boundingBox.getDepth() / 2f;

			default:
				throw new IllegalStateException("Invalid navigation direction");
		}
	}

	public static AxisDirection6 closestDirection(float x, float y, float z) {
		AxisDirection6 closest = values[0];
		for(int i = 1; i < values.length; i++)
			if(values[i].direction.dot(x, y, z) > closest.direction.dot(x, y, z))
				closest = values[i];
		return closest;
	}

	public static AxisDirection6 closestDirection(Vector3 vector) {
		return closestDirection(vector.x, vector.y, vector.z);
	}

	public AxisDirection6 opposite() {
		return values[(ordinal() + values.length / 2) % values.length];
	}

	public int x() {
		return (int)asVector().x;
	}

	public int y() {
		return (int)asVector().y;
	}

	public int z() {
		return (int)asVector().z;
	}

	public Vector3 asVector() {
		return direction;
	}
}
