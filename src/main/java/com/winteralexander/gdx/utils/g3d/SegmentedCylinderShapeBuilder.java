package com.winteralexander.gdx.utils.g3d;

import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.CylinderShapeBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.EllipseShapeBuilder;
import com.badlogic.gdx.math.Matrix4;

/**
 * Builds a cylinder that has vertices along its length, useful for transformations
 * <p>
 * Created on 2025-02-08.
 *
 * @author Alexander Winter
 */
public class SegmentedCylinderShapeBuilder {
	private static final Matrix4 prevVertexTransform = new Matrix4();
	private static final Matrix4 vertexTransform = new Matrix4();

	public static void build(MeshPartBuilder builder,
	                         float width, float height, float depth,
	                         int segments, int divisions) {
		build(builder, width, height, depth, segments, divisions, true);
	}

	public static void build(MeshPartBuilder builder,
	                         float width, float height, float depth,
	                         int segments, int divisions,
	                         boolean closed) {

		float segmentHeight = height / segments;

		boolean transform = builder.isVertexTransformationEnabled();
		builder.getVertexTransform(prevVertexTransform);

		for(int i = 0; i < segments; i++) {
			vertexTransform.setToTranslation(0f, height / 2f - segmentHeight * (i + 1f) + segmentHeight / 2f, 0f);
			vertexTransform.mul(prevVertexTransform);
			builder.setVertexTransform(vertexTransform);
			builder.setUVRange(0f, (float)i / segments, 1f, (i + 1f) / segments);
			CylinderShapeBuilder.build(builder, width, segmentHeight, depth, divisions, 0, 360, false);
		}
		builder.setVertexTransform(prevVertexTransform);
		builder.setVertexTransformationEnabled(transform);
		builder.setUVRange(0f, 0f, 1f, 1f);

		if(!closed)
			return;

		EllipseShapeBuilder.build(builder,
				width, depth,
				0f, 0f,
				divisions,
				0f, height / 2f, 0f,
				0f, 1f, 0f,
				1f, 0f, 0f,
				0f, 0f, 1f,
				0f, 360f);
		EllipseShapeBuilder.build(builder,
				width, depth,
				0f, 0f,
				divisions,
				0f, -height / 2f, 0f,
				0f, -1f, 0f,
				-1f, 0f, 0f,
				0f, 0f, 1f,
				-180f, 180f);
	}
}
