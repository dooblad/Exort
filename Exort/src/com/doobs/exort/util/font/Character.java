package com.doobs.exort.util.font;

public class Character {
	float[] texCoords;

	Character(int texWidth, int texHeight, int x, int y, int charWidth, int charHeight) {
		this.texCoords = new float[8];
		float x1 = (texWidth - x) / texWidth;
		float y1 = (texHeight - y) / texHeight;
		float x2 = (texWidth - x + charWidth) / texWidth;
		float y2 = (texHeight - y + charHeight) / texHeight;
		texCoords[0] = x1;
		texCoords[1] = y1;
		texCoords[2] = x2;
		texCoords[3] = y1;
		texCoords[4] = x2;
		texCoords[5] = y2;
		texCoords[6] = x1;
		texCoords[7] = y2;
	}
}
