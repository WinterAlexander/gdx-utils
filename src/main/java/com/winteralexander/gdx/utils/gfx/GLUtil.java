package com.winteralexander.gdx.utils.gfx;

import com.badlogic.gdx.Gdx;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import static com.badlogic.gdx.graphics.GL20.*;

/**
 * Utility class for OpenGL related operations. Not thread safe and can only be
 * called from the OpenGL context thread
 * <p>
 * Created on 2020-09-18.
 *
 * @author Alexander Winter
 */
public class GLUtil {
	public static final int GL_LINE_SMOOTH = 2_848;
	public static final int GL_SMOOTH_LINE_WIDTH_RANGE = 0xB22;

	private static int maxTextureImageUnits = -1;
	private static int maxTextureSize = -1;
	private static int maxVertexUniformVectors = -1;

	private static int minSmoothLineWidth = -1;
	private static int maxSmoothLineWidth = -1;
	private static int minAliasedLineWidth = -1;
	private static int maxAliasedLineWidth = -1;

	public static int getMaxTextureImageUnits() {
		if(maxTextureImageUnits != -1)
			return maxTextureImageUnits;

		IntBuffer
				buffer = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asIntBuffer();
		Gdx.gl.glGetIntegerv(GL_MAX_TEXTURE_IMAGE_UNITS, buffer);
		return maxTextureImageUnits = buffer.get();
	}

	public static int getMaxTextureSize() {
		if(maxTextureSize != -1)
			return maxTextureSize;

		IntBuffer
				buffer = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asIntBuffer();
		Gdx.gl.glGetIntegerv(GL_MAX_TEXTURE_SIZE, buffer);
		return maxTextureSize = buffer.get();
	}

	public static int getMaxVertexUniformVectors() {
		if(maxVertexUniformVectors != -1)
			return maxVertexUniformVectors;

		IntBuffer
				buffer = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asIntBuffer();
		Gdx.gl.glGetIntegerv(GL_MAX_VERTEX_UNIFORM_VECTORS, buffer);
		return maxVertexUniformVectors = buffer.get();
	}

	public static int getMinSmoothLineWidth() {
		if(minSmoothLineWidth != -1)
			return minSmoothLineWidth;

		IntBuffer
				buffer = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asIntBuffer();
		Gdx.gl.glGetIntegerv(GL_SMOOTH_LINE_WIDTH_RANGE, buffer);
		minSmoothLineWidth = buffer.get();
		maxSmoothLineWidth = buffer.get();
		return minSmoothLineWidth;
	}

	public static int getMaxSmoothLineWidth() {
		if(maxSmoothLineWidth != -1)
			return maxSmoothLineWidth;

		IntBuffer
				buffer = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asIntBuffer();
		Gdx.gl.glGetIntegerv(GL_SMOOTH_LINE_WIDTH_RANGE, buffer);
		minSmoothLineWidth = buffer.get();
		maxSmoothLineWidth = buffer.get();
		return maxSmoothLineWidth;
	}

	public static int getMinAliasLineWidth() {
		if(minAliasedLineWidth != -1)
			return minAliasedLineWidth;

		IntBuffer
				buffer = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asIntBuffer();
		Gdx.gl.glGetIntegerv(GL_ALIASED_LINE_WIDTH_RANGE, buffer);
		minAliasedLineWidth = buffer.get();
		maxAliasedLineWidth = buffer.get();
		return minAliasedLineWidth;
	}

	public static int getMaxAliasLineWidth() {
		if(maxAliasedLineWidth != -1)
			return maxAliasedLineWidth;

		IntBuffer
				buffer = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asIntBuffer();
		Gdx.gl.glGetIntegerv(GL_ALIASED_LINE_WIDTH_RANGE, buffer);
		minAliasedLineWidth = buffer.get();
		maxAliasedLineWidth = buffer.get();
		return maxAliasedLineWidth;
	}
}
