package com.winteralexander.gdx.utils.test;

import com.badlogic.gdx.math.Vector2;

/**
 * Vector2 compatible with junit assertions
 * <p>
 * Created on 2019-01-06.
 *
 * @author Alexander Winter
 */
public class TestVec2 extends Vector2 {
	public TestVec2() {
	}

	public TestVec2(float x, float y) {
		super(x, y);
	}

	public TestVec2(Vector2 v) {
		super(v);
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Vector2))
			return false;

		return super.epsilonEquals((Vector2)obj);
	}
}
