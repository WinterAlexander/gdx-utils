package com.winteralexander.gdx.utils.io;

import com.winteralexander.gdx.utils.EnumConstantCache;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Double.doubleToLongBits;
import static java.lang.Double.longBitsToDouble;
import static java.lang.Float.floatToIntBits;
import static java.lang.Float.intBitsToFloat;

/**
 * Used to do the work of a DataInputStream and DataOutputStream without creating new objects
 * <p>
 * Created on 2016-12-15.
 *
 * @author Alexander Winter
 */
public class StreamUtil {
	public static boolean readBoolean(InputStream stream) throws IOException {
		int val = stream.read();

		if(val < 0)
			throw new EOFException();

		return val != 0;
	}

	public static void readBitset(InputStream stream, boolean[] outBits) throws IOException {
		if(outBits.length == 0)
			return;

		int byteCount = (outBits.length - 1) / 8 + 1;
		int left = outBits.length;
		for(int i = 0; i < byteCount; i++) {
			int val = readUnsignedByte(stream);
			for(int j = 0; j < Math.min(left, 8); j++)
				outBits[i * 8 + j] = (val & (1 << j)) != 0;
			left -= 8;
		}
	}

	public static byte readByte(InputStream stream) throws IOException {
		int val = stream.read();

		if(val < 0)
			throw new EOFException();

		return (byte)(val);
	}

	public static int readUnsignedByte(InputStream stream) throws IOException {
		int val = stream.read();

		if(val < 0)
			throw new EOFException();

		return val;
	}

	public static short readShort(InputStream stream) throws IOException {
		int val1 = stream.read();
		int val2 = stream.read();

		if((val1 | val2) < 0)
			throw new EOFException();

		return (short)((val1 << 8) + val2);
	}

	public static int readUnsignedShort(InputStream stream) throws IOException {
		int val1 = stream.read();
		int val2 = stream.read();

		if((val1 | val2) < 0)
			throw new EOFException();

		return (val1 << 8) + val2;
	}

	public static char readChar(InputStream stream) throws IOException {
		int val1 = stream.read();
		int val2 = stream.read();

		if((val1 | val2) < 0)
			throw new EOFException();

		return (char)((val1 << 8) + val2);
	}

	public static int readInt24(InputStream stream) throws IOException {
		int val1 = stream.read();
		int val2 = stream.read();
		int val3 = stream.read();

		if((val1 | val2 | val3) < 0)
			throw new EOFException();

		return (val1 << 16) + (val2 << 8) + val3;
	}

	public static int readInt(InputStream stream) throws IOException {
		int val1 = stream.read();
		int val2 = stream.read();
		int val3 = stream.read();
		int val4 = stream.read();

		if((val1 | val2 | val3 | val4) < 0)
			throw new EOFException();

		return (val1 << 24) + (val2 << 16) + (val3 << 8) + val4;
	}

	public static int readInt(InputStream stream, int maxValue) throws IOException {
		if(maxValue < 1 << 8)
			return readByte(stream);

		if(maxValue < 1 << 16)
			return readShort(stream);

		if(maxValue < 1 << 24)
			return readInt24(stream);

		return readInt(stream);
	}

	public static float readFloat(InputStream stream) throws IOException {
		return intBitsToFloat(readInt(stream));
	}

	public static long readLong(InputStream stream) throws IOException {
		int val1 = stream.read();
		int val2 = stream.read();
		int val3 = stream.read();
		int val4 = stream.read();
		int val5 = stream.read();
		int val6 = stream.read();
		int val7 = stream.read();
		int val8 = stream.read();

		if((val1 | val2 | val3 | val4 | val5 | val6 | val7 | val8) < 0)
			throw new EOFException();

		return ((long)((byte)val1) << 56) +
				((long)(((byte)val2) & 255) << 48) +
				((long)(((byte)val3) & 255) << 40) +
				((long)(((byte)val4) & 255) << 32) +
				((long)(((byte)val5) & 255) << 24) +
				((((byte)val6) & 255) << 16) +
				((((byte)val7) & 255) << 8) +
				(((byte)val8) & 255);
	}

	public static double readDouble(InputStream stream) throws IOException {
		return longBitsToDouble(readLong(stream));
	}

	public static <T extends Enum<T>> T readEnum(InputStream stream, Class<T> type) throws IOException {
		int val = readInt(stream);
		if(val == -1)
			return null;
		return EnumConstantCache.get(type)[val];
	}

