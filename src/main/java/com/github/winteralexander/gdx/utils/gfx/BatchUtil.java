package com.github.winteralexander.gdx.utils.gfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Utility class to draw in a Batch
 * <p>
 * Created on 2016-12-23.
 *
 * @author Alexander Winter
 */
public class BatchUtil {
	private final static float[] tmpVertices = new float[20];

	private BatchUtil() {}

	public static void setAlpha(Batch batch, float alpha) {
		Color color = batch.getColor();
		batch.setColor(color.r, color.g, color.b, alpha);
	}

	public static void premultiplyAlpha(Batch batch) {
		Color color = batch.getColor();
		batch.setColor(color.r * color.a, color.g * color.a, color.b * color.a, color.a);
	}

	public static void draw(Batch batch, Texture texture, Vector2 position, Vector2 origin, Vector2 size) {
		batch.draw(texture, position.x + origin.x, position.y + origin.y, size.x, size.y);
	}

	public static void draw(Batch batch, TextureRegion region, Vector2 position, Vector2 origin, Vector2 size) {
		batch.draw(region, position.x + origin.x, position.y + origin.y, size.x, size.y);
	}

	public static void draw(Batch batch, TextureRegion region, Vector2 position, Vector2 origin, Vector2 size, float rotation) {
		batch.draw(region, position.x + origin.x, position.y + origin.y, -origin.x, -origin.y, size.x, size.y, 1, 1, rotation);
	}

	public static void draw(Batch batch, TextureRegion region, float x, float y, float width, float height, boolean flippedX, boolean flippedY) {
		batch.draw(region, x + (flippedX ? width : 0), y + (flippedY ? height : 0), 0, 0, width, height, flippedX ? -1f : 1f, flippedY ? -1f : 1f, 0f);
	}

	public static void draw(Batch batch, TextureRegion region,
	                        float x, float y,
	                        float originX, float originY,
	                        float width, float height,
	                        float scaleX, float scaleY,
	                        float rotation,
	                        UVTransform uvTransform) {
		switch(uvTransform) {
			case NONE:
				batch.draw(region, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
				return;
			case FLIPPED_X:
				batch.draw(region.getTexture(), x, y, originX, originY, width, height, scaleX, scaleY, rotation,
						region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight(),
						true, false);
				return;
			case FLIPPED_Y:
				batch.draw(region.getTexture(), x, y, originX, originY, width, height, scaleX, scaleY, rotation,
						region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight(),
						false, true);
				return;
			case UPSIDE_DOWN:
				batch.draw(region.getTexture(), x, y, originX, originY, width, height, scaleX, scaleY, rotation,
						region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight(),
						true, true);
				return;
			case COUNTER_CLOCKWISE:
				batch.draw(region, x, y, originX, originY, width, height, scaleX, scaleY, rotation, false);
				return;
			case CLOCKWISE:
				batch.draw(region, x, y, originX, originY, width, height, scaleX, scaleY, rotation, true);
				return;

			case FLIPPED_CLOCKWISE:
			case FLIPPED_COUNTER_CLOCKWISE:
				throw new UnsupportedOperationException("UV transform not supported");
		}
	}

	public static void draw(Batch batch,
	                        TextureRegion region,
	                        float x1, float y1,
	                        float x2, float y2,
	                        float x3, float y3,
	                        float x4, float y4) {

		final float u = region.getU();
		final float v = region.getV2();
		final float u2 = region.getU2();
		final float v2 = region.getV();

		float color = batch.getPackedColor();
		tmpVertices[0] = x1;
		tmpVertices[1] = y1;
		tmpVertices[2] = color;
		tmpVertices[3] = u;
		tmpVertices[4] = v;

		tmpVertices[5] = x2;
		tmpVertices[6] = y2;
		tmpVertices[7] = color;
		tmpVertices[8] = u;
		tmpVertices[9] = v2;

		tmpVertices[10] = x3;
		tmpVertices[11] = y3;
		tmpVertices[12] = color;
		tmpVertices[13] = u2;
		tmpVertices[14] = v2;

		tmpVertices[15] = x4;
		tmpVertices[16] = y4;
		tmpVertices[17] = color;
		tmpVertices[18] = u2;
		tmpVertices[19] = v;
		batch.draw(region.getTexture(), tmpVertices, 0, 20);
	}
}
