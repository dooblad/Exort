package com.doobs.exort.util.texture;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.*;
import java.io.*;
import java.nio.*;

import javax.imageio.*;

import org.lwjgl.*;

public class TextureLoader {
	private static final int BYTES_PER_PIXEL = 4;

	public static Texture getTexture(String URL, boolean mipmapped) {
		try {
			Texture result;

			int texture = glGenTextures();

			BufferedImage image = ImageIO.read(new File(URL));
			int width = image.getWidth();
			int height = image.getHeight();

			result = new Texture(texture, width, height);

			int[] pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);

			ByteBuffer data = BufferUtils.createByteBuffer(width * height * BYTES_PER_PIXEL);

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int color = pixels[x + (height - y - 1) * width];
					data.put((byte) ((color >> 16) & 0xFF));
					data.put((byte) ((color >> 8) & 0xFF));
					data.put((byte) (color & 0xFF));
					data.put((byte) ((color >> 24) & 0xFF));
				}
			}
			data.flip();

			glBindTexture(GL_TEXTURE_2D, texture);

			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

			// Actual mipmapping makes texture disappear for some reason
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, mipmapped ? GL_LINEAR : GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, mipmapped ? GL_LINEAR : GL_NEAREST);

			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
			glBindTexture(GL_TEXTURE_2D, 0);

			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