	public static String readUTF(InputStream stream) throws IOException {
		int utflen = readUnsignedShort(stream);
		byte[] bytearr = new byte[utflen];
		char[] chararr = new char[utflen];

		int c, char2, char3;
		int chararr_count=0;

		int n = 0;
		while(n < utflen) {
			int count = stream.read(bytearr, n, utflen - n);
			if(count < 0)
				throw new EOFException();
			n += count;
		}

		int count = 0;
		while (count < utflen) {
			c = (int) bytearr[count] & 0xff;
			if (c > 127) break;
			count++;
			chararr[chararr_count++]=(char)c;
		}

		while (count < utflen) {
			c = (int) bytearr[count] & 0xff;
			switch (c >> 4) {
				case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7:
					/* 0xxxxxxx*/
					count++;
					chararr[chararr_count++]=(char)c;
					break;
				case 12: case 13:
					/* 110x xxxx   10xx xxxx*/
					count += 2;
					if (count > utflen)
						throw new UTFDataFormatException(
								"malformed input: partial character at end");
					char2 = (int) bytearr[count-1];
					if ((char2 & 0xC0) != 0x80)
						throw new UTFDataFormatException(
								"malformed input around byte " + count);
					chararr[chararr_count++]=(char)(((c & 0x1F) << 6) |
							(char2 & 0x3F));
					break;
				case 14:
					/* 1110 xxxx  10xx xxxx  10xx xxxx */
					count += 3;
					if (count > utflen)
						throw new UTFDataFormatException(
								"malformed input: partial character at end");
					char2 = (int) bytearr[count-2];
					char3 = (int) bytearr[count-1];
					if (((char2 & 0xC0) != 0x80) || ((char3 & 0xC0) != 0x80))
						throw new UTFDataFormatException(
								"malformed input around byte " + (count-1));
					chararr[chararr_count++]=(char)(((c     & 0x0F) << 12) |
							((char2 & 0x3F) << 6)  |
							((char3 & 0x3F) << 0));
					break;
				default:
					/* 10xx xxxx,  1111 xxxx */
					throw new UTFDataFormatException(
							"malformed input around byte " + count);
			}
		}
		// The number of chars produced may be less than utflen
		return new String(chararr, 0, chararr_count);
	}

	public static void writeBoolean(OutputStream stream, boolean value) throws IOException {
		stream.write(value ? 1 : 0);
	}

	public static void writeBitset(OutputStream stream, boolean[] bits) throws IOException {
		if(bits.length == 0)
			return;

		int byteCount = (bits.length - 1) / 8 + 1;
		int left = bits.length;
		for(int i = 0; i < byteCount; i++) {
			int val = 0;
			for(int j = 0; j < Math.min(left, 8); j++)
				val |= (bits[i * 8 + j] ? 1 : 0) << j;
			left -= 8;
			writeByte(stream, val);
		}
	}

	public static void writeByte(OutputStream stream, int value) throws IOException {
		stream.write(value);
	}

	public static void writeShort(OutputStream stream, int value) throws IOException {
		stream.write((value >>> 8) & 0xFF);
		stream.write(value & 0xFF);
	}

	public static void writeChar(OutputStream stream, int value) throws IOException {
		stream.write((value >>> 8) & 0xFF);
		stream.write(value & 0xFF);
	}

	public static void writeInt24(OutputStream stream, int value) throws IOException {
		stream.write((value >>> 16) & 0xFF);
		stream.write((value >>> 8) & 0xFF);
		stream.write(value & 0xFF);
	}

	public static void writeInt(OutputStream stream, int value) throws IOException {
		stream.write((value >>> 24) & 0xFF);
		stream.write((value >>> 16) & 0xFF);
		stream.write((value >>> 8) & 0xFF);
		stream.write(value & 0xFF);
	}

	public static void writeInt(OutputStream stream, int value, int maxValue) throws IOException {
		if(maxValue < 1 << 8)
			writeByte(stream, value);
		else if(maxValue < 1 << 16)
			writeShort(stream, value);
		else if(maxValue < 1 << 24)
			writeInt24(stream, value);
		else
			writeInt(stream, value);
	}

	public static void writeFloat(OutputStream stream, float value) throws IOException {
		writeInt(stream, floatToIntBits(value));
	}

