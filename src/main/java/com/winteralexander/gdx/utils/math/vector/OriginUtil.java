package com.winteralexander.gdx.utils.math.vector;

import com.badlogic.gdx.math.Vector2;

/**
 * Utilitary class to find origin of an object based on its size and aligment.
 * Note that it matches {@link com.badlogic.gdx.utils.Align} perfectly.
 * <p>
 * Created on 2018-09-07.
 *
 * @author Alexander Winter
 */
public class OriginUtil {
	@SuppressWarnings("PointlessBitwiseExpression")
	public static final int ALIGN_CENTER = 1 << 0;

	public static final int ALIGN_TOP = 1 << 1;
	public static final int ALIGN_BOTTOM = 1 << 2;

	public static final int ALIGN_LEFT = 1 << 3;
	public static final int ALIGN_RIGHT = 1 << 4;

	public static Vector2 getOrigin(Vector2 size, int align) {
		return getOrigin(size.x, size.y, align);
	}

	public static Vector2 getOrigin(Vector2 size, int align, Vector2 out) {
		return getOrigin(size.x, size.y, align, out);
	}

	public static Vector2 getOrigin(float width, float height, int align) {
		return getOrigin(width, height, align, new Vector2());
	}

	public static Vector2 getOrigin(float width, float height, int align, Vector2 out) {
		if((align & ALIGN_LEFT) != 0)
			out.x = 0;
		else if((align & ALIGN_RIGHT) != 0)
			out.x = -width;
		else
			out.x = width / -2f;

		if((align & ALIGN_BOTTOM) != 0)
			out.y = 0;
		else if((align & ALIGN_TOP) != 0)
			out.y = -height;
		else
			out.y = height / -2f;

		return out;
	}
}
