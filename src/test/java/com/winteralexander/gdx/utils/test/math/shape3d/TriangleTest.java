package com.winteralexander.gdx.utils.test.math.shape3d;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.winteralexander.gdx.utils.math.shape3d.Triangle;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the features of {@link Triangle}
 * <p>
 * Created on 2024-08-19.
 *
 * @author Alexander Winter
 */
public class TriangleTest {
	@Test
	public void testBarycentricCoordinates() {
		Triangle triangle = new Triangle();
		Vector3 point = new Vector3();
		Vector3 expected = new Vector3();

		triangle.p1.set(0f, 0f, 0f);
		triangle.p2.set(2f, 0f, 0f);
		triangle.p3.set(0f, 2f, 0f);

		point.set(triangle.p1);
		expected.set(1f, 0f, 0f);
		assertEquals(expected, triangle.getBarycentricCoordinates(point));

		point.set(triangle.p2);
		expected.set(0f, 1f, 0f);
		assertEquals(expected, triangle.getBarycentricCoordinates(point));

		point.set(triangle.p3);
		expected.set(0f, 0f, 1f);
		assertEquals(expected, triangle.getBarycentricCoordinates(point));

		point.set(triangle.p1).scl(0.5f).mulAdd(triangle.p2, 0.5f);
		expected.set(0.5f, 0.5f, 0f);
		assertEquals(expected, triangle.getBarycentricCoordinates(point));

		triangle.p1.set(0f, 0f, 0f);
		triangle.p2.set(0f, 0f, 2f);
		triangle.p3.set(0f, 2f, 0f);

		point.set(triangle.p3);
		expected.set(0f, 0f, 1f);
		assertEquals(expected, triangle.getBarycentricCoordinates(point));

		Random random = new Random();

		for(int i = 0; i < 1000; i++) {
			triangle.p1.set(random.nextFloat() * 10f - 5f,
					random.nextFloat() * 10f - 5f,
					random.nextFloat() * 10f - 5f);
			do {
				triangle.p2.set(random.nextFloat() * 10f - 5f,
						random.nextFloat() * 10f - 5f,
						random.nextFloat() * 10f - 5f);
			} while(triangle.p2.dst2(triangle.p1) < 1e-3f);
			do {
				triangle.p3.set(random.nextFloat() * 10f - 5f,
						random.nextFloat() * 10f - 5f,
						random.nextFloat() * 10f - 5f);
			} while(triangle.p3.dst2(triangle.p1) < 1e-3f
				|| triangle.p3.dst2(triangle.p2) < 1e-3f);

			float alpha = random.nextFloat();
			float beta = random.nextFloat() * (1f - alpha);
			float gamma = 1f - alpha - beta;

			point.set(triangle.p1).scl(alpha).mulAdd(triangle.p2, beta).mulAdd(triangle.p3, gamma);
			expected.set(alpha, beta, gamma);
			assertTrue(expected.epsilonEquals(triangle.getBarycentricCoordinates(point), 1e-5f));
		}
	}

	@Test
	public void testArea() {
		Triangle triangle = new Triangle();

		triangle.p1.set(0f, 0f, 0f);
		triangle.p2.set(2f, 0f, 0f);
		triangle.p3.set(0f, 2f, 0f);

		assertEquals(2f, triangle.getArea(), 0.01f);

		triangle.p1.set(-1f, 0f, 0f);
		triangle.p2.set(2f, 0f, 0f);
		triangle.p3.set(0f, 2f, 0f);

		assertEquals(3f, triangle.getArea(), 0.01f);
	}

	@Test
	public void testAreaPlane() {
		Triangle triangle = new Triangle();
		Plane plane = new Plane();

		triangle.p1.set(0f, 0f, 1f);
		triangle.p2.set(2f, 0f, 2f);
		triangle.p3.set(0f, 2f, 3f);
		plane.set(0f, 0f, 1f, 5f);

		assertEquals(2f, triangle.getArea(plane), 0.01f);

		triangle.p1.set(-1f, 0f, -1000f);
		triangle.p2.set(2f, 0f, 10f);
		triangle.p3.set(0f, 2f, 0f);

		assertEquals(3f, triangle.getArea(plane), 0.01f);
	}

	@Test
	public void testAreaRotationTranslationInvariant() {

		Triangle triangle = new Triangle();
		Random random = new Random();
		Matrix4 transform = new Matrix4();

		for(int i = 0; i < 1000; i++) {
			triangle.p1.set(0f, 0f, 0f);
			triangle.p2.set(0f, 0f, 6f);
			triangle.p3.set(0f, 8f, 0f);
			transform.idt().translate(random.nextFloat() * 100f - 50f,
					random.nextFloat() * 100f - 50f,
					random.nextFloat() * 100f - 50f)
					.rotate(random.nextFloat(), random.nextFloat(), random.nextFloat(), random.nextFloat() * 360f);

			triangle.p1.mul(transform);
			triangle.p2.mul(transform);
			triangle.p3.mul(transform);

			assertEquals(24f, triangle.getArea(), 0.01f);
		}
	}
}