	public static void writeLong(OutputStream stream, long value) throws IOException {
		stream.write((byte)(value >>> 56));
		stream.write((byte)(value >>> 48));
		stream.write((byte)(value >>> 40));
		stream.write((byte)(value >>> 32));
		stream.write((byte)(value >>> 24));
		stream.write((byte)(value >>> 16));
		stream.write((byte)(value >>> 8));
		stream.write((byte)(value));
	}

	public static void writeDouble(OutputStream stream, double value) throws IOException {
		writeLong(stream, doubleToLongBits(value));
	}

	public static void writeEnum(OutputStream stream, Enum<?> e) throws IOException {
		writeInt(stream, e == null ? -1 : e.ordinal());
	}

	public static void writeUTF(OutputStream stream, String string) throws IOException {
		int strlen = string.length();
		int utflen = 0;
		int c, count = 0;

		/* use charAt instead of copying String to char array */
		for (int i = 0; i < strlen; i++) {
			c = string.charAt(i);
			if ((c >= 0x0001) && (c <= 0x007F)) {
				utflen++;
			} else if (c > 0x07FF) {
				utflen += 3;
			} else {
				utflen += 2;
			}
		}

		if (utflen > 65535)
			throw new UTFDataFormatException(
					"encoded string too long: " + utflen + " bytes");

		byte[] bytearr = new byte[utflen+2];

		bytearr[count++] = (byte) ((utflen >>> 8) & 0xFF);
		bytearr[count++] = (byte) ((utflen >>> 0) & 0xFF);

		int i;
		for(i = 0; i < strlen; i++) {
			c = string.charAt(i);
			if (!((c >= 0x0001) && (c <= 0x007F)))
				break;
			bytearr[count++] = (byte) c;
		}

		for (; i < strlen; i++) {
			c = string.charAt(i);
			if ((c >= 0x0001) && (c <= 0x007F)) {
				bytearr[count++] = (byte) c;

			} else if (c > 0x07FF) {
				bytearr[count++] = (byte) (0xE0 | ((c >> 12) & 0x0F));
				bytearr[count++] = (byte) (0x80 | ((c >>  6) & 0x3F));
				bytearr[count++] = (byte) (0x80 | ((c >>  0) & 0x3F));
			} else {
				bytearr[count++] = (byte) (0xC0 | ((c >>  6) & 0x1F));
				bytearr[count++] = (byte) (0x80 | ((c >>  0) & 0x3F));
			}
		}
		stream.write(bytearr, 0, utflen + 2);
	}

	/**
	 * From Java 9
	 * @link <a href="https://github.com/AdoptOpenJDK/openjdk-jdk11/blob/master/src/java.base/share/classes/java/io/InputStream.java">InputStream.java</a>
	 */
	public byte[] readNBytes(InputStream stream, int len) throws IOException {
		if (len < 0) {
			throw new IllegalArgumentException("len < 0");
		}

		List<byte[]> bufs = null;
		byte[] result = null;
		int total = 0;
		int remaining = len;
		int n;
		do {
			byte[] buf = new byte[Math.min(remaining, 8192)];
			int nread = 0;

			// read to EOF which may read more or less than buffer size
			while ((n = stream.read(buf, nread,
					Math.min(buf.length - nread, remaining))) > 0) {
				nread += n;
				remaining -= n;
			}

			if (nread > 0) {
				if (Integer.MAX_VALUE - 8 - total < nread) {
					throw new OutOfMemoryError("Required array size too large");
				}
				total += nread;
				if (result == null) {
					result = buf;
				} else {
					if (bufs == null) {
						bufs = new ArrayList<>();
						bufs.add(result);
					}
					bufs.add(buf);
				}
			}
			// if the last call to read returned -1 or the number of bytes
			// requested have been read then break
		} while (n >= 0 && remaining > 0);

		if (bufs == null) {
			if (result == null) {
				return new byte[0];
			}
			return result.length == total ?
					result : Arrays.copyOf(result, total);
		}

		result = new byte[total];
		int offset = 0;
		remaining = total;
		for (byte[] b : bufs) {
			int count = Math.min(b.length, remaining);
			System.arraycopy(b, 0, result, offset, count);
			offset += count;
			remaining -= count;
		}

		return result;
	}

	/**
	 * From Java 9
	 * @link <a href="https://github.com/AdoptOpenJDK/openjdk-jdk11/blob/master/src/java.base/share/classes/java/io/InputStream.java">InputStream.java</a>
	 */
	public byte[] readAllBytes(InputStream stream) throws IOException {
		return readNBytes(stream, Integer.MAX_VALUE);
	}
}
