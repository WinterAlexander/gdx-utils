package com.github.winteralexander.gdx.utils.gfx;

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
	private static int MAX_TEXTURE_IMAGE_UNITS = -1;
	private static int MAX_TEXTURE_SIZE = -1;
	private static int MAX_VERTEX_UNIFORM_VECTORS = -1;

	public static int getMaxTextureImageUnits() {
		if(MAX_TEXTURE_IMAGE_UNITS != -1)
			return MAX_TEXTURE_IMAGE_UNITS;

		IntBuffer buffer = ByteBuffer.allocateDirect(64)
				.order(ByteOrder.nativeOrder())
				.asIntBuffer();
		Gdx.gl.glGetIntegerv(GL_MAX_TEXTURE_IMAGE_UNITS, buffer);
		return MAX_TEXTURE_IMAGE_UNITS = buffer.get();
	}

	public static int getMaxTextureSize() {
		if(MAX_TEXTURE_SIZE != -1)
			return MAX_TEXTURE_SIZE;

		IntBuffer buffer = ByteBuffer.allocateDirect(64)
				.order(ByteOrder.nativeOrder())
				.asIntBuffer();
		Gdx.gl.glGetIntegerv(GL_MAX_TEXTURE_SIZE, buffer);
		return MAX_TEXTURE_SIZE = buffer.get();
	}

	public static int getMaxVertexUniformVectors() {
		if(MAX_VERTEX_UNIFORM_VECTORS != -1)
			return MAX_VERTEX_UNIFORM_VECTORS;

		IntBuffer buffer = ByteBuffer.allocateDirect(64)
				.order(ByteOrder.nativeOrder())
				.asIntBuffer();
		Gdx.gl.glGetIntegerv(GL_MAX_VERTEX_UNIFORM_VECTORS, buffer);
		return MAX_VERTEX_UNIFORM_VECTORS = buffer.get();
	}
}
