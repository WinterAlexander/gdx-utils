package com.winteralexander.gdx.utils.io;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.IOException;
import java.util.Properties;

/**
 * LibGDX asset loader for a .properties file
 * <p>
 * Created on 2024-04-25.
 *
 * @author Alexander Winter
 */
public class PropertiesFileLoader
		extends SynchronousAssetLoader<Properties, AssetLoaderParameters<Properties>> {
	public PropertiesFileLoader(FileHandleResolver resolver) {
		super(resolver);
	}

	@Override
	public Properties load(AssetManager assetManager,
	                       String fileName,
	                       FileHandle fileHandle,
	                       AssetLoaderParameters<Properties> param) {
		Properties properties = new Properties();
		try {
			properties.load(fileHandle.reader());
		} catch(IOException ex) {
			throw new GdxRuntimeException("Failed to load .properties asset " + fileHandle.path(),
					ex);
		}

		return properties;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName,
	                                              FileHandle fileHandle,
	                                              AssetLoaderParameters<Properties> param) {
		return null;
	}
}
