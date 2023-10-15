package com.winteralexander.gdx.utils.test;

import com.winteralexander.gdx.utils.io.StreamUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

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
}
