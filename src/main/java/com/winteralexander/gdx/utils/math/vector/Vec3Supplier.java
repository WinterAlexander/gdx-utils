package com.winteralexander.gdx.utils.math.vector;

import com.badlogic.gdx.math.Vector3;
import com.winteralexander.gdx.utils.ConstSupplier;
import com.winteralexander.gdx.utils.MutableSupplier;

import java.util.function.Consumer;

/**
 * {@link MutableSupplier} for a {@link Vector3}
 * <p>
 * Created on 2024-04-29.
 *
 * @author Alexander Winter
 */
public class Vec3Supplier extends MutableSupplier<Vector3> {
	public Vec3Supplier(Consumer<Vector3> cacheModifier) {
		super(new Vector3(), cacheModifier);
	}

	public static ConstSupplier<Vector3> constVec(float x, float y, float z) {
		return ConstSupplier.asConst(new Vector3(x, y, z));
	}
}
