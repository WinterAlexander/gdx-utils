package com.winteralexander.gdx.utils.gfx;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;

/**
 * Utility class to interact with textures and texture regions
 * <p>
 * Created on 2018-05-17.
 *
 * @author Alexander Winter
 */
public class TextureUtil {
	private TextureUtil() {}

	public static float[] getUVs(TextureRegion region) {
		return getUVs(region, new float[4]);
	}

	public static float[] getUVs(TextureRegion region, float[] destination) {
		if(destination.length != 4)
			throw new IllegalArgumentException("Destination array must be of size 4");

		destination[0] = region.getU();
		destination[1] = region.getV();
		destination[2] = region.getU2() - region.getU();
		destination[3] = region.getV2() - region.getV();

		return destination;
	}

	public static Pixmap getPixmap(Texture texture) {
		if(!texture.getTextureData().isPrepared())
			texture.getTextureData().prepare();
		return texture.getTextureData().consumePixmap();
	}

	public static Pixmap getPixmap(TextureRegion region) {
		Pixmap pixmap = new Pixmap(region.getRegionWidth(), region.getRegionHeight(), Format.RGBA8888);

		Pixmap texPixmap = getPixmap(region.getTexture());

		pixmap.drawPixmap(texPixmap, region.getRegionX(),
				region.getRegionY(),
				region.getRegionWidth(),
				region.getRegionHeight(),
				0,
				0,
				region.getRegionWidth(),
				region.getRegionHeight());

		if(region.getTexture().getTextureData().disposePixmap())
			texPixmap.dispose();

		return pixmap;
	}

	public static void flipPixmapX(Pixmap p) {
		int w = p.getWidth();
		int h = p.getHeight();

		//change blending to 'none' so that alpha areas will not show
		//previous orientation of image
		p.setBlending(Pixmap.Blending.None);

		for(int y = 0; y < h / 2; y++)
			for(int x = 0; x < w / 2; x++) {
				//get color of current pixel
				int hold = p.getPixel(x, y);
				//draw color of pixel from opposite side of pixmap to current position
				p.drawPixel(x, y, p.getPixel(w - x - 1, y));
				//draw saved color to other side of pixmap
				p.drawPixel(w - x - 1, y, hold);

				//repeat for height/width inverted pixels
				hold = p.getPixel(x, h - y - 1);
				p.drawPixel(x, h - y - 1, p.getPixel(w - x - 1, h - y - 1));
				p.drawPixel(w - x - 1, h - y - 1, hold);
			}

		//set blending back to default
		p.setBlending(Pixmap.Blending.SourceOver);
	}

	public static BufferedImage toBufferedImage(Pixmap pixmap) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		PixmapIO.PNG writer = new PixmapIO.PNG(pixmap.getWidth() * pixmap.getHeight() * 4);
		try {
			writer.setFlipY(false);
			writer.setCompression(Deflater.NO_COMPRESSION);
			writer.write(baos, pixmap);
		} finally {
			writer.dispose();
		}

		return ImageIO.read(new ByteArrayInputStream(baos.toByteArray()));
	}
}
