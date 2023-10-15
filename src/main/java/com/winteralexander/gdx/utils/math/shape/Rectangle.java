package com.winteralexander.gdx.utils.math.shape;

import com.badlogic.gdx.math.Vector2;
import com.winteralexander.gdx.utils.ConstFloatSupplier;
import com.winteralexander.gdx.utils.FloatSupplier;
import com.winteralexander.gdx.utils.Validation;
import com.winteralexander.gdx.utils.Vec2Supplier;
import com.winteralexander.gdx.utils.math.MathUtil;

import java.util.function.Supplier;

import static com.winteralexander.gdx.utils.math.MathUtil.*;

/**
 * A rectangle shape which can be rotated
 * <p>
 * Created on 2016-12-05.
 *
 * @author Alexander Winter
 */
public class Rectangle implements Shape {
	private final Supplier<Vector2> position;
	private final Supplier<Vector2> size;
	private final FloatSupplier angle;

	private final Vector2 tmpVec2 = new Vector2();

	private final com.badlogic.gdx.math.Rectangle tmpGdxRect = new com.badlogic.gdx.math.Rectangle();

	public Rectangle(Vector2 position, Vector2 size) {
		this(() -> position, () -> size);
	}

	public Rectangle(Supplier<Vector2> position, Vector2 size) {
		this(position, () -> size);
	}

	public Rectangle(Supplier<Vector2> position, Supplier<Vector2> size) {
		this(position, size, ConstFloatSupplier.ZERO);
	}

	public Rectangle(Supplier<Vector2> position, Supplier<Vector2> size, FloatSupplier angle) {
		Validation.ensureNotNull(position, "position");
		Validation.ensureNotNull(size, "size");
		Validation.ensureNotNull(angle, "angle");
		this.position = position;
		this.size = size;
		this.angle = angle;
	}

	public Rectangle(com.badlogic.gdx.math.Rectangle gdxRect) {
		this(new Vector2(gdxRect.x + gdxRect.width / 2f, gdxRect.y + gdxRect.height / 2f),
				new Vector2(gdxRect.width, gdxRect.height));
	}

	public Rectangle(Supplier<com.badlogic.gdx.math.Rectangle> gdxRect) {
		this(new Vec2Supplier(v -> v.set(
						gdxRect.get().x - gdxRect.get().width / 2f,
						gdxRect.get().y - gdxRect.get().height / 2f)),
				new Vec2Supplier(v -> v.set(gdxRect.get().width, gdxRect.get().height)));
	}

	@Override
	public boolean contains(float x, float y) {
		return inOBB(x, y, getPosition().x, getPosition().y, getSize().x, getSize().y, getAngle());
	}

	@Override
	public boolean crossesSegment(float x, float y, float x2, float y2) {
		tmpVec2.set(x, y).sub(getPosition()).rotateDeg(-getAngle());
		x = tmpVec2.x;
		y = tmpVec2.y;
		tmpVec2.set(x2, y2).sub(getPosition()).rotateDeg(-getAngle());

		return MathUtil.lineCrossesAABB(x, y, tmpVec2.x, tmpVec2.y,
				-getSize().x / 2f,
				-getSize().y / 2f,
				getSize().x,
				getSize().y);
	}

