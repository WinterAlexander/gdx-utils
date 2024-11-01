package com.winteralexander.gdx.utils.test;

import com.winteralexander.gdx.utils.io.BitsetUtil;
import org.junit.Test;

import static com.winteralexander.gdx.utils.io.BitsetUtil.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests to test functionality of {@link BitsetUtil}
 * <p>
 * Created on 2024-10-31.
 *
 * @author Alexander Winter
 */
public class BitsetUtilTest {
	@Test
	public void testBitsetToIntConversion() {
		assertEquals((byte)0, toByte());
		assertEquals((byte)0, toByte(false));
		assertEquals((byte)1, toByte(true));
		assertEquals((byte)2, toByte(false, true));
		assertEquals((byte)11, toByte(true, true, false, true));
		assertEquals((byte)13, toByte(true, false, true, true));
		assertEquals((byte)16, toByte(false, false, false, false, true));
		assertEquals((byte)32, toByte(false, false, false, false, false, true));
		assertEquals((byte)64, toByte(false, false, false, false, false, false, true));
		assertEquals((byte)128, toByte(false, false, false, false, false, false, false, true));
		assertEquals((byte)255, toByte(true, true, true, true, true, true, true, true));

		assertEquals((short)256, toShort(false, false, false, false, false, false, false, false,
				true));
		assertEquals((short)65535, toShort(true, true, true, true, true, true, true, true,
				true, true, true, true, true, true, true, true));

		assertEquals(65536, toInt(false, false, false, false, false, false, false, false,
				false, false, false, false, false, false, false, false,
				true));
		assertEquals((int)4_294_967_295L, toInt(true, true, true, true, true, true, true, true,
				true, true, true, true, true, true, true, true,
				true, true, true, true, true, true, true, true,
				true, true, true, true, true, true, true, true));

		assertEquals(4_294_967_296L, toLong(false, false, false, false, false, false, false, false,
				false, false, false, false, false, false, false, false,
				false, false, false, false, false, false, false, false,
				false, false, false, false, false, false, false, false,
				true));
		assertEquals(0xFFFFFFFFFFFFFFFFL, toLong(true, true, true, true, true, true, true, true,
				true, true, true, true, true, true, true, true,
				true, true, true, true, true, true, true, true,
				true, true, true, true, true, true, true, true,
				true, true, true, true, true, true, true, true,
				true, true, true, true, true, true, true, true,
				true, true, true, true, true, true, true, true,
				true, true, true, true, true, true, true, true));

		try {
			toByte(new boolean[9]);
			fail("toByte() did not throw IllegalArgumentException when providing bitset too large");
		} catch(IllegalArgumentException expected) {}


		try {
			toShort(new boolean[17]);
			fail("toShort() did not throw IllegalArgumentException when providing bitset too large");
		} catch(IllegalArgumentException expected) {}


		try {
			toInt(new boolean[33]);
			fail("toInt() did not throw IllegalArgumentException when providing bitset too large");
		} catch(IllegalArgumentException expected) {}


		try {
			toLong(new boolean[65]);
			fail("toByte() did not throw IllegalArgumentException when providing bitset too large");
		} catch(IllegalArgumentException expected) {}
	}

}
