package me.winter.gdx.utils.math.shape;

import com.badlogic.gdx.math.Vector2;

import java.util.function.Supplier;

import static me.winter.gdx.utils.ConstSupplier.asConst;
import static me.winter.gdx.utils.Validation.ensureNotNull;

/**
 * A single point, cannot contain other points but can be contained in other
 * shapes
 * <p>
 * Created on 2020-10-20.
 *
 * @author Alexander Winter
 */
public class Point implements Shape {
	private final Supplier<Vector2> position;

	private final Rectangle boundingRectangle;

	public Point(Vector2 position) {
		this(() -> position);
	}

	public Point(Supplier<Vector2> position) {
		ensureNotNull(position, "position");
		this.position = position;

		boundingRectangle = new Rectangle(position, asConst(new Vector2(0f, 0f)));
	}

	@Override
	public boolean contains(float x, float y) {
		return false;
	}

	@Override
	public boolean crossesSegment(float x, float y, float x2, float y2) {
		return false;
	}

	@Override
	public boolean overlaps(Shape shape) {
		return shape.contains(getPosition());
	}

	@Override
	public void projectOnto(Vector2 point) {
		point.set(getPosition());
	}

	@Override
	public boolean fullyContains(Shape shape) {
		return false;
	}

	public Vector2 getPosition() {
		return position.get();
	}

	@Override
	public Rectangle getBoundingRectangle() {
		return boundingRectangle;
	}
}
