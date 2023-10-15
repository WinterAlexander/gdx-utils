package com.winteralexander.gdx.utils.io;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.winteralexander.gdx.utils.io.SerializableLoader.CustomSerializableParameter;

import java.io.InputStream;

import static com.winteralexander.gdx.utils.io.SerializationUtil.readSerializable;

/**
 * Asset loader for any {@link Serializable}
 * <p>
 * Created on 2018-12-13.
 *
 * @author Alexander Winter
 */
public class SerializableLoader<T extends Serializable> extends SynchronousAssetLoader<T, CustomSerializableParameter<T>> {
	private final Class<T> type;

	public SerializableLoader(FileHandleResolver resolver, Class<T> type) {
		super(resolver);
		this.type = type;
	}

	@Override
	public T load(AssetManager assetManager, String fileName, FileHandle file, CustomSerializableParameter<T> parameter) {
		try {
			InputStream inputStream = file.read();

			T obj = readSerializable(inputStream, type);

			inputStream.close();

			return obj;
		} catch(Exception ex) {
			throw new GdxRuntimeException("Couldn't load " + type.getSimpleName() + " in CustomSerializableLoader", ex);
		}
	}

	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, CustomSerializableParameter<T> parameter) {
		return null;
	}

	public static class CustomSerializableParameter<T> extends AssetLoaderParameters<T> {}
}