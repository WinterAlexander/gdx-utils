package com.winteralexander.gdx.utils;

/**
 * Utility class for very broad operations
 * <p>
 * Created on 2018-10-25.
 *
 * @author Alexander Winter
 */
public class ObjectUtil {
	private ObjectUtil() {}

	/**
	 * Returns the first non-null object of the specified parameters, in order
	 *
	 * @param o1 object 1
	 * @param o2 object 2
	 * @return first non-null object of parameters or null if none non-null
	 */
	public static <T> T firstNonNull(T o1, T o2) {
		return o1 != null ? o1 : o2;
	}

	/**
	 * Returns the first non-null object of the specified parameters, in order
	 *
	 * @param o1 object 1
	 * @param o2 object 2
	 * @param o3 object 3
	 * @return first non-null object of parameters or null if none non-null
	 */
	public static <T> T firstNonNull(T o1, T o2, T o3) {
		return o1 != null ? o1 : (o2 != null ? o2 : o3);
	}

	/**
	 * Returns the first non-null object of the specified objects
	 *
	 * @param objs objects to find non-null in
	 * @return first non-null object of specified objects or null if none non-null
	 */
	@SafeVarargs
	public static <T> T firstNonNull(T... objs) {
		for(T object : objs)
			if(object != null)
				return object;
		return null;
	}

	/**
	 * Returns the first non-negative integer of the specified parameters, in order
	 *
	 * @param i1 integer 1
	 * @param i2 integer 2
	 * @return first non-negative integer of parameters or -1 if none is non-negative
	 */
	public static int firstNonNegative(int i1, int i2) {
		return i1 >= 0 ? i1 :
				i2 >= 0 ? i2 : -1;
	}

	/**
	 * Returns the first non-negative integer of the specified parameters, in order
	 *
	 * @param i1 integer 1
	 * @param i2 integer 2
	 * @param i3 integer 3
	 * @return first non-negative integer of parameters or -1 if none non-negative
	 */
	public static int firstNonNegative(int i1, int i2, int i3) {
		return i1 > 0 ? i1 :
				i2 > 0 ? i2 :
				i3 > 0 ? i3 : -1;
	}

	/**
	 * Returns the first non-negative integer of the specified parameters, in order
	 *
	 * @param i1 integer 1
	 * @param i2 integer 2
	 * @param i3 integer 3
	 * @param i4 integer 4
	 * @return first non-negative integer of parameters or -1 if none non-negative
	 */
	public static int firstNonNegative(int i1, int i2, int i3, int i4) {
		return i1 > 0 ? i1 :
				i2 > 0 ? i2 :
				i3 > 0 ? i3 :
				i4 > 0 ? i4 : -1;
	}

	/**
	 * Returns the first non-negative integer of the specified parameters, in order
	 *
	 * @param i1 integer 1
	 * @param i2 integer 2
	 * @param i3 integer 3
	 * @param i4 integer 4
	 * @param i5 integer 5
	 * @return first non-negative integer of parameters or -1 if none non-negative
	 */
	public static long firstNonNegative(int i1, int i2, int i3, int i4, int i5) {
		return i1 > 0 ? i1 :
				i2 > 0 ? i2 :
				i3 > 0 ? i3 :
				i4 > 0 ? i4 :
				i5 > 0 ? i5 : -1;
	}

	/**
	 * Returns the first non-negative integer of the specified parameters, in order
	 *
	 * @param i1 integer 1
	 * @param i2 integer 2
	 * @param i3 integer 3
	 * @param i4 integer 4
	 * @param i5 integer 5
	 * @param i6 integer 6
	 * @return first non-negative integer of parameters or -1 if none non-negative
	 */
	public static long firstNonNegative(int i1, int i2, int i3, int i4, int i5, int i6) {
		return i1 > 0 ? i1 :
				i2 > 0 ? i2 :
				i3 > 0 ? i3 :
				i4 > 0 ? i4 :
				i5 > 0 ? i5 :
				i6 > 0 ? i6 : -1;
	}

	/**
	 * Returns the first non-negative integer of the specified integers
	 *
	 * @param ints integers to find non-negative in
	 * @return first non-negative integer of specified integers or -1 if none
	 * is non-negative
	 */
	public static int firstNonNegative(int... ints) {
		for(int i : ints)
			if(i > 0)
				return i;
		return -1;
	}

	/**
	 * Returns the first non-negative integer of the specified parameters, in order
	 *
	 * @param i1 long integer 1
	 * @param i2 long integer 2
	 * @return first non-negative integer of parameters or -1 if none is non-negative
	 */
	public static long firstNonNegative(long i1, long i2) {
		return i1 >= 0 ? i1 :
				i2 >= 0 ? i2 : -1;
	}

	/**
	 * Returns the first non-negative integer of the specified parameters, in order
	 *
	 * @param i1 long integer 1
	 * @param i2 long integer 2
	 * @param i3 long integer 3
	 * @return first non-negative integer of parameters or -1 if none non-negative
	 */
	public static long firstNonNegative(long i1, long i2, long i3) {
		return i1 > 0 ? i1 :
				i2 > 0 ? i2 :
				i3 > 0 ? i3 : -1;
	}

	/**
	 * Returns the first non-negative integer of the specified parameters, in order
	 *
	 * @param i1 long integer 1
	 * @param i2 long integer 2
	 * @param i3 long integer 3
	 * @param i4 long integer 4
	 * @return first non-negative integer of parameters or -1 if none non-negative
	 */
	public static long firstNonNegative(long i1, long i2, long i3, long i4) {
		return i1 > 0 ? i1 :
				i2 > 0 ? i2 :
				i3 > 0 ? i3 :
				i4 > 0 ? i4 : -1;
	}

	/**
	 * Returns the first non-negative integer of the specified parameters, in order
	 *
	 * @param i1 long integer 1
	 * @param i2 long integer 2
	 * @param i3 long integer 3
	 * @param i4 long integer 4
	 * @param i5 long integer 5
	 * @return first non-negative integer of parameters or -1 if none non-negative
	 */
	public static long firstNonNegative(long i1, long i2, long i3, long i4, long i5) {
		return i1 > 0 ? i1 :
				i2 > 0 ? i2 :
				i3 > 0 ? i3 :
				i4 > 0 ? i4 :
				i5 > 0 ? i5 : -1;
	}

	/**
	 * Returns the first non-negative integer of the specified parameters, in order
	 *
	 * @param i1 long integer 1
	 * @param i2 long integer 2
	 * @param i3 long integer 3
	 * @param i4 long integer 4
	 * @param i5 long integer 5
	 * @param i6 long integer 6
	 * @return first non-negative integer of parameters or -1 if none non-negative
	 */
	public static long firstNonNegative(long i1, long i2, long i3, long i4, long i5, long i6) {
		return i1 > 0 ? i1 :
				i2 > 0 ? i2 :
				i3 > 0 ? i3 :
				i4 > 0 ? i4 :
				i5 > 0 ? i5 :
				i6 > 0 ? i6 : -1;
	}

	/**
	 * Returns the first non-negative integer of the specified integers
	 *
	 * @param ints long integers to find non-negative in
	 * @return first non-negative integer of specified integers or -1 if none
	 * is non-negative
	 */
	public static long firstNonNegative(long... ints) {
		for(long i : ints)
			if(i > 0)
				return i;
		return -1;
	}
}
