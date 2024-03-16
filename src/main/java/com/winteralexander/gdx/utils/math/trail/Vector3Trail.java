package com.winteralexander.gdx.utils.math.trail;

import com.badlogic.gdx.math.Vector3;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.winteralexander.gdx.utils.io.StreamUtil.readFloat;
import static com.winteralexander.gdx.utils.io.StreamUtil.writeFloat;

/**
 * {@link Trail} for points in 3D space using Vector3 (float coordinates)
 * <p>
 * Created on 2024-03-16.
 *
 * @author Alexander Winter
 */
public class Vector3Trail extends Trail<Vector3> {
	@Override
	protected void writeElement(OutputStream output, Vector3 element) throws IOException {
		writeFloat(output, element.x);
		writeFloat(output, element.y);
		writeFloat(output, element.z);
	}

	@Override
	protected Vector3 readElement(InputStream input) throws IOException {
		return new Vector3(readFloat(input), readFloat(input), readFloat(input));
	}

	@Override
	protected Vector3 copyElement(Vector3 element) {
		return element.cpy();
	}

	@Override
	public Vector3Trail cpy() {
		Vector3Trail trail = new Vector3Trail();
		trail.set(this);
		return trail;
	}
}
