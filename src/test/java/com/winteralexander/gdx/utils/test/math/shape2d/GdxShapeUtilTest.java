package com.winteralexander.gdx.utils.test.math.shape2d;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.winteralexander.gdx.utils.math.shape2d.GdxShapeUtil;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link GdxShapeUtil}
 * <p>
 * Created on 2026-04-19.
 *
 * @author Alexander Winter
 */
public class GdxShapeUtilTest {
	@Test
	public void testRectangleNegativeSpaceSpecific() {
		Rectangle bound = new Rectangle();
		bound.set(0f, 0f, 100f, 100f);

		Rectangle[] rects = new Rectangle[] {
				new Rectangle(10f, 20f, 10f, 5f),
				new Rectangle(90f, 20f, 10f, 12f),
				new Rectangle(30f, 40f, 10f, 10f),
				new Rectangle(30f, 70f, 10f, 10f),
				new Rectangle(80f, 90f, 5f, 5f),
		};

		Array<Rectangle> out = new Array<>();

		GdxShapeUtil.computeNegativeSpace(bound, rects, rect -> out.add(new Rectangle(rect)));

		for(Rectangle input : rects)
			for(Rectangle output : out)
				assertFalse(input.overlaps(output));

		float inputArea = 0f, outputArea = 0f;

		for(Rectangle input : rects)
			inputArea += input.area();

		for(Rectangle output : out) {
			outputArea += output.area();
			assertTrue(output.area() > 0f);
		}

		assertEquals(bound.area(), inputArea + outputArea, 1e-6f);
	}

	@Test
	public void testRectangleNegativeSpaceRandom() {
		Random random = new Random();
		for(int it = 0; it < 1_000; it++) {
			Rectangle bound = new Rectangle();
			bound.set(0f, 0f, 100f, 100f);

			Rectangle[] inputs = new Rectangle[10];

			for(int i = 0; i < 10; i++) {
				boolean overlap;
				do {
					int width = random.nextInt(19) + 1;
					int height = random.nextInt(19) + 1;
					int x = random.nextInt(100 - width);
					int y = random.nextInt(100 - height);
					inputs[i] = new Rectangle(x, y, width, height);

					overlap = false;
					for(int j = 0; j < i; j++) {
						if(inputs[i].overlaps(inputs[j])) {
							overlap = true;
							break;
						}
					}
				} while(overlap);
			}

			Array<Rectangle> outputs = new Array<>();

			GdxShapeUtil.computeNegativeSpace(bound,
					inputs,
					rect -> outputs.add(new Rectangle(rect)));

			for(Rectangle input : inputs)
				for(Rectangle output : outputs)
					assertFalse(input.overlaps(output));

			float inputArea = 0f, outputArea = 0f;

			for(Rectangle input : inputs)
				inputArea += input.area();

			for(Rectangle output : outputs) {
				outputArea += output.area();
				assertTrue(output.area() > 0f);
			}

			assertEquals(bound.area(), inputArea + outputArea, 1e-1f);
		}
	}

	@Deprecated
	private static void debugImage(Array<Rectangle> out) {
		try {
			BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
			for(Rectangle input : out)
				img.getGraphics().fillRect((int)input.x,
						(int)input.y,
						(int)input.width,
						(int)input.height);
			ImageIO.write(img, "PNG", new File("out.png"));
		} catch(IOException ex) {
			throw new RuntimeException(ex);
		}
	}
}
