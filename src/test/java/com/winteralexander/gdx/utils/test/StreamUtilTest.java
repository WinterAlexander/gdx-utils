package com.winteralexander.gdx.utils.test;

import com.badlogic.gdx.graphics.Color;
import com.winteralexander.gdx.utils.io.StreamUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.Random;

import static com.winteralexander.gdx.utils.io.SerializationUtil.readColor;
import static com.winteralexander.gdx.utils.io.SerializationUtil.writeColor;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Tests StreamUtil
 * <p>
 * Created on 2018-05-28.
 *
 * @author Alexander Winter
 */
public class StreamUtilTest {
	@Test
	public void testDataOutputStreamCompability() throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		DataOutputStream oStream = new DataOutputStream(outputStream);

		StreamUtil.writeLong(outputStream, -1);
		oStream.writeLong(-1);

		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

		DataInputStream iStream = new DataInputStream(inputStream);

		Assert.assertEquals(iStream.readLong(), StreamUtil.readLong(inputStream));
	}

	@Test
	public void testBitsetSerialization() throws IOException {
		Random random = new Random();
		for(int i = 0; i < 20000; i++) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			boolean[] bitset = new boolean[random.nextInt(1000)];

			for(int j = 0; j < bitset.length; j++)
				bitset[j] = random.nextBoolean();

			StreamUtil.writeBitset(baos, bitset);

			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

			boolean[] read = new boolean[bitset.length];

			StreamUtil.readBitset(bais, read);

			assertArrayEquals(bitset, read);
		}
	}
}
