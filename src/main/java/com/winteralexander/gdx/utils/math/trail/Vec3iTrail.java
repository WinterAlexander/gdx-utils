package com.winteralexander.gdx.utils.math.trail;

import com.winteralexander.gdx.utils.math.vector.Vector3i;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * {@link Trail} for points in 3D space using Vector3 (integer coordinates)
 * <p>
 * Created on 2024-03-16.
 *
 * @author Alexander Winter
 */
public class Vec3iTrail extends Trail<Vector3i> {
	@Override
	protected void writeElement(OutputStream output, Vector3i element) throws IOException {
		element.writeTo(output);
	}

	@Override
	protected Vector3i readElement(InputStream input) throws IOException {
		Vector3i vec = new Vector3i();
		vec.readFrom(input);
		return vec;
	}

	@Override
	protected Vector3i copyElement(Vector3i element) {
		return element.cpy();
	}

	@Override
	public Vec3iTrail cpy() {
		Vec3iTrail trail = new Vec3iTrail();
		trail.set(this);
		return trail;
	}
}
