package com.github.winteralexander.gdx.utils.memory;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.github.winteralexander.gdx.utils.ReflectionUtil;

import static com.badlogic.gdx.utils.Pools.free;
import static com.badlogic.gdx.utils.Pools.obtain;

/**
 * Extension to LibGDx pools allowing handling of array faster
 * <p>
 * Created on 2017-01-29.
 *
 * @author Alexander Winter
 */
public class PoolsPlus {
	@SuppressWarnings("unchecked")
	public static <T> Array<T> obtainArray() {
		return obtain(Array.class);
	}

	/**
	 * Free a GDX array for future uses
	 * <p>
	 * Assumes it's elements are already freed or doesn't need to be.
	 * <p>
	 * If you want to free the elements of an array,
	 *
	 * @see Pools#freeAll(Array)
	 */
	public static void freeArray(Array<?> array) {
		array.clear();
		free(array);
	}

	public static void resetPoolCapacity(Pool<?> pool) {
		((Array<?>)ReflectionUtil.get(pool, "freeObjects")).shrink();
	}
}
