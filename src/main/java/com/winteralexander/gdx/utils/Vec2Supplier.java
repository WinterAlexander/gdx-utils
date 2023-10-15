package com.winteralexander.gdx.utils;

import com.badlogic.gdx.math.Vector2;

import java.util.function.Consumer;

/**
 * {@link MutableSupplier} for a {@link Vector2}
 * <p>
 * Created on 2021-02-19.
 *
 * @author Alexander Winter
 */
public class Vec2Supplier extends MutableSupplier<Vector2> {
	public Vec2Supplier(Consumer<Vector2> cacheModifier) {
		super(new Vector2(), cacheModifier);
	}

	public static ConstSupplier<Vector2> constVec(float x, float y) {
		return ConstSupplier.asConst(new Vector2(x, y));
	}
}
