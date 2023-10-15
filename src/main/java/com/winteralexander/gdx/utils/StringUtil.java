package com.winteralexander.gdx.utils;

import java.nio.ByteOrder;
import java.util.Iterator;
import java.util.Locale;

/**
 * Utility class for string operations
 * <p>
 * Created on 2018-01-04.
 *
 * @author Alexander Winter
 */
public class StringUtil {
	private static final char[] HEX_LOOKUP_TABLE_LOWER = new char[] {
			0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37,
			0x38, 0x39, 0x61, 0x62, 0x63, 0x64, 0x65, 0x66
	};
	private static final char[] HEX_LOOKUP_TABLE_UPPER = new char[] {
			0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37,
			0x38, 0x39, 0x41, 0x42, 0x43, 0x44, 0x45, 0x46
	};

	private StringUtil() {}

	public static String join(Iterable<String> strings, String separator) {
		Iterator<String> it = strings.iterator();

		if(!it.hasNext())
			return "";

		StringBuilder sb = new StringBuilder(it.next());

		while(it.hasNext())
			sb.append(separator).append(it.next());
		return sb.toString();
	}

	public static String join(String[] strings, String separator) {
		return join(strings, 0, strings.length, separator);
	}

	public static String join(String[] strings, int startIndex, int endIndex, String separator) {
		if(startIndex >= endIndex || endIndex > strings.length)
			throw new IndexOutOfBoundsException();

		if(strings.length == 0)
			return "";

		StringBuilder sb = new StringBuilder(strings[startIndex]);

		for(int i = startIndex + 1; i < endIndex; i++)
			sb.append(separator).append(strings[i]);
		return sb.toString();
	}

	public static boolean matchesFilter(String input, String filter) {
		if(filter == null
				|| filter.isEmpty()
				|| input == null
				|| input.isEmpty())
			return true;

		return containsIgnoreCase(input, filter);
	}

	public static String toString(long value, String digitSeparator) {
		char[] chars = Long.toString(value).toCharArray();
		StringBuilder sb = new StringBuilder(Math.round(chars.length * 1.25f)); //one digit separator every 4 chars

		for(int a = 0, i = chars.length - 1; i >= 0; a++, i--) {
			sb.insert(0, chars[i]);
			if(a >= 2 && i != 0) {
				sb.insert(0, digitSeparator);
				a = -1;
			}
		}
		return sb.toString();
	}

	/**
	 * Returns the parameter required to convert a number to an english ordinal
	 *
	 * @param value value to convert to ordinal
	 * @return english parameter in ordinal conversion
	 */
	public static int ordinalEnglishParam(int value) {
		return value % 100 >= 11 && value % 100 <= 13
				? 0
				: value % 10;
	}

	/**
	 * Returns the parameter required to convert a number to a swedish ordinal
	 *
	 * @param value value to convert to ordinal
	 * @return swedish parameter in ordinal conversion
	 */
	public static int ordinalSwedishParam(int value) {
		return value % 100 >= 11 && value % 100 <= 12
				? 0
				: value % 10;
	}

	/**
	 * Returns the parameter required for polish plural forms when used in translation
	 *
	 * @param value value to get plural parameter from
	 * @return plural polish parameter
	 */
	public static int pluralPolishParam(int value) {
		if(value < 0)
			return 0;

		if(value == 1)
			return -1;

		if(value % 100 >= 11 && value % 100 < 20)
			return 0;

		return value % 10;
	}

	public static boolean equalsIgnoreCase(String s1, String s2) {
		return s1 == null ? s2 == null : s1.equalsIgnoreCase(s2);
	}

	public static boolean containsIgnoreCase(String container, String element) {
		return container.toLowerCase(Locale.ENGLISH)
				.contains(element.toLowerCase(Locale.ENGLISH));
	}

	public static boolean containsIgnoreCase(String[] array, String element) {
		for(String current : array)
			if(equalsIgnoreCase(element, current))
				return true;
		return false;
	}

	public static int indexOfIgnoreCase(String[] array, String element) {
		for(int i = 0; i < array.length; i++)
			if(equalsIgnoreCase(element, array[i]))
				return i;
		return -1;
	}

	public static char lastChar(String string) {
		return string.charAt(string.length() - 1);
	}

	public static String padLeft(String string, char character, int count) {
		StringBuilder sb = new StringBuilder();

		for(int i = 0; i < count; i++)
			sb.append(character);
		sb.append(string);
		return sb.toString();
	}

	public static String quote(String value) {
		return "\"" + value.replaceAll("\\\\", "\\\\\\\\")
				.replaceAll("\"", "\\\\\"") + "\"";
	}

	/**
	 * Takes a byte array and returns its hexadecimal representation in string
	 *
	 * @param byteArray array of data to represent as hexadecimal string
	 * @param upperCase true for upper case, false for lower case
	 * @param byteOrder endianess of the hexadecimal representation
	 * @return hexadecimal representation of the data
	 * @see <a href="https://stackoverflow.com/a/58118078/5771029">StackOverflow answer</a>
	 */
	public static String toHexString(byte[] byteArray, boolean upperCase, ByteOrder byteOrder) {
		// our output size will be exactly 2x byte-array length
		final char[] buffer = new char[byteArray.length * 2];

		// choose lower or uppercase lookup table
		final char[] lookup = upperCase ? HEX_LOOKUP_TABLE_UPPER : HEX_LOOKUP_TABLE_LOWER;

		int index;
		for(int i = 0; i < byteArray.length; i++) {
			// for little endian we count from last to first
			index = (byteOrder == ByteOrder.BIG_ENDIAN) ? i : byteArray.length - i - 1;

			// extract the upper 4 bit and look up char (0-A)
			buffer[i << 1] = lookup[(byteArray[index] >> 4) & 0xF];
			// extract the lower 4 bit and look up char (0-A)
			buffer[(i << 1) + 1] = lookup[(byteArray[index] & 0xF)];
		}
		return new String(buffer);
	}

	public static String toHexString(byte[] byteArray) {
		return toHexString(byteArray, true, ByteOrder.BIG_ENDIAN);
	}
}
