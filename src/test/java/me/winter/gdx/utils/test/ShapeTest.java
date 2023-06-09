package me.winter.gdx.utils.test;

import com.badlogic.gdx.math.Vector2;
import me.winter.gdx.utils.math.shape.*;
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

		assertTrue(rect1.contains(-9, 1));
		assertTrue(rect1.contains(-9, 19));
		assertTrue(rect1.contains(9, 19));
		assertTrue(rect1.contains(9, 1));
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
		assertEquals(proj.x, 5f, 0.001f);
		assertEquals(proj.y, 0f, 0.001f);

		annulus.projectOnto(proj);
		assertEquals(proj.x, 5f, 0.001f);
		assertEquals(proj.y, 0f, 0.001f);
	}

	@Test
	public void infiniteProjection() {
		Circle circle = new Circle(new Vector2(0f, 0f), 1f);
		Vector2 point = new Vector2(Float.POSITIVE_INFINITY, 0f);
		circle.projectOnto(point);
		System.out.println(point);

		Rectangle rectangle = new Rectangle(new Vector2(0f, 0f), new Vector2(1f, 1f));
		point = new Vector2(Float.POSITIVE_INFINITY, 0f);
		rectangle.projectOnto(point);
		System.out.println(point);
	}
}
