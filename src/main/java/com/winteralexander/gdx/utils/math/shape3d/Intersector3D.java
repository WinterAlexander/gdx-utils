package com.winteralexander.gdx.utils.math.shape3d;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.math.collision.Segment;
import com.winteralexander.gdx.utils.EnumConstantCache;

import static com.winteralexander.gdx.utils.math.MathUtil.pow2;
import static com.winteralexander.gdx.utils.math.shape3d.Intersector3D.LineIntersectionResult.*;
import static java.lang.Math.*;

/**
 * Extension of libGDX's {@link com.badlogic.gdx.math.Intersector} that adds support for some extra
 * intersection detection, specifically for 3D shapes. The functions of this class are not thread
 * safe.
 * <p>
 * Created on 2024-08-04.
 *
 * @author Alexander Winter
 */
public class Intersector3D {
	private static final Ray tmpIntersectRay = new Ray();
	private static final Ray tmpEdgeLine1 = new Ray(), tmpEdgeLine2 = new Ray(),
							 tmpEdgeLine3 = new Ray();
	private static final Vector3 tmpIntersection1 = new Vector3(), tmpIntersection2 = new Vector3(),
								 tmpIntersection3 = new Vector3();
	private static final SegmentPlus
			tmpSegment1 = new SegmentPlus(),
			tmpSegment2 = new SegmentPlus(), tmpSegmentOut = new SegmentPlus();
	private static final Vector3 tmpSegDir1 = new Vector3(), tmpSegDir2 = new Vector3(),
								 tmpVec1 = new Vector3(), tmpVec2 = new Vector3();
	private static final Triangle tmpTriangle = new Triangle();
	private static final Plane tmpPlane = new Plane();
	private static final Polygon
			tmpPolygon1 = new Polygon(new float[6]),
			tmpPolygon2 = new Polygon(new float[6]), tmpPolygon3 = new Polygon(new float[6]);

	private Intersector3D() {}

	/**
	 * Computes the intersection of 2 rays in 3D space. If there an infinite amount of intersections
	 * (same rays), returns {@link LineIntersectionResult#COLLINEAR} and the output is not set. The
	 * output is only modified when this function returns {@link LineIntersectionResult#POINT}.
	 *
	 * @param first first ray to find intersection
	 * @param second second ray to find intersection
	 * @param tolerance tolerance to use to determine if the rays intersect or not
	 * @param out output intersection point
	 * @return result of the intersection, which is either no intersection, a point or collinear
	 */
	public static LineIntersectionResult intersectRayRay(Ray first,
			Ray second,
			float tolerance,
			Vector3 out) {
		return intersectRayRay(first.origin,
				first.direction,
				second.origin,
				second.direction,
				tolerance,
				out);
	}

	/**
	 * @see #intersectRayRay(Ray, Ray, float, Vector3)
	 */
	public static LineIntersectionResult intersectRayRay(Vector3 origin1,
			Vector3 direction1,
			Vector3 origin2,
			Vector3 direction2,
			float tolerance,
			Vector3 out) {
		tmpVec1.setZero();
		tmpVec2.setZero();
		if(computeRayRaySquaredDistance(origin1, direction1, origin2, direction2, tmpVec1, tmpVec2)
				> pow2(tolerance))
			return NONE;

		if(Float.isInfinite(tmpVec1.x))
			return COLLINEAR;

		out.setZero().mulAdd(tmpVec1, 0.5f).mulAdd(tmpVec2, 0.5f);
		return POINT;
	}

	private static double computeRayRaySquaredDistance(Ray ray1,
			Ray ray2,
			Vector3 out1,
			Vector3 out2) {
		return computeRayRaySquaredDistance(ray1.origin,
				ray1.direction,
				ray2.origin,
				ray2.direction,
				out1,
				out2);
	}

