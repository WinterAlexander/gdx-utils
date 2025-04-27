package com.winteralexander.gdx.utils.test.g3d;

import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.ShortArray;
import com.winteralexander.gdx.utils.BufferUtil;
import com.winteralexander.gdx.utils.g3d.IcoSphereShapeBuilder;
import com.winteralexander.gdx.utils.test.g3d.debugviewer.ModelViewer;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static com.badlogic.gdx.graphics.GL20.GL_TRIANGLES;
import static com.winteralexander.gdx.utils.BufferUtil.getVector3;
import static com.winteralexander.gdx.utils.BufferUtil.putVector2;
import static com.winteralexander.gdx.utils.g3d.IcoSphereShapeBuilder.getVertexUV;

/**
 * Not an unit test but allows debugging {@link IcoSphereShapeBuilder} and testing it visually
 * (no asserts and requires GPU capabilities)
 * <p>
 * Created on 2025-02-09.
 *
 * @author Alexander Winter
 */
@Ignore
public class IcoSphereShapeBuilderTest {
	@BeforeClass
	public static void initGL() {
		MockGL.init();
	}

	@Test
	public void testTriangleFixupVisualization() {

		int subdivisions = 0;
		FloatArray tmpVertices = new FloatArray();
		ShortArray tmpIndices = new ShortArray();
		ShortArray tmpNewIndices = new ShortArray();

		IcoSphereShapeBuilder.createIcosahedron(tmpVertices, tmpIndices);

		for(int i = 0; i < subdivisions; i++)
			IcoSphereShapeBuilder.subdivide(tmpVertices, tmpIndices);

		for(int i = 0; i < tmpVertices.size; i += 5)
			putVector2(tmpVertices, i + 3, getVertexUV(getVector3(tmpVertices, i)));

		tmpNewIndices.clear();
		IcoSphereShapeBuilder.detectWrappedTriangles(tmpVertices, tmpIndices, tmpNewIndices);

		ModelViewer.__debugOnlyRenderables.addFirst(r -> {
			for(int i = 0; i < tmpNewIndices.size; i++) {
				int triangle = tmpNewIndices.get(i);
				int v1 = tmpIndices.get(triangle * 3);
				int v2 = tmpIndices.get(triangle * 3 + 1);
				int v3 = tmpIndices.get(triangle * 3 + 2);

				Vector3 p1 = BufferUtil.getVector3(tmpVertices, v1 * 5);
				Vector3 p2 = BufferUtil.getVector3(tmpVertices, v2 * 5);
				Vector3 p3 = BufferUtil.getVector3(tmpVertices, v3 * 5);

				r.line(p1, p2);
				r.line(p2, p3);
				r.line(p3, p1);
			}
		});
		ModelViewer.start();
	}

	@Test
	public void testIcoSphere() {
		ModelBuilder builder = new ModelBuilder();
		int attrs = VertexAttributes.Usage.Position
				| VertexAttributes.Usage.Normal
				| VertexAttributes.Usage.TextureCoordinates;

		builder.begin();
		IcoSphereShapeBuilder.build(builder.part("sphere", GL_TRIANGLES, attrs, new Material()), 1f, 0);
		Model ico0 = builder.end();
		ico0.meshes.get(0).transform(new Matrix4().translate(0f, 0f, 2f));
		builder.begin();
		IcoSphereShapeBuilder.build(builder.part("sphere", GL_TRIANGLES, attrs, new Material()), 1f, 1);
		Model ico1 = builder.end();
		ico1.meshes.get(0).transform(new Matrix4().translate(0f, 0f, 4f));
		builder.begin();
		IcoSphereShapeBuilder.build(builder.part("sphere", GL_TRIANGLES, attrs, new Material()), 1f, 2);
		Model ico2 = builder.end();
		ico2.meshes.get(0).transform(new Matrix4().translate(0f, 0f, 6f));
		builder.begin();
		IcoSphereShapeBuilder.build(builder.part("sphere", GL_TRIANGLES, attrs, new Material()), 1f, 3);
		Model ico3 = builder.end();
		ico3.meshes.get(0).transform(new Matrix4().translate(0f, 0f, 8f));
		builder.begin();
		IcoSphereShapeBuilder.build(builder.part("sphere", GL_TRIANGLES, attrs, new Material()), 1f, 4);
		Model ico4 = builder.end();
		ico4.meshes.get(0).transform(new Matrix4().translate(0f, 0f, 10f));

		ModelViewer.start(ico0, ico1, ico2, ico3, ico4);
	}
}
