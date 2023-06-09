package me.winter.gdx.utils.math;

import com.badlogic.gdx.math.Vector2;

/**
 * Offers useful methods and constants for LibGDX vectors
 * <p>
 * Created by on 2017-04-13.
 *
 * @author Alexander Winter
 */
public class VectorUtil {
	public static final Vector2 UP = new Vector2(0f, 1f);
	public static final Vector2 DOWN = new Vector2(0f, -1f);
	public static final Vector2 LEFT = new Vector2(-1f, 0f);
	public static final Vector2 RIGHT = new Vector2(1f, 0f);

	private VectorUtil() {}

	public static Vector2 round(Vector2 vec, int digits) {
		vec.x = MathUtil.round(vec.x, digits);
		vec.y = MathUtil.round(vec.y, digits);
		return vec;
	}
}
