package com.winteralexander.gdx.utils.test.math.shape2d;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.winteralexander.gdx.utils.math.shape3d.PlaneUtil;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

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
		float spaceScale = 1000f;

		for(int j = 0; j < points.length; j++)
			points[j] = new Vector3();

		for(int i = 0; i < 10; i++) {
			plane.set(random.nextFloat(), random.nextFloat(), random.nextFloat(), random.nextFloat() * spaceScale - spaceScale / 2f);
			plane.normal.nor();
			transform.idt().translate(random.nextFloat() * spaceScale - spaceScale / 2f,
							random.nextFloat() * spaceScale - spaceScale / 2f,
							random.nextFloat() * spaceScale - spaceScale / 2f)
					.scl(random.nextFloat(), random.nextFloat(), random.nextFloat())
					.rotateRad(random.nextFloat(), random.nextFloat(), random.nextFloat(), random.nextFloat() * (float)Math.PI * 2f);

			for(int j = 0; j < points.length; j++) {
				points[j].set(random.nextFloat() * spaceScale - spaceScale / 2f,
						random.nextFloat() * spaceScale - spaceScale / 2f,
						random.nextFloat() * spaceScale - spaceScale / 2f);
				sides[j] = plane.testPoint(points[j]);
			}

			PlaneUtil.mul(plane, transform);

			for(int j = 0; j < points.length; j++) {
				points[j].mul(transform);
				Plane.PlaneSide newSide = plane.testPoint(points[j]);
				assertEquals(sides[j], newSide);
			}
		}
	}
}
