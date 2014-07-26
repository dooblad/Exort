package res.textures.cursors;

import java.awt.image.*;
import java.io.*;
import java.nio.*;

import javax.imageio.*;

import org.lwjgl.*;
import org.lwjgl.input.*;

public class Cursor {
	public static BufferedImage cursorImage;

	public static void init() {
		try {
			cursorImage = ImageIO.read(Cursor.class.getResourceAsStream("cursor.png"));
			int width = cursorImage.getWidth();
			int height = cursorImage.getHeight();

			IntBuffer buffer = BufferUtils.createIntBuffer(width * height);
			int[] temp = new int[width * height];
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					temp[x + (height - y - 1) * width] = cursorImage.getRGB(x, y);
				}
			}
			buffer.put(temp);
			buffer.flip();

			org.lwjgl.input.Cursor cursor = new org.lwjgl.input.Cursor(width, height, 0, height - 1, 1, buffer, null);

			Mouse.setNativeCursor(cursor);
		} catch (IOException | LWJGLException e) {
			e.printStackTrace();
		}
	}
}
