package com.winteralexander.gdx.utils.test.math.shape3d;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.math.collision.Segment;
import com.badlogic.gdx.utils.Array;
import com.winteralexander.gdx.utils.math.shape3d.Intersector3D;
import com.winteralexander.gdx.utils.math.shape3d.Intersector3D.LineIntersectionResult;
import com.winteralexander.gdx.utils.math.shape3d.SegmentPlus;
import com.winteralexander.gdx.utils.math.shape3d.Triangle;
import com.winteralexander.gdx.utils.test.debugviewer.TriangleViewer;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Random;

import static com.winteralexander.gdx.utils.math.shape3d.Intersector3D.*;
import static com.winteralexander.gdx.utils.math.shape3d.Intersector3D.LineIntersectionResult.*;
import static com.winteralexander.gdx.utils.math.shape3d.Intersector3D.TriangleIntersectionResult.COPLANAR_FACE_FACE;
import static com.winteralexander.gdx.utils.math.shape3d.Intersector3D.TriangleIntersectionResult.EDGE_EDGE;
import static com.winteralexander.gdx.utils.math.shape3d.Intersector3D.TriangleIntersectionResult.EDGE_FACE;
import static com.winteralexander.gdx.utils.math.shape3d.Intersector3D.TriangleIntersectionResult.NONCOPLANAR_FACE_FACE;
import static org.junit.Assert.*;

/**
 * Unit test for {@link Intersector3D}
 * <p>
 * Created on 2024-08-04.
 *
 * @author Alexander Winter
 */
public class Intersector3DTest {
	@Test
	public void testRayRayIntersection() {
		Ray ray1 = new Ray();
		ray1.origin.set(0f, 0f, 0f);
		ray1.direction.set(1f, 1f, 1f).nor();

		Ray ray2 = new Ray();
		ray2.origin.set(15f, 15f, 15f);
		ray2.direction.set(-9f, 10f, 55f).nor();

		Vector3 intersection = new Vector3();

		assertEquals(POINT, intersectRayRay(ray1, ray2, 1e-5f, intersection));
		assertTrue(intersection.epsilonEquals(15f, 15f, 15f, 1e-5f));

		ray1.origin.set(0f, 0f, 0f);
		ray1.direction.set(-61f, 31f, 12f).nor();

		ray2.origin.set(15f, 15f, 15f);
		ray2.direction.set(-9f, 10f, 55f).nor();

		LineIntersectionResult result = intersectRayRay(ray1, ray2, 1e-5f, intersection);
		assertEquals(NONE, result);
	}

	@Test
	public void testRayRayCollinear() {
		Ray ray1 = new Ray();
		ray1.origin.set(0f, 0f, 0f);
		ray1.direction.set(1f, 1f, 1f).nor();

		Ray ray2 = new Ray();
		ray2.origin.set(15f, 15f, 15f);
		ray2.direction.set(-1f, -1f, -1f).nor();

		Vector3 intersection = new Vector3();

		assertEquals(COLLINEAR, intersectRayRay(ray1, ray2, 1e-5f, intersection));
	}

	@Test
	public void testRayRayAdversarialCase() {
		// this test case reproduced a precision issue with a prior version of the algorithm
		// remains present to ensure good functioning of the new algorithm
		Ray ray1 = new Ray();
		ray1.origin.set(0.9067098f, -0.8748553f, 0.0036551952f);
		ray1.direction.set(-0.26122704f, 0.6468483f, -0.71648294f);

		Ray ray2 = new Ray();
		ray2.origin.set(0.80948985f, -1.0571202f, -0.2630019f);
		ray2.direction.set(-0.3424806f, 0.01793293f, -0.93935376f);

		Vector3 expectedIntersection = new Vector3(0.9840071f, -1.0662583f, 0.21566316f);

		Vector3 intersection = new Vector3();

		assertEquals(POINT,
				intersectRayRay(ray1, ray2, 1e-4f, intersection));

		float precision = 0f;

		precision = Math.max(precision, Math.abs(expectedIntersection.x - intersection.x));
		precision = Math.max(precision, Math.abs(expectedIntersection.y - intersection.y));
		precision = Math.max(precision, Math.abs(expectedIntersection.z - intersection.z));

		System.out.println("Precision: " + precision);

		if(precision > 1e-6f)
			fail("Computed intersection point has too poor precision");
	}

