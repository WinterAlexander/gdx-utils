package com.winteralexander.gdx.utils.test.g3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglGraphics;
import com.badlogic.gdx.backends.lwjgl.LwjglNativesLoader;
import org.lwjgl.opengl.Display;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Allows initializing libGDX's OpenGL capabilities without spawning a window, useful for unit tests
 * using some OpenGL features
 * <p>
 * Created on 2025-02-09.
 *
 * @author Alexander Winter
 */
public class MockGL {
	public static void init() {
		try {
			if(Gdx.gl != null) {
				Display.destroy();
				Gdx.gl = null;
				Gdx.graphics = null;
				Gdx.gl20 = null;
				Gdx.gl30 = null;
				Gdx.gl31 = null;
				Gdx.gl32 = null;
			}

			LwjglNativesLoader.load();
			Class<LwjglGraphics> gfx = LwjglGraphics.class;
			Constructor<LwjglGraphics> cons = gfx.getDeclaredConstructor(LwjglApplicationConfiguration.class);
			cons.setAccessible(true);
			LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
			config.undecorated = true;
			config.width = 1;
			config.height = 1;

			Gdx.graphics = cons.newInstance(config);

			Method method = gfx.getDeclaredMethod("setupDisplay");
			method.setAccessible(true);
			method.invoke(Gdx.graphics);
		} catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
