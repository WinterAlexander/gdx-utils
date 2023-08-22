package com.github.winteralexander.gdx.utils.error;

/**
 * StackTrace wrapper for StackTracker
 * <p>
 * Created on 2018-07-25.
 *
 * @author Alexander Winter
 */
public class Tracker {
	private final String name;
	private final TrackedStackTrace ex;

	public Tracker(String name, TrackedStackTrace ex) {
		this.name = name;
		this.ex = ex;
	}

	public String getName() {
		return name;
	}

	public TrackedStackTrace get() {
		return ex;
	}
}
