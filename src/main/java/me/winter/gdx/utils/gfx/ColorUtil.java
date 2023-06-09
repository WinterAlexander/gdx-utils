package me.winter.gdx.utils.gfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

import java.util.Random;

import static me.winter.gdx.utils.Validation.ensureNotNull;

/**
 * Utility class for libgdx colors
 * <p>
 * Created on 2018-06-16.
 *
 * @author Alexander Winter
 */
public class ColorUtil {
	private ColorUtil() {}

	public static boolean isBlack(Color color) {
		return (color.toIntBits() & 0x00FFFFFF) == 0;
	}

	public static void toArrayRGB(Color color, float[] array) {
		ensureNotNull(color, "color");
		ensureNotNull(array, "array");

		array[0] = color.r;
		array[1] = color.g;
		array[2] = color.b;
	}

	public static Color randomRGBColor(Random random, Color out) {
		out.r = random.nextFloat();
		out.g = random.nextFloat();
		out.b = random.nextFloat();
		out.a = 1f;
		return out;
	}

	public static Color randomRGBColor(Random random) {
		return randomRGBColor(random, new Color());
	}

	public static Color randomRGBColor(Color out) {
		return randomRGBColor(MathUtils.random, out);
	}

	public static Color randomRGBColor() {
		return randomRGBColor(new Color());
	}
}