	@Override
	public boolean overlaps(Shape shape) {
		if(shape instanceof Rectangle)
			return doOBBCollideWithOBB(getPosition().x,
					getPosition().y,
					getSize().x,
					getSize().y,
					getAngle(),
					((Rectangle)shape).getPosition().x,
					((Rectangle)shape).getPosition().y,
					((Rectangle)shape).getSize().x,
					((Rectangle)shape).getSize().y,
					((Rectangle)shape).getAngle());
		else if(shape instanceof Circle)
			return overlapsCircle(((Circle)shape).getPosition().x,
					((Circle)shape).getPosition().y,
					((Circle)shape).getRadius());
		else if(shape instanceof Annulus) {
			if(!overlapsCircle(((Annulus)shape).getPosition().x,
					((Annulus)shape).getPosition().y,
					((Annulus)shape).getOuterRadius()))
				return false;

			tmpVec2.set(getSize()).scl(0.5f).rotateDeg(getAngle());

			return shape.contains(getPosition().x - tmpVec2.x, getPosition().y - tmpVec2.y)
					|| shape.contains(getPosition().x + tmpVec2.x, getPosition().y - tmpVec2.y)
					|| shape.contains(getPosition().x - tmpVec2.x, getPosition().y + tmpVec2.y)
					|| shape.contains(getPosition().x + tmpVec2.x, getPosition().y + tmpVec2.y);
		}

		return shape.overlaps(this);
	}

	private boolean overlapsCircle(float x, float y, float radius) {
		tmpVec2.set(x, y).sub(getPosition()).rotateDeg(-getAngle());
		x = tmpVec2.x;
		y = tmpVec2.y;

		float hw = getSize().x / 2f;
		float hh = getSize().y / 2f;

		float closestX = x, closestY = y;

		if(x < -hw)
			closestX = -hw;
		else if(x > hw)
			closestX = hw;

		if(y < -hh)
			closestY = -hh;
		else if(y > hh)
			closestY = hh;


		closestX = closestX - x;
		closestX *= closestX;
		closestY = closestY - y;
		closestY *= closestY;

		return closestX + closestY < pow2(radius);
	}

	@Override
	public void projectOnto(Vector2 point) {
		if(contains(point.x, point.y))
			return;

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

			point.nor().scl(Math.abs(getSize().x) + Math.abs(getSize().y)).add(getPosition());
		}

		point.sub(getPosition()).rotateDeg(-getAngle());

		float startX = -getSize().x / 2f;
		float startY = -getSize().y / 2f;
		float endX = getSize().x / 2f;
		float endY = getSize().y / 2f;

		if(point.x < startX)
			point.x = startX;

		if(point.x > endX)
			point.x = endX;

		if(point.y < startY)
			point.y = startY;

		if(point.y > endY)
			point.y = endY;

		point.rotateDeg(getAngle()).add(getPosition());
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
			if(((Circle)shape).getRadius() > getSize().x / 2f || ((Circle)shape).getRadius() > getSize().y / 2f)
				return false;

			tmpVec2.set(getPosition()).sub(((Circle)shape).getPosition());

			return Math.abs(tmpVec2.x) <= getSize().x / 2f - ((Circle)shape).getRadius()
					&& Math.abs(tmpVec2.y) <= getSize().y / 2f - ((Circle)shape).getRadius();
		}

		if(shape instanceof Annulus) {
			if(((Annulus)shape).getOuterRadius() > getSize().x / 2f || ((Annulus)shape).getOuterRadius() > getSize().y / 2f)
				return false;

			tmpVec2.set(getPosition()).sub(((Annulus)shape).getPosition());

			return Math.abs(tmpVec2.x) <= getSize().x / 2f - ((Annulus)shape).getOuterRadius()
					&& Math.abs(tmpVec2.y) <= getSize().y / 2f - ((Annulus)shape).getOuterRadius();
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
		return this;
	}

	public com.badlogic.gdx.math.Rectangle asGdxRect() {
		return asGdxRect(tmpGdxRect);
	}

	public com.badlogic.gdx.math.Rectangle asGdxRect(com.badlogic.gdx.math.Rectangle out) {
		Vector2 pos = getPosition();
		Vector2 size = getSize();
		out.set(pos.x - size.x / 2f, pos.y - size.y / 2f, size.x, size.y);
		return out;
	}

	public Vector2 getPosition() {
		return position.get();
	}

	public Vector2 getSize() {
		return size.get();
	}

	public float getAngle() {
		return angle.getAsFloat();
	}
}
