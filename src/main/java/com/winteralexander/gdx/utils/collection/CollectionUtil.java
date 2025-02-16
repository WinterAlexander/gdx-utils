package com.winteralexander.gdx.utils.collection;

import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.winteralexander.gdx.utils.collection.iterator.JavaArrayIndexIterator;

import java.util.*;
import java.util.function.Function;

/**
 * Utility class for collections
 * <p>
 * Created on 2016-12-20.
 *
 * @author Alexander Winter
 */
public class CollectionUtil {
	private static final Random DEFAULT_RANDOM = new Random();

	private static final Array<?> EMPTY_ARRAY = new Array<>(0);

	/**
	 * Counts the number of element in an iterable by iterating through it
	 *
	 * @param iterable iterable to compute the size of
	 * @return number of element in iterable
	 */
	public static int sizeOf(Iterable<?> iterable) {
		int count = 0;
		for(Object ignored : iterable)
			count++;
		return count;
	}

	/**
	 * Counts the number of element left in an iterator by iterating through it
	 *
	 * @param iterator iterator to compute the size of
	 * @return number of element in iterator
	 */
	public static int sizeOf(Iterator<?> iterator) {
		int count = 0;
		for(; iterator.hasNext(); iterator.next())
			count++;
		return count;
	}

	/**
	 * Retrieves the last element of an array of integers
	 * @param array array to get the last element of
	 * @return last element
	 */
	public static int last(int[] array) {
		return array[array.length - 1];
	}

	/**
	 * Retrieves the last element of an array of floats
	 * @param array array to get the last element of
	 * @return last element
	 */
	public static float last(float[] array) {
		return array[array.length - 1];
	}

	/**
	 * Retrieves the last element of an array of longs
	 * @param array array to get the last element of
	 * @return last element
	 */
	public static long last(long[] array) {
		return array[array.length - 1];
	}

	/**
	 * Retrieves the last element of an array of doubles
	 * @param array array to get the last element of
	 * @return last element
	 */
	public static double last(double[] array) {
		return array[array.length - 1];
	}

	/**
	 * Retrieves the last element of an array of shorts
	 * @param array array to get the last element of
	 * @return last element
	 */
	public static short last(short[] array) {
		return array[array.length - 1];
	}

	/**
	 * Retrieves the last element of an array of bytes
	 * @param array array to get the last element of
	 * @return last element
	 */
	public static byte last(byte[] array) {
		return array[array.length - 1];
	}

	/**
	 * Retrieves the last element of an array of booleans
	 * @param array array to get the last element of
	 * @return last element
	 */
	public static boolean last(boolean[] array) {
		return array[array.length - 1];
	}

	/**
	 * Retrieves the last element of an array
	 * @param array array to get the last element of
	 * @return last element
	 * @param <T> type of array elements
	 */
	public static <T> T last(T[] array) {
		return array[array.length - 1];
	}

	/**
	 * Retrieves the last element of an array
	 * @param array array to get the last element of
	 * @return last element
	 * @param <T> type of array elements
	 */
	public static <T> T last(Array<T> array) {
		if(array.size == 0)
			throw new IllegalArgumentException("Array is empty");

		return array.get(array.size - 1);
	}

	/**
	 * Selects the element of the array that is of greatest value according to the comparator. In
	 * case of equality, it returns the first one encountered in the array
	 * @param array array to select greatest element
	 * @param comparator to determine greatest element
	 * @return greatest element in array
	 * @param <T> type of array elements
	 */
	public static <T> T greatest(T[] array, Comparator<T> comparator) {
		if(array.length == 0)
			throw new IllegalArgumentException("Array is empty");

		T greatest = array[0];
		for(int i = 1; i < array.length; i++)
			if(comparator.compare(array[i], greatest) > 0)
				greatest = array[i];
		return greatest;
	}

	/**
	 * Provides an iterable over the range of an array, without copying the array
	 *
	 * @param array array to get iterable over a range of its values
	 * @param startIndex start index of the range, inclusive
	 * @param endIndex end index of the range, exclusive
	 * @return iterable object to iterange over range of array
	 * @param <T> type of array elements
	 */
	public static <T> Iterable<T> range(T[] array, int startIndex, int endIndex) {
		return new JavaArrayIndexIterator<>(array, startIndex, endIndex);
	}

	/**
	 * Returns a random element in the specified array. NOT THREAD SAFE. Use
	 * {@link #random(Random, Object[])} for thread safe version
	 *
	 * @param array array of elements to pick from
	 * @return a random element in the array
	 * @param <T> type of elements
	 */
	public static <T> T random(T[] array) {
		return random(DEFAULT_RANDOM, array);
	}

	/**
	 * Returns a random element in the specified array.
	 *
	 * @param random random number generator to use
	 * @param array array of elements to pick from
	 * @return a random element in the array
	 * @param <T> type of elements
	 */
	public static <T> T random(Random random, T[] array) {
		return array[random.nextInt(array.length)];
	}

