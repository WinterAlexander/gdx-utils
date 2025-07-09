package com.winteralexander.gdx.utils.test.math;

import com.winteralexander.gdx.utils.math.direction.GridCorner;
import com.winteralexander.gdx.utils.math.direction.GridDirection4;
import com.winteralexander.gdx.utils.math.direction.GridDirection8;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for {@link GridDirection4}
 * <p>
 * Created on 2024-11-21.
 *
 * @author Alexander Winter
 */
public class GridDirectionTest {
	@Test
	public void testGridDirection4Vectors() {
		assertEquals(1f, GridDirection4.RIGHT.asVector().x, 0f);
		assertEquals(0f, GridDirection4.RIGHT.asVector().y, 0f);

		assertEquals(0f, GridDirection4.UP.asVector().x, 0f);
		assertEquals(1f, GridDirection4.UP.asVector().y, 0f);

		assertEquals(-1f, GridDirection4.LEFT.asVector().x, 0f);
		assertEquals(0f, GridDirection4.LEFT.asVector().y, 0f);

		assertEquals(0f, GridDirection4.DOWN.asVector().x, 0f);
		assertEquals(-1f, GridDirection4.DOWN.asVector().y, 0f);

		assertEquals(1, GridDirection4.RIGHT.x());
		assertEquals(0, GridDirection4.RIGHT.y());

		assertEquals(0, GridDirection4.UP.x());
		assertEquals(1, GridDirection4.UP.y());

		assertEquals(-1, GridDirection4.LEFT.x());
		assertEquals(0, GridDirection4.LEFT.y());

		assertEquals(0, GridDirection4.DOWN.x());
		assertEquals(-1, GridDirection4.DOWN.y());
	}

	@Test
	public void testGridCornerVectors() {
		assertEquals(-1f, GridCorner.UP_LEFT.asVector().x, 0f);
		assertEquals(1f, GridCorner.UP_LEFT.asVector().y, 0f);
		assertEquals(1f, GridCorner.UP_RIGHT.asVector().x, 0f);
		assertEquals(1f, GridCorner.UP_RIGHT.asVector().y, 0f);
		assertEquals(-1f, GridCorner.DOWN_LEFT.asVector().x, 0f);
		assertEquals(-1f, GridCorner.DOWN_LEFT.asVector().y, 0f);
		assertEquals(1f, GridCorner.DOWN_RIGHT.asVector().x, 0f);
		assertEquals(-1f, GridCorner.DOWN_RIGHT.asVector().y, 0f);

		assertEquals(-1, GridCorner.UP_LEFT.x());
		assertEquals(1, GridCorner.UP_LEFT.y());

		assertEquals(1, GridCorner.UP_RIGHT.x());
		assertEquals(1, GridCorner.UP_RIGHT.y());

		assertEquals(-1, GridCorner.DOWN_LEFT.x());
		assertEquals(-1, GridCorner.DOWN_LEFT.y());

		assertEquals(1, GridCorner.DOWN_RIGHT.x());
		assertEquals(-1, GridCorner.DOWN_RIGHT.y());
	}

	@Test
	public void testGridDirection8Vectors() {
		assertEquals(1f, GridDirection8.RIGHT.asVector().x, 0f);
		assertEquals(0f, GridDirection8.RIGHT.asVector().y, 0f);

		assertEquals(0f, GridDirection8.UP.asVector().x, 0f);
		assertEquals(1f, GridDirection8.UP.asVector().y, 0f);

		assertEquals(-1f, GridDirection8.LEFT.asVector().x, 0f);
		assertEquals(0f, GridDirection8.LEFT.asVector().y, 0f);

		assertEquals(0f, GridDirection8.DOWN.asVector().x, 0f);
		assertEquals(-1f, GridDirection8.DOWN.asVector().y, 0f);

		assertEquals(-1f, GridDirection8.UP_LEFT.asVector().x, 0f);
		assertEquals(1f, GridDirection8.UP_LEFT.asVector().y, 0f);

		assertEquals(1f, GridDirection8.UP_RIGHT.asVector().x, 0f);
		assertEquals(1f, GridDirection8.UP_RIGHT.asVector().y, 0f);

		assertEquals(-1f, GridDirection8.DOWN_LEFT.asVector().x, 0f);
		assertEquals(-1f, GridDirection8.DOWN_LEFT.asVector().y, 0f);

		assertEquals(1f, GridDirection8.DOWN_RIGHT.asVector().x, 0f);
		assertEquals(-1f, GridDirection8.DOWN_RIGHT.asVector().y, 0f);

		assertEquals(1, GridDirection8.RIGHT.x());
		assertEquals(0, GridDirection8.RIGHT.y());

		assertEquals(0, GridDirection8.UP.x());
		assertEquals(1, GridDirection8.UP.y());

		assertEquals(-1, GridDirection8.LEFT.x());
		assertEquals(0, GridDirection8.LEFT.y());

		assertEquals(0, GridDirection8.DOWN.x());
		assertEquals(-1, GridDirection8.DOWN.y());

		assertEquals(-1, GridDirection8.UP_LEFT.x());
		assertEquals(1, GridDirection8.UP_LEFT.y());

		assertEquals(1, GridDirection8.UP_RIGHT.x());
		assertEquals(1, GridDirection8.UP_RIGHT.y());

		assertEquals(-1, GridDirection8.DOWN_LEFT.x());
		assertEquals(-1, GridDirection8.DOWN_LEFT.y());

		assertEquals(1, GridDirection8.DOWN_RIGHT.x());
		assertEquals(-1, GridDirection8.DOWN_RIGHT.y());
	}

