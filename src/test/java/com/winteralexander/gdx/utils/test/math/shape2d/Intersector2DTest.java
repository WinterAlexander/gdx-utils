package com.winteralexander.gdx.utils.test.math.shape2d;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.winteralexander.gdx.utils.math.MathUtil;
import com.winteralexander.gdx.utils.math.shape2d.Intersector2D;
import com.winteralexander.gdx.utils.test.math.TestVec2;
import org.junit.Test;

import static com.winteralexander.gdx.utils.math.shape2d.Intersector2D.*;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link Intersector2D}
 * <p>
 * Created on 2025-09-16.
 *
 * @author Alexander Winter
 */
public class Intersector2DTest {

	@Test
	public void testProjectToRectEdge() {
		assertEquals(new TestVec2(5f, 0f), projectToRectEdge(0f, 10f, 20f, new TestVec2()));
		assertEquals(new TestVec2(5f, 5f), projectToRectEdge(45f, 10f, 10f, new TestVec2()));
		assertEquals(new TestVec2(0f, 10f), projectToRectEdge(90f, 10f, 20f, new TestVec2()));
		assertEquals(new TestVec2(-6f, 6f), projectToRectEdge(135f, 12f, 12f, new TestVec2()));
		assertEquals(new TestVec2(-6f, 6f), projectToRectEdge(-225f, 12f, 12f, new TestVec2()));
		assertEquals(new TestVec2(0f, -7f), projectToRectEdge(-90f, 10f, 14f, new TestVec2()));
		assertEquals(new TestVec2(0f, -7f), projectToRectEdge(270f, 10f, 14f, new TestVec2()));
		assertEquals(new TestVec2(-5f, 0f), projectToRectEdge(-180f, 10f, 14f, new TestVec2()));
		assertEquals(new TestVec2(-5f, 0f), projectToRectEdge(180f, 10f, 14f, new TestVec2()));
	}

	@Test
	public void testLineIntersectsLine() {
		assertTrue(lineIntersectsLine(0f, 0f, 5f, 5f, 0f, 5f, 5f, 0f));
		assertTrue(lineIntersectsLine(0f, 5f, 5f, 0f, 0f, 0f, 5f, 5f));

		assertFalse(lineIntersectsLine(-1f, -1f, 1f, 1f, 0f, 2f, 2f, 0f));
		assertFalse(lineIntersectsLine(-1f, -1f, 1f, 1f, 0f, 0f, 1f, -1f));
	}

	@Test
	public void testComputeOverlap() {
		Rectangle rect1 = new Rectangle(0f, 0f, 10f, 10f);
		Rectangle rect2 = new Rectangle(5f, 5f, 10f, 10f);
		Vector2 overlap = new Vector2();

		computeOverlap(rect1, rect2, overlap);
		assertEquals(5f, overlap.x, 1e-10f);
		assertEquals(5f, overlap.y, 1e-10f);

		computeOverlap(rect2, rect1, overlap);
		assertEquals(5f, overlap.x, 1e-10f);
		assertEquals(5f, overlap.y, 1e-10f);

		rect2.set(9f, 8f, 2f, 1f);
		computeOverlap(rect1, rect2, overlap);
		assertEquals(1f, overlap.x, 1e-10f);
		assertEquals(1f, overlap.y, 1e-10f);

		computeOverlap(rect2, rect1, overlap);
		assertEquals(1f, overlap.x, 1e-10f);
		assertEquals(1f, overlap.y, 1e-10f);

		rect1.set(-2f, 1f, 4f, 4f);
		rect2.set(1f, -3f, 4f, 8f);
		computeOverlap(rect1, rect2, overlap);
		assertEquals(1f, overlap.x, 1e-10f);
		assertEquals(4f, overlap.y, 1e-10f);

		computeOverlap(rect2, rect1, overlap);
		assertEquals(1f, overlap.x, 1e-10f);
		assertEquals(4f, overlap.y, 1e-10f);

		rect1.set(0f, 0f, 4f, 4f);
		rect2.set(10f, -2f, 4f, 4f);
		computeOverlap(rect1, rect2, overlap);
		assertEquals(0f, overlap.x, 1e-10f);
		assertEquals(0f, overlap.y, 1e-10f);

		computeOverlap(rect1, rect2, overlap);
		assertEquals(0f, overlap.x, 1e-10f);
		assertEquals(0f, overlap.y, 1e-10f);

		rect1.set(0f, 0f, 4f, 4f);
		rect2.set(0f, -20f, 4f, 4f);
		computeOverlap(rect1, rect2, overlap);
		assertEquals(0f, overlap.x, 1e-10f);
		assertEquals(0f, overlap.y, 1e-10f);

		computeOverlap(rect1, rect2, overlap);
		assertEquals(0f, overlap.x, 1e-10f);
		assertEquals(0f, overlap.y, 1e-10f);
	}

