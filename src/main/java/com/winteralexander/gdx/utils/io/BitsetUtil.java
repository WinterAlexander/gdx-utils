package com.winteralexander.gdx.utils.io;

/**
 * Utility to convert bitsets to bytes or other integers
 * <p>
 * Created on 2024-10-31.
 *
 * @author Alexander Winter
 */
public class BitsetUtil {
	private BitsetUtil() {}

	public static byte toByte(boolean... bitset) {
		if(bitset.length > 8)
			throw new IllegalArgumentException("bitset of size " + bitset.length +
					" is too large to fit in a byte");
		int val = 0;
		for(int j = 0; j < bitset.length; j++)
			val |= (bitset[j] ? 1 : 0) << j;
		return (byte)val;
	}

	public static short toShort(boolean... bitset) {
		if(bitset.length > 16)
			throw new IllegalArgumentException("bitset of size " + bitset.length +
					" is too large to fit in a short");

		int val = 0;
		for(int j = 0; j < bitset.length; j++)
			val |= (bitset[j] ? 1 : 0) << j;
		return (short)val;
	}

	public static int toInt(boolean... bitset) {
		if(bitset.length > 32)
			throw new IllegalArgumentException("bitset of size " + bitset.length +
					" is too large to fit in an int");

		int val = 0;
		for(int j = 0; j < bitset.length; j++)
			val |= (bitset[j] ? 1 : 0) << j;
		return val;
	}

	public static long toLong(boolean... bitset) {
		if(bitset.length > 64)
			throw new IllegalArgumentException("bitset of size " + bitset.length +
					" is too large to fit in a long");

		long val = 0;
		for(int j = 0; j < bitset.length; j++)
			val |= (bitset[j] ? 1L : 0L) << j;
		return val;
	}
}
