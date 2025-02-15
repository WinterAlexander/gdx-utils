package com.winteralexander.gdx.utils;


import com.badlogic.gdx.utils.Array;

import java.lang.reflect.*;
import java.util.regex.Pattern;

import static com.winteralexander.gdx.utils.TypeUtil.isPrimitiveBox;
import static com.winteralexander.gdx.utils.Validation.ensureNotNull;
import static com.winteralexander.gdx.utils.collection.CollectionUtil.last;

/**
 * Utility class to use reflection on objects
 * <p>
 * Created on 2018-02-09.
 *
 * @author Alexander Winter
 */
@SuppressWarnings("deprecation")
public class ReflectionUtil {
	private ReflectionUtil() {}

	/**
	 * Swap all fields value for 2 objects
	 *
	 * @param o1 object 1
	 * @param o2 object 2
	 */
	public static void swapFields(Object o1, Object o2) {
		if(o1 == null || o2 == null)
			throw new IllegalArgumentException("Objects must not be null");

		if(!o1.getClass().equals(o2.getClass()))
			throw new IllegalArgumentException("Objects are not the same type");

		Class<?> type = o1.getClass();

		while(type != null) {
			for(Field field : type.getDeclaredFields()) {
				if(Modifier.isStatic(field.getModifiers()))
					continue;

				if(!field.isAccessible())
					field.setAccessible(true);

				try {
					Object tmp = field.get(o1);

					field.set(o1, field.get(o2));
					field.set(o2, tmp);
				} catch(IllegalAccessException ex) {
					throw new RuntimeException(ex);
				}
			}

			type = type.getSuperclass();
		}
	}

	/**
	 * Clones an object into another
	 *
	 * @param source      source object
	 * @param destination destination object
	 */
	public static void copy(Object source, Object destination) {
		ensureNotNull(source, "source");
		ensureNotNull(destination, "destination");

		if(!source.getClass().equals(destination.getClass()))
			throw new IllegalArgumentException("Objects are not the same type");

		Class<?> type = source.getClass();

		while(type != null) {
			for(Field field : type.getDeclaredFields()) {
				if(Modifier.isStatic(field.getModifiers()))
					continue;

				if(!field.isAccessible())
					field.setAccessible(true);

				try {
					field.set(destination, field.get(source));
				} catch(IllegalAccessException ex) {
					throw new RuntimeException(ex);
				}
			}

			type = type.getSuperclass();
		}
	}

	/**
	 * Sets the field of an object to a specified value
	 *
	 * @param object object to edit field's of
	 * @param field  field to edit
	 * @param value  value to set
	 */
	public static void set(Object object, String field, Object value) {
		ensureNotNull(object, "object");

		set(object.getClass(), object, field, value);
	}

	/**
	 * Sets the field of an object to a specified value
	 *
	 * @param type   type of object to set value of
	 * @param object object to edit field's of, or null if static field
	 * @param field  field to edit
	 * @param value  value to set
	 */
	public static void set(Class<?> type, Object object, String field, Object value) {
		ensureNotNull(type, "type");
		ensureNotNull(field, "field");
		ensureNotNull(value, "value");

		while(type != null) {
			try {
				Field fieldHandle = type.getDeclaredField(field);

				fieldHandle.setAccessible(true);

				fieldHandle.set(object, value);
				return;
			} catch(IllegalAccessException ex) {
				throw new RuntimeException(ex);
			} catch(NoSuchFieldException ignored) {}

			type = type.getSuperclass();
		}

		throw new RuntimeException("Field not found");
	}

	@SuppressWarnings("unchecked")
	public static <T> T getStatic(Class<?> type, String field) {
		return (T)getStatic(type, field, Object.class);
	}

