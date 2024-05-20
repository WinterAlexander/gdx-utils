package com.winteralexander.gdx.utils;

/**
 * TODO Undocumented :(
 * <p>
 * Created on 2024-05-20.
 *
 * @author Alexander Winter
 */
public class AdaptiveSleeper {
	private long precision = 0L;

	public void sleep(long nanos) {
		long start = System.nanoTime();
		SystemUtil.sleep((nanos - precision) / 1_000_000L);
		long error = (System.nanoTime() - start) - (nanos - precision);
		this.precision = Math.max(this.precision, error);
		while(System.nanoTime() < start + nanos);
	}
}
