package com.winteralexander.gdx.utils;

import com.winteralexander.gdx.utils.math.vector.Vector2i;

import java.util.function.Consumer;

/**
 * {@link MutableSupplier} for a {@link Vector2i}
 * <p>
 * Created on 2024-04-29.
 *
 * @author Alexander Winter
 */
public class Vec2iSupplier extends MutableSupplier<Vector2i> {
	public Vec2iSupplier(Consumer<Vector2i> cacheModifier) {
		super(new Vector2i(), cacheModifier);
	}

	public static ConstSupplier<Vector2i> constVec(int x, int y) {
		return ConstSupplier.asConst(new Vector2i(x, y));
	}
}
