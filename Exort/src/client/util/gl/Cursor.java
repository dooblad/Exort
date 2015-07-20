package client.util.gl;

import java.awt.image.*;
import java.io.*;
import java.nio.*;

import javax.imageio.*;

import org.lwjgl.*;
import org.lwjgl.input.*;

/**
 * Changes the cursor within Exort.
 */
public class Cursor {
	public static final String DIRECTORY = "res/textures/cursors/";

	public static BufferedImage cursorImage;

	public static void init() {
		try {
			cursorImage = ImageIO.read(new File(DIRECTORY + "cursor.png"));
			int width = cursorImage.getWidth();
			int height = cursorImage.getHeight();

			// Read the RGB values into a buffer.
			IntBuffer buffer = BufferUtils.createIntBuffer(width * height);
			int[] temp = new int[width * height];
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					temp[x + ((height - y - 1) * width)] = cursorImage.getRGB(x, y);
				}
			}
			buffer.put(temp);
			buffer.flip();

			// Set the cursor using LWGJL's utilities.
			org.lwjgl.input.Cursor cursor = new org.lwjgl.input.Cursor(width, height, 0, height - 1, 1, buffer, null);
			Mouse.setNativeCursor(cursor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
