package client.util.font;

/**
 * Represents a single Character in a Font.
 */
public class Character {
	private float[] texCoords;
	private float width, height;
	private float xo, yo;
	private float xa;

	public Character(float texWidth, float texHeight, float x, float y, float charWidth, float charHeight, float xOffset, float yOffset, float xAdvance) {
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

	/**
	 * Returns a float array of texture coordinates.
	 */
	public float[] getTexCoords() {
		return this.texCoords;
	}

	/**
	 * Returns the original width of this Character.
	 */
	public float getWidth() {
		return this.width;
	}

	/**
	 * Returns the original height of this Character.
	 */
	public float getHeight() {
		return this.height;
	}

	/**
	 * Returns how far this Character needs to be offset in the x-direction to be rendered properly.
	 */
	public float getXO() {
		return this.xo;
	}

	/**
	 * Returns how far this Character needs to be offset in the y-direction to be rendered properly.
	 */
	public float getYO() {
		return this.yo;
	}

	/**
	 * Returns how far the 'cursor' should be advanced in the x-direction after this
	 * Character has been rendered.
	 */
	public float getXA() {
		return this.xa;
	}
}
