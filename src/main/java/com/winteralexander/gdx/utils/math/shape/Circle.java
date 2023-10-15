package com.winteralexander.gdx.utils.math.shape;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.winteralexander.gdx.utils.FloatSupplier;
import com.winteralexander.gdx.utils.Validation;
import com.winteralexander.gdx.utils.Vec2Supplier;
import com.winteralexander.gdx.utils.math.MathUtil;

import java.util.function.Supplier;

import static com.winteralexander.gdx.utils.math.MathUtil.pow2;

/**
 * A circle shape
 * <p>
 * Created on 2017-03-02.
 *
 * @author Alexander Winter
 */
public class Circle implements Shape {
	private final Supplier<Vector2> position;
	private final FloatSupplier radius;

	private final Vector2 tmpVec = new Vector2(), tmpVec2 = new Vector2();

	private final Rectangle boundingRectangle;

	public Circle(Vector2 position, float radius) {
		this(() -> position, () -> radius);
	}

	public Circle(Supplier<Vector2> position, FloatSupplier radius) {
		Validation.ensureNotNull(position, "position");
		Validation.ensureNotNull(radius, "radius");
		this.position = position;
		this.radius = radius;

		boundingRectangle = new Rectangle(position,
				new Vec2Supplier(v -> v.set(radius.getAsFloat(), radius.getAsFloat()).scl(2f)));
	}

	@Override
	public boolean contains(float x, float y) {
		float dx = getPosition().x - x;
		float dy = getPosition().y - y;

		return dx * dx + dy * dy <= MathUtil.pow2(getRadius());
	}

	@Override
	public boolean crossesSegment(float x, float y, float x2, float y2) {
		return Intersector.intersectSegmentCircle(tmpVec.set(x, y), tmpVec2.set(x2, y2), getPosition(), getRadius());
	}

	@Override
	public boolean overlaps(Shape shape) {
		if(shape instanceof Circle) {
			float dx = getPosition().x - ((Circle)shape).getPosition().x;
			float dy = getPosition().y - ((Circle)shape).getPosition().y;

			return dx * dx + dy * dy <= MathUtil.pow2(getRadius() + ((Circle)shape).getRadius());
		}

		return shape.overlaps(this);
	}

	@Override
	public void projectOnto(Vector2 point) {
		if(contains(point.x, point.y))
			return;

		Vector2 center = getPosition();

		if(Float.isInfinite(point.x) || Float.isInfinite(point.y)) {
			if(point.x == Float.POSITIVE_INFINITY)
				point.x = 1f;
			else if(point.x == Float.NEGATIVE_INFINITY)
				point.x = -1f;
			else
				point.x = 0f;

			if(point.y == Float.POSITIVE_INFINITY)
				point.y = 1f;
			else if(point.y == Float.NEGATIVE_INFINITY)
				point.y = -1f;
			else
				point.y = 0f;

			point.nor().scl(getRadius()).add(center);
			return;
		}

		point.sub(center).nor().scl(getRadius()).add(center);
	}

	@Override
	public boolean fullyContains(Shape shape) {
		if(shape instanceof Point)
			return overlaps(shape);

		if(shape instanceof Rectangle) {
			tmpVec2.set(((Rectangle)shape).getSize())
					.scl(-0.5f, -0.5f)
					.rotateDeg(((Rectangle)shape).getAngle())
					.add(((Rectangle)shape).getPosition());
			if(!contains(tmpVec2))
				return false;

			tmpVec2.set(((Rectangle)shape).getSize())
					.scl(0.5f, -0.5f)
					.rotateDeg(((Rectangle)shape).getAngle())
					.add(((Rectangle)shape).getPosition());
			if(!contains(tmpVec2))
				return false;

			tmpVec2.set(((Rectangle)shape).getSize())
					.scl(-0.5f, 0.5f)
					.rotateDeg(((Rectangle)shape).getAngle())
					.add(((Rectangle)shape).getPosition());
			if(!contains(tmpVec2))
				return false;

			tmpVec2.set(((Rectangle)shape).getSize())
					.scl(0.5f, 0.5f)
					.rotateDeg(((Rectangle)shape).getAngle())
					.add(((Rectangle)shape).getPosition());
			return contains(tmpVec2);
		}

		if(shape instanceof Circle) {
			if(((Circle)shape).getRadius() > getRadius())
				return false;

			tmpVec2.set(getPosition()).sub(((Circle)shape).getPosition());

			return getPosition().dst2(((Circle)shape).getPosition()) <= MathUtil.pow2(getRadius() - ((Circle)shape).getRadius());
		}

		if(shape instanceof Annulus) {
			if(((Annulus)shape).getOuterRadius() > getRadius())
				return false;

			tmpVec2.set(getPosition()).sub(((Annulus)shape).getPosition());

			return getPosition().dst2(((Annulus)shape).getPosition()) <= MathUtil.pow2(getRadius() - ((Annulus)shape).getOuterRadius());
		}

		if(shape instanceof MultiShape) {
			for(Shape inner : ((MultiShape)shape).getShapes())
				if(!fullyContains(inner))
					return false;
			return true;
		}

		if(shape instanceof Polygon) {
			float[] vertices = ((Polygon)shape).getVertices();

			for(int i = 0; i < vertices.length; i += 2)
				if(!contains(vertices[i], vertices[i + 1]))
					return false;

			return true;
		}

		return false;
	}

	@Override
	public Rectangle getBoundingRectangle() {
		return boundingRectangle;
	}

	public Vector2 getPosition() {
		return position.get();
	}

	public float getRadius() {
		return radius.getAsFloat();
	}
}