	@Test
	public void testClosestDirection() {
		assertEquals(GridDirection4.RIGHT, GridDirection4.closestDirection(10f, 0f));
		assertEquals(GridDirection4.RIGHT, GridDirection4.closestDirection(10f, 1f));
		assertEquals(GridDirection4.RIGHT, GridDirection4.closestDirection(10f, 4f));
		assertEquals(GridDirection4.RIGHT, GridDirection4.closestDirection(10f, 9f));
		assertEquals(GridDirection4.RIGHT, GridDirection4.closestDirection(10f, -9f));

		assertEquals(GridDirection4.UP, GridDirection4.closestDirection(10f, 11f));
		assertEquals(GridDirection4.DOWN, GridDirection4.closestDirection(10f, -11f));

		assertEquals(GridDirection4.LEFT, GridDirection4.closestDirection(-10f, 1f));

		assertEquals(GridDirection8.RIGHT, GridDirection8.closestDirection(10f, 0f));
		assertEquals(GridDirection8.RIGHT, GridDirection8.closestDirection(10f, 1f));
		assertEquals(GridDirection8.RIGHT, GridDirection8.closestDirection(10f, 4f));
		assertEquals(GridDirection8.UP_RIGHT, GridDirection8.closestDirection(10f, 9f));
		assertEquals(GridDirection8.DOWN_RIGHT, GridDirection8.closestDirection(10f, -9f));

		assertEquals(GridDirection8.UP_RIGHT, GridDirection8.closestDirection(10f, 11f));
		assertEquals(GridDirection8.DOWN_RIGHT, GridDirection8.closestDirection(10f, -11f));

		assertEquals(GridDirection8.LEFT, GridDirection8.closestDirection(-10f, 1f));
	}

	@Test
	public void testOppositesAreOpposites() {
		for(GridDirection4 dir : GridDirection4.values)
			assertEquals("GridDirection4." + dir + " is not opposite to GridDirection4." + dir.opposite(),
					-1f,
					dir.opposite().asVector().dot(dir.asVector()),
					0.0001f);

		for(GridDirection8 dir : GridDirection8.values)
			assertEquals("GridDirection8." + dir + " is not opposite to GridDirection8." + dir.opposite(),
					-1f,
					dir.opposite().asNormal().dot(dir.asNormal()),
					0.0001f);

		for(GridCorner dir : GridCorner.values)
			assertEquals("GridCorner." + dir + " is not opposite to GridCorner." + dir.opposite(),
					-1f,
					dir.opposite().asNormal().dot(dir.asNormal()),
					0.0001f);
	}

	@Test
	public void test90RotationAreOrthogonal() {
		for(GridDirection4 dir : GridDirection4.values) {
			assertEquals(0f, dir.nextClockwise().asVector().dot(dir.asVector()), 0.0001f);
			assertEquals(0f, dir.nextCounterClockwise().asVector().dot(dir.asVector()), 0.0001f);
		}

		for(GridDirection8 dir : GridDirection8.values) {
			assertEquals(0f, dir.nextClockwiseOrthogonal().asVector().dot(dir.asVector()), 0.0001f);
			assertEquals(0f, dir.nextCounterClockwiseOrthogonal().asVector().dot(dir.asVector()), 0.0001f);
			assertEquals(0f, dir.nextClockwise().nextClockwise().asVector().dot(dir.asVector()), 0.0001f);
			assertEquals(0f, dir.nextCounterClockwise().nextCounterClockwise().asVector().dot(dir.asVector()), 0.0001f);
		}

		for(GridCorner dir : GridCorner.values) {
			assertEquals(0f, dir.nextClockwise().asVector().dot(dir.asVector()), 0.0001f);
			assertEquals(0f, dir.nextCounterClockwise().asVector().dot(dir.asVector()), 0.0001f);
		}
	}
}
