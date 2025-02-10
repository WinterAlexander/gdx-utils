package com.winteralexander.gdx.utils.math.vector;

import com.winteralexander.gdx.utils.ConstSupplier;
import com.winteralexander.gdx.utils.MutableSupplier;

import java.util.function.Consumer;

/**
 * {@link MutableSupplier} for a {@link Vector3i}
 * <p>
 * Created on 2024-04-29.
 *
 * @author Alexander Winter
 */
public class Vec3iSupplier extends MutableSupplier<Vector3i> {
	public Vec3iSupplier(Consumer<Vector3i> cacheModifier) {
		super(new Vector3i(), cacheModifier);
	}

	public static ConstSupplier<Vector3i> constVec(int x, int y, int z) {
		return ConstSupplier.asConst(new Vector3i(x, y, z));
	}
}
