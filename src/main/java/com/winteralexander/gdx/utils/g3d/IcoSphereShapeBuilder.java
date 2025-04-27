package com.winteralexander.gdx.utils.g3d;

import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BaseShapeBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.IntIntMap;
import com.badlogic.gdx.utils.ShortArray;

import static com.badlogic.gdx.math.MathUtils.*;
import static com.winteralexander.gdx.utils.BufferUtil.*;
import static com.winteralexander.gdx.utils.math.MathUtil.pow2;
import static java.lang.Math.sqrt;

/**
 * Helper class with static methods to build sphere shapes using {@link MeshPartBuilder}.
 * Spheres built are IcoSpheres, spheres with triangles equally spread on the surface. See
 * <a href="http://blog.andreaskahler.com/2009/06/creating-icosphere-mesh-in-code.html">
 * Creating an icosphere mesh in code</a> for details on the algorithm and difference between
 * IcoSphere and UVSphere.
 * <p>
 * Created on 2025-02-07.
 *
 * @author Alexander Winter
 */
public class IcoSphereShapeBuilder extends BaseShapeBuilder {
	private static final int FLOAT_PER_TRIANGLE = 5;
	private static final float ISOSPHERE_EXTENT_Y = (1.0f + (float)sqrt(5.0f)) / 2.0f;
	/**
	 * Missing vertical uv of the undivided icosphere compared to a normal sphere
	 */
	private static final float UNDIVIDED_ICOSPHERE_UV_GAP =
			asin(-(ISOSPHERE_EXTENT_Y / (float)sqrt(1f + pow2(ISOSPHERE_EXTENT_Y)))) / PI + 0.5f;

	private static final FloatArray tmpVertices = new FloatArray();
	private static final ShortArray tmpIndices = new ShortArray();
	private static final ShortArray tmpNewIndices = new ShortArray();
	private static final IntIntMap tmpAlreadyVisited = new IntIntMap();

	private static final Vector2 tmpUV = new Vector2();

	/**
	 * Builds an ico sphere on the provided builder, with given radius and subdivisions. The radius
	 * is the radius for the sphere in every axis. If a spheroid is needed, a scale transformation
	 * can be applied with {@link MeshPartBuilder#setVertexTransform(Matrix4)}. The subdivisions
	 * parameter determines the number of time the IcoSphere triangles are subdivided for a smoother
	 * sphere. When the value is 0, the IcoSphere is composed of 12 vertices and 20 faces, which is
	 * the minimal possible IcoSphere. Any further subdivision subdivides each face into 4 triangles
	 * which are then projected onto the sphere to make it rounder.
	 * <p>
	 * This function is not thread safe.
	 *
	 * @param builder builder to build the IcoSphere into
	 * @param radius radius of the sphere
	 * @param subdivisions number of subdivisions to apply to the IcoSphere
	 */
	public static void build(MeshPartBuilder builder, float radius, int subdivisions) {
		tmpVertices.clear();
		tmpIndices.clear();

		createIcosahedron(tmpVertices, tmpIndices);

		for(int i = 0; i < subdivisions; i++)
			subdivide(tmpVertices, tmpIndices);

		for(int i = 0; i < tmpVertices.size; i += FLOAT_PER_TRIANGLE)
			putVector2(tmpVertices, i + 3, getVertexUV(getVector3(tmpVertices, i, tmpV0)));

		tmpNewIndices.clear();
		detectWrappedTriangles(tmpVertices, tmpIndices, tmpNewIndices);
		fixWrappedTriangles(tmpNewIndices, tmpVertices, tmpIndices);
		tmpNewIndices.clear();

		for(int i = 0; i < tmpVertices.size; i += FLOAT_PER_TRIANGLE) {
			vertTmp0.setPos(tmpVertices.get(i), tmpVertices.get(i + 1), tmpVertices.get(i + 2));
			vertTmp0.position.nor();
			vertTmp0.setNor(vertTmp0.position);
			vertTmp0.position.scl(radius);
			vertTmp0.setUV(tmpVertices.get(i + 3), tmpVertices.get(i + 4));
			if(subdivisions == 0) {
				vertTmp0.uv.y -= UNDIVIDED_ICOSPHERE_UV_GAP;
				vertTmp0.uv.y /= 1f - UNDIVIDED_ICOSPHERE_UV_GAP * 2f;
			}
			builder.vertex(vertTmp0);
		}

		for(int i = 0; i < tmpIndices.size; i += 3)
			builder.triangle(tmpIndices.get(i), tmpIndices.get(i + 1), tmpIndices.get(i + 2));
		tmpVertices.clear();
		tmpIndices.clear();
	}