	/**
	 * Computes the closest points between two rays and returns the squared distance between them
	 *
	 * @param origin1 origin of the first ray
	 * @param direction1 direction vector of the first ray
	 * @param origin2 origin of the second ray
	 * @param direction2 direction vector of the second ray
	 * @param out1 vector to be set to the closest point on the first ray
	 * @param out2 vector to be set to the closest point on the second ray
	 * @return squared distance between the two rays
	 */
	private static double computeRayRaySquaredDistance(Vector3 origin1,
			Vector3 direction1,
			Vector3 origin2,
			Vector3 direction2,
			Vector3 out1,
			Vector3 out2) {
		double sx = origin1.x - origin2.x;
		double sy = origin1.y - origin2.y;
		double sz = origin1.z - origin2.z;

		// cross product
		double denom1 = direction2.y * direction1.x - direction1.y * direction2.x;
		double denom2 = direction2.z * direction1.y - direction1.z * direction2.y;
		double denom3 = direction2.x * direction1.z - direction1.x * direction2.z;

		// means the ray directions are collinear
		if(pow2(denom1) + pow2(denom2) + pow2(denom3) <= MathUtils.FLOAT_ROUNDING_ERROR) {
			double crossX = sy * direction1.z - sz * direction1.y;
			double crossY = sz * direction1.x - sx * direction1.z;
			double crossZ = sx * direction1.y - sy * direction1.x;
			out1.set(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
			out2.set(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
			return (pow2(crossX) + pow2(crossY) + pow2(crossZ))
					/ (direction1.len2() + pow2(sx) + pow2(sy) + pow2(sz));
		}

		double t;
		// for the sake of precision, use the largest dominator for the computation
		if(abs(denom1) > max(abs(denom2), abs(denom3)))
			t = (sy * direction2.x - sx * direction2.y) / denom1;
		else if(abs(denom2) > abs(denom3))
			t = (sz * direction2.y - sy * direction2.z) / denom2;
		else
			t = (sx * direction2.z - sz * direction2.x) / denom3;

		double t2;
		// for the sake of precision, compute t2 from t using the largest component
		if(abs(direction2.x) > max(abs(direction2.y), abs(direction2.z)))
			t2 = t * direction1.x / direction2.x + sx / direction2.x;
		else if(abs(direction2.y) > abs(direction2.z))
			t2 = t * direction1.y / direction2.y + sy / direction2.y;
		else
			t2 = t * direction1.z / direction2.z + sz / direction2.z;

		double x1 = origin1.x + direction1.x * t;
		double y1 = origin1.y + direction1.y * t;
		double z1 = origin1.z + direction1.z * t;
		out1.set((float)x1, (float)y1, (float)z1);

		double x2 = origin2.x + direction2.x * t2;
		double y2 = origin2.y + direction2.y * t2;
		double z2 = origin2.z + direction2.z * t2;
		out2.set((float)x2, (float)y2, (float)z2);

		return pow2(x1 - x2) + pow2(y1 - y2) + pow2(z1 - z2);
	}

	/**
	 * Computes the intersection of 2 segments. The intersection can either be a point or another
	 * collinear segment. If the intersection is a point, it is set in the out parameter. If the
	 * segments are collinear but not intersecting, {@link LineIntersectionResult#NONE} is returned.
	 *
	 * @param first first segment to check for intersection
	 * @param second second segment to check for intersection
	 * @param tol tolerance to use for computations
	 * @param out intersection point if non-collinear intersection
	 * @return result of the intersection
	 */
	public static LineIntersectionResult intersectSegmentSegment(Segment first,
			Segment second,
			float tol,
			Vector3 out) {
		return intersectSegmentSegment(first.a, first.b, second.a, second.b, tol, out);
	}

	/**
	 * Computes the intersection of 2 segments. The intersection can either be a point or another
	 * collinear segment. If the intersection is a point or a segment, it is set in the out
	 * parameter. (points are represented by a segment of length 0) If the segments are collinear
	 * but not intersecting, {@link LineIntersectionResult#NONE} is returned.
	 *
	 * @param first first segment to check for intersection
	 * @param second second segment to check for intersection
	 * @param tol tolerance to use for computations
	 * @param out intersection segment or point if intersection
	 * @return result of the intersection
	 */
	public static LineIntersectionResult intersectSegmentSegment(Segment first,
			Segment second,
			float tol,
			Segment out) {
		return intersectSegmentSegment(first.a, first.b, second.a, second.b, tol, out);
	}

	/**
	 * @see #intersectSegmentSegment(Segment, Segment, float, Vector3)
	 */
	public static LineIntersectionResult intersectSegmentSegment(Vector3 firstStart,
			Vector3 firstEnd,
			Vector3 secondStart,
			Vector3 secondEnd,
			float tol,
			Vector3 out) {
		LineIntersectionResult result = intersectSegmentSegment(firstStart,
				firstEnd,
				secondStart,
				secondEnd,
				tol,
				tmpSegmentOut);
		if(result == POINT)
			out.set(tmpSegmentOut.a);
		return result;
	}

	/**
	 * @see #intersectSegmentSegment( Segment, Segment, float, Segment)
	 */
	public static LineIntersectionResult intersectSegmentSegment(Vector3 firstStart,
			Vector3 firstEnd,
			Vector3 secondStart,
			Vector3 secondEnd,
			float tol,
			Segment out) {
		LineIntersectionResult result = intersectRayRay(firstStart,
				tmpSegDir1.set(firstEnd).sub(firstStart).nor(),
				secondStart,
				tmpSegDir2.set(secondEnd).sub(secondStart).nor(),
				tol,
				tmpIntersection1);

		if(result == NONE)
			return NONE;

		if(result == COLLINEAR) {
			float len = firstStart.dst(firstEnd);
			tol /= len;
			float t1 = SegmentPlus.getParameter(firstStart, firstEnd, secondStart);
			float t2 = SegmentPlus.getParameter(firstStart, firstEnd, secondEnd);

			float tMin = min(t1, t2);
			float tMax = max(t1, t2);

			if(tMin - 1f > tol / len || tMax < -tol)
				return NONE;

			out.a.set(tMin < -tol ? firstStart : secondStart);
			out.b.set(tMax - 1f > tol ? firstEnd : secondEnd);

			return tMin - 1f < -tol && tMax > tol ? COLLINEAR : POINT;
		}

		float len1 = firstStart.dst(firstEnd);
		float len2 = secondStart.dst(secondEnd);
		float t1 = SegmentPlus.getParameter(firstStart, firstEnd, tmpIntersection1) * len1;
		float t2 = SegmentPlus.getParameter(secondStart, secondEnd, tmpIntersection1) * len2;

		if(t1 < -tol || t1 - len1 > tol || t2 < -tol || t2 - len2 > tol)
			return NONE;

		out.a.set(out.b.set(tmpIntersection1));
		return POINT;
	}

	/**
	 * @see #intersectTriangleTriangle(Triangle, Triangle, float, Segment)
	 */
	public static TriangleIntersectionResult intersectTriangleTriangle(Triangle first,
			Triangle second,
			float tol,
			Segment out) {
		return intersectTriangleTriangle(first, second, tol, false, out);
	}

	/**
	 * Performs triangle-triangle intersection. If the result is
	 * {@link TriangleIntersectionResult#NONE}, the segment output parameter is left untouched. If
	 * it is {@link TriangleIntersectionResult#POINT} both ends of the segment will be set to the
	 * intersection point. If the result is {@link TriangleIntersectionResult#COPLANAR_FACE_FACE}
	 * the output segment is left unset. In any other cases, the output segment is set to be the
	 * intersection of the 2 triangles as specified in the corresponding
	 * {@link TriangleIntersectionResult}.
	 *
	 * @param first first triangle
	 * @param second second triangle
	 * @param tol distance at which 2 points are considered to be the same
	 * @param ignoreCoplanar if true, coplanar triangles will be considered as non-intersecting,
	 * increasing performance as coplanar triangle intersection won't need to be checked
	 * @param out segment of the intersection, only set if applicable based on the result.
	 * @return result of the intersection
	 */
	public static TriangleIntersectionResult intersectTriangleTriangle(Triangle first,
			Triangle second,
			float tol,
			boolean ignoreCoplanar,
			Segment out) {
		// distance from the face1 vertices to the face2 plane
		float distFace1Vert1 = signedDistanceFromPlane(second, first.p1);
		float distFace1Vert2 = signedDistanceFromPlane(second, first.p2);
		float distFace1Vert3 = signedDistanceFromPlane(second, first.p3);

		// distances signs from the face1 vertices to the face2 plane
		int signFace1Vert1 = (distFace1Vert1 > tol ? 1 : (distFace1Vert1 < -tol ? -1 : 0));
		int signFace1Vert2 = (distFace1Vert2 > tol ? 1 : (distFace1Vert2 < -tol ? -1 : 0));
		int signFace1Vert3 = (distFace1Vert3 > tol ? 1 : (distFace1Vert3 < -tol ? -1 : 0));

		// if all points are on the same side of the plane
		if(signFace1Vert1 == signFace1Vert2 && signFace1Vert2 == signFace1Vert3) {
			// if they are all 0, they are all in the same plane
			if(signFace1Vert1 != 0 || ignoreCoplanar)
				return TriangleIntersectionResult.NONE;

			return intersectCoplanarTriangles(first, second, tol, out);
		}

		rayFromIntersection(first, second, tmpIntersectRay);

		if(!intersectTriangleRay(first, tmpIntersectRay, tol, tmpSegment1)
				|| !intersectTriangleRay(second, tmpIntersectRay, tol, tmpSegment2))
			return TriangleIntersectionResult.NONE;

		boolean firstIsEdge = !tmpSegment1.a.epsilonEquals(tmpSegment1.b, tol)
				&& (intersectSegmentSegment(tmpSegment1.a,
							tmpSegment1.b,
							first.p1,
							first.p2,
							tol,
							tmpIntersection1)
								== COLLINEAR
						|| intersectSegmentSegment(tmpSegment1.a,
								   tmpSegment1.b,
								   first.p2,
								   first.p3,
								   tol,
								   tmpIntersection1)
								== COLLINEAR
						|| intersectSegmentSegment(tmpSegment1.a,
								   tmpSegment1.b,
								   first.p3,
								   first.p1,
								   tol,
								   tmpIntersection1)
								== COLLINEAR);
		boolean secondIsEdge = !tmpSegment2.a.epsilonEquals(tmpSegment2.b, tol)
				&& (intersectSegmentSegment(tmpSegment2.a,
							tmpSegment2.b,
							second.p1,
							second.p2,
							tol,
							tmpIntersection2)
								== COLLINEAR
						|| intersectSegmentSegment(tmpSegment2.a,
								   tmpSegment2.b,
								   second.p2,
								   second.p3,
								   tol,
								   tmpIntersection2)
								== COLLINEAR
						|| intersectSegmentSegment(tmpSegment2.a,
								   tmpSegment2.b,
								   second.p3,
								   second.p1,
								   tol,
								   tmpIntersection2)
								== COLLINEAR);

		float dist1A = tmpIntersectRay.direction.dot(tmpSegment1.a.x - tmpIntersectRay.origin.x,
				tmpSegment1.a.y - tmpIntersectRay.origin.y,
				tmpSegment1.a.z - tmpIntersectRay.origin.z);
		float dist1B = tmpIntersectRay.direction.dot(tmpSegment1.b.x - tmpIntersectRay.origin.x,
				tmpSegment1.b.y - tmpIntersectRay.origin.y,
				tmpSegment1.b.z - tmpIntersectRay.origin.z);

		float dist2A = tmpIntersectRay.direction.dot(tmpSegment2.a.x - tmpIntersectRay.origin.x,
				tmpSegment2.a.y - tmpIntersectRay.origin.y,
				tmpSegment2.a.z - tmpIntersectRay.origin.z);
		float dist2B = tmpIntersectRay.direction.dot(tmpSegment2.b.x - tmpIntersectRay.origin.x,
				tmpSegment2.b.y - tmpIntersectRay.origin.y,
				tmpSegment2.b.z - tmpIntersectRay.origin.z);

		float startDist1 = min(dist1A, dist1B);
		float endDist1 = max(dist1A, dist1B);

		float startDist2 = min(dist2A, dist2B);
		float endDist2 = max(dist2A, dist2B);

		boolean intersection = endDist1 > startDist2 - tol && startDist1 < endDist2 + tol
				|| endDist2 > startDist1 - tol && startDist2 < endDist1 + tol;

		if(!intersection)
			return TriangleIntersectionResult.NONE;

		out.a.set(tmpIntersectRay.direction)
				.scl(max(startDist1, startDist2))
				.add(tmpIntersectRay.origin);
		out.b.set(tmpIntersectRay.direction)
				.scl(min(endDist1, endDist2))
				.add(tmpIntersectRay.origin);

		if(out.a.epsilonEquals(out.b, tol))
			return TriangleIntersectionResult.POINT;

		if(firstIsEdge && secondIsEdge)
			return TriangleIntersectionResult.EDGE_EDGE;

		if(firstIsEdge || secondIsEdge)
			return TriangleIntersectionResult.EDGE_FACE;

		return TriangleIntersectionResult.NONCOPLANAR_FACE_FACE;
	}

	/**
	 * Test whether 2 given co-planar triangles are intersecting, sharing an edge, touching at a
	 * point or not intersecting. This function assumes the provided triangles are co-planar and
	 * if they aren't, the result is undefined.
	 *
	 * @param first first triangle
	 * @param second second triangle
	 * @param tol distance at which 2 floating points are considered to be the same
	 * @param out segment of the intersection, only set if applicable based on the result.
	 * @return result of the intersection
	 */
	public static TriangleIntersectionResult intersectCoplanarTriangles(Triangle first,
			Triangle second,
			float tol,
			Segment out) {
		// if any pair of segments intersect at a point htat isn't their edges,
		// then the triangles must intersect
		if(anySegmentsIntersect(first, second, tol))
			return TriangleIntersectionResult.COPLANAR_FACE_FACE;

		// if all points of one triangle are inside the other (including on its edges), then it must
		// intersect
		if(inTriangle(first, second.p1, tol) && inTriangle(first, second.p2, tol)
						&& inTriangle(first, second.p3, tol)
				|| inTriangle(second, first.p1, tol) && inTriangle(second, first.p2, tol)
						&& inTriangle(second, first.p3, tol))
			return TriangleIntersectionResult.COPLANAR_FACE_FACE;

		// otherwise, all other cases are edges touching or points touching

		// for each side of each triangle, try to find collinear sides
		for(int i = 0; i < 3; i++) {
			Vector3 start = first.getPoint(i + 1);
			Vector3 end = first.getPoint((i + 1) % 3 + 1);
			for(int j = 0; j < 3; j++) {

				LineIntersectionResult result = intersectSegmentSegment(start,
						end,
						second.getPoint(j + 1),
						second.getPoint((j + 1) % 3 + 1),
						tol,
						tmpSegmentOut);

				if(result != COLLINEAR)
					continue;

				out.a.set(tmpSegmentOut.a);
				out.b.set(tmpSegmentOut.b);
				return TriangleIntersectionResult.EDGE_EDGE;
			}
		}

		// check for triangle corners matching other corners
		for(int i = 0; i < 3; i++) {
			Vector3 a = first.getPoint(i + 1);
			for(int j = 0; j < 3; j++) {
				Vector3 b = second.getPoint(j + 1);

				if(!a.epsilonEquals(b, tol))
					continue;

				out.a.set(out.b.set(a));
				return TriangleIntersectionResult.POINT;
			}
		}

		// look for corners of a triangle being on the edge of another
		for(int i = 0; i < 3; i++) {
			Vector3 a = first.getPoint(i + 1);
			Vector3 e1a = first.getPoint((i + 1) % 3 + 1);
			Vector3 e2a = first.getPoint((i + 2) % 3 + 1);
			for(int j = 0; j < 3; j++) {
				Vector3 b = second.getPoint(j + 1);
				Vector3 e1b = second.getPoint((j + 1) % 3 + 1);
				Vector3 e2b = second.getPoint((j + 2) % 3 + 1);

				if(intersectSegmentSegment(a, e1a, e1b, e2b, tol, tmpIntersection1) == POINT
						&& tmpIntersection1.epsilonEquals(a, tol)) {
					out.a.set(out.b.set(a));
					return TriangleIntersectionResult.POINT;
				}

				if(intersectSegmentSegment(e1a, e2a, b, e2b, tol, tmpIntersection1) == POINT
						&& tmpIntersection1.epsilonEquals(b, tol)) {
					out.a.set(out.b.set(b));
					return TriangleIntersectionResult.POINT;
				}
			}
		}

		return TriangleIntersectionResult.NONE;
	}

	private static void rayFromIntersection(Triangle first, Triangle second, Ray out) {
		Vector3 normalFace1 = first.getNormal();
		Vector3 normalFace2 = second.getNormal();

		// direction: cross product of the faces normals
		out.direction.set(normalFace1).crs(normalFace2);

		// getting a line point, zero is set to a coordinate whose direction
		// component isn't zero (line intersecting its origin plan)
		Vector3 v1p = first.p1;
		Vector3 v2p = second.p2;

		float d1 = -(normalFace1.x * v1p.x + normalFace1.y * v1p.y + normalFace1.z * v1p.z);
		float d2 = -(normalFace2.x * v2p.x + normalFace2.y * v2p.y + normalFace2.z * v2p.z);
		if(abs(out.direction.x) > max(abs(out.direction.y), abs(out.direction.z))) {
			out.origin.x = 0;
			out.origin.y = (d2 * normalFace1.z - d1 * normalFace2.z) / out.direction.x;
			out.origin.z = (d1 * normalFace2.y - d2 * normalFace1.y) / out.direction.x;
		} else if(abs(out.direction.y) > abs(out.direction.z)) {
			out.origin.x = (d1 * normalFace2.z - d2 * normalFace1.z) / out.direction.y;
			out.origin.y = 0;
			out.origin.z = (d2 * normalFace1.x - d1 * normalFace2.x) / out.direction.y;
		} else {
			out.origin.x = (d2 * normalFace1.y - d1 * normalFace2.y) / out.direction.z;
			out.origin.y = (d1 * normalFace2.x - d2 * normalFace1.x) / out.direction.z;
			out.origin.z = 0;
		}

		out.direction.nor();
	}

	/**
	 * Tests whether the given plane and the ray are coplanar
	 *
	 * @param plane plane to check
	 * @param ray ray to check
	 * @param tolerance tolerance to use for computations
	 * @return true if coplanar, otherwise false
	 */
	public static boolean areCoplanar(Plane plane, Ray ray, float tolerance) {
		return abs(plane.normal.dot(ray.direction)) <= tolerance
				&& abs(plane.normal.dot(ray.origin) + plane.getD()) <= tolerance;
	}

	/**
	 * Tests whether the given triangle and the ray are coplanar
	 *
	 * @param triangle triangle to check
	 * @param ray ray to check
	 * @param tolerance tolerance to use for computations
	 * @return true if coplanar, otherwise false
	 */
	public static boolean areCoplanar(Triangle triangle, Ray ray, float tolerance) {
		Vector3 normal = triangle.getNormal();
		return abs(normal.dot(ray.direction)) <= tolerance
				&& abs(normal.dot(ray.origin) - normal.dot(triangle.p1)) <= tolerance;
	}

	/**
	 * Given two coplanar triangles, compute the area of their overlapping region.
	 * Undefined if the provided triangles are not coplanar.
	 * @param first first triangle
	 * @param second second triangle
	 * @return area of their intersection
	 */
	public static float computeOverlapArea(Triangle first, Triangle second) {
		tmpPlane.set(first.p1, first.getNormal());
		tmpVec1.set(first.p1).sub(first.p2).nor();
		first.project(tmpPlane, tmpVec1, tmpPolygon1);
		second.project(tmpPlane, tmpVec1, tmpPolygon2);
		if(!Intersector.intersectPolygons(tmpPolygon1, tmpPolygon2, tmpPolygon3))
			return 0f;
		return tmpPolygon3.area();
	}

	/**
	 * Finds the intersection between a ray and a triangle. This intersection can be either a point
	 * or a segment of the ray, which only happens in the case where the ray and the triangle are
	 * co planar.
	 *
	 * @param triangle the triangle to check for intersection
	 * @param ray the ray to check for intersection
	 * @param tol tolerance to use for computations
	 * @param out segment of the intersection, both ends the same if the intersection is a point
	 * @return true if the triangle and the ray intersect, otherwise false
	 */
	public static boolean intersectTriangleRay(Triangle triangle, Ray ray, float tol, Segment out) {
		Vector3 normal = triangle.getNormal();
		float d = -normal.dot(triangle.p1);
		float denom = ray.direction.dot(normal);
		if(abs(denom) > MathUtils.FLOAT_ROUNDING_ERROR)
			// not coplanar, single point intersection
			return intersectTriangleRayNonCoplanar(triangle, ray, tol, out, normal, d, denom);

		if(abs(normal.dot(ray.origin) + d) > tol)
			return false; // parallel but not coplanar

		tmpEdgeLine1.origin.set(triangle.p1);
		tmpEdgeLine1.direction.set(triangle.p2).sub(triangle.p1).nor();

		tmpEdgeLine2.origin.set(triangle.p2);
		tmpEdgeLine2.direction.set(triangle.p3).sub(triangle.p2).nor();

		tmpEdgeLine3.origin.set(triangle.p3);
		tmpEdgeLine3.direction.set(triangle.p1).sub(triangle.p3).nor();

		int countIntersections = 0;

		LineIntersectionResult result1 = intersectRayRay(ray, tmpEdgeLine1, tol, tmpIntersection1);
		double dst1 = computeRayRaySquaredDistance(ray, tmpEdgeLine1, tmpVec1, tmpVec2);
		LineIntersectionResult result2 = intersectRayRay(ray, tmpEdgeLine2, tol, tmpIntersection2);
		double dst2 = computeRayRaySquaredDistance(ray, tmpEdgeLine2, tmpVec1, tmpVec2);
		LineIntersectionResult result3 = intersectRayRay(ray, tmpEdgeLine3, tol, tmpIntersection3);
		double dst3 = computeRayRaySquaredDistance(ray, tmpEdgeLine3, tmpVec1, tmpVec2);

		if((result1 == COLLINEAR ? 1 : 0) + (result2 == COLLINEAR ? 1 : 0)
						+ (result3 == COLLINEAR ? 1 : 0)
				> 1)
			throw new IllegalStateException("Multiple triangle edges collinear with ray");

		if(result1 == COLLINEAR) {
			out.a.set(triangle.p1);
			out.b.set(triangle.p2);
			return true;
		}

		if(result2 == COLLINEAR) {
			out.a.set(triangle.p2);
			out.b.set(triangle.p3);
			return true;
		}

		if(result3 == COLLINEAR) {
			out.a.set(triangle.p3);
			out.b.set(triangle.p1);
			return true;
		}

		if(result1 == POINT) {
			float t = tmpEdgeLine1.direction.dot(tmpIntersection1.x - tmpEdgeLine1.origin.x,
					tmpIntersection1.y - tmpEdgeLine1.origin.y,
					tmpIntersection1.z - tmpEdgeLine1.origin.z);
			float tEnd = tmpEdgeLine1.direction.dot(triangle.p2.x - tmpEdgeLine1.origin.x,
					triangle.p2.y - tmpEdgeLine1.origin.y,
					triangle.p2.z - tmpEdgeLine1.origin.z);

			if(t < -tol || t - tEnd > tol)
				result1 = NONE;
		}

		if(result2 == POINT) {
			float t = tmpEdgeLine2.direction.dot(tmpIntersection2.x - tmpEdgeLine2.origin.x,
					tmpIntersection2.y - tmpEdgeLine2.origin.y,
					tmpIntersection2.z - tmpEdgeLine2.origin.z);
			float tEnd = tmpEdgeLine2.direction.dot(triangle.p3.x - tmpEdgeLine2.origin.x,
					triangle.p3.y - tmpEdgeLine2.origin.y,
					triangle.p3.z - tmpEdgeLine2.origin.z);

			if(t < -tol || t - tEnd > tol)
				result2 = NONE;
		}

		if(result3 == POINT) {
			float t = tmpEdgeLine3.direction.dot(tmpIntersection3.x - tmpEdgeLine3.origin.x,
					tmpIntersection3.y - tmpEdgeLine3.origin.y,
					tmpIntersection3.z - tmpEdgeLine3.origin.z);
			float tEnd = tmpEdgeLine3.direction.dot(triangle.p1.x - tmpEdgeLine3.origin.x,
					triangle.p1.y - tmpEdgeLine3.origin.y,
					triangle.p1.z - tmpEdgeLine3.origin.z);

			if(t < -tol || t - tEnd > tol)
				result3 = NONE;
		}

		// in this case then the ray hits a corner
		if(result1 == POINT && result2 == POINT && result3 == POINT) {
			float dst12 = tmpIntersection1.dst2(tmpIntersection2);
			float dst23 = tmpIntersection2.dst2(tmpIntersection3);
			float dst31 = tmpIntersection3.dst2(tmpIntersection1);
			// find the 2 points closest (most likely to be a corner)

			if(dst12 < dst23 && dst12 < dst31) {
				if(dst1 > dst2) // find the worst of the 2 points on the ray in terms of distance to
								// the ray
					result1 = NONE;
				else
					result2 = NONE;
			} else if(dst23 < dst31) {
				if(dst2 > dst3)
					result2 = NONE;
				else
					result3 = NONE;
			} else {
				if(dst3 > dst1)
					result3 = NONE;
				else
					result1 = NONE;
			}
		}

		if(result1 == POINT) {
			out.a.set(tmpIntersection1);
			countIntersections++;
		}

		if(result2 == POINT) {
			(countIntersections == 0 ? out.a : out.b).set(tmpIntersection2);
			countIntersections++;

			if(countIntersections == 2)
				return true;
		}

		if(result3 == POINT) {
			(countIntersections == 0 ? out.a : out.b).set(tmpIntersection3);
			countIntersections++;

			if(countIntersections == 2)
				return true;
		}

		if(countIntersections == 0)
			return false;

		out.b.set(out.a);
		return true;
	}

	public static boolean inTriangle(Triangle triangle, Vector3 point, float tol) {
		tmpIntersectRay.set(point, triangle.getNormal());
		if(intersectTriangleRay(triangle, tmpIntersectRay, tol, tmpSegmentOut))
			return true;
		tmpIntersectRay.direction.scl(-1f);
		return intersectTriangleRay(triangle, tmpIntersectRay, tol, tmpSegmentOut);
	}

	private static boolean intersectTriangleRayNonCoplanar(Triangle triangle,
			Ray ray,
			float tol,
			Segment out,
			Vector3 normal,
			float d,
			float denom) {
		float t = -(ray.origin.dot(normal) + d) / denom;
		if(t < -tol)
			return false;

		tmpSegDir1.set(triangle.p2).sub(triangle.p1);
		tmpSegDir2.set(tmpSegDir1).crs(normal);

		float len2 = tmpSegDir1.len2();
		float h2 = tmpSegDir2.dot(triangle.p3.x - triangle.p1.x,
				triangle.p3.y - triangle.p1.y,
				triangle.p3.z - triangle.p1.z);

		float x = ray.origin.x + ray.direction.x * t;
		float y = ray.origin.y + ray.direction.y * t;
		float z = ray.origin.z + ray.direction.z * t;

		float pU = tmpSegDir1.dot(x - triangle.p1.x, y - triangle.p1.y, z - triangle.p1.z) / len2;
		float pV = tmpSegDir2.dot(x - triangle.p1.x, y - triangle.p1.y, z - triangle.p1.z) / h2;

		float p3U = tmpSegDir1.dot(triangle.p3.x - triangle.p1.x,
							triangle.p3.y - triangle.p1.y,
							triangle.p3.z - triangle.p1.z)
				/ len2;

		if(!inTriangle(pU, pV, p3U, tol / (float)Math.sqrt(Math.abs(Math.min(len2, h2)))))
			return false;

		out.b.set(out.a.set(x, y, z));
		return true;
	}

	private static boolean inTriangle(float pU, float pV, float p3U, float tol) {
		// check pV > 0 (point is above the floor), applies to all cases
		if(pV < -tol)
			return false;

		if(p3U < tol) {
			if(p3U > -tol)
				// .
				// |\
				// ._\
				// this is for the case where p3U is on top of p1 so close to 0, in this case
				// check pU > 0 and pV < 1 - pU (under the diagonal)
				return pU >= -tol && pV - (1f - pU) <= tol;

			// ._
			//  \ - _
			//   \____- .
			// this is for the case where p3U is to the left of p1, in this case check
			// for both diagonals
			return pV - pU / p3U >= tol && pV - (1f - pU) / (1f - p3U) <= tol;
		}

		if(pU < -tol || pV - pU / p3U > tol)
			return false;

		if(Math.abs(p3U - 1f) > tol)
			return (p3U < 1f ? 1f : -1f) * (pV - (1 - pU) / (1 - p3U)) <= tol;

		return pU - 1f <= tol;
	}

	/**
	 * Test whether 2 given co-planar triangles' edges are intersecting or not. This function
	 * assumes the provided triangles are co-planar and if they aren't, the result is undefined.
	 * Any intersections not in the middle of two segments are ignored
	 *
	 * @param first first triangle to check
	 * @param second second triangle to check
	 * @param tol distance at which 2 floating points are considered to be the same
	 * @return true if they are intersecting, otherwise false
	 */
	public static boolean anySegmentsIntersect(Triangle first, Triangle second, float tol) {
		for(int i = 1; i <= 3; i++)
			for(int j = 1; j <= 3; j++)
				if(intersectSegmentSegment(tmpSegment1.set(first.getPoint(i),
												   first.getPoint((i % 3) + 1)),
						   tmpSegment2.set(second.getPoint(j), second.getPoint((j % 3) + 1)),
						   tol,
						   tmpSegmentOut)
								== POINT
						// crossing that is not happening at the edges
						&& !tmpSegmentOut.a.epsilonEquals(tmpSegment1.a, tol)
						&& !tmpSegmentOut.b.epsilonEquals(tmpSegment1.b, tol)
						&& !tmpSegmentOut.a.epsilonEquals(tmpSegment2.a, tol)
						&& !tmpSegmentOut.b.epsilonEquals(tmpSegment2.b, tol))
					return true;

		return false;
	}

	private static float signedDistanceFromPlane(Triangle triangle, Vector3 point) {
		Vector3 normal = triangle.getNormal();
		float a = normal.x;
		float b = normal.y;
		float c = normal.z;
		Vector3 v1 = triangle.p1;
		float d = -(a * v1.x + b * v1.y + c * v1.z);
		return a * point.x + b * point.y + c * point.z + d;
	}

	/**
	 * Result of a Line-Line intersection where the lines can either be rays or segments
	 */
	public enum LineIntersectionResult {
		/**
		 * Result when the lines do not intersect
		 */
		NONE,

		/**
		 * Result when the lines are intersecting but collinear
		 */
		COLLINEAR,

		/**
		 * Result when the lines are intersecting at a single point
		 */
		POINT;

		public static final LineIntersectionResult[] values = EnumConstantCache.store(values());
	}

	/**
	 * Result of a Triangle-Triangle intersection
	 */
	public enum TriangleIntersectionResult {
		/**
		 * Result when the triangles do not intersect.
		 */
		NONE,

		/**
		 * Result when the triangles only intersect at a single point.
		 */
		POINT,

		/**
		 * Result when the triangles share part of an edge.
		 * In this case the intersection is defined by a line segment which corresponds to the part
		 * of the edges that intersect.
		 */
		EDGE_EDGE,

		/**
		 * Result when one triangle's edge intersects the other triangle's face.
		 * In this case the intersection is defined by a line segment which corresponds to the part
		 * of the edge that intersect with the face.
		 */
		EDGE_FACE,

		/**
		 * Result when the 2 triangles are coplanar and their faces overlap.
		 * In this case the intersection is defined by a polygon which corresponds to the shared
		 * area of both triangles.
		 */
		COPLANAR_FACE_FACE,

		/**
		 * Result when the 2 triangles are not coplanar and their faces cross.
		 * In this case the intersection is defined by a line segment which corresponds to the
		 * line at which the 2 triangles are crossing.
		 */
		NONCOPLANAR_FACE_FACE;

		public static final TriangleIntersectionResult[] values = EnumConstantCache.store(values());
	}
}