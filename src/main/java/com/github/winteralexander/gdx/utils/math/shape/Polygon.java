package com.github.winteralexander.gdx.utils.math.shape;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.github.winteralexander.gdx.utils.ConstFloatSupplier;
import com.github.winteralexander.gdx.utils.ConstSupplier;
import com.github.winteralexander.gdx.utils.FloatSupplier;
import com.github.winteralexander.gdx.utils.Validation;
import com.github.winteralexander.gdx.utils.math.MathUtil;

import java.util.function.Supplier;

import static com.badlogic.gdx.math.Intersector.intersectSegmentCircle;
import static com.badlogic.gdx.math.Intersector.overlapConvexPolygons;

/**
 * {@link Shape} implemented from LibGDX's Polygon
 * <p>
 * Created on 2018-03-02.
 *
 * @author Alexander Winter
 */
public class Polygon implements Shape {
	private final Supplier<Vector2> position;
	private final Supplier<Vector2> scale;
	private final FloatSupplier angle;
	private final com.badlogic.gdx.math.Polygon polygon;

	private final com.badlogic.gdx.math.Polygon tmpRectPolygon = new com.badlogic.gdx.math.Polygon(new float[8]);
	private final Vector2 tmpCircleCenter = new Vector2();
	private final Vector2 tmpStart = new Vector2(), tmpEnd = new Vector2();

	private final Rectangle boundingRectangle;

	public Polygon(Vector2 position, Vector2 scale, float angle, float[] vertices) {
		this(position, scale, angle, new com.badlogic.gdx.math.Polygon(vertices));
	}

	public Polygon(Supplier<Vector2> position, Supplier<Vector2> scale, FloatSupplier angle, float[] vertices) {
		this(position, scale, angle, new com.badlogic.gdx.math.Polygon(vertices));
	}

	public Polygon(Vector2 position, Vector2 scale, float angle, com.badlogic.gdx.math.Polygon polygon) {
		this(ConstSupplier.asConst(position), ConstSupplier.asConst(scale), new ConstFloatSupplier(angle), polygon);
	}

	public Polygon(Supplier<Vector2> position, Supplier<Vector2> scale, FloatSupplier angle, com.badlogic.gdx.math.Polygon polygon) {
		Validation.ensureNotNull(position, "position");
		Validation.ensureNotNull(scale, "scale");
		Validation.ensureNotNull(angle, "angle");
		Validation.ensureNotNull(polygon, "polygon");
		this.position = position;
		this.scale = scale;
		this.angle = angle;
		this.polygon = polygon;

		boundingRectangle = new Rectangle(() -> getGdxPolygon().getBoundingRectangle());
	}

	@Override
	public boolean contains(float x, float y) {
		return getGdxPolygon().contains(x, y);
	}

	@Override
	public boolean crossesSegment(float x, float y, float x2, float y2) {
		com.badlogic.gdx.math.Polygon polygon = getGdxPolygon();
		return Intersector.intersectSegmentPolygon(tmpStart.set(x, y), tmpEnd.set(x2, y2), polygon);
	}

	@Override
	public boolean overlaps(Shape shape) {
		com.badlogic.gdx.math.Polygon polygon = getGdxPolygon();

		if(shape instanceof Polygon)
			return overlapConvexPolygons(polygon, ((Polygon)shape).polygon);
		else if(shape instanceof Rectangle) {
			tmpRectPolygon.setPosition(((Rectangle)shape).getPosition().x - ((Rectangle)shape).getSize().x / 2f,
					((Rectangle)shape).getPosition().y - ((Rectangle)shape).getSize().y / 2f);
			tmpRectPolygon.getVertices()[2] = ((Rectangle)shape).getSize().x;

			tmpRectPolygon.getVertices()[4] = ((Rectangle)shape).getSize().x;
			tmpRectPolygon.getVertices()[5] = ((Rectangle)shape).getSize().y;

			tmpRectPolygon.getVertices()[7] = ((Rectangle)shape).getSize().y;

			return overlapConvexPolygons(polygon, tmpRectPolygon);
		} else if(shape instanceof Circle)
			return overlapsCircle(((Circle)shape).getPosition().x,
					((Circle)shape).getPosition().y,
					((Circle)shape).getRadius());
		else if(shape instanceof Annulus) {
			if(!overlapsCircle(((Annulus)shape).getPosition().x,
					((Annulus)shape).getPosition().y,
					((Annulus)shape).getOuterRadius()))
				return false;

			for(int i = 0; i < polygon.getTransformedVertices().length / 2; i++) {
				float x = polygon.getTransformedVertices()[i * 2];
				float y = polygon.getTransformedVertices()[i * 2 + 1];

				if(shape.contains(x, y))
					return true;
			}
			return false;
		}

		return shape.overlaps(this);
	}

	private boolean overlapsCircle(float x, float y, float radius) {
		com.badlogic.gdx.math.Polygon polygon = getGdxPolygon();
		float[] vertices = polygon.getTransformedVertices();

		tmpCircleCenter.set(x, y);
		float squareRadius = MathUtil.pow2(radius);
		for(int i = 0; i < vertices.length; i += 2) {
			if(i == 0)
				tmpStart.set(vertices[vertices.length - 2], vertices[vertices.length - 1]);
			else
				tmpStart.set(vertices[i - 2], vertices[i - 1]);

			tmpEnd.set(vertices[i], vertices[i + 1]);
			if(intersectSegmentCircle(tmpStart, tmpEnd, tmpCircleCenter, squareRadius))
				return true;
		}
		return polygon.contains(tmpCircleCenter.x, tmpCircleCenter.y);
	}

	@Override
	public void projectOnto(Vector2 point) {
		if(contains(point.x, point.y))
			return;

		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public boolean fullyContains(Shape shape) {
		throw new UnsupportedOperationException("Not implemented");
	}

	public Vector2 getPosition() {
		return position.get();
	}

	public Vector2 getScale() {
		return scale.get();
	}

	public float getAngle() {
		return angle.getAsFloat();
	}

	public float[] getVertices() {
		return getGdxPolygon().getTransformedVertices();
	}

	public com.badlogic.gdx.math.Polygon getGdxPolygon() {
		polygon.setPosition(getPosition().x, getPosition().y);
		polygon.setOrigin(0f, 0f);
		polygon.setScale(getScale().x, getScale().y);
		polygon.setRotation(getAngle());
		return polygon;
	}

	@Override
	public Rectangle getBoundingRectangle() {
		return boundingRectangle;
	}
}
