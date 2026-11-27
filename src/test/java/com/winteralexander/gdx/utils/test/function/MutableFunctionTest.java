package com.winteralexander.gdx.utils.test.function;

import com.badlogic.gdx.math.Vector2;
import com.winteralexander.gdx.utils.function.MutableFunction;
import com.winteralexander.gdx.utils.math.vector.Vector2i;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for {@link MutableFunction}
 * <p>
 * Created on 2026-11-26.
 *
 * @author Alexander Winter
 */
public class MutableFunctionTest {
	@Test
	public void testBasic() {
		MutableFunction<Vector2i, Vector2> func = new MutableFunction<>(new Vector2(),
				(vecInt, out) -> out.set(vecInt.x * 5f, vecInt.y * 6f));

		Vector2 val = func.apply(new Vector2i(4, 5));
		assertEquals(20f, val.x, 0f);
		assertEquals(30f, val.y, 0f);

		Vector2 val2 = func.apply(new Vector2i(1, -1));
		assertEquals(5f, val.x, 0f);
		assertEquals(-6f, val.y, 0f);

		assertSame(val, val2);
	}
}
