package com.winteralexander.gdx.utils.test;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.winteralexander.gdx.utils.math.quadtree.Bounded;
import com.winteralexander.gdx.utils.math.quadtree.QuadTree;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for {@link QuadTree}
 * <p>
 * Created on 2019-09-16.
 *
 * @author Alexander Winter
 */
public class QuadTreeTest {
	@Test
	public void testRetrieve() {
		QuadTree<Rect> tree = new QuadTree<>(5, 1);
		tree.init(0, 0, 100f, 100f);
		tree.insert(new Rect(10, 10, 10, 10));
		for(int i = 0; i < 20; i++)
			tree.insert(new Rect(30, 10 + i, 10, 10));
		Rect middle = new Rect(45, 45, 10, 10);
		tree.insert(middle);

		Array<Rect> out = new Array<>();

		tree.retrieve(out, 55, 55, 10, 10);

		assertEquals(1, out.size);
		assertEquals(middle, out.get(0));

		out.clear();
		tree.retrieve(out, 45, 45, 10, 10);

		assertEquals(22, out.size);
	}

	private static class Rect implements Bounded {
		float x, y, w, h;

		public Rect(float x, float y, float w, float h) {
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
		}

		@Override
		public void getBounds(Rectangle out) {
			out.x = x;
			out.y = y;
			out.width = w;
			out.height = h;
		}
	}
}
