package com.winteralexander.gdx.utils.math.interval;

/**
 * Interval that is always empty, doesn't contain anything
 * <p>
 * Created on 2018-08-30.
 *
 * @author Alexander Winter
 */
public class EmptyInterval implements Interval {
	public static final EmptyInterval INSTANCE = new EmptyInterval();

	@Override
	public Interval add(Interval other) {
		return other;
	}

	@Override
	public boolean contains(float value) {
		return false;
	}

	@Override
	public boolean contains(float start, float end) {
		return false;
	}

	@Override
	public boolean intersects(float start, float end) {
		return false;
	}
}
