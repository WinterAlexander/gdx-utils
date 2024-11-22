package com.winteralexander.gdx.utils;

import com.winteralexander.gdx.utils.io.TransferObject;
import com.winteralexander.gdx.utils.math.direction.GridCorner;
import com.winteralexander.gdx.utils.math.direction.GridDirection4;
import com.winteralexander.gdx.utils.math.direction.GridDirection8;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;

import static com.winteralexander.gdx.utils.Validation.ensureNotNull;
import static com.winteralexander.gdx.utils.io.SerializationUtil.readAny;
import static com.winteralexander.gdx.utils.io.SerializationUtil.writeAny;

/**
 * Map of adjacent tiles to a given tile, where each adjacent tile contain data
 * <p>
 * Created on 2024-11-14.
 *
 * @author Alexander Winter
 */
public class AdjacentTileMap<T> implements TransferObject<AdjacentTileMap<T>> {
	private final Class<T> type;
	private final T[] data;

	@SuppressWarnings("unchecked")
	public AdjacentTileMap(Class<T> type) {
		ensureNotNull(type, "type");
		this.type = type;
		this.data = (T[])Array.newInstance(type, 8);
	}

	public T get(GridDirection8 tile) {
		return data[tile.ordinal()];
	}

	public void set(GridDirection8 tile, T value) {
		data[tile.ordinal()] = value;
	}

	public T get(GridDirection4 tile) {
		return get(tile.asGridDirection8());
	}

	public void set(GridDirection4 tile, T value) {
		set(tile.asGridDirection8(), value);
	}

	public T get(GridCorner tile) {
		return get(tile.asGridDirection8());
	}

	public void set(GridCorner tile, T value) {
		set(tile.asGridDirection8(), value);
	}

	@Override
	public void set(AdjacentTileMap<T> other) {
		System.arraycopy(other.data, 0, data, 0, 8);
	}

	@Override
	public void readFrom(InputStream input) throws IOException {
		for(int i = 0; i < 8; i++)
			data[i] = readAny(input, type);
	}

	@Override
	public void writeTo(OutputStream output) throws IOException {
		for(int i = 0; i < 8; i++)
			writeAny(output, data[i]);
	}
}
