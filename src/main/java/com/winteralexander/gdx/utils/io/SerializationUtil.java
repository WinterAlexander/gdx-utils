package com.winteralexander.gdx.utils.io;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Vector4;
import com.badlogic.gdx.utils.*;

import java.io.*;

import static com.badlogic.gdx.graphics.Color.rgba8888;
import static com.winteralexander.gdx.utils.io.StreamUtil.*;

/**
 * Serialization utility class
 * <p>
 * Created on 2018-06-03.
 *
 * @author Cedric Martens
 * @author Alexander Winter
 */
public class SerializationUtil {
	private static final byte INT_T = 0;
	private static final byte FLOAT_T = 1;
	private static final byte LONG_T = 2;
	private static final byte DOUBLE_T = 3;
	private static final byte SHORT_T = 4;
	private static final byte BYTE_T = 5;
	private static final byte NULL_T = 6;

	public static <T extends Readable> T readSerializable(InputStream inputStream,
														  Class<T> type) throws IOException {
		T newInstance;
		try {
			newInstance = type.getDeclaredConstructor().newInstance();
		} catch(ReflectiveOperationException e) {
			throw new IOException("Failed to create an instance of " + type.getSimpleName(), e);
		}

		newInstance.readFrom(inputStream);
		return newInstance;
	}

	public static <T> void readSmallArray(InputStream stream,
										  Class<T> type,
										  Array<T> out) throws IOException {
		out.clear();
		int size = readShort(stream);
		out.ensureCapacity(size);
		while(size-- > 0)
			out.add(readAny(stream, type));
	}

	public static void writeSmallArray(OutputStream stream,
									   Array<?> array) throws IOException {
		writeShort(stream, array.size);
		for(int i = 0; i < array.size; i++)
			writeAny(stream, array.get(i));
	}

	public static <T> void readArray(InputStream stream,
									 Class<T> type,
									 Array<T> out) throws IOException {
		out.clear();
		int size = readInt(stream);
		out.ensureCapacity(size);
		while(size-- > 0)
			out.add(readAny(stream, type));
	}

	public static void writeArray(OutputStream stream,
								  Array<?> array) throws IOException {
		writeInt(stream, array.size);
		for(int i = 0; i < array.size; i++)
			writeAny(stream, array.get(i));
	}

	public static <K, V> void readMap(InputStream stream,
									  Class<K> keyType,
									  Class<V> valueType,
									  ObjectMap<K, V> out) throws IOException {
		out.clear();
		int size = readInt(stream);
		out.ensureCapacity(size);
		while(size-- > 0)
			out.put(readAny(stream, keyType), readAny(stream, valueType));
	}

	public static void writeMap(OutputStream stream,
								ObjectMap<?, ?> map) throws IOException {
		writeInt(stream, map.size);
		for(ObjectMap.Entry<?, ?> entry : map.entries()) {
			writeAny(stream, entry.key);
			writeAny(stream, entry.value);
		}
	}

	public static void readColor(InputStream stream,
								 Color out) throws IOException {
		Color.argb8888ToColor(out, readInt(stream));
	}

	public static void writeColor(OutputStream stream,
								  Color color) throws IOException {
		writeInt(stream, Color.argb8888(color));
	}

	public static void readRGB(InputStream stream,
							   Color out) throws IOException {
		float prevA = out.a;
		Color.argb8888ToColor(out, readInt24(stream));
		out.a = prevA;
	}

	public static void writeRGB(OutputStream stream,
								Color color) throws IOException {
		writeInt24(stream, Color.argb8888(color));
	}

	public static void readBuffered(InputStream stream,
									Readable serializable) throws IOException {
		int size = readInt(stream);

		if(size < 0)
			throw new IOException("Invalid size of buffer: " + size);

		byte[] data = new byte[size];

		int totalRead = 0;
		do {
			int read = stream.read(data, totalRead, size - totalRead);

			if(read < 0)
				throw new EOFException();

			totalRead += read;
		}
		while(totalRead < size);

		serializable.readFrom(new ByteArrayInputStream(data));
	}