	public static <T> T getStatic(Class<?> type, String field, Class<T> returnType) {
		ensureNotNull(type, "type");
		ensureNotNull(field, "field");
		ensureNotNull(returnType, "returnType");


		while(type != null) {
			try {
				Field fieldHandle = type.getDeclaredField(field);

				if(!fieldHandle.isAccessible())
					fieldHandle.setAccessible(true);

				return returnType.cast(fieldHandle.get(null));
			} catch(IllegalAccessException ex) {
				throw new RuntimeException(ex);
			} catch(NoSuchFieldException ignored) {}

			type = type.getSuperclass();
		}

		throw new RuntimeException("Field " + field + " not found for type " + type);
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(Object object, String field) {
		return (T)get(object, field, Object.class);
	}

	/**
	 * Gets the field of an object for a specified field name
	 *
	 * @param object object to edit field's of
	 * @param field  field to edit
	 * @param type   type of field
	 */
	public static <T> T get(Object object, String field, Class<T> type) {
		ensureNotNull(object, "object");
		ensureNotNull(field, "field");
		ensureNotNull(type, "type");

		Class<?> t = object.getClass();

		while(t != null) {
			try {
				Field fieldHandle = t.getDeclaredField(field);

				if(!fieldHandle.isAccessible())
					fieldHandle.setAccessible(true);

				return type.cast(fieldHandle.get(object));
			} catch(IllegalAccessException ex) {
				throw new RuntimeException(ex);
			} catch(NoSuchFieldException ignored) {}

			t = t.getSuperclass();
		}

		throw new RuntimeException("Field " + field + " not found for type " + object.getClass());
	}

	public static boolean has(Class<?> type, String field) {
		ensureNotNull(type, "type");
		ensureNotNull(field, "field");

		Class<?> t = type;

		while(t != null) {
			try {
				t.getDeclaredField(field);
				return true;
			} catch(NoSuchFieldException ignored) {}

			t = t.getSuperclass();
		}

		return false;
	}

	public static Class<?> getType(Class<?> type, String field) {
		ensureNotNull(type, "type");
		ensureNotNull(field, "field");

		Class<?> t = type;

		while(t != null) {
			try {
				return t.getDeclaredField(field).getType();
			} catch(NoSuchFieldException ignored) {}

			t = t.getSuperclass();
		}

		throw new RuntimeException("Field " + field + " not found for type " + type);
	}

	@SuppressWarnings({"unchecked", "StringEquality"})
	public static <T> T callStatic(Class<?> type, String method, Object... params) {
		ensureNotNull(method, "method");

		method = method.intern();

		while(type != null) {
			try {
				for(Method methodHandle : type.getDeclaredMethods()) {
					if(methodHandle.getName() != method)
						continue;

					if(methodHandle.getParameterCount() != params.length)
						continue;

					if(!methodHandle.isAccessible())
						methodHandle.setAccessible(true);

					try {
						return (T)methodHandle.invoke(null, params);
					} catch(IllegalArgumentException ignored) {}
				}
			} catch(IllegalAccessException | InvocationTargetException ex) {
				throw new RuntimeException(ex);
			}

			type = type.getSuperclass();
		}

		throw new RuntimeException("Method " + method + " not found for type " + type);
	}

	@SuppressWarnings({"unchecked", "StringEquality"})
	public static <T> T call(Object object, String method, Object... params) {
		ensureNotNull(object, "object");
		ensureNotNull(method, "method");

		method = method.intern();
		Class<?> t = object.getClass();

		while(t != null) {
			try {
				for(Method methodHandle : t.getDeclaredMethods()) {
					if(methodHandle.getName() != method)
						continue;

					if(methodHandle.getParameterCount() != params.length)
						continue;

					if(!methodHandle.isAccessible())
						methodHandle.setAccessible(true);

					try {
						return (T)methodHandle.invoke(object, params);
					} catch(IllegalArgumentException ignored) {}
				}
			} catch(IllegalAccessException | InvocationTargetException ex) {
				throw new RuntimeException(ex);
			}

			t = t.getSuperclass();
		}

		throw new RuntimeException("Method " + method + " not found for type " + object.getClass());
	}

	@SuppressWarnings("unchecked")
	public static <T> T construct(Class<T> type, Object... params) {
		ensureNotNull(type, "type");

		for(Constructor<?> constructor : type.getDeclaredConstructors()) {
			try {
				if(!constructor.isAccessible())
					constructor.setAccessible(true);

				return (T)constructor.newInstance(params);
			} catch(InstantiationException | IllegalArgumentException | IllegalAccessException ignored) {
				// continue
			} catch(InvocationTargetException ex) {
				throw new RuntimeException(ex);
			}
		}

		throw new RuntimeException("No matching constructor found");
	}

	public static String toPrettyString(Object object) {
		return toPrettyString(object, Integer.MAX_VALUE, 0, new Array<>());
	}

	public static String toPrettyString(Object object,
	                                     int maxDepth,
	                                     int indentationLevel) {
		return toPrettyString(object, maxDepth, indentationLevel, new Array<>());
	}

	private static String toPrettyString(Object object,
										 int maxDepth,
	                                     int indentationLevel,
	                                     Array<Object> objects) {
		objects.add(object);

		if(maxDepth <= 0)
			return object.toString() + '\n';

		StringBuilder sb = new StringBuilder();
		String newLine = System.lineSeparator();
		Class<?> type = object.getClass();

		sb.append(type.getSimpleName())
				.append('@')
				.append(Integer.toHexString(object.hashCode()))
				.append(newLine);

		for(int i = 0; i < indentationLevel; i++)
			sb.append('\t');

		if(type.isArray()) {
			if(!Object[].class.isAssignableFrom(type)) {
				int length = java.lang.reflect.Array.getLength(object);

				Object[] objArr = new Object[length];

				for(int i = 0; i < length; i++)
					objArr[i] = java.lang.reflect.Array.get(object, i);

				object = objArr;
			}

			sb.append('[').append(newLine);

			int n = 0;
			for(Object obj : (Object[])object) {
				for(int i = 0; i < indentationLevel + 1; i++)
					sb.append('\t');

				sb.append(n).append(": ");
				try {
					if(obj == null)
						sb.append("null").append(newLine);

					else if(obj.getClass().isAssignableFrom(String.class))
						sb.append("\"").append(obj).append("\"").append(newLine);

					else if(obj.getClass().isPrimitive()
							|| obj.getClass().isEnum()
							|| isPrimitiveBox(obj.getClass()))
						sb.append(obj).append(newLine);

					else if(objects.contains(obj, true))
						sb.append(obj.getClass().getSimpleName())
								.append('@')
								.append(Integer.toHexString(obj.hashCode()))
								.append(newLine);

					else
						sb.append(toPrettyString(obj, maxDepth - 1, indentationLevel + 1, objects));
				} catch(Throwable ex) {
					sb.append('(')
							.append(ex.getClass().getSimpleName())
							.append(')')
							.append(newLine);
				}
				n++;
			}

			for(int i = 0; i < indentationLevel; i++)
				sb.append('\t');
			sb.append(']').append(newLine);

		} else {

			sb.append('{').append(newLine);

			while(type != null) {
				for(Field field : type.getDeclaredFields()) {
					if(Modifier.isStatic(field.getModifiers()))
						continue;
					for(int i = 0; i < indentationLevel + 1; i++)
						sb.append('\t');

					if(!field.isAccessible())
						field.setAccessible(true);

					try {
						sb.append(field.getName()).append(": ");
						Object obj = field.get(object);

						if(obj == null)
							sb.append("null").append(newLine);

						else if(String.class.isAssignableFrom(field.getType()))
							sb.append("\"").append(obj).append("\"").append(newLine);

						else if(field.getType().isPrimitive() || field.getType().isEnum())
							sb.append(obj).append(newLine);

						else if(objects.contains(obj, true))
							sb.append(obj.getClass().getSimpleName())
									.append('@')
									.append(Integer.toHexString(obj.hashCode()))
									.append(newLine);

						else {
							sb.append(toPrettyString(obj, maxDepth - 1, indentationLevel + 1, objects));
						}
					} catch(Throwable ex) {
						sb.append("(").append(ex.getClass().getSimpleName()).append(")").append(newLine);
						ex.printStackTrace();
					}
				}

				type = type.getSuperclass();
			}

			for(int i = 0; i < indentationLevel; i++)
				sb.append('\t');
			sb.append('}').append(newLine);
		}

		return sb.toString();
	}

	@SuppressWarnings("raw")
	public static void disableAccessWarnings() {
		try {
			Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
			Field field = unsafeClass.getDeclaredField("theUnsafe");
			field.setAccessible(true);
			Object unsafe = field.get(null);

			Method putObjectVolatile = unsafeClass.getDeclaredMethod("putObjectVolatile",
					Object.class, long.class, Object.class);
			Method staticFieldOffset = unsafeClass.getDeclaredMethod("staticFieldOffset",
					Field.class);

			Class<?> loggerClass = Class.forName("jdk.internal.module.IllegalAccessLogger");
			Field loggerField = loggerClass.getDeclaredField("logger");
			Long offset = (Long)staticFieldOffset.invoke(unsafe, loggerField);
			putObjectVolatile.invoke(unsafe, loggerClass, offset, null);
		} catch(Exception ignored) {}
	}


	public static String getParentStackLocation() {
		return getParentStackLocation(3);
	}

	public static String getParentStackLocation(int parent) {
		Thread thread = Thread.currentThread();
		StackTraceElement[] elements = thread.getStackTrace();

		if(elements.length == 0)
			throw new IllegalStateException("Thread not started");

		int index = 1 + parent;

		if(elements.length <= index)
			throw new IllegalStateException("Thread stack not big enough to retrieve the parent " +
					"with depth " + parent);

		return last(elements[index].getClassName().split(Pattern.quote("."))) +
				"#" + elements[index].getMethodName() + "() " +
				"(" + elements[index].getFileName() + ":" + elements[index].getLineNumber() + ")";
	}
}
