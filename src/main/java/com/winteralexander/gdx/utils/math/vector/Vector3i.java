package com.winteralexander.gdx.utils.math.vector;

import com.badlogic.gdx.math.Vector3;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

import static com.winteralexander.gdx.utils.io.StreamUtil.readInt;
import static com.winteralexander.gdx.utils.io.StreamUtil.writeInt;
import static com.winteralexander.gdx.utils.math.MathUtil.pow2;

/**
 * 3D implementation for {@link IntVector}
 * <p>
 * Created on 2024-03-16.
 *
 * @author Alexander Winter
 */
public class Vector3i implements IntVector<Vector3i> {
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

	public Vector3i set(float x, float y, float z) {
		return set(x, y, z, 1);
	}

	public Vector3i set(float x, float y, float z, int unitSize) {
		return set(Math.round(x * unitSize), Math.round(y * unitSize), Math.round(z * unitSize));
	}

	@Override
	public Vector3i set(Vector3i v) {
		return set(v.x, v.y, v.z);
	}

	public Vector3i set(Vector3 vec) {
		return set(vec.x, vec.y, vec.z);
	}

	public Vector3i set(Vector3 vec, int unitSize) {
		return set(vec.x, vec.y, vec.z, unitSize);
	}

	public boolean equals(int x, int y, int z) {
		return this.x == x
				&& this.y == y
				&& this.z == z;
	}

	@Override
	public boolean equals(Vector3i other) {
		return x == other.x
				&& y == other.y
				&& z == other.z;
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

	public Vector3i add(int x, int y, int z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	@Override
	public Vector3i add(Vector3i v) {
		return add(v.x, v.y, v.z);
	}

	public Vector3i sub(int x, int y, int z) {
		return add(-x, -y, -z);
	}

	@Override
	public Vector3i sub(Vector3i v) {
		return sub(v.x, v.y, v.z);
	}

	public int dot(int x, int y, int z) {
		return this.x * x + this.y * y + this.z * z;
	}

	@Override
	public int dot(Vector3i v) {
		return x * v.x + y * v.y + z * v.z;
	}

	@Override
	public Vector3i scl(float scalar) {
		return scl(scalar, scalar, scalar);
	}

	@Override
	public Vector3i scl(int scalar) {
		return scl(scalar, scalar, scalar);
	}

	public Vector3i scl(int x, int y, int z) {
		this.x *= x;
		this.y *= y;
		this.z *= z;
		return this;
	}

	@Override
	public Vector3i scl(Vector3i v) {
		this.x *= v.x;
		this.y *= v.y;
		this.z *= v.z;
		return this;
	}

	public Vector3i scl(float x, float y, float z) {
		this.x = Math.round(this.x * x);
		this.y = Math.round(this.y * y);
		this.z = Math.round(this.z * z);
		return this;
	}

	public Vector3i scl(Vector3 floatVec) {
		return scl(floatVec.x, floatVec.y, floatVec.z);
	}

	@Override
	public int dst2(Vector3i v) {
		return pow2(x - v.x) + pow2(y - v.y) + pow2(z - v.z);
	}

	@Override
	public Vector3i lerp(Vector3i target, float alpha) {
		x = Math.round(x * (1.0f - alpha) + target.x * alpha);
		y = Math.round(y * (1.0f - alpha) + target.y * alpha);
		z = Math.round(z * (1.0f - alpha) + target.z * alpha);
		return this;
	}

	@Override
	public Vector3i mulAdd(Vector3i v, int scalar) {
		return mulAdd(v, scalar, scalar, scalar);
	}

	@Override
	public Vector3i mulAdd(Vector3i v, float scalar) {
		return mulAdd(v, scalar, scalar, scalar);
	}

	public Vector3i mulAdd(Vector3i v, int x, int y, int z) {
		this.x += v.x * x;
		this.y += v.y * y;
		this.z += v.z * z;
		return this;
	}

	public Vector3i mulAdd(Vector3i v, float x, float y, float z) {
		this.x += Math.round(v.x * x);
		this.y += Math.round(v.y * y);
		this.z += Math.round(v.z * z);
		return this;
	}

	@Override
	public Vector3i mulAdd(Vector3i v, Vector3i mulVec) {
		return mulAdd(v, mulVec.x, mulVec.y, mulVec.z);
	}

	public Vector3i mulAdd(Vector3i v, Vector3 mulVec) {
		return mulAdd(v, mulVec.x, mulVec.y, mulVec.z);
	}

	public Vector3 toVec3() {
		return toVec3(new Vector3());
	}

	public Vector3 toVec3(Vector3 out) {
		return toVec3(out, 1);
	}

	public Vector3 toVec3(Vector3 out, int unitSize) {
		out.x = (float)x / unitSize;
		out.y = (float)y / unitSize;
		out.z = (float)z / unitSize;
		return out;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}

	@Override
	public boolean equals(Object o) {
		if(this == o)
			return true;
		if(o == null || getClass() != o.getClass())
			return false;
		Vector3i vector3i = (Vector3i)o;
		return x == vector3i.x && y == vector3i.y && z == vector3i.z;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}
}