	@Test
	public void testAABBToAABBDistanceTouching() {
		Rectangle rect1 = new Rectangle();
		Rectangle rect2 = new Rectangle();

		rect1.x = 0f;
		rect1.y = 0f;
		rect1.width = 100f;
		rect1.height = 20f;

		rect2.x = 15f;
		rect2.y = 10f;
		rect2.width = 1f;
		rect2.height = 1f;
		assertEquals(0f, Intersector2D.distanceSquaredAABBToAABB(rect1, rect2), 1e-10f);
		assertEquals(0f, Intersector2D.distanceSquaredAABBToAABB(rect2, rect1), 1e-10f);

		rect2.width = 100f;
		rect2.height = 100f;
		assertEquals(0f, Intersector2D.distanceSquaredAABBToAABB(rect1, rect2), 1e-10f);
		assertEquals(0f, Intersector2D.distanceSquaredAABBToAABB(rect2, rect1), 1e-10f);

		rect2.x = 100f;
		rect2.y = 10f;
		assertEquals(0f, Intersector2D.distanceSquaredAABBToAABB(rect1, rect2), 1e-10f);
		assertEquals(0f, Intersector2D.distanceSquaredAABBToAABB(rect2, rect1), 1e-10f);

		rect2.x = -100f;
		rect2.y = 10f;
		assertEquals(0f, Intersector2D.distanceSquaredAABBToAABB(rect1, rect2), 1e-10f);
		assertEquals(0f, Intersector2D.distanceSquaredAABBToAABB(rect2, rect1), 1e-10f);

		rect2.x = 50f;
		rect2.y = 20f;
		assertEquals(0f, Intersector2D.distanceSquaredAABBToAABB(rect1, rect2), 1e-10f);
		assertEquals(0f, Intersector2D.distanceSquaredAABBToAABB(rect2, rect1), 1e-10f);

		rect2.x = 50f;
		rect2.y = -100f;
		assertEquals(0f, Intersector2D.distanceSquaredAABBToAABB(rect1, rect2), 1e-10f);
		assertEquals(0f, Intersector2D.distanceSquaredAABBToAABB(rect2, rect1), 1e-10f);
	}

	@Test
	public void testAABBToAABBDistanceDisjoint() {
		Rectangle rect1 = new Rectangle();
		Rectangle rect2 = new Rectangle();

		rect1.x = 0f;
		rect1.y = 0f;
		rect1.width = 100f;
		rect1.height = 20f;

		rect2.x = 115f;
		rect2.y = 10f;
		rect2.width = 10f;
		rect2.height = 100f;
		assertEquals(MathUtil.pow2(15f),
				Intersector2D.distanceSquaredAABBToAABB(rect1, rect2), 1e-10f);
		assertEquals(MathUtil.pow2(15f),
				Intersector2D.distanceSquaredAABBToAABB(rect2, rect1), 1e-10f);


		rect2.x = 115f;
		rect2.y = 30f;
		rect2.width = 30f;
		rect2.height = 100f;
		assertEquals(MathUtil.pow2(15f) + MathUtil.pow2(10f),
				Intersector2D.distanceSquaredAABBToAABB(rect1, rect2), 1e-10f);
		assertEquals(MathUtil.pow2(15f) + MathUtil.pow2(10f),
				Intersector2D.distanceSquaredAABBToAABB(rect2, rect1), 1e-10f);

	}
}
