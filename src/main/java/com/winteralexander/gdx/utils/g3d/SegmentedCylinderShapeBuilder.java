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
	private static final Matrix4 vertexTransform = new Matrix4();

	public static void build(MeshPartBuilder builder,
	                         float width, float height, float depth,
	                         int segments, int divisions) {

		float segmentHeight = height / segments;

		for(int i = 0; i < segments; i++) {
			vertexTransform.setToTranslation(0f, segmentHeight * i + segmentHeight / 2f - height / 2f, 0f);
			builder.setVertexTransform(vertexTransform);
			CylinderShapeBuilder.build(builder, width, segmentHeight, depth, divisions, 0, 360, false);
		}
		builder.setVertexTransformationEnabled(false);

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