	@Test
	public void testRayRayIntersectionRandom() {
		Ray ray1 = new Ray();
		Ray ray2 = new Ray();
		Random r = new Random();
		Vector3 tmpIntersection = new Vector3();
		Vector3 tmpComputedIntersection = new Vector3();
		float worstPrecision = 0f;

		for(int i = 0; i < 100_000; i++) {
			ray1.origin.set(r.nextFloat() * 2f - 1f,
					r.nextFloat() * 2f - 1f,
					r.nextFloat() * 2f - 1f);
			do
				ray1.direction.set(r.nextFloat() * 2f - 1f,
						r.nextFloat() * 2f - 1f,
						r.nextFloat() * 2f - 1f).nor();
			while(ray1.direction.len2() == 0f);

			do
				ray2.direction.set(r.nextFloat() * 2f - 1f,
						r.nextFloat() * 2f - 1f,
						r.nextFloat() * 2f - 1f).nor();
			while(ray2.direction.len2() == 0f
					|| Math.abs(ray2.direction.dot(ray1.direction)) > 0.90f);

			float posRay1 = r.nextFloat() * 2f - 1f;
			float posRay2 = r.nextFloat() * 2f - 1f;
			ray1.getEndPoint(tmpIntersection, posRay1);
			ray2.origin.set(tmpIntersection);
			ray2.origin.mulAdd(ray2.direction, posRay2);

			assertEquals(POINT,
					intersectRayRay(ray1, ray2, 1e-6f, tmpComputedIntersection));

			float precision = 0f;

			precision = Math.max(precision, Math.abs(tmpIntersection.x - tmpComputedIntersection.x));
			precision = Math.max(precision, Math.abs(tmpIntersection.y - tmpComputedIntersection.y));
			precision = Math.max(precision, Math.abs(tmpIntersection.z - tmpComputedIntersection.z));
			worstPrecision = Math.max(worstPrecision, precision);

			if(precision > 1e-5f)
				fail("Computed intersection point has too poor precision " + precision);
		}

		System.out.println("Worst precision: " + worstPrecision);
	}
	@Test
	public void testSegmentSegmentCollinearRandom() {
		Ray ray = new Ray();
		SegmentPlus segment1 = new SegmentPlus();
		SegmentPlus segment2 = new SegmentPlus();
		Random r = new Random();
		Vector3 tmpIntersection = new Vector3();

		for(int i = 0; i < 100_0000; i++) {
			ray.origin.set(r.nextFloat() * 2f - 1f,
					r.nextFloat() * 2f - 1f,
					r.nextFloat() * 2f - 1f);
			do
				ray.direction.set(r.nextFloat() * 2f - 1f,
						r.nextFloat() * 2f - 1f,
						r.nextFloat() * 2f - 1f).nor();
			while(ray.direction.len2() == 0f);

			boolean shouldIntersect = r.nextBoolean();

			float startRay1 = r.nextFloat() * 2f;
			float endRay1 = startRay1 + r.nextFloat() * 2f + 1f;
			float startRay2 = endRay1 + r.nextFloat() * 2f + 0.01f;
			if(shouldIntersect) {
				float t = r.nextFloat() * 0.8f + 0.1f;
				startRay2 = startRay1 * (1f - t) + endRay1 * t;
			}
			float endRay2 = startRay2 + r.nextFloat() * 2f + 0.01f;
			segment1.a.set(ray.getEndPoint(tmpIntersection, startRay1));
			segment1.b.set(ray.getEndPoint(tmpIntersection, endRay1));
			segment2.a.set(ray.getEndPoint(tmpIntersection, startRay2));
			segment2.b.set(ray.getEndPoint(tmpIntersection, endRay2));

			LineIntersectionResult result = intersectSegmentSegment(segment1, segment2, 1e-5f, tmpIntersection);
			LineIntersectionResult expected = shouldIntersect
					? COLLINEAR
					: NONE;

			assertEquals(expected, result);
		}

	}

