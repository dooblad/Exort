package client.util.font;

public class Character {
	private float[] texCoords;
	private int width, height;
	private int xo, yo;
	private int xa;

	public Character(int texWidth, int texHeight, int x, int y, int charWidth, int charHeight, int xOffset, int yOffset, int xAdvance) {
		this.texCoords = new float[8];
		this.width = charWidth;
		this.height = charHeight;

		float x1 = (float) (x) / texWidth;
		float y1 = (float) (texHeight - y - charHeight) / texHeight;
		float x2 = (float) (x + charWidth) / texWidth;
		float y2 = (float) (texHeight - y) / texHeight;

		this.texCoords[0] = x1;
		this.texCoords[1] = y1;
		this.texCoords[2] = x2;
		this.texCoords[3] = y1;
		this.texCoords[4] = x2;
		this.texCoords[5] = y2;
		this.texCoords[6] = x1;
		this.texCoords[7] = y2;

		this.xo = xOffset;
		this.yo = yOffset - charHeight;

		this.xa = xAdvance;
	}

	public Character(int texWidth, int texHeight, int x, int y, int charWidth, int charHeight) {
		this(texWidth, texHeight, x, y, charWidth, charHeight, 0, 0, 0);
	}

	// Getters and setters
	public float[] getTexCoords() {
		return this.texCoords;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public int getXO() {
		return this.xo;
	}

	public int getYO() {
		return this.yo;
	}

	public int getXA() {
		return this.xa;
	}
}
