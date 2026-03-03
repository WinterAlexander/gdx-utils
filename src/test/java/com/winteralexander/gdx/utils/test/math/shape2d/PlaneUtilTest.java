package com.winteralexander.gdx.utils.test.math.shape2d;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.winteralexander.gdx.utils.math.shape3d.PlaneUtil;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link PlaneUtil}
 * <p>
 * Created on 2025-09-16.
 *
 * @author Alexander Winter
 */
public class PlaneUtilTest {
	@Test
	public void testPlaneMul() {
		Plane plane = new Plane();
		Matrix4 transform = new Matrix4();
		Random random = new Random();
		Vector3[] points = new Vector3[1000];
		Plane.PlaneSide[] sides = new Plane.PlaneSide[points.length];
		float spaceScale = 100f;
		float epsilon = 0.001f;
		Vector3 randDir = new Vector3();

		for(int j = 0; j < points.length; j++)
			points[j] = new Vector3();

		for(int i = 0; i < 10_000; i++) {
			plane.set(random.nextFloat(), random.nextFloat(), random.nextFloat(),
					random.nextFloat() * spaceScale - spaceScale / 2f);
			plane.normal.nor();
			randDir.set(random.nextFloat(), random.nextFloat(), random.nextFloat());
			randDir.nor();
			if(randDir.len2() == 0f)
				randDir.set(1f, 0f, 0f);
			transform.idt().translate(random.nextFloat() * spaceScale - spaceScale / 2f,
							random.nextFloat() * spaceScale - spaceScale / 2f,
							random.nextFloat() * spaceScale - spaceScale / 2f)
					.scl(random.nextFloat() + 1f, random.nextFloat() + 1f, random.nextFloat() + 1f)
					.rotateRad(randDir.x, randDir.y, randDir.z,
							random.nextFloat() * (float)Math.PI * 2f);

			for(int j = 0; j < points.length; j++) {
				points[j].set(random.nextFloat() * spaceScale - spaceScale / 2f,
						random.nextFloat() * spaceScale - spaceScale / 2f,
						random.nextFloat() * spaceScale - spaceScale / 2f);
				float dst = plane.distance(points[j]);
				sides[j] = dst < epsilon ? Plane.PlaneSide.OnPlane : plane.testPoint(points[j]);
			}

			PlaneUtil.mul(plane, transform);

			for(int j = 0; j < points.length; j++) {
				points[j].mul(transform);

				if(sides[j] == Plane.PlaneSide.OnPlane)
					continue;

				Plane.PlaneSide newSide = plane.testPoint(points[j]);
				assertEquals(sides[j], newSide);
			}
		}
	}

	@Test
	public void testPlaneMulSpecificCase() {
		Plane plane1 = new Plane(new Vector3(0f, 1f, 0f), 0.4f);
		Plane plane2 = new Plane(new Vector3(0f, 1f, 0f), -5.6f);
		Matrix4 matrix4 = new Matrix4();
		matrix4.set(new float[] {
				0.84984475f, 0.0f, 0.0f, 0.0f,
				0.0f, 0.8380602f, 0.8337162f, 0.83205044f,
				0.0f, 1.257091f, -0.55581045f, -0.55469996f,
				0.0f, -9.386282f, 6.724224f, 8.708791f
		});

		PlaneUtil.mul(plane1, matrix4);
		PlaneUtil.mul(plane2, matrix4);

		assertTrue(plane1.normal.epsilonEquals(plane2.normal, 0.01f));
	}
}
