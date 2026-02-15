package com.winteralexander.gdx.utils;

/**
 * Benchmark utility to benchmark any piece of code. Not thread safe.
 * @deprecated Meant to be used exclusively for debug. Should not be included in production code
 * <p>
 * Created on 2024-10-23.
 *
 * @author Alexander Winter
 */
@SuppressWarnings("DeprecatedIsStillUsed")
@Deprecated
public class Benchmark {
	private static long time = -1L;

	private Benchmark() {}

	public static void start() {
		time = System.nanoTime();
	}

	public static void step(String name) {
		end(name);
		start();
	}

	public static void end(String name) {
		long endTime = System.nanoTime();
		if(time == -1L)
			throw new IllegalStateException("must call start() first");

		System.out.print(name);
		System.out.print(": ");
		double ms = (endTime - time) / 1_000_000.0;
		System.out.print(ms);
		System.out.print(" ms\n");
		time = -1L;
	}
}
