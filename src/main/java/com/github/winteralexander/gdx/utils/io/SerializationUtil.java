package com.github.winteralexander.gdx.utils.io;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import java.io.*;

import static com.badlogic.gdx.graphics.Color.rgba8888;

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

	public static <T extends CustomSerializable> T readSerializable(InputStream inputStream, Class<T> type) throws IOException {
		T newInstance;
		try {
			newInstance = type.getDeclaredConstructor().newInstance();
		} catch(ReflectiveOperationException e) {
			throw new IOException("Failed to create an instance of " + type.getSimpleName(), e);
		}

		newInstance.readFrom(inputStream);
		return newInstance;
	}

	public static <T> void readSmallArray(InputStream stream, Class<T> type, Array<T> out) throws IOException {
		out.clear();
		int size = StreamUtil.readShort(stream);
		out.ensureCapacity(size);
		while(size-- > 0)
			out.add(readAny(stream, type));
	}

	public static void writeSmallArray(OutputStream stream, Array<?> array) throws IOException {
		StreamUtil.writeShort(stream, array.size);
		for(int i = 0; i < array.size; i++)
			writeAny(stream, array.get(i));
	}

	public static <T> void readArray(InputStream stream, Class<T> type, Array<T> out) throws IOException {
		out.clear();
		int size = StreamUtil.readInt(stream);
		out.ensureCapacity(size);
		while(size-- > 0)
			out.add(readAny(stream, type));
	}

	public static void writeArray(OutputStream stream, Array<?> array) throws IOException {
		StreamUtil.writeInt(stream, array.size);
		for(int i = 0; i < array.size; i++)
			writeAny(stream, array.get(i));
	}

	public static <K, V> void readMap(InputStream stream, Class<K> keyType, Class<V> valueType, ObjectMap<K, V> out) throws IOException {
		out.clear();
		int size = StreamUtil.readInt(stream);
		out.ensureCapacity(size);
		while(size-- > 0)
			out.put(readAny(stream, keyType), readAny(stream, valueType));
	}

	public static void writeMap(OutputStream stream, ObjectMap<?, ?> map) throws IOException {
		StreamUtil.writeInt(stream, map.size);
		for(ObjectMap.Entry<?, ?> entry : map.entries()) {
			writeAny(stream, entry.key);
			writeAny(stream, entry.value);
		}
	}

	public static void readColor(InputStream stream, Color out) throws IOException {
		Color.argb8888ToColor(out, StreamUtil.readInt(stream));
	}

	public static void writeColor(OutputStream stream, Color color) throws IOException {
		StreamUtil.writeInt(stream, Color.argb8888(color));
	}

	public static void readRGB(InputStream stream, Color out) throws IOException {
		float prevA = out.a;
		Color.argb8888ToColor(out, StreamUtil.readInt24(stream));
		out.a = prevA;
	}

	public static void writeRGB(OutputStream stream, Color color) throws IOException {
		StreamUtil.writeInt24(stream, Color.argb8888(color));
	}

	public static void readBuffered(InputStream stream, CustomSerializable serializable) throws IOException {
		int size = StreamUtil.readInt(stream);

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

	public static void writeBuffered(OutputStream stream, CustomSerializable serializable) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		serializable.writeTo(baos);
		StreamUtil.writeInt(stream, baos.size());
		baos.writeTo(stream);
	}

	@SuppressWarnings("unchecked")
	public static <T> T readPrimitive(InputStream stream, Class<T> type) throws IOException {
		if(type == int.class || type == Integer.class)
			return (T)Integer.valueOf(StreamUtil.readInt(stream));

		if(type == float.class || type == Float.class)
			return (T)Float.valueOf(StreamUtil.readFloat(stream));

		if(type == long.class || type == Long.class)
			return (T)Long.valueOf(StreamUtil.readLong(stream));

		if(type == short.class || type == Short.class)
			return (T)Short.valueOf(StreamUtil.readShort(stream));

		if(type == byte.class || type == Byte.class)
			return (T)Byte.valueOf(StreamUtil.readByte(stream));

		if(type == double.class || type == Double.class)
			return (T)Double.valueOf(StreamUtil.readDouble(stream));

		if(type == char.class || type == Character.class)
			return (T)Character.valueOf(StreamUtil.readChar(stream));

		if(type == boolean.class || type == Boolean.class)
			return (T)Boolean.valueOf(StreamUtil.readBoolean(stream));

		throw new IllegalArgumentException("Specified type must be primitive");
	}

	public static void writePrimitive(OutputStream outputStream, Object primitive) throws IOException {
		if(primitive instanceof Integer)
			StreamUtil.writeInt(outputStream, (Integer)primitive);

		else if(primitive instanceof Float)
			StreamUtil.writeFloat(outputStream, (Float)primitive);

		else if(primitive instanceof Long)
			StreamUtil.writeLong(outputStream, (Long)primitive);

		else if(primitive instanceof Short)
			StreamUtil.writeShort(outputStream, (Short)primitive);

		else if(primitive instanceof Byte)
			StreamUtil.writeByte(outputStream, (Byte)primitive);

		else if(primitive instanceof Double)
			StreamUtil.writeDouble(outputStream, (Double)primitive);

		else if(primitive instanceof Character)
			StreamUtil.writeChar(outputStream, (Character)primitive);

		else if(primitive instanceof Boolean)
			StreamUtil.writeBoolean(outputStream, (Boolean)primitive);

		else
			throw new IllegalArgumentException("Specified type isn't primitive: " + primitive);
	}

	/**
	 * Reads a number and its type from stream
	 *
	 * @return number read from stream
	 */
	public static Number readNumber(InputStream stream) throws IOException {
		int type = StreamUtil.readByte(stream);
		switch(type) {
			case INT_T:
				return StreamUtil.readInt(stream);

			case FLOAT_T:
				return StreamUtil.readFloat(stream);

			case LONG_T:
				return StreamUtil.readLong(stream);

			case DOUBLE_T:
				return StreamUtil.readDouble(stream);

			case SHORT_T:
				return StreamUtil.readShort(stream);

			case BYTE_T:
				return StreamUtil.readByte(stream);

			case NULL_T:
				return null;

			default:
				throw new IOException("Invalid primitive number type: " + type);
		}
	}

	/**
	 * Writes a number and its type into the stream
	 *
	 * @param number to write into stream
	 */
	public static void writeNumber(OutputStream stream, Number number) throws IOException {
		if(number == null) {
			StreamUtil.writeByte(stream, NULL_T);
			return;
		}

		if(number.getClass() == Integer.class) {
			StreamUtil.writeByte(stream, INT_T);
			StreamUtil.writeInt(stream, number.intValue());
		} else if(number.getClass() == Float.class) {
			StreamUtil.writeByte(stream, FLOAT_T);
			StreamUtil.writeFloat(stream, number.floatValue());
		} else if(number.getClass() == Long.class) {
			StreamUtil.writeByte(stream, LONG_T);
			StreamUtil.writeLong(stream, number.longValue());
		} else if(number.getClass() == Double.class) {
			StreamUtil.writeByte(stream, DOUBLE_T);
			StreamUtil.writeDouble(stream, number.doubleValue());
		} else if(number.getClass() == Short.class) {
			StreamUtil.writeByte(stream, SHORT_T);
			StreamUtil.writeShort(stream, number.shortValue());
		} else if(number.getClass() == Byte.class) {
			StreamUtil.writeByte(stream, BYTE_T);
			StreamUtil.writeByte(stream, number.byteValue());
		} else
			throw new IllegalArgumentException("Unknown number type: " + number.getClass());
	}

	@SuppressWarnings({"unchecked", "RedundantCast"})
	public static <T> T readAny(InputStream stream, Class<T> type) throws IOException {
		if(CustomSerializable.class.isAssignableFrom(type))
			// this cast is not redundant, this is an error from IntelliJ
			return (T)readSerializable(stream, type.asSubclass(CustomSerializable.class));
		else if(type == Color.class)
			return (T)new Color(StreamUtil.readInt(stream));
		else if(type == String.class)
			return (T)StreamUtil.readUTF(stream);
		else if(type == Vector2.class)
			return (T)new Vector2(StreamUtil.readFloat(stream), StreamUtil.readFloat(stream));
		else if(type == Vector3.class)
			return (T)new Vector3(StreamUtil.readFloat(stream), StreamUtil.readFloat(stream), StreamUtil.readFloat(stream));
		else if(type.isEnum())
			return (T)StreamUtil.readEnum(stream, type.asSubclass(Enum.class));
		else
			return readPrimitive(stream, type);
	}

	public static void writeAny(OutputStream stream, Object thing) throws IOException {
		if(thing instanceof CustomSerializable)
			((CustomSerializable)thing).writeTo(stream);
		else if(thing instanceof Color)
			StreamUtil.writeInt(stream, rgba8888((Color)thing));
		else if(thing instanceof String)
			StreamUtil.writeUTF(stream, (String)thing);
		else if(thing instanceof Vector2) {
			StreamUtil.writeFloat(stream, ((Vector2)thing).x);
			StreamUtil.writeFloat(stream, ((Vector2)thing).y);
		} else if(thing instanceof Vector3) {
			StreamUtil.writeFloat(stream, ((Vector3)thing).x);
			StreamUtil.writeFloat(stream, ((Vector3)thing).y);
			StreamUtil.writeFloat(stream, ((Vector3)thing).z);
		} else if(thing instanceof Enum)
			StreamUtil.writeEnum(stream, (Enum<?>)thing);
		else
			writePrimitive(stream, thing);
	}
}
