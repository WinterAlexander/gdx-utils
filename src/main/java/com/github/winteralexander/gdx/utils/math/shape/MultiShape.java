package com.github.winteralexander.gdx.utils.math.shape;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.github.winteralexander.gdx.utils.Validation;

/**
 * {@link Shape} made of multiple shapes.
 * <p>
 * Created on 2018-02-10.
 *
 * @author Alexander Winter
 */
public class MultiShape implements Shape {
	private final Iterable<Shape> shapes;

	private final Vector2 tmpBest = new Vector2(), tmpCurrent = new Vector2();

	private final Vector2 tmpBoundPos = new Vector2(), tmpBoundSize = new Vector2();

	private final Rectangle boundingRectangle;

	public MultiShape(Shape... shapes) {
		Validation.ensureNotNull(shapes, "shapes");
		this.shapes = new Array<>(shapes);
		boundingRectangle = new Rectangle(tmpBoundPos, tmpBoundSize);
	}

	public MultiShape(Iterable<Shape> shapes) {
		Validation.ensureNotNull(shapes, "shapes");
		this.shapes = shapes;
		boundingRectangle = new Rectangle(tmpBoundPos, tmpBoundSize);
	}

	@Override
	public boolean contains(float x, float y) {
		for(Shape current : shapes)
			if(current.contains(x, y))
				return true;

		return false;
	}

	@Override
	public boolean crossesSegment(float x, float y, float x2, float y2) {
		for(Shape shape : shapes)
			if(shape.crossesSegment(x, y, x2, y2))
				return true;

		return false;
	}

	@Override
	public boolean overlaps(Shape shape) {
		for(Shape current : shapes)
			if(current.overlaps(shape))
				return true;

		return false;
	}

	@Override
	public void projectOnto(Vector2 point) {
		if(!shapes.iterator().hasNext())
			throw new IllegalStateException("No shapes in this MultiShape");

		tmpBest.set(Float.NaN, Float.NaN);
		for(Shape shape : shapes) {
			tmpCurrent.set(point);
			shape.projectOnto(tmpCurrent);

			if(Float.isNaN(tmpBest.x) || Float.isNaN(tmpBest.y))
				tmpBest.set(tmpCurrent);

			else if(Float.isFinite(point.x) && Float.isFinite(point.y)) {
				if(point.dst2(tmpBest) > point.dst2(tmpCurrent))
					tmpBest.set(tmpCurrent);
			} else {
				float dirX = Math.signum(point.x);
				float dirY = Math.signum(point.y);

				if(tmpBest.dot(dirX, dirY) < tmpCurrent.dot(dirX, dirY))
					tmpBest.set(tmpCurrent);
			}
		}
		point.set(tmpBest);
	}

	@Override
	public boolean fullyContains(Shape shape) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public Rectangle getBoundingRectangle() {
		float minX = Float.POSITIVE_INFINITY,
				minY = Float.POSITIVE_INFINITY,
				maxX = Float.NEGATIVE_INFINITY,
				maxY = Float.NEGATIVE_INFINITY;

		for(Shape shape : getShapes()) {
			Rectangle rect = shape.getBoundingRectangle();
			Vector2 pos = rect.getPosition();
			Vector2 size = rect.getSize();

			minX = Math.min(pos.x - size.x / 2f, minX);
			minY = Math.min(pos.y - size.y / 2f, minY);
			maxX = Math.max(pos.x + size.x / 2f, maxX);
			maxY = Math.max(pos.y + size.y / 2f, maxY);
		}

		if(Float.isInfinite(minX) || Float.isInfinite(minY))
			return null;

		tmpBoundPos.set(minX + maxX, minY + maxY).scl(0.5f);
		tmpBoundSize.set(maxX - minX, maxY - minY);
		return boundingRectangle;
	}

	public Iterable<Shape> getShapes() {
		return shapes;
	}
}