	public static void writeBuffered(OutputStream stream,
									 Writable serializable) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		serializable.writeTo(baos);
		writeInt(stream, baos.size());
		baos.writeTo(stream);
	}

	@SuppressWarnings("unchecked")
	public static <T> T readPrimitive(InputStream stream,
									  Class<T> type) throws IOException {
		if(type == int.class || type == Integer.class)
			return (T)Integer.valueOf(readInt(stream));

		if(type == float.class || type == Float.class)
			return (T)Float.valueOf(readFloat(stream));

		if(type == long.class || type == Long.class)
			return (T)Long.valueOf(readLong(stream));

		if(type == short.class || type == Short.class)
			return (T)Short.valueOf(readShort(stream));

		if(type == byte.class || type == Byte.class)
			return (T)Byte.valueOf(readByte(stream));

		if(type == double.class || type == Double.class)
			return (T)Double.valueOf(readDouble(stream));

		if(type == char.class || type == Character.class)
			return (T)Character.valueOf(readChar(stream));

		if(type == boolean.class || type == Boolean.class)
			return (T)Boolean.valueOf(readBoolean(stream));

		throw new IllegalArgumentException("Specified type must be primitive");
	}

	public static void writePrimitive(OutputStream outputStream,
									  Object primitive) throws IOException {
		if(primitive instanceof Integer)
			writeInt(outputStream, (Integer)primitive);

		else if(primitive instanceof Float)
			writeFloat(outputStream, (Float)primitive);

		else if(primitive instanceof Long)
			writeLong(outputStream, (Long)primitive);

		else if(primitive instanceof Short)
			writeShort(outputStream, (Short)primitive);

		else if(primitive instanceof Byte)
			writeByte(outputStream, (Byte)primitive);

		else if(primitive instanceof Double)
			writeDouble(outputStream, (Double)primitive);

		else if(primitive instanceof Character)
			writeChar(outputStream, (Character)primitive);

		else if(primitive instanceof Boolean)
			writeBoolean(outputStream, (Boolean)primitive);

		else
			throw new IllegalArgumentException("Specified type isn't primitive: " + primitive);
	}

	/**
	 * Reads a number and its type from stream
	 *
	 * @param stream to read from
	 * @return number read from stream
	 */
	public static Number readNumber(InputStream stream) throws IOException {
		int type = readByte(stream);
		switch(type) {
			case INT_T:
				return readInt(stream);

			case FLOAT_T:
				return readFloat(stream);

			case LONG_T:
				return readLong(stream);

			case DOUBLE_T:
				return readDouble(stream);

			case SHORT_T:
				return readShort(stream);

			case BYTE_T:
				return readByte(stream);

			case NULL_T:
				return null;

			default:
				throw new IOException("Invalid primitive number type: " + type);
		}
	}

	/**
	 * Writes a number and its type into the stream
	 *
	 * @param stream stream to write to
	 * @param number to write into stream
	 */
	public static void writeNumber(OutputStream stream, Number number) throws IOException {
		if(number == null) {
			writeByte(stream, NULL_T);
			return;
		}

		if(number.getClass() == Integer.class) {
			writeByte(stream, INT_T);
			writeInt(stream, number.intValue());
		} else if(number.getClass() == Float.class) {
			writeByte(stream, FLOAT_T);
			writeFloat(stream, number.floatValue());
		} else if(number.getClass() == Long.class) {
			writeByte(stream, LONG_T);
			writeLong(stream, number.longValue());
		} else if(number.getClass() == Double.class) {
			writeByte(stream, DOUBLE_T);
			writeDouble(stream, number.doubleValue());
		} else if(number.getClass() == Short.class) {
			writeByte(stream, SHORT_T);
			writeShort(stream, number.shortValue());
		} else if(number.getClass() == Byte.class) {
			writeByte(stream, BYTE_T);
			writeByte(stream, number.byteValue());
		} else
			throw new IllegalArgumentException("Unknown number type: " + number.getClass());
	}

	public static Vector2 readVec2(InputStream stream) throws IOException {
		return readVec2(stream, new Vector2());
	}

	public static Vector2 readVec2(InputStream stream, Vector2 out) throws IOException {
		out.x = readFloat(stream);
		out.y = readFloat(stream);
		return out;
	}

	public static Vector3 readVec3(InputStream stream) throws IOException {
		return readVec3(stream, new Vector3());
	}

	public static Vector3 readVec3(InputStream stream, Vector3 out) throws IOException {
		out.x = readFloat(stream);
		out.y = readFloat(stream);
		out.z = readFloat(stream);
		return out;
	}

	public static Vector4 readVec4(InputStream stream) throws IOException {
		return readVec4(stream, new Vector4());
	}

	public static Vector4 readVec4(InputStream stream, Vector4 out) throws IOException {
		out.x = readFloat(stream);
		out.y = readFloat(stream);
		out.z = readFloat(stream);
		out.w = readFloat(stream);
		return out;
	}

	public static void writeVec2(OutputStream stream, Vector2 vector) throws IOException {
		writeFloat(stream, vector.x);
		writeFloat(stream, vector.y);
	}

	public static void writeVec3(OutputStream stream, Vector3 vector) throws IOException {
		writeFloat(stream, vector.x);
		writeFloat(stream, vector.y);
		writeFloat(stream, vector.z);
	}

	public static void writeVec4(OutputStream stream, Vector4 vector) throws IOException {
		writeFloat(stream, vector.x);
		writeFloat(stream, vector.y);
		writeFloat(stream, vector.z);
		writeFloat(stream, vector.w);
	}

	@SuppressWarnings({"unchecked", "RedundantCast"})
	public static <T> T readAny(InputStream stream, Class<T> type) throws IOException {
		if(Readable.class.isAssignableFrom(type))
			// this cast is not redundant, this is an error from IntelliJ
			return (T)readSerializable(stream, type.asSubclass(Readable.class));
		else if(type == Color.class)
			return (T)new Color(readInt(stream));
		else if(type == String.class)
			return (T)readUTF(stream);
		else if(type == Vector2.class)
			return (T)readVec2(stream);
		else if(type == Vector3.class)
			return (T)readVec3(stream);
		else if(type == Vector4.class)
			return (T)readVec4(stream);
		else if(type.isEnum())
			return (T)readEnum(stream, type.asSubclass(Enum.class));
		else
			return readPrimitive(stream, type);
	}

	public static void writeAny(OutputStream stream, Object thing) throws IOException {
		if(thing instanceof Writable)
			((Writable)thing).writeTo(stream);
		else if(thing instanceof Color)
			writeInt(stream, rgba8888((Color)thing));
		else if(thing instanceof String)
			writeUTF(stream, (String)thing);
		else if(thing instanceof Vector2)
			writeVec2(stream, (Vector2)thing);
		else if(thing instanceof Vector3)
			writeVec3(stream, (Vector3)thing);
		else if(thing instanceof Vector4)
			writeVec4(stream, (Vector4)thing);
		else if(thing instanceof Enum)
			writeEnum(stream, (Enum<?>)thing);
		else
			writePrimitive(stream, thing);
	}

	public static void writeMany(OutputStream stream, Object first, Object second)
			throws IOException {
		writeAny(stream, first);
		writeAny(stream, second);
	}

	public static void writeMany(OutputStream stream, Object first, Object second, Object third)
			throws IOException {
		writeAny(stream, first);
		writeAny(stream, second);
		writeAny(stream, third);
	}

	public static void writeMany(OutputStream stream,
								 Object first,
								 Object second,
								 Object third,
								 Object fourth)
			throws IOException {
		writeAny(stream, first);
		writeAny(stream, second);
		writeAny(stream, third);
		writeAny(stream, fourth);
	}

	public static void writeMany(OutputStream stream,
								 Object first,
								 Object second,
								 Object third,
								 Object fourth,
								 Object fifth)
			throws IOException {
		writeAny(stream, first);
		writeAny(stream, second);
		writeAny(stream, third);
		writeAny(stream, fourth);
		writeAny(stream, fifth);
	}

	public static void writeMany(OutputStream stream, Object... things) throws IOException {
		for(Object thing : things)
			writeAny(stream, thing);
	}

	public static <T> IntMap<T> readIntMap(InputStream stream,
	                                       Class<T> type) throws IOException {
		return readIntMap(stream, type, new IntMap<>());
	}

	public static <T> IntMap<T> readIntMap(InputStream stream,
	                                       Class<T> type,
	                                       IntMap<T> out) throws IOException {
		int size = readInt(stream);
		for(int i = 0; i < size; i++) {
			int key = readInt(stream);
			T value = readAny(stream, type);
			out.put(key, value);
		}
		return out;
	}

	public static void writeIntMap(OutputStream stream, IntMap<?> map) throws IOException {
		writeInt(stream, map.size);
		for(IntMap.Entry<?> entry : map) {
			writeInt(stream, entry.key);
			writeAny(stream, entry.value);
		}
	}

	public static <T> LongMap<T> readLongMap(InputStream stream,
	                                         Class<T> type) throws IOException {
		return readLongMap(stream, type, new LongMap<>());
	}

	public static <T> LongMap<T> readLongMap(InputStream stream,
	                                         Class<T> type,
	                                         LongMap<T> out) throws IOException {
		int size = readInt(stream);
		for(int i = 0; i < size; i++) {
			long key = readLong(stream);
			T value = readAny(stream, type);
			out.put(key, value);
		}
		return out;
	}

	public static void writeLongMap(OutputStream stream, LongMap<?> map)
			throws IOException {
		writeInt(stream, map.size);
		for(LongMap.Entry<?> entry : map) {
			writeLong(stream, entry.key);
			writeAny(stream, entry.value);
		}
	}

	public static <K, V> ObjectMap<K, V> readObjectMap(InputStream stream,
	                                                   Class<K> keyType,
	                                                   Class<V> valueType) throws IOException {
		return readObjectMap(stream, keyType, valueType, new ObjectMap<>());
	}

	public static <K, V> ObjectMap<K, V> readObjectMap(InputStream stream,
	                                                   Class<K> keyType,
	                                                   Class<V> valueType,
	                                                   ObjectMap<K, V> out) throws IOException {
		int size = readInt(stream);
		for(int i = 0; i < size; i++) {
			K key = readAny(stream, keyType);
			V value = readAny(stream, valueType);
			out.put(key, value);
		}
		return out;
	}

	public static void writeObjectMap(OutputStream stream,
	                                  ObjectMap<?, ?> map) throws IOException {
		writeInt(stream, map.size);
		for(ObjectMap.Entry<?, ?> entry : map) {
			writeAny(stream, entry.key);
			writeAny(stream, entry.value);
		}
	}

	public static <T> ObjectSet<T> readObjectSet(InputStream stream,
	                                             Class<T> type) throws IOException {
		return readObjectSet(stream, type, new ObjectSet<>());
	}

	public static <T> ObjectSet<T> readObjectSet(InputStream stream,
	                                             Class<T> type,
	                                             ObjectSet<T> out) throws IOException {
		int size = readInt(stream);
		for(int i = 0; i < size; i++)
			out.add(readAny(stream, type));
		return out;
	}

	public static void writeObjectSet(OutputStream stream,
	                                  ObjectSet<?> set) throws IOException {
		writeInt(stream, set.size);
		for(Object val : set) {
			writeAny(stream, val);
		}
	}

	public static IntSet readIntSet(InputStream stream) throws IOException {
		return readIntSet(stream, new IntSet());
	}

	public static IntSet readIntSet(InputStream stream, IntSet out) throws IOException {
		int size = readInt(stream);
		for(int i = 0; i < size; i++)
			out.add(readInt(stream));
		return out;
	}

	public static void writeIntSet(OutputStream stream, IntSet set) throws IOException {
		writeInt(stream, set.size);
		IntSet.IntSetIterator it = set.iterator();
		while(it.hasNext)
			writeInt(stream, it.next());
	}

	public static IntFloatMap readIntFloatMap(InputStream stream) throws IOException {
		return readIntFloatMap(stream, new IntFloatMap());
	}

	public static IntFloatMap readIntFloatMap(InputStream stream,
	                                          IntFloatMap out) throws IOException {
		int size = readInt(stream);
		for(int i = 0; i < size; i++) {
			int key = readInt(stream);
			float val = readFloat(stream);
			out.put(key, val);
		}
		return out;
	}

	public static void writeIntFloatMap(OutputStream stream, IntFloatMap map) throws IOException {
		writeInt(stream, map.size);
		for(IntFloatMap.Entry entry : map) {
			writeInt(stream, entry.key);
			writeFloat(stream, entry.value);
		}
	}
}
