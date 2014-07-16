package com.doobs.exort.util.texture;

import static org.lwjgl.opengl.GL11.*;

public class Texture {
	private int id;
	private int width, height;

	public Texture(int id, int width, int height) {
		this.id = id;
		this.width = width;
		this.height = height;
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, id);
	}

	// Getters and setters
	public int getID() {
		return id;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