	private static void createIcosahedron(FloatArray vertices, ShortArray indices) {
		tmpV0.set(-1f, ISOSPHERE_EXTENT_Y, 0f);
		tmpV1.set(1f, ISOSPHERE_EXTENT_Y, 0f);
		tmpV2.set(-1f, -ISOSPHERE_EXTENT_Y, 0f);
		tmpV3.set(1f, -ISOSPHERE_EXTENT_Y, 0f);

		vertices.ensureCapacity(36);
		vertices.add(tmpV0.x, tmpV0.y, tmpV0.z);
		vertices.add(0f, 0f);
		vertices.add(tmpV1.x, tmpV1.y, tmpV1.z);
		vertices.add(0f, 0f);
		vertices.add(tmpV2.x, tmpV2.y, tmpV2.z);
		vertices.add(0f, 0f);
		vertices.add(tmpV3.x, tmpV3.y, tmpV3.z);
		vertices.add(0f, 0f);

		vertices.add(tmpV0.z, tmpV0.x, tmpV0.y);
		vertices.add(0f, 0f);
		vertices.add(tmpV1.z, tmpV1.x, tmpV1.y);
		vertices.add(0f, 0f);
		vertices.add(tmpV2.z, tmpV2.x, tmpV2.y);
		vertices.add(0f, 0f);
		vertices.add(tmpV3.z, tmpV3.x, tmpV3.y);
		vertices.add(0f, 0f);

		vertices.add(tmpV0.y, tmpV0.z, tmpV0.x);
		vertices.add(0f, 0f);
		vertices.add(tmpV1.y, tmpV1.z, tmpV1.x);
		vertices.add(0f, 0f);
		vertices.add(tmpV2.y, tmpV2.z, tmpV2.x);
		vertices.add(0f, 0f);
		vertices.add(tmpV3.y, tmpV3.z, tmpV3.x);
		vertices.add(0f, 0f);

		indices.ensureCapacity(60);
		indices.add((short)0, (short)11, (short)5);
		indices.add((short)0, (short)5, (short)1);
		indices.add((short)0, (short)1, (short)7);
		indices.add((short)0, (short)7, (short)10);
		indices.add((short)0, (short)10, (short)11);

		indices.add((short)1, (short)5, (short)9);
		indices.add((short)5, (short)11, (short)4);
		indices.add((short)11, (short)10, (short)2);
		indices.add((short)10, (short)7, (short)6);
		indices.add((short)7, (short)1, (short)8);

		indices.add((short)3, (short)9, (short)4);
		indices.add((short)3, (short)4, (short)2);
		indices.add((short)3, (short)2, (short)6);
		indices.add((short)3, (short)6, (short)8);
		indices.add((short)3, (short)8, (short)9);

		indices.add((short)4, (short)9, (short)5);
		indices.add((short)2, (short)4, (short)11);
		indices.add((short)6, (short)2, (short)10);
		indices.add((short)8, (short)6, (short)7);
		indices.add((short)9, (short)8, (short)1);
	}

	private static void subdivide(FloatArray vertices, ShortArray indices) {
		tmpNewIndices.clear();
		int numTriangles = indices.size / 3;

		for (int i = 0; i < numTriangles; i++) {
			int i1 = indices.get(i * 3);
			int i2 = indices.get(i * 3 + 1);
			int i3 = indices.get(i * 3 + 2);

			tmpV0.set(vertices.get(i1 * FLOAT_PER_TRIANGLE),
					vertices.get(i1 * FLOAT_PER_TRIANGLE + 1),
					vertices.get(i1 * FLOAT_PER_TRIANGLE + 2));
			tmpV1.set(vertices.get(i2 * FLOAT_PER_TRIANGLE),
					vertices.get(i2 * FLOAT_PER_TRIANGLE + 1),
					vertices.get(i2 * FLOAT_PER_TRIANGLE + 2));
			tmpV2.set(vertices.get(i3 * FLOAT_PER_TRIANGLE),
					vertices.get(i3 * FLOAT_PER_TRIANGLE + 1),
					vertices.get(i3 * FLOAT_PER_TRIANGLE + 2));

			tmpV3.set(tmpV0).add(tmpV1).scl(0.5f);
			tmpV4.set(tmpV1).add(tmpV2).scl(0.5f);
			tmpV5.set(tmpV2).add(tmpV0).scl(0.5f);

			int i12 = vertices.size / FLOAT_PER_TRIANGLE;
			vertices.add(tmpV3.x, tmpV3.y, tmpV3.z);
			vertices.add(0f, 0f);
			int i23 = vertices.size / FLOAT_PER_TRIANGLE;
			vertices.add(tmpV4.x, tmpV4.y, tmpV4.z);
			vertices.add(0f, 0f);
			int i31 = vertices.size / FLOAT_PER_TRIANGLE;
			vertices.add(tmpV5.x, tmpV5.y, tmpV5.z);
			vertices.add(0f, 0f);

			tmpNewIndices.addAll((short)i1, (short)i12, (short)i31);
			tmpNewIndices.addAll((short)i12, (short)i2, (short)i23);
			tmpNewIndices.addAll((short)i31, (short)i23, (short)i3);
			tmpNewIndices.addAll((short)i12, (short)i23, (short)i31);
		}

		indices.clear();
		indices.addAll(tmpNewIndices);
		tmpNewIndices.clear();
	}