	@Test
	public void testSegmentSegmentIntersection() {
		Segment segment1 = new Segment(0f, 0f, 0f, 1f, 1f, 0f);
		Segment segment2 = new Segment(0f, 1f, 0f, 1f, 0f, 0f);

		Vector3 intersection = new Vector3();

		assertEquals(POINT, intersectSegmentSegment(segment1, segment2, 1e-5f, intersection));
		assertTrue(intersection.epsilonEquals(0.5f, 0.5f, 0f, 1e-5f));

		segment1.a.set(0f, 0f, 0f);
		segment1.b.set(1f, 1f, 0f);

		segment2.a.set(0f, 5f, 0f);
		segment2.b.set(5f, 0f, 0f);

		assertEquals(NONE, intersectSegmentSegment(segment1,
				segment2, 1e-5f, intersection));

		segment1.a.set(0f, 0f, 0f);
		segment1.b.set(1f, 1f, 0f);

		segment2.a.set(0.5f, 0.5f, 0f);
		segment2.b.set(6f, 6f, 0f);

		assertEquals(COLLINEAR, intersectSegmentSegment(segment1,
				segment2, 1e-5f, intersection));

		segment1.a.set(0f, 0f, 0f);
		segment1.b.set(1f, 1f, 0f);

		segment2.a.set(5f, 5f, 0f);
		segment2.b.set(6f, 6f, 0f);

		assertEquals(NONE, intersectSegmentSegment(segment1,
				segment2, 1e-5f, intersection));

		segment1.a.set(-0.5f, 0.45f, -0.4f);
		segment1.b.set(-0.5f, 0.45f, -0.35499996f);

		segment2.a.set(-0.5f, 0.45f, -0.35499996f);
		segment2.b.set(-0.5f, 0.45f, -0.4f);

		assertEquals(COLLINEAR, intersectSegmentSegment(segment1, segment2, 1e-5f, intersection));

		segment1.a.set(-0.5f, -0.19999999f, 0.4f);
		segment1.b.set(-1.0f, 0.8f, 0.4f);

		segment2.a.set(0.5f, -0.19999999f, 0.4f);
		segment2.b.set(0.0f, -0.19999999f, 0.4f);

		assertEquals(NONE, intersectSegmentSegment(segment1, segment2, 1e-5f, intersection));
	}

	@Test
	public void testTriangleRay() {
		Triangle triangle = new Triangle(0f, 0f, 0f,
				0f, 1f, 0f,
				0f, 1f, 1f);

		Ray ray = new Ray();
		ray.origin.set(0f, 0.5f, 0f);
		ray.direction.set(0f, 0f, 1f);

		Segment segment = new SegmentPlus();

		assertTrue(intersectTriangleRay(triangle, ray, 1e-5f, segment));

		assertTrue(segment.a.epsilonEquals(0.0f, 0.5f, 0.0f));
		assertTrue(segment.b.epsilonEquals(0.0f, 0.5f, 0.5f));

		ray.origin.set(-1f, 0.5f, 0.25f);
		ray.direction.set(1f, 0f, 0f);

		//TriangleViewer.start(new Triangle[]{ triangle }, new Ray[]{ ray });

		assertTrue(intersectTriangleRay(triangle, ray, 1e-5f, segment));

		assertTrue(segment.a.epsilonEquals(0.0f, 0.5f, 0.25f));
		assertTrue(segment.b.epsilonEquals(0.0f, 0.5f, 0.25f));

		ray.origin.set(-1f, 0.5f, 0.25f);
		ray.direction.set(0f, 1f, 0f);

		assertFalse(intersectTriangleRay(triangle, ray, 1e-5f, segment));

		triangle.p1.set(-0.2828202f, 0.5f, -0.2828202f);
		triangle.p2.set(-0.28282905f, 0.5f, -0.28279895f);
		triangle.p3.set(0.28278846f, 0.5f, 0.282897f);

		ray.origin.set(0.0f, 0.5f, -0.4f);
		ray.direction.set(0.92391634f, 0.0f, -0.38259482f);

		//TriangleViewer.start(new Triangle[]{ triangle }, new Ray[]{ ray });

		// super thin triangle case
		//assertTrue(IntersectorPlus.intersectTriangleRay(triangle, ray, 1e-5f, segment));

		triangle.set(0.4089327f, -0.4089327f, 0.5f,
				0.5f, -0.5f, 0.5f,
				2.9802322E-8f, 0.5f, 0.5f);

		ray.set(0.0f, 0.39349112f, 0.5f, 0.4540586f, -0.8909718f, 0.0f);

		//TriangleViewer.start(new Triangle[]{ triangle }, new Ray[]{ ray });

		assertTrue(intersectTriangleRay(triangle, ray, 1e-6f, segment));

		triangle.set(-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f);
		ray.set(0.6545632f, 0.5f, 0.9755105f, 0.0f, 1.0f, 0.0f);
		//TriangleViewer.start(new Triangle[]{ triangle }, new Ray[]{ ray });
		assertFalse(intersectTriangleRay(triangle, ray, 1e-5f, segment));

		triangle.set(0.22423086f, -0.07930406f, -0.079304114f,
				0.20224862f, -0.14695412f, -0.99999994f,
				0.25f, -7.1054274E-15f, -1.1920929E-7f);
		ray.set(0.23374009f, -0.20375702f, -0.12407229f, 0.0f, 1.0f, 0.0f);
		//TriangleViewer.start(new Triangle[]{ triangle }, new Ray[]{ ray });
		assertTrue(intersectTriangleRay(triangle, ray, 1e-5f, segment));
	}

