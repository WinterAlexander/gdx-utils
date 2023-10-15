package com.winteralexander.gdx.utils;

import com.badlogic.gdx.Gdx;
import com.winteralexander.gdx.utils.io.NullPrintStream;

import java.io.PrintStream;

/**
 * Utility class to interact with LibGDX
 * <p>
 * Created on 2017-11-08.
 *
 * @author Alexander Winter
 */
public class GdxUtil {
	private GdxUtil() {}

	public static void postRunnable(Runnable runnable) {
		Gdx.app.postRunnable(runnable);
	}

	public static void postRunnable(Runnable runnable, long millis) {
		new Thread(() -> {
			SystemUtil.sleep(millis);
			postRunnable(runnable);
		}).start();
	}

	public static boolean openURI(String uri) {
		PrintStream prevErr = System.err;
		try {
			System.setErr(NullPrintStream.instance);
		} catch(SecurityException ignored) {
			return Gdx.net.openURI(uri);
		}

		try {

			return Gdx.net.openURI(uri);
		} finally {
			System.setErr(prevErr);
		}
	}
}
