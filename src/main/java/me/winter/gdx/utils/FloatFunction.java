package me.winter.gdx.utils;

/**
 * Represents a function that returns a primitive floating point type
 * <p>
 * Created on 2022-04-14.
 *
 * @author Alexander Winter
 */
public interface FloatFunction<T> {
	float apply(T object);
}