	@Test
	public void testCoplanarTriangleIntersection() {
		Triangle tri1 = new Triangle(
				0f, 0f, 0f,
				0f, 1f, 0f,
				0f, 0f, 1f
		);

		Triangle tri2 = new Triangle(
				0f, 1f, 1f,
				0f, -0.5f, 1f,
				0f, 1f, -0.5f
		);

		assertTrue(intersectCoplanarTriangles(tri1, tri2, 1e-5f));

		tri2.set(0f, 1f, 1f,
				0f, 0.2f, 1f,
				0f, 1f, 0.2f);

		assertFalse(intersectCoplanarTriangles(tri1, tri2, 1e-5f));

		tri2.set(0f, 1f, 1f,
				0f, 0f, 1f,
				0f, 1f, 0f);

		assertTrue(intersectCoplanarTriangles(tri1, tri2, 1e-5f));

		tri2.set(0f, 0f, 0f,
				0f, 0f, -1f,
				0f, -1f, 0f);

		assertTrue(intersectCoplanarTriangles(tri1, tri2, 1e-5f));

		tri2.set(tri1);

		assertTrue(intersectCoplanarTriangles(tri1, tri2, 1e-5f));

		tri1.set(-1.0f, -0.19999999f, -0.4f, -1.0f, 0.8f, -0.4f, 0.0f, 0.8f, -0.4f);
		tri2.set(0.5f, -0.19999999f, -0.4f, 0.5f, 0.5f, -0.4f, 0.0f, -0.19999999f, -0.4f);

		assertFalse(intersectCoplanarTriangles(tri1, tri2, 1e-5f));

		tri1.set(-1.0f, 0.8f, 0.4f, -1.0f, -0.19999999f, 0.4f, -0.5f, -0.19999999f, 0.4f);
		tri2.set(0.0f, 0.5f, 0.4f, 0.5f, -0.19999999f, 0.4f, 0.0f, -0.19999999f, 0.4f);

		assertFalse(intersectCoplanarTriangles(tri1, tri2, 1e-5f));
	}