	private static void detectWrappedTriangles(FloatArray vertices, ShortArray indices, ShortArray out) {
		int numTriangles = indices.size / 3;
		for(int triangle = 0; triangle < numTriangles; triangle++) {
			int v1 = indices.get(triangle * 3);
			int v2 = indices.get(triangle * 3 + 1);
			int v3 = indices.get(triangle * 3 + 2);

			tmpV0.set(getVector2(vertices, v1 * FLOAT_PER_TRIANGLE + 3, tmpUV), 0f);
			tmpV1.set(getVector2(vertices, v2 * FLOAT_PER_TRIANGLE + 3, tmpUV), 0f);
			tmpV2.set(getVector2(vertices, v3 * FLOAT_PER_TRIANGLE + 3, tmpUV), 0f);

			if(tmpV1.sub(tmpV0).crs(tmpV2.sub(tmpV0)).z > 0f)
				out.add(triangle);
		}
	}

	private static void fixWrappedTriangles(ShortArray triangles,
	                                        FloatArray vertices,
	                                        ShortArray indices) {
		tmpAlreadyVisited.clear();
		for(int i = 0; i < triangles.size; i++) {
			int triangle = triangles.get(i);
			int v1 = indices.get(triangle * 3);
			int v2 = indices.get(triangle * 3 + 1);
			int v3 = indices.get(triangle * 3 + 2);

			if(vertices.get(v1 * FLOAT_PER_TRIANGLE + 3) < 0.5f) {
				if(!tmpAlreadyVisited.containsKey(v1)) {
					int newIdx = vertices.size / FLOAT_PER_TRIANGLE;
					vertices.add(vertices.get(v1 * FLOAT_PER_TRIANGLE),
							vertices.get(v1 * FLOAT_PER_TRIANGLE + 1),
							vertices.get(v1 * FLOAT_PER_TRIANGLE + 2));

					vertices.add(vertices.get(v1 * FLOAT_PER_TRIANGLE + 3) + 1f,
							vertices.get(v1 * FLOAT_PER_TRIANGLE + 4));
					tmpAlreadyVisited.put(v1, newIdx);
				}
				v1 = tmpAlreadyVisited.get(v1, -1);
			}

			if(vertices.get(v2 * FLOAT_PER_TRIANGLE + 3) < 0.5f) {
				if(!tmpAlreadyVisited.containsKey(v2)) {
					int newIdx = vertices.size / FLOAT_PER_TRIANGLE;
					vertices.add(vertices.get(v2 * FLOAT_PER_TRIANGLE),
							vertices.get(v2 * FLOAT_PER_TRIANGLE + 1),
							vertices.get(v2 * FLOAT_PER_TRIANGLE + 2));

					vertices.add(vertices.get(v2 * FLOAT_PER_TRIANGLE + 3) + 1f,
							vertices.get(v2 * FLOAT_PER_TRIANGLE + 4));
					tmpAlreadyVisited.put(v2, newIdx);
				}
				v2 = tmpAlreadyVisited.get(v2, -1);
			}

			if(vertices.get(v3 * FLOAT_PER_TRIANGLE + 3) < 0.5f) {
				if(!tmpAlreadyVisited.containsKey(v3)) {
					int newIdx = vertices.size / FLOAT_PER_TRIANGLE;
					vertices.add(vertices.get(v3 * FLOAT_PER_TRIANGLE),
							vertices.get(v3 * FLOAT_PER_TRIANGLE + 1),
							vertices.get(v3 * FLOAT_PER_TRIANGLE + 2));

					vertices.add(vertices.get(v3 * FLOAT_PER_TRIANGLE + 3) + 1f,
							vertices.get(v3 * FLOAT_PER_TRIANGLE + 4));
					tmpAlreadyVisited.put(v3, newIdx);
				}
				v3 = tmpAlreadyVisited.get(v3, -1);
			}

			indices.set(triangle * 3, (short)v1);
			indices.set(triangle * 3 + 1, (short)v2);
			indices.set(triangle * 3 + 2, (short)v3);
		}

		tmpAlreadyVisited.clear();
	}

	private static Vector2 getVertexUV(Vector3 pos) {
		Vector3 normal = pos.nor();
		float theta = atan2(normal.x, normal.z) / PI / 2f + 0.5f;
		float phi = asin(-normal.y) / PI + 0.5f;
		return tmpUV.set(theta, phi);
	}
}
