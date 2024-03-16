package com.winteralexander.gdx.utils.math.vector;

import com.winteralexander.gdx.utils.io.Serializable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.winteralexander.gdx.utils.io.StreamUtil.readInt;
import static com.winteralexander.gdx.utils.io.StreamUtil.writeInt;

/**
 * 3D implementation for {@link IntVector}
 * <p>
 * Created on 2024-03-16.
 *
 * @author Alexander Winter
 */
public class Vector3i implements IntVector<Vector3i>, Serializable {
	public int x, y, z;

	public Vector3i() {
		this(0, 0, 0);
	}

	public Vector3i(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public Vector3i cpy() {
		return new Vector3i(x, y, z);
	}

	@Override
	public int len2() {
		return x * x + y * y + z * z;
	}

	@Override
	public long len2l() {
		return (long)x * (long)x + (long)y * (long)y + (long)z * (long)z;
	}

	public Vector3i set(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	@Override
	public Vector3i set(Vector3i v) {
		return set(v.x, v.y, v.z);
	}

	public boolean equals(int x, int y, int z) {
		return this.x == x && this.y == y && this.z == z;
	}

	@Override
	public boolean equals(Vector3i other) {
		return x == other.x && y == other.y && z == other.z;
	}

	@Override
	public void readFrom(InputStream input) throws IOException {
		x = readInt(input);
		y = readInt(input);
		z = readInt(input);
	}

	@Override
	public void writeTo(OutputStream output) throws IOException {
		writeInt(output, x);
		writeInt(output, y);
		writeInt(output, z);
	}

	@Override
	public Vector3i add(Vector3i v) {
		return this;
	}

	@Override
	public Vector3i sub(Vector3i v) {
		return this;
	}

	@Override
	public int dot(Vector3i v) {
		return 0;
	}

	@Override
	public Vector3i scl(float scalar) {
		return null;
	}

	@Override
	public Vector3i scl(int scalar) {
		return null;
	}

	@Override
	public Vector3i scl(Vector3i v) {
		return null;
	}

	@Override
	public int dst2(Vector3i v) {
		return 0;
	}

	@Override
	public Vector3i lerp(Vector3i target, float alpha) {
		return null;
	}

	@Override
	public Vector3i mulAdd(Vector3i v, int scalar) {
		return null;
	}

	@Override
	public Vector3i mulAdd(Vector3i v, float scalar) {
		return null;
	}

	@Override
	public Vector3i mulAdd(Vector3i v, Vector3i mulVec) {
		return null;
	}
}
