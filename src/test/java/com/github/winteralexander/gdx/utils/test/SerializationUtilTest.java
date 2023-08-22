package com.github.winteralexander.gdx.utils.test;

import com.badlogic.gdx.graphics.Color;
import com.github.winteralexander.gdx.utils.io.SerializationUtil;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

import static com.github.winteralexander.gdx.utils.io.SerializationUtil.*;
import static org.junit.Assert.assertEquals;

/**
 * Tests the {@link SerializationUtil} utility class
 * <p>
 * Created on 2020-10-13.
 *
 * @author Alexander Winter
 */
public class SerializationUtilTest {
	@Test
	public void testColorSerialization() throws IOException {
		Random random = new Random();
		for(int i = 0; i < 20000; i++) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Color color = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), random.nextFloat());

			writeColor(baos, color);

			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

			Color color2 = new Color(0f, 0f, 0f, 0f);

			readColor(bais, color2);

			assertEquals(color.r, color2.r, 1f / 255f);
			assertEquals(color.g, color2.g, 1f / 255f);
			assertEquals(color.b, color2.b, 1f / 255f);
			assertEquals(color.a, color2.a, 1f / 255f);
		}
	}

	@Test
	public void testRGBSerialization() throws IOException {
		Random random = new Random();
		for(int i = 0; i < 20000; i++) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Color color = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), random.nextFloat());

			writeRGB(baos, color);

			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

			Color color2 = new Color(0f, 0f, 0f, 0.5f);

			readRGB(bais, color2);

			assertEquals(color.r, color2.r, 1f / 255f);
			assertEquals(color.g, color2.g, 1f / 255f);
			assertEquals(color.b, color2.b, 1f / 255f);
			assertEquals(0.5f, color2.a, 0.0000001f);
		}
	}
}
