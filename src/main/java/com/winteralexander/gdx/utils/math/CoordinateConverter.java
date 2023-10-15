package com.winteralexander.gdx.utils.math;

import com.badlogic.gdx.math.Vector2;

/**
 * Utility class that converts coordinates from a space to another
 * <p>
 * Created on 2023-07-28.
 *
 * @param <S> type that represents the space of the coordinates for conversion
 * @author Alexander Winter
 */
public interface CoordinateConverter<S> {
	/**
	 * Converts specified coordinates from a space to the other, in-place.
	 *
	 * @param from        space of the original coordinates
	 * @param to          space of the desired coordinates
	 * @param coordinates coordinates to convert
	 * @return coordinates parameter for chaining
	 */
	Vector2 convert(S from, S to, Vector2 coordinates);

	/**
	 * Returns the current position of the mouse in the specified coordinate space
	 *
	 * @param space coordinate space to return the mouse coordinates in
	 * @return mouse coordinates
	 */
	Vector2 getMousePosition(S space);
}