	@Test
	public void testTriangleTriangle() throws InterruptedException {
		Triangle tri1 = new Triangle(), tri2 = new Triangle();

		SegmentPlus segment = new SegmentPlus(), expected = new SegmentPlus();

		tri1.set(0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f);
		tri2.set(0.49824142f, 0.0f, 0.0f, 0.14803512f, 1.0f, 0.0f, 0.14803512f, 0.0f, 1.0f);

		assertEquals(TriangleIntersectionResult.NONE, intersectTriangleTriangle(tri1, tri2, 1e-5f, segment));

		tri1.set(0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f);
		tri2.set(0.15553457f, 0.0f, 0.1826436f,
				1.0338287f, 1.0f, 0.1826436f,
				1.0338287f, 0.0f, 1.1826437f);

		assertEquals(TriangleIntersectionResult.NONE, intersectTriangleTriangle(tri1, tri2, 1e-5f, segment));

		tri1.set(2f, 2f, 0.9423616295572568f,
				0.9685134704003172f, 2f, 0.9678422992674797f,
				2f, 1.124710354419025f, 1.068692504586136f);
		tri2.set(2.5f, 1.624710354419025f, 1.568692504586136f,
				2.5f, 2.5f, 1.442361629557257f,
				1.588259113885977f, 2.5f, 0.5f);

		assertEquals(TriangleIntersectionResult.NONE, intersectTriangleTriangle(tri1, tri2, 1e-5f, segment));

		expected.a.set(0.0f, 0.100607894f, 0.77444464f);
		expected.b.set(0.0f, 0.45588672f, 0.4191658f);
		tri1.set(0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f);
		tri2.set(-0.18178494f, 0.100607894f, 0.41916582f,
				0.3298834f, 1.1006078f, 0.41916582f,
				0.3298834f, 0.100607894f, 1.4191657f);

		assertEquals(NONCOPLANAR_FACE_FACE, intersectTriangleTriangle(tri1, tri2, 1e-5f, segment));
		assertTrue(expected.epsilonEquals(segment, 1e-5f));

		tri1.set(0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f);
		tri2.set(-4.5360066E-4f, 1.5718032f, 0.0f,
				0.9995464f, 0.571803f, 0.0f,
				0.9995464f, 1.5718032f, 0.0f);
		assertEquals(TriangleIntersectionResult.NONE, intersectTriangleTriangle(tri1, tri2, 1e-5f, segment));

		tri1.set(0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f);
		tri2.set(-0.14996159f, 0.83515376f, 0.14882556f,
				0.85003835f, -0.16484623f, 0.14882556f,
				0.85003835f, 0.83515376f, 0.14882556f);

		assertEquals(NONCOPLANAR_FACE_FACE, intersectTriangleTriangle(tri1, tri2, 1e-5f, segment));

		tri1.set(0.013628483f, 0.44765303f, 0.396547f, 0.013628453f, 0.49999997f, 0.5f, 0.013628453f, 0.5523469f, 0.5f);
		tri2.set(-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f);

		//TriangleViewer.start(new Triangle[]{ tri1, tri2 }, new Ray[]{});

		assertEquals(NONCOPLANAR_FACE_FACE, intersectTriangleTriangle(tri1, tri2, 1e-5f, segment));
	}

	@Test
	public void testTriangleTriangleEdgeCase() throws InterruptedException {
		Triangle tri1 = new Triangle(), tri2 = new Triangle();
		tri1.set(new Vector3(-0.3f,0.3f,0.5f),
				new Vector3(-0.20710671f,0.3f,0.5f),
				new Vector3(-0.20710675f,0.33049607f,0.5f));
		tri2.set(new Vector3(-0.70710677f,1.3f,5.9604645E-8f),
				new Vector3(-0.70710677f,0.3f,5.9604645E-8f),
				new Vector3(5.9604645E-8f,0.3f,0.70710677f));

		SegmentPlus segment = new SegmentPlus();

		assertEquals(EDGE_FACE, intersectTriangleTriangle(tri1, tri2, 1e-5f, segment));

		tri1.set(new Vector3(1.08245f, 0.0f,1.7132657f), new Vector3(1.1869159f, 0.0f, 1.7459112f), new Vector3(1.231845f, 0.0f, 1.7132657f));
		tri2.set(new Vector3(1.6067458f, 0.1f, 1.4408622f), new Vector3(1.2318448f, 0.1f, 1.7132657f), new Vector3(1.2318448f, -0.1f, 1.7132657f));

		assertEquals(TriangleIntersectionResult.POINT,
				intersectTriangleTriangle(tri1, tri2, 1e-5f, segment));
	}