	/**
	 * Finds the index of an element in an array. Equality is defined by the equals() function. If
	 * the element could not be found in the array, returns -1 instead.
	 *
	 * @param array array of elements to find the index
	 * @param element element to find
	 * @return index of element in array, or -1 if not found
	 * @param <T> type of elements
	 */
	public static <T> int indexOf(T[] array, T element) {
		for(int i = 0; i < array.length; i++)
			if(Objects.equals(element, array[i]))
				return i;

		return -1;
	}

	/**
	 * Evaluates elements of an array and returns true if any of the element match the specified
	 * predicate. May not evaluate all elements if one is found to be true early.
	 *
	 * @param array array of elements to evaluate
	 * @param predicate predicate to test on elements
	 * @return true if the predicate matches any element in the array
	 * @param <T> type of elements
	 */
	public static <T> boolean any(Iterable<T> array, Predicate<T> predicate) {
		for(T element : array)
			if(predicate.evaluate(element))
				return true;

		return false;
	}

	/**
	 * Evaluates elements of an array and returns true if any of the elements matches the specified
	 * predicate. May not evaluate all elements if one is found to be true early.
	 *
	 * @param array array of elements to evaluate
	 * @param predicate predicate to test on elements
	 * @return true if the predicate matches any element in the array
	 * @param <T> type of elements
	 */
	public static <T> boolean any(T[] array, Predicate<T> predicate) {
		for(T element : array)
			if(predicate.evaluate(element))
				return true;

		return false;
	}

	/**
	 * Evaluates elements of an array and returns true if all the elements match the specified
	 * predicate. May not evaluate all elements if one is found to be false early.
	 *
	 * @param array array of elements to evaluate
	 * @param predicate predicate to test on elements
	 * @return true if the predicate matches all elements in the array
	 * @param <T> type of elements
	 */
	public static <T> boolean all(Iterable<T> array, Predicate<T> predicate) {
		for(T element : array)
			if(!predicate.evaluate(element))
				return false;

		return true;
	}

	/**
	 * Evaluates elements of an array and returns true if all the elements match the specified
	 * predicate. May not evaluate all elements if one is found to be false early.
	 *
	 * @param array array of elements to evaluate
	 * @param predicate predicate to test on elements
	 * @return true if the predicate matches all elements in the array
	 * @param <T> type of elements
	 */
	public static <T> boolean all(T[] array, Predicate<T> predicate) {
		for(T element : array)
			if(!predicate.evaluate(element))
				return false;

		return true;
	}

	public static <T> boolean anyDuplicates(T[] array) {
		return anyDuplicates(array, new ObjectSet<>());
	}

	public static <T> boolean anyDuplicates(T[] array, ObjectSet<T> tmpSet) {
		tmpSet.addAll(array);
		return tmpSet.size < array.length;
	}

	public static <T> boolean anyDuplicates(Array<T> array) {
		return anyDuplicates(array, new ObjectSet<>());
	}

	public static <T> boolean anyDuplicates(Array<T> array, ObjectSet<T> tmpSet) {
		tmpSet.addAll(array);
		return tmpSet.size < array.size;
	}

	public static <T> boolean contains(T[] array, T value) {
		for(T current : array)
			if(Objects.equals(current, value))
				return true;
		return false;
	}

	public static boolean contains(int[] array, int value) {
		for(int current : array)
			if(current == value)
				return true;
		return false;
	}

	@SafeVarargs
	public static <T> Array<T> toGdxArray(T... array) {
		return new Array<>(array);
	}

	public static <T> Array<T> toGdxArray(Iterable<T> iterable) {
		return toGdxArray(iterable.iterator());
	}

	public static <T> Array<T> toGdxArray(Iterator<T> iterator) {
		Array<T> array = new Array<>();
		while(iterator.hasNext())
			array.add(iterator.next());
		return array;
	}

	@SafeVarargs
	public static <T> T[] toArray(T... values) {
		return values;
	}

	public static <T> T[] toArray(Class<T> type, Iterable<T> iterable) {
		return toArray(type, iterable.iterator());
	}

	public static <T> T[] toArray(Class<T> type, Iterator<T> iterator) {
		return toGdxArray(iterator).toArray(type);
	}

	@SafeVarargs
	public static <T> T[] mergeWithArray(T[] arrayA, T... arrayB) {
		return mergeArrays(arrayA, arrayB);
	}

	@SafeVarargs
	public static <T> T[] mergeWithArray(Class<?> type, T[] arrayA, T... arrayB) {
		return mergeArrays(type, arrayA, arrayB);
	}

	@SafeVarargs
	public static <T> T[] mergeArrays(T[]... arrays) {
		return mergeArrays(arrays[0].getClass().getComponentType(), arrays);
	}

	@SafeVarargs
	public static <T> T[] mergeArrays(Class<?> type, T[]... arrays) {
		if(arrays.length == 0)
			throw new IllegalArgumentException("array of array mustn't be empty");

		int totalLength = 0;

		for(T[] array : arrays)
			totalLength += array.length;

		@SuppressWarnings("unchecked")
		T[] result = (T[])java.lang.reflect.Array.newInstance(type, totalLength);

		int index = 0;
		for(T[] array : arrays) {
			System.arraycopy(array, 0, result, index, array.length);
			index += array.length;
		}

		return result;
	}

