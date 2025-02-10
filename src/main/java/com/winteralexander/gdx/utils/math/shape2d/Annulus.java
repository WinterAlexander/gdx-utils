package com.winteralexander.gdx.utils.math.shape2d;

import com.badlogic.gdx.math.Vector2;
import com.winteralexander.gdx.utils.FloatSupplier;
import com.winteralexander.gdx.utils.Validation;
import com.winteralexander.gdx.utils.math.vector.Vec2Supplier;

import java.util.function.Supplier;

import static com.badlogic.gdx.math.Intersector.intersectSegmentCircle;
import static com.winteralexander.gdx.utils.math.MathUtil.pow2;

/**
 * Represents an annulus, which is basically a 2D donut
 * <p>
 * Created on 2020-10-20.
 *
 * @author Alexander Winter
 */
public class Annulus implements Shape {
	private final Supplier<Vector2> position;
	private final FloatSupplier innerRadius, outerRadius;

	private final Vector2 tmpVec = new Vector2(), tmpVec2 = new Vector2();

	private final Circle tmpInnerCircle;
	private final Rectangle boundingRectangle;

	public Annulus(Vector2 position, float innerRadius, float outerRadius) {
		this(() -> position, () -> innerRadius, () -> outerRadius);
	}

	public Annulus(Supplier<Vector2> position, FloatSupplier innerRadius, FloatSupplier outerRadius) {
		Validation.ensureNotNull(position, "position");
		this.position = position;
		this.innerRadius = innerRadius;
		this.outerRadius = outerRadius;

		tmpInnerCircle = new Circle(position, innerRadius);

		boundingRectangle = new Rectangle(position,
				new Vec2Supplier(v -> v.set(outerRadius.getAsFloat(), outerRadius.getAsFloat()).scl(2f)));
	}

	@Override
	public boolean contains(float x, float y) {
		float dst2 = getPosition().dst2(x, y);
		return dst2 <= pow2(getOuterRadius()) && dst2 >= pow2(getInnerRadius());
	}

	@Override
	public boolean crossesSegment(float x, float y, float x2, float y2) {
		float firstDst2 = getPosition().dst2(x, y);
		float secondDst2 = getPosition().dst2(x2, y2);

		if(firstDst2 < pow2(getInnerRadius()) && secondDst2 < pow2(getInnerRadius()))
			return false;

		return intersectSegmentCircle(tmpVec.set(x, y),
				tmpVec2.set(x2, y2),
				getPosition(),
				getOuterRadius());
	}

	@Override
	public boolean overlaps(Shape shape) {
		if(shape instanceof Circle) {
			float dst2 = getPosition().dst2(((Circle)shape).getPosition());

			if(dst2 > pow2(getOuterRadius() + ((Circle)shape).getRadius()))
				return false;

			float innerR = getInnerRadius() - ((Circle)shape).getRadius();

			return innerR <= 0f || dst2 >= pow2(innerR);
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

			point.nor().scl(getOuterRadius()).add(center);
			return;
		}

		point.sub(center);
		float dst = point.len();
		point.scl(1f / dst);

		if(dst <= getInnerRadius())
			point.scl(getInnerRadius());
		else
			point.scl(getOuterRadius());
		point.add(center);
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
			if(!contains(tmpVec2))
				return false;

			return !shape.overlaps(tmpInnerCircle);
		}

		if(shape instanceof Circle) {
			if(((Circle)shape).getRadius() > getOuterRadius())
				return false;

			tmpVec2.set(getPosition()).sub(((Circle)shape).getPosition());

			if(getPosition().dst2(((Circle)shape).getPosition()) > pow2(getOuterRadius() - ((Circle)shape).getRadius()))
				return false;

			return !shape.overlaps(tmpInnerCircle);
		}

		if(shape instanceof Annulus) {
			if(((Annulus)shape).getOuterRadius() > getOuterRadius())
				return false;

			tmpVec2.set(getPosition()).sub(((Annulus)shape).getPosition());

			if(getPosition().dst2(((Annulus)shape).getPosition()) > pow2(getOuterRadius() - ((Annulus)shape).getOuterRadius()))
				return false;

			return !shape.overlaps(tmpInnerCircle);
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

			return !tmpInnerCircle.overlaps(shape);
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

	public float getInnerRadius() {
		return innerRadius.getAsFloat();
	}

	public float getOuterRadius() {
		return outerRadius.getAsFloat();
	}
}
