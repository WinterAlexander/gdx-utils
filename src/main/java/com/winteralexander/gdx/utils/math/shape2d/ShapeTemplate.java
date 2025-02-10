package com.winteralexander.gdx.utils.math.shape2d;

import com.badlogic.gdx.math.Vector2;
import com.winteralexander.gdx.utils.FloatSupplier;

import java.util.function.Supplier;

/**
 * Provides a {@link Shape} when given position, scale and rotation, representing
 * a shape independently of those parameters.
 * <p>
 * Created on 2020-10-24.
 *
 * @author Alexander Winter
 */
public interface ShapeTemplate {
	Shape makeShape(Supplier<Vector2> position, Supplier<Vector2> scale, FloatSupplier angle);
}
