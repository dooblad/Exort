package com.doobs.exort.util.texture;

public class Texture {
	private int id;
	private int width, height;
	
	public Texture(int id, int width, int height) {
		this.id = id;
		this.width = width;
		this.height = height;
	}
	
	// Getters and setters
	public int getID()	 {
		return id;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