	@SafeVarargs
	public static <T extends Enum<T>> T[] valuesExcept(Class<T> enumClass, T... values) {
		Array<T> constants = new Array<>(enumClass.getEnumConstants());
		constants.removeAll(new Array<>(values), false);
		return constants.toArray(enumClass);
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] except(Class<T> type, T[] array, T element) {
		int index = indexOf(array, element);
		if(index == -1)
			return array;

		T[] cpy = (T[])java.lang.reflect.Array.newInstance(type, array.length - 1);

		for(int i = 0; i < array.length; i++)
			if(i != index)
				cpy[i > index ? i - 1 : i] = array[i];

		return cpy;
	}

	public static <T> void sort(Array<T> array, Comparator<T> comparator) {
		Arrays.sort(array.items, 0, array.size, comparator);
	}

	public static <K, V> void putAll(IdentityMap<K, V> into,
	                                 IdentityMap<? extends K, ? extends V> content) {
		into.ensureCapacity(content.size);
		for(Entry<? extends K, ? extends V> entry : content)
			into.put(entry.key, entry.value);
	}

	/**
	 * Adds all the values of an iterable to an {@link ObjectSet}
	 *
	 * @param set set to add values to
	 * @param values values to add
	 * @param <T> type of elements
	 */
	public static <T> void addAll(ObjectSet<T> set, Iterable<T> values) {
		for(T value : values)
			set.add(value);
	}

	/**
	 * Adds all the integers of an iterable to an {@link IntSet}
	 *
	 * @param set set to add values to
	 * @param values values to add
	 */
	public static void addAll(IntSet set, Iterable<Integer> values) {
		for(Integer value : values)
			set.add(value);
	}

	/**
	 * Adds all the values of an iterable to an {@link Array}
	 *
	 * @param array array to add values to
	 * @param values values to add
	 */
	public static <T> void addAll(Array<T> array, Iterable<T> values) {
		for(T value : values)
			array.add(value);
	}

	public static Object[] toObjectArray(int[] intArray) {
		Object[] res = new Object[intArray.length];

		for(int i = 0; i < intArray.length; i++)
			res[i] = intArray[i];

		return res;
	}

	public static <K, V> ObjectMap<K, V> buildMap(K[] keys, Function<K, V> builder) {
		ObjectMap<K, V> map = new ObjectMap<>(keys.length);
		for(K key : keys)
			map.put(key, builder.apply(key));
		return map;
	}

	public static <K, V> ObjectMap<K, V> buildMap(K[] keys, V[] values) {
		if(keys.length != values.length)
			throw new IllegalArgumentException("Number of keys and values mismatch");

		ObjectMap<K, V> map = new ObjectMap<>(keys.length);
		for(int i = 0; i < keys.length; i++)
			map.put(keys[i], values[i]);

		return map;
	}

	public static <K, V> ObjectMap<K, V> buildMap(Array<K> keys, Function<K, V> builder) {
		ObjectMap<K, V> map = new ObjectMap<>(keys.size);
		for(K key : keys)
			map.put(key, builder.apply(key));
		return map;
	}

	public static <K, V> ObjectMap<K, V> buildMap(Array<K> keys, Array<V> values) {
		if(keys.size != values.size)
			throw new IllegalArgumentException("Number of keys and values mismatch");

		ObjectMap<K, V> map = new ObjectMap<>(keys.size);
		for(int i = 0; i < keys.size; i++)
			map.put(keys.get(i), values.get(i));

		return map;
	}

	public static <T, R> Array<R> mapArray(T[] input, Function<T, R> mapper) {
		return mapArray(input, mapper, new Array<>());
	}

	public static <T, R> Array<R> mapArray(T[] input, Function<T, R> mapper, Array<R> output) {
		output.clear();
		output.ensureCapacity(input.length);
		for(T t : input)
			output.add(mapper.apply(t));
		return output;
	}

	public static <T, R> Array<R> mapArray(Array<T> input, Function<T, R> mapper) {
		return mapArray(input, mapper, new Array<>());
	}

	public static <T, R> Array<R> mapArray(Array<T> input, Function<T, R> mapper, Array<R> output) {
		output.clear();
		output.ensureCapacity(input.size);
		for(int i = 0; i < input.size; i++)
			output.add(mapper.apply(input.get(i)));
		return output;
	}

	@SuppressWarnings({ "rawtypes", "RedundantCast", "unchecked" })
	public static <T> Iterable<T> castIterable(Iterable<?> other) {
		return (Iterable<T>)(Iterable)other;
	}

	public static boolean allInstanceOf(Iterable<?> iterable, Class<?> type) {
		return all(iterable, type::isInstance);
	}

	@SuppressWarnings("unchecked")
	public static <T> Array<T> emptyArray() {
		return (Array<T>)EMPTY_ARRAY;
	}
}