	@Test
	public void rayTriangleSinglePointNonCoplanar() throws InterruptedException {
		Triangle tri = new Triangle(new Vector3(-0.70710677f, 1.3f, 5.9604645E-8f),
				new Vector3(5.9604645E-8f, 1.3f, 0.70710677f),
				new Vector3(0.70710677f, 1.3f, -5.9604645E-8f));

		Ray ray = new Ray(new Vector3(-0.5f, 0.5f,-0.5f),
				new Vector3(0.0f, 1.0f, 0.0f));

		SegmentPlus segment = new SegmentPlus();
		assertFalse(intersectTriangleRay(tri, ray, 1e-5f, segment));

		tri.p1.set(0.20710674f, 0.3f, -0.5f);
		tri.p2.set(0.035533965f, 0.3f, 0.5f);
		tri.p3.set(4.214685E-8f, 0.3f, 0.5f);

		ray.set(-0.5f, -0.5f, -0.5f, 0f, 1f, 0f);

		//TriangleViewer.start(new Triangle[]{ tri }, new Ray[]{ ray });

		assertFalse(intersectTriangleRay(tri, ray, 1e-5f, segment));

		tri.p1.set(-0.20710674f, 0.3f, -0.5f);
		tri.p2.set(0.035533965f, 0.3f, 0.5f);
		tri.p3.set(4.214685e-8f, 0.3f, 0.5f);

		ray.set(0.5f, 0.5f, -0.5f, 0f, 1f, 0f);

		assertFalse(intersectTriangleRay(tri, ray, 1e-5f, segment));

		tri.set(new Vector3(-0.09085471f,0.3955028f,-0.2795128f),
				new Vector3(-0.23777002f,0.3955028f,-0.17276402f),
				new Vector3(-0.327236f,0.50609183f,-0.23777011f));
		ray.set(-0.31519663f, 0.5f, -0.23935616f, 0f, 1f, 0f);

		//TriangleViewer.start(new Triangle[]{ tri }, new Ray[]{ ray });

		assertTrue(intersectTriangleRay(tri, ray, 1e-5f, segment));

		tri.set(0.090854734f, 0.3955028f, 0.2795128f,
				0.23777005f, 0.3955028f, 0.17276399f,
				0.32723603f, 0.50609183f, 0.23777007f);

		ray.set(0.32230777f, 0.5f, 0.23418921f, 0f, 1f, 0f);
		//TriangleViewer.start(new Triangle[]{ tri }, new Ray[]{ ray });

		assertTrue(intersectTriangleRay(tri, ray, 1e-5f, segment));
	}

	@Test
	public void testCoplanarIssue() {
		Triangle tri1 = new Triangle(new Vector3(0.0f, -0.19999999f, 0.5f),
				new Vector3(0.5f, -0.19999999f, 0.5f),
				new Vector3(0.0f, 0.5f, 0.5f));
		Triangle tri2 = new Triangle(new Vector3(0.0f, 0.5f, 0.5f),
				new Vector3(0.0f, -0.19999999f, 0.5f),
				new Vector3(0.5f, -0.19999999f, 0.5f));
		Segment out = new SegmentPlus();

		assertEquals(COPLANAR_FACE_FACE, intersectTriangleTriangle(tri1, tri2, 1e-5f, out));

		tri1.set(new Vector3(0.0f, -0.19999999f, -0.5f), new Vector3(0.0f, -0.19999999f, 0.5f), new Vector3(-0.5f, -0.19999999f, 0.5f));
		tri2.set(new Vector3(-0.5f, -0.19999999f, 0.5f), new Vector3(0.5f, -0.5f, 0.5f), new Vector3(0.5f, -0.19999999f, 0.5f));

		assertEquals(EDGE_EDGE, intersectTriangleTriangle(tri1, tri2, 1e-5f, out));

		tri2.set(new Vector3(0.5f, -0.19999999f, 0.5f), new Vector3(0.5f, -0.19999999f, -0.5f), new Vector3(0.0f, -0.19999999f, 0.5f));

		assertEquals(TriangleIntersectionResult.POINT,
				intersectTriangleTriangle(tri1, tri2, 1e-5f, out));
	}

