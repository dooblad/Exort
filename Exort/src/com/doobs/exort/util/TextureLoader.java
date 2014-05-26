package com.doobs.exort.util;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

import java.awt.image.*;
import java.io.*;
import java.nio.*;

import javax.imageio.*;

import org.lwjgl.*;

import res.textures.*;

public class TextureLoader {
	private static final int BYTES_PER_PIXEL = 4;

	public static Texture getTexture(String URL) {
		try {
			Texture result;
			
			int texture = glGenTextures();

			BufferedImage image = ImageIO.read(Textures.class.getResourceAsStream(URL));
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

			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
			glBindTexture(GL_TEXTURE_2D, 0);

			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
