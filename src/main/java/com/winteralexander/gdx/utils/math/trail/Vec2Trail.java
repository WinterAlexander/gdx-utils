package com.winteralexander.gdx.utils.math.trail;

import com.badlogic.gdx.math.Vector2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.winteralexander.gdx.utils.io.StreamUtil.readFloat;
import static com.winteralexander.gdx.utils.io.StreamUtil.writeFloat;

/**
 * {@link Trail} for points in 2D space using Vector2 (float coordinates)
 * <p>
 * Created on 2024-03-16.
 *
 * @author Alexander Winter
 */
public class Vec2Trail extends Trail<Vector2> {
	@Override
	protected void writeElement(OutputStream output, Vector2 element) throws IOException {
		writeFloat(output, element.x);
		writeFloat(output, element.y);
	}

	@Override
	protected Vector2 readElement(InputStream input) throws IOException {
		return new Vector2(readFloat(input), readFloat(input));
	}

	@Override
	protected Vector2 copyElement(Vector2 element) {
		return element.cpy();
	}

	@Override
	public Vec2Trail cpy() {
		Vec2Trail trail = new Vec2Trail();
		trail.set(this);
		return trail;
	}
}
