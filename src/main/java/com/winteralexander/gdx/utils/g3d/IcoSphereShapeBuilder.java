package com.winteralexander.gdx.utils.g3d;

import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BaseShapeBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.ShortArray;

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
	private static final FloatArray tmpVertices = new FloatArray();
	private static final ShortArray tmpIndices = new ShortArray();
	private static final ShortArray tmpNewIndices = new ShortArray();

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

		// Create the initial icosahedron vertices and indices
		createIcosahedron(tmpVertices, tmpIndices);

		// Subdivide the icosahedron
		for(int i = 0; i < subdivisions; i++)
			subdivide(tmpVertices, tmpIndices);

		// Normalize vertices to create a sphere
		for(int i = 0; i < tmpVertices.size; i += 3) {
			vertTmp0.setPos(tmpVertices.get(i), tmpVertices.get(i + 1), tmpVertices.get(i + 2));
			vertTmp0.position.nor();
			vertTmp0.setNor(vertTmp0.position);
			vertTmp0.position.scl(radius);
			vertTmp0.setUV(0f, 0f);
			builder.vertex(vertTmp0);
		}

		for(int i = 0; i < tmpIndices.size; i += 3)
			builder.triangle(tmpIndices.get(i), tmpIndices.get(i + 1), tmpIndices.get(i + 2));
		tmpVertices.clear();
		tmpIndices.clear();
	}

	private static void createIcosahedron(FloatArray vertices, ShortArray indices) {
		float t = (1.0f + (float)Math.sqrt(5.0f)) / 2.0f;

		tmpV0.set(-1f, t, 0);
		tmpV1.set(1f, t, 0);
		tmpV2.set(-1f, -t, 0);
		tmpV3.set(1f, -t, 0);

		vertices.ensureCapacity(36);
		vertices.add(tmpV0.x, tmpV0.y, tmpV0.z);
		vertices.add(tmpV1.x, tmpV1.y, tmpV1.z);
		vertices.add(tmpV2.x, tmpV2.y, tmpV2.z);
		vertices.add(tmpV3.x, tmpV3.y, tmpV3.z);

		vertices.add(tmpV0.z, tmpV0.x, tmpV0.y);
		vertices.add(tmpV1.z, tmpV1.x, tmpV1.y);
		vertices.add(tmpV2.z, tmpV2.x, tmpV2.y);
		vertices.add(tmpV3.z, tmpV3.x, tmpV3.y);

		vertices.add(tmpV0.y, tmpV0.z, tmpV0.x);
		vertices.add(tmpV1.y, tmpV1.z, tmpV1.x);
		vertices.add(tmpV2.y, tmpV2.z, tmpV2.x);
		vertices.add(tmpV3.y, tmpV3.z, tmpV3.x);

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

			tmpV0.set(vertices.get(i1 * 3), vertices.get(i1 * 3 + 1), vertices.get(i1 * 3 + 2));
			tmpV1.set(vertices.get(i2 * 3), vertices.get(i2 * 3 + 1), vertices.get(i2 * 3 + 2));
			tmpV2.set(vertices.get(i3 * 3), vertices.get(i3 * 3 + 1), vertices.get(i3 * 3 + 2));

			tmpV3.set(tmpV0).add(tmpV1).scl(0.5f);
			tmpV4.set(tmpV1).add(tmpV2).scl(0.5f);
			tmpV5.set(tmpV2).add(tmpV0).scl(0.5f);

			int i12 = vertices.size / 3;
			vertices.add(tmpV3.x, tmpV3.y, tmpV3.z);
			int i23 = vertices.size / 3;
			vertices.add(tmpV4.x, tmpV4.y, tmpV4.z);
			int i31 = vertices.size / 3;
			vertices.add(tmpV5.x, tmpV5.y, tmpV5.z);

			tmpNewIndices.addAll((short)i1, (short)i12, (short)i31);
			tmpNewIndices.addAll((short)i12, (short)i2, (short)i23);
			tmpNewIndices.addAll((short)i31, (short)i23, (short)i3);
			tmpNewIndices.addAll((short)i12, (short)i23, (short)i31);
		}

		indices.clear();
		indices.addAll(tmpNewIndices);
		tmpNewIndices.clear();
	}
}
