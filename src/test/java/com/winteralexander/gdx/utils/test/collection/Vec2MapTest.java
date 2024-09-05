package com.winteralexander.gdx.utils.test.collection;

import com.winteralexander.gdx.utils.collection.Vec2iMap;
import com.winteralexander.gdx.utils.collection.Vec2sMap;
import com.winteralexander.gdx.utils.math.vector.Vector2i;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test for {@link Vec2iMap} and {@link Vec2sMap}
 * <p>
 * Created on 2024-09-05.
 *
 * @author Alexander Winter
 */
public class Vec2MapTest {
	@Test
	public void testVec2iMapSimple() {
		Vec2iMap<Integer> map = new Vec2iMap<>();

		assertNull(map.get(0, 1));
		assertEquals(0, map.size());

		map.put(0, 0, 10);
		map.put(0, 1, 8);
		map.put(1, 0, 4);
		map.put(new Vector2i(-1, 0), 5);
		map.put(0, -1, 6);

		assertEquals(10, map.get(0, 0).intValue());
		assertEquals(8, map.get(0, 1).intValue());
		assertEquals(4, map.get(1, 0).intValue());
		assertEquals(5, map.get(-1, 0).intValue());
		assertEquals(6, map.get(0, -1).intValue());
		assertEquals(5, map.size());
		map.remove(new Vector2i(0, 1));
		assertEquals(4, map.size());
		assertNull(map.get(0, 1));
		assertNull(map.get(0, 2));
		int i = 0;
		for(Vector2i vec : map.keys()) {
			if(Math.abs(vec.x) > 1 || Math.abs(vec.y) > 1)
				fail("Invalid value returned by map.keys(): " + vec);
			i++;
		}

		assertEquals(4, i);

		map.put(1_000_000_000, 1_000_000_000, 7);
		map.put(1_000_000_000, -1_000_000_000, 17);
		assertEquals(7, map.get(1_000_000_000, 1_000_000_000).intValue());
		assertEquals(17, map.get(1_000_000_000, -1_000_000_000).intValue());
	}

	@Test
	public void testVec2sMapSimple() {
		Vec2sMap<Integer> map = new Vec2sMap<>();

		assertNull(map.get(0, 1));
		assertEquals(0, map.size());

		map.put(0, 0, 10);
		map.put(0, 1, 8);
		map.put(1, 0, 4);
		map.put(new Vector2i(-1, 0), 5);
		map.put(0, -1, 6);

		assertEquals(10, map.get(0, 0).intValue());
		assertEquals(8, map.get(0, 1).intValue());
		assertEquals(4, map.get(1, 0).intValue());
		assertEquals(5, map.get(-1, 0).intValue());
		assertEquals(6, map.get(0, -1).intValue());
		assertEquals(5, map.size());
		map.remove(new Vector2i(0, 1));
		assertEquals(4, map.size());
		assertNull(map.get(0, 1));
		assertNull(map.get(0, 2));
		int i = 0;
		for(Vector2i vec : map.keys()) {
			if(Math.abs(vec.x) > 1 || Math.abs(vec.y) > 1)
				fail("Invalid value returned by map.keys(): " + vec);
			i++;
		}

		assertEquals(4, i);

		map.put(32_000, 32_000, 7);
		map.put(32_000, -32_000, 17);
		assertEquals(7, map.get(32_000, 32_000).intValue());
		assertEquals(17, map.get(32_000, -32_000).intValue());
	}
}
