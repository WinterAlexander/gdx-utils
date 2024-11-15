package com.winteralexander.gdx.utils;

import com.winteralexander.gdx.utils.io.TransferObject;

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

	public T get(AdjacentTile tile) {
		return data[tile.ordinal()];
	}

	public void set(AdjacentTile tile, T value) {
		data[tile.ordinal()] = value;
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

	public enum AdjacentTile {
		LEFT,
		RIGHT,
		TOP,
		BOTTOM,
		TOP_LEFT,
		TOP_RIGHT,
		BOTTOM_LEFT,
		BOTTOM_RIGHT;

		public static final AdjacentTile[] values = EnumConstantCache.store(values());
	}
}
