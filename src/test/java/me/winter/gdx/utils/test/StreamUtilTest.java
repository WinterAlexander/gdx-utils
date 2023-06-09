package me.winter.gdx.utils.test;

import org.junit.Test;

import java.io.*;

import static me.winter.gdx.utils.io.StreamUtil.readLong;
import static me.winter.gdx.utils.io.StreamUtil.writeLong;
import static org.junit.Assert.assertEquals;

/**
 * Tests StreamUtil
 * <p>
 * Created on 2018-05-28.
 *
 * @author Alexander Winter
 */
public class StreamUtilTest
{
	@Test
	public void testDataOutputStreamCompability() throws IOException
	{
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		DataOutputStream oStream = new DataOutputStream(outputStream);

		writeLong(outputStream, -1);
		oStream.writeLong(-1);

		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

		DataInputStream iStream = new DataInputStream(inputStream);

		assertEquals(iStream.readLong(), readLong(inputStream));
	}
}
