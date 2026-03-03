package com.winteralexander.gdx.utils.test.math.shape2d;

import com.badlogic.gdx.math.Vector2;
import com.winteralexander.gdx.utils.math.shape2d.Annulus;
import com.winteralexander.gdx.utils.math.shape2d.Circle;
import com.winteralexander.gdx.utils.math.shape2d.Polygon;
import com.winteralexander.gdx.utils.math.shape2d.Rectangle;
import com.winteralexander.gdx.utils.math.shape2d.Shape;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests to ensure hitboxes are working propely
 * <p>
 * Created on 2018-01-27.
 *
 * @author Alexander Winter
 * @see Shape
 */
public class ShapeTest {
	@Test
	public void testRectContains() {
		Rectangle rect1 = new Rectangle(new Vector2(0f, 10f), new Vector2(20, 20));

		assertTrue(rect1.contains(-9f, 1f));
		assertTrue(rect1.contains(-9f, 19f));
		assertTrue(rect1.contains(9f, 19f));
		assertTrue(rect1.contains(9f, 1f));
	}

	@Test
	public void testRectangleToGdxConversion() {
		Rectangle first = new Rectangle(new Vector2(10f, 40f), new Vector2(20f, 30f));
		com.badlogic.gdx.math.Rectangle gdxRect = first.asGdxRect();
		Rectangle second = new Rectangle(gdxRect);
		assertTrue(first.fullyContains(second) && second.fullyContains(first));
	}

	@Test
	public void testRectangleToGdxConversionSupplier() {
		Rectangle first = new Rectangle(new Vector2(10f, 40f), new Vector2(20f, 30f));
		com.badlogic.gdx.math.Rectangle gdxRect = first.asGdxRect();
		Rectangle second = new Rectangle(() -> gdxRect);
		assertTrue(first.fullyContains(second) && second.fullyContains(first));
	}

	@Test
	public void testCirclePolyOverlap() {
		Vector2 polyPos = new Vector2();
		Vector2 circlePos = new Vector2();

		Polygon polygonHitbox = new Polygon(polyPos, new Vector2(1f, 1f), 0f, new float[]{
				0f, 0f,
				0f, 1f,
				1f, 0f
		});

		Circle circleHitbox = new Circle(circlePos, 1f);

		assertTrue(polygonHitbox.overlaps(circleHitbox));

		polyPos.set(1f, 1f);
		assertFalse(polygonHitbox.overlaps(circleHitbox));

		polyPos.set(0.5f, 0.5f);
		assertTrue(polygonHitbox.overlaps(circleHitbox));
	}

	@Test
	public void testAnnulusProject() {
		Annulus annulus = new Annulus(new Vector2(0f, 0f), 1f, 5f);
		Vector2 proj = new Vector2(6f, 0f);
		annulus.projectOnto(proj);
		assertEquals(5f, proj.x, 0.001f);
		assertEquals(0f, proj.y, 0.001f);

		annulus.projectOnto(proj);
		assertEquals(5f, proj.x, 0.001f);
		assertEquals(0f, proj.y, 0.001f);
	}

	@Test
	public void infiniteProjection() {
		Circle circle = new Circle(new Vector2(0f, 0f), 1f);
		Vector2 point = new Vector2(Float.POSITIVE_INFINITY, 0f);
		circle.projectOnto(point);
		assertEquals(new Vector2(1f, 0f), point);

		Rectangle rectangle = new Rectangle(new Vector2(0f, 0f), new Vector2(1f, 1f));
		point = new Vector2(Float.POSITIVE_INFINITY, 0f);
		rectangle.projectOnto(point);
		assertEquals(new Vector2(0.5f, 0f), point);
	}
}
