package com.winteralexander.gdx.utils;

/**
 * {@link java.util.function.Predicate} for floating points
 * <p>
 * Created on 2025-11-18.
 *
 * @author Alexander Winter
 */
@FunctionalInterface
public interface FloatPredicate {
	boolean test(float value);
}
