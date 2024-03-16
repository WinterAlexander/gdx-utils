package com.winteralexander.gdx.utils.math.trail;

import com.winteralexander.gdx.utils.math.vector.Vector2i;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * {@link Trail} for points in 2D space using Vector2i (integer coordinates)
 * <p>
 * Created on 2024-03-16.
 *
 * @author Alexander Winter
 */
public class Vector2iTrail extends Trail<Vector2i> {
	@Override
	protected void writeElement(OutputStream output, Vector2i element) throws IOException {
		element.writeTo(output);
	}

	@Override
	protected Vector2i readElement(InputStream input) throws IOException {
		Vector2i vec = new Vector2i();
		vec.readFrom(input);
		return vec;
	}

	@Override
	protected Vector2i copyElement(Vector2i element) {
		return element.cpy();
	}

	@Override
	public Vector2iTrail cpy() {
		Vector2iTrail trail = new Vector2iTrail();
		trail.set(this);
		return trail;
	}
}
