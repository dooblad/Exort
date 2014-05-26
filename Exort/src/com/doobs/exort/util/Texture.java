package com.doobs.exort.util;

public class Texture {
	private int texture;
	private int width, height;
	
	public Texture(int texture, int width, int height) {
		this.texture = texture;
		this.width = width;
		this.height = height;
	}
	
	// Getters and setters
	public int getTexture()	 {
		return texture;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
