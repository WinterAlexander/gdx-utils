package com.winteralexander.gdx.utils.math.vector;

import com.badlogic.gdx.math.Vector2;
import com.winteralexander.gdx.utils.io.Serializable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.winteralexander.gdx.utils.io.StreamUtil.readInt;
import static com.winteralexander.gdx.utils.io.StreamUtil.writeInt;
import static com.winteralexander.gdx.utils.math.MathUtil.pow2;

/**
 * 2D implementation for {@link IntVector}
 * <p>
 * Created on 2024-03-16.
 *
 * @author Alexander Winter
 */
public class Vector2i implements IntVector<Vector2i>, Serializable {
	public int x, y;

	public Vector2i() {
		this(0, 0);
	}

	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Vector2i cpy() {
		return new Vector2i(x, y);
	}

	public int len2() {
		return x * x + y * y;
	}

	@Override
	public long len2l() {
		return (long)x * (long)x + (long)y * (long)y;
	}

	public Vector2i set(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}

	@Override
	public Vector2i set(Vector2i v) {
		return set(v.x, v.y);
	}

	public Vector2i add(int x, int y) {
		this.x += x;
		this.y += y;
		return this;
	}

	@Override
	public Vector2i add(Vector2i v) {
		return add(v.x, v.y);
	}

	public Vector2i sub(int x, int y) {
		return add(-x, -y);
	}

	@Override
	public Vector2i sub(Vector2i v) {
		return sub(v.x, v.y);
	}

	@Override
	public int dot(Vector2i v) {
		return x * v.x + y * v.y;
	}

	@Override
	public Vector2i scl(float scalar) {
		x = Math.round(x * scalar);
		y = Math.round(y * scalar);
		return this;
	}

	@Override
	public Vector2i scl(int scalar) {
		x *= scalar;
		y *= scalar;
		return this;
	}

	public Vector2i scl(int x, int y) {
		this.x *= x;
		this.y *= y;
		return this;
	}

	@Override
	public Vector2i scl(Vector2i v) {
		return scl(v.x, v.y);
	}

	public Vector2i scl(float x, float y) {
		this.x = Math.round(this.x * x);
		this.y = Math.round(this.y * y);
		return this;
	}

	public Vector2i scl(Vector2 vec) {
		return scl(vec.x, vec.y);
	}

	@Override
	public int dst2(Vector2i v) {
		return pow2(v.x - x) + pow2(v.y - y);
	}

	@Override
	public Vector2i lerp(Vector2i target, float alpha) {


		return null;
	}

	public boolean equals(int x, int y) {
		return this.x == x && this.y == y;
	}

	@Override
	public boolean equals(Vector2i other) {
		return x == other.x && y == other.y;
	}

	@Override
	public void readFrom(InputStream input) throws IOException {
		x = readInt(input);
		y = readInt(input);
	}

	@Override
	public void writeTo(OutputStream output) throws IOException {
		writeInt(output, x);
		writeInt(output, y);
	}

	@Override
	public Vector2i mulAdd(Vector2i v, int scalar) {
		this.x += v.x * scalar;
		this.y += v.y * scalar;
		return this;
	}

	@Override
	public Vector2i mulAdd(Vector2i v, float scalar) {
		this.x += Math.round(v.x * scalar);
		this.y += Math.round(v.y * scalar);
		return this;
	}

	@Override
	public Vector2i mulAdd(Vector2i v, Vector2i mulVec) {
		this.x += v.x * mulVec.x;
		this.y += v.y * mulVec.y;
		return this;
	}

	public Vector2i mulAdd(Vector2i v, Vector2 mulVec) {
		this.x += Math.round(v.x * mulVec.x);
		this.y += Math.round(v.y * mulVec.y);
		return this;
	}
}
