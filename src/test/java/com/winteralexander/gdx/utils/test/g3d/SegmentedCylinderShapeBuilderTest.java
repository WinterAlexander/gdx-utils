package com.winteralexander.gdx.utils.test.g3d;

import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.winteralexander.gdx.utils.g3d.SegmentedCylinderShapeBuilder;
import com.winteralexander.gdx.utils.test.g3d.debugviewer.ModelViewer;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static com.badlogic.gdx.graphics.GL20.GL_TRIANGLES;

/**
 *
 * Not an unit test but allows debugging {@link SegmentedCylinderShapeBuilder} and testing it
 * visually (no asserts and requires GPU capabilities)
 * <p>
 * Created on 2025-02-09.
 *
 * @author Alexander Winter
 */
@Ignore
public class SegmentedCylinderShapeBuilderTest {
	@BeforeClass
	public static void initGL() {
		MockGL.init();
	}

	@Test
	public void testSegmentedCylinderSphere() {
		ModelBuilder builder = new ModelBuilder();
		int attrs = VertexAttributes.Usage.Position
				| VertexAttributes.Usage.Normal
				| VertexAttributes.Usage.TextureCoordinates;

		builder.begin();
		SegmentedCylinderShapeBuilder.build(
				builder.part("cyl", GL_TRIANGLES, attrs, new Material()), 0.5f, 1f, 0.5f, 5, 5);
		Model ico0 = builder.end();
		ico0.meshes.get(0).transform(new Matrix4().translate(0f, 0f, 2f));
		builder.begin();
		SegmentedCylinderShapeBuilder.build(
				builder.part("cyl", GL_TRIANGLES, attrs, new Material()), 0.5f, 1f, 0.5f, 8, 8);
		Model ico1 = builder.end();
		ico1.meshes.get(0).transform(new Matrix4().translate(0f, 0f, 4f));
		builder.begin();
		SegmentedCylinderShapeBuilder.build(
				builder.part("cyl", GL_TRIANGLES, attrs, new Material()), 0.5f, 1f, 0.5f, 10, 10);
		Model ico2 = builder.end();
		ico2.meshes.get(0).transform(new Matrix4().translate(0f, 0f, 6f));
		builder.begin();
		SegmentedCylinderShapeBuilder.build(
				builder.part("cyl", GL_TRIANGLES, attrs, new Material()), 0.5f, 1f, 0.5f, 20, 20);
		Model ico3 = builder.end();
		ico3.meshes.get(0).transform(new Matrix4().translate(0f, 0f, 8f));
		builder.begin();
		SegmentedCylinderShapeBuilder.build(
				builder.part("cyl", GL_TRIANGLES, attrs, new Material()), 0.5f, 1f, 0.5f, 50, 50);
		Model ico4 = builder.end();
		ico4.meshes.get(0).transform(new Matrix4().translate(0f, 0f, 10f));

		ModelViewer.start(ico0, ico1, ico2, ico3, ico4);
	}

}
