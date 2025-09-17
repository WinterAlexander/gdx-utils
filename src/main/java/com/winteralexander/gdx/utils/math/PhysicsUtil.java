package com.winteralexander.gdx.utils.math;

import com.badlogic.gdx.math.Vector2;

import static java.lang.Math.signum;

/**
 * Utility class for physics computation for momentum, velocity or forces. Does not deal with
 * collision detection or resolution.
 * <p>
 * Created on 2025-09-16.
 *
 * @author Alexander Winter
 */
public class PhysicsUtil {
	private PhysicsUtil() {}

	public static Vector2 push(Vector2 input, Vector2 direction, float scale, float cap) {
		return push(input, direction, scale, cap, input);
	}

	public static Vector2 push(Vector2 input, Vector2 direction, float scale, float cap, Vector2 vel) {
		vel.set(input);

		if(vel.x * direction.x < cap) {
			vel.x += direction.x * scale;

			if(vel.x * direction.x > cap)
				vel.x = cap * direction.x;
		}

		if(vel.y * direction.y < cap) {
			vel.y += direction.y * scale;

			if(vel.y * direction.y > cap)
				vel.y = cap * direction.y;
		}

		return vel;
	}

	public static float decelerate(float velocity, float deceleration, float delta) {
		float dir = signum(velocity);

		velocity -= deceleration * delta * dir;

		if(velocity * dir < 0f) // if velocity switched it's sign by deceleration
			velocity = 0f; // set it to 0, deceleration don't make you switch side

		return velocity;
	}

	public static Vector2 decelerate(Vector2 velocity, float deceleration, float delta) {
		float xDir = signum(velocity.x);
		float yDir = signum(velocity.y);

		velocity.x -= deceleration * delta * xDir;

		if(velocity.x * xDir < 0)
			velocity.x = 0;

		velocity.y -= deceleration * delta * yDir;

		if(velocity.y * yDir < 0)
			velocity.y = 0;

		return velocity;
	}
}
