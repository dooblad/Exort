package com.doobs.exort.util.font;

public class Character {
	private float[] texCoords;
	private int width, height;

	public Character(int texWidth, int texHeight, int x, int y, int charWidth, int charHeight) {
		this.texCoords = new float[8];
		this.width = charWidth;
		this.height = charHeight;
		
		float x1 = (float) (x) / texWidth;
		float y1 = (float) (texHeight - y - charHeight) / texHeight;
		float x2 = (float) (x + charWidth) / texWidth;
		float y2 = (float) (texHeight - y) / texHeight;
		
		texCoords[0] = x1;
		texCoords[1] = y1;
		texCoords[2] = x2;
		texCoords[3] = y1;
		texCoords[4] = x2;
		texCoords[5] = y2;
		texCoords[6] = x1;
		texCoords[7] = y2;
	}
	
	// Getters and setters
	public float[] getTexCoords() {
		return texCoords;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