	@Test
	@Ignore
	public void testBadSplit() {
		Triangle tri = new Triangle(1.375f, 0.0f, 0.625f, 2.0f, 0.0f, 0.625f, 1.375f, 0.0f, 0.0f);
		Plane plane = new Plane();
		plane.normal.set(0.58781636f, -0.0f, -0.8089944f);
		plane.d = -0.49213207f;
		float[] arr = new float[9];
		tri.toArray(arr);

		Intersector.SplitTriangle splitTriangle = new Intersector.SplitTriangle(3);

		Intersector.splitTriangle(arr, plane, splitTriangle);

		Array<Triangle> tris = new Array<>();

		for(int i = 0; i < splitTriangle.numBack; i++) {
			Triangle newTri = new Triangle();
			newTri.set(splitTriangle.back, i * 9);
			tris.add(newTri);
		}

		for(int i = 0; i < splitTriangle.numFront; i++) {
			Triangle newTri = new Triangle();
			newTri.set(splitTriangle.front, i * 9);
			tris.add(newTri);
		}

		TriangleViewer.start(tri,
				tris.get(0),
				tris.get(1),
				tris.get(2),
				new Ray(plane.getNormal().cpy().scl(-plane.getD()), plane.getNormal().cpy().crs(0f, 1f, 0f)),
				new Ray(plane.getNormal().cpy().scl(-plane.getD()), plane.getNormal().cpy().crs(plane.getNormal().cpy().crs(0f, 1f, 0f))));
	}

	@Test
	public void testTrianglePointIntersection() {
		Triangle tri1 = new Triangle(-0.5f, 0.5f, -0.4209333f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f);
		Triangle tri2 = new Triangle(-0.49999997f, -0.19087355f, -0.45090222f,
				-0.5f, -0.34360337f, -0.48763424f,
				-0.5f, -0.49999994f, 0.5f);

		Segment out = new SegmentPlus();
		//TriangleViewer.start(tri1, tri2);

		assertEquals(TriangleIntersectionResult.POINT,
				intersectTriangleTriangle(tri1, tri2, 1e-5f, out));
	}

	@Test
	public void testTriangleEdgeIntersection() {
		Triangle tri1 = new Triangle(0.49999994f,-0.5f,-0.5f, 0.42672676f,-0.5f,-0.4267268f, -0.5f,-0.49999994f,-0.5f);
		Triangle tri2 = new Triangle(-0.5f,-0.5f,-0.5f, -0.3436038f,-0.5f,-0.48769438f, -0.5f,-0.5f,0.5f);

		Segment out = new SegmentPlus();
		//TriangleViewer.start(tri1, tri2);

		assertEquals(EDGE_EDGE,
				intersectTriangleTriangle(tri1, tri2, 1e-4f, out));

		tri1 = new Triangle(-0.34360343f,-0.49999994f,-0.48763424f, 0.42672676f,-0.5f,-0.4267268f, 0.3264776f,-0.5f,-0.32647762f);
		tri2 = new Triangle(-0.3436038f,-0.5f,-0.48769438f, -0.19087377f,-0.5f,-0.45102096f, -0.5f,-0.5f,0.5f);
		//TriangleViewer.start(tri1, tri2);

		assertEquals(EDGE_EDGE,
				intersectTriangleTriangle(tri1, tri2, 1e-4f, out));

	}

	@Test
	public void testFailingCollinearSegmentIntersection() {
		Segment segment1 = new SegmentPlus(0.42672676f, -0.5f, -0.4267268f, -0.5f,-0.49999994f,-0.5f);
		Segment segment2 = new SegmentPlus(-0.5f, -0.5f, -0.5f, -0.3436038f, -0.5f, -0.48769438f);

		Vector3 out = new Vector3();

		assertEquals(COLLINEAR, intersectSegmentSegment(segment1, segment2, 1e-4f, out));
	}
}