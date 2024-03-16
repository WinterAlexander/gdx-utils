package com.winteralexander.gdx.utils.math.vector;

import com.badlogic.gdx.math.Interpolation;
import com.winteralexander.gdx.utils.io.Serializable;

/**
 * A vector made with integer components
 * <p>
 * Created on 2024-03-16.
 *
 * @author Alexander Winter
 */
public interface IntVector<T extends IntVector<T>> extends Serializable {
	/**
	 * @return copy of this vector
	 */
	T cpy();

	/**
	 * @return length of this vector as float
	 */
	default float len() {
		return (float)Math.sqrt(len2());
	}

	/**
	 * @return squared length of this vector as integer
	 */
	int len2();

	/**
	 * @return squared length of this vector as a long integer
	 */
	long len2l();

	/**
	 * Set the value of this vector to another one
	 *
	 * @param v vector to set
	 * @return this vector for chaining
	 */
	T set(T v);

	/**
	 * @return sets this vector to zero
	 */
	default T setZero() {
		return scl(0);
	}

	/**
	 * @return true if this vector is zero, otherwise false
	 */
	default boolean isZero() {
		return len2() == 0;
	}

	/**
	 * Checks for equality between this vector and the other one, equality is defined as all
	 * components being equal
	 *
	 * @param other other vector to compare
	 * @return true if this vector equals the one provided, otherwise false
	 */
	boolean equals(T other);

	/**
	 * Adds the specified vector to this one
	 * @param v vector to add
	 * @return this vector for chaining
	 */
	T add(T v);

	/**
	 *
	 * @param v
	 * @return this vector for chaining
	 */
	T sub(T v);

	/**
	 *
	 * @param v
	 * @return
	 */
	int dot(T v);

	/**
	 *
	 * @param scalar
	 * @return this vector for chaining
	 */
	T scl(float scalar);

	/**
	 *
	 * @param scalar
	 * @return this vector for chaining
	 */
	T scl(int scalar);

	/**
	 *
	 * @param v
	 * @return this vector for chaining
	 */
	T scl(T v);

	/**
	 *
	 * @param v
	 * @return distance between this vector and the specified one
	 */
	default float dst(T v) {
		return (float)Math.sqrt(dst2(v));
	}

	/**
	 *
	 * @param v
	 * @return squared distance between this vector and the specified one
	 */
	int dst2(T v);

	/**
	 *
	 * @param target
	 * @param alpha
	 * @return
	 */
	T lerp(T target, float alpha);

	/**
	 *
	 * @param target
	 * @param alpha
	 * @param interpolation
	 * @return this vector for chaining
	 */
	default T interpolate(T target, float alpha, Interpolation interpolation) {
		return lerp(target, interpolation.apply(alpha));
	}

	/**
	 *
	 * @param v
	 * @param scalar
	 * @return this vector for chaining
	 */
	T mulAdd(T v, int scalar);

	/**
	 *
	 * @param v
	 * @param scalar
	 * @return this vector for chaining
	 */
	T mulAdd(T v, float scalar);

	/**
	 *
	 * @param v
	 * @param mulVec
	 * @return this vector for chaining
	 */
	T mulAdd(T v, T mulVec);
}
