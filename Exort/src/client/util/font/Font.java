package client.util.font;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import java.awt.*;
import java.util.*;

import client.*;
import client.util.loaders.*;

import com.doobs.modern.util.Color;
import com.doobs.modern.util.batch.*;
import com.doobs.modern.util.texture.*;

/**
 * Used as an interface for rendering Strings on the screen.
 */
public class Font {
	private static final String DIRECTORY = "res/textures/fonts/";
	private static final int DEFAULT_SIZE = 12;
	private static final float[] DEFAULT_COLOR = new float[] { 1f, 1f, 1f, 1f };

	private float size;
	private float[] color;

	private Dimension windowSize;

	private Texture texture;
	private Map<Integer, Character> characters;

	/**
	 * Creates a Font at size {@value #DEFAULT_SIZE} from the File in {@value #DIRECTORY}
	 * specified by "URL". "main" is used for keeping track of the window's size (for
	 * centered rendering).
	 */
	public Font(Main main, String URL) {
		this.size = DEFAULT_SIZE;
		this.color = new float[4];
		// Set to DEFAULT_COLOR to start.
		for (int i = 0; i < this.color.length; i++) {
			this.color[i] = DEFAULT_COLOR[i];
		}

		this.windowSize = main.getSize();

		this.texture = TextureLoader.getTexture(DIRECTORY + URL + ".png", true);
		this.characters = CharInfoLoader.load(this.texture, DIRECTORY + URL + ".txt");
	}

	/**
	 * Renders "phrase" at ("x", "y") on the screen.
	 */
	public void render(StringBuffer phrase, int x, int y) {
		this.render(phrase.toString(), x, y);
	}

	/**
	 * Renders "phrase" at ("x", "y") on the screen.
	 */
	public void render(String phrase, int x, int y) {
		Shaders.use("font");
		// Apply color.
		Color.set(Shaders.current, this.color[0], this.color[1], this.color[2], this.color[3]);
		// Bind texture.
		glActiveTexture(GL_TEXTURE0);
		Shaders.current.setUniform1i("texture", 0);
		this.texture.bind();

		Character character;
		int length = phrase.length();
		// 4 vertices * 2 components.
		float[] texCoords = new float[8 * length];
		// 4 vertices * 4 components.
		float[] vertexData = new float[16 * length];
		// Drawing with GL_TRIANGLES, so 3 vertices * 2 triangles to form a rectangle.
		short[] indexData = new short[6 * length];

		float width, height;

		// Used to apply the current size to each Character's original values.
		float sizeFactor = this.getSizeFactor();

		float xo = 0, yo = 0;

		for (int i = 0; i < phrase.length(); i++) {
			character = this.characters.get((int) phrase.charAt(i));
			if (character != null) { // If it exists.
				width = character.getWidth() * sizeFactor;
				height = character.getHeight() * sizeFactor;

				xo = character.getXO() * sizeFactor;
				yo = character.getYO() * sizeFactor;

				// Tex Coords.
				for (int j = 0; j < 8; j++) {
					texCoords[(i * 8) + j] = character.getTexCoords()[j];
				}

				// Vertices.
				vertexData[i * 16] = x + xo;
				vertexData[(i * 16) + 1] = y + yo;
				vertexData[(i * 16) + 2] = 0f;
				vertexData[(i * 16) + 3] = 1f;

				vertexData[(i * 16) + 4] = x + xo + width;
				vertexData[(i * 16) + 5] = y + yo;
				vertexData[(i * 16) + 6] = 0f;
				vertexData[(i * 16) + 7] = 1f;

				vertexData[(i * 16) + 8] = x + xo + width;
				vertexData[(i * 16) + 9] = y + yo + height;
				vertexData[(i * 16) + 10] = 0f;
				vertexData[(i * 16) + 11] = 1f;

				vertexData[(i * 16) + 12] = x + xo;
				vertexData[(i * 16) + 13] = y + yo + height;
				vertexData[(i * 16) + 14] = 0f;
				vertexData[(i * 16) + 15] = 1f;

				// Indices.
				indexData[i * 6] = (short) (i * 4);
				indexData[(i * 6) + 1] = (short) ((i * 4) + 1);
				indexData[(i * 6) + 2] = (short) ((i * 4) + 2);

				indexData[(i * 6) + 3] = (short) (i * 4);
				indexData[(i * 6) + 4] = (short) ((i * 4) + 2);
				indexData[(i * 6) + 5] = (short) ((i * 4) + 3);

				x += character.getXA() * sizeFactor;
			}
		}
		// The reason we've done all this... so it's done in one batch!
		new SimpleBatch(GL_TRIANGLES, 4, vertexData, null, null, texCoords, indexData).draw(Shaders.current.getAttributeLocations());
	}

	/**
	 * Renders "phrase" in the center of the screen.
	 */
	public void renderCentered(String phrase) {
		this.renderCentered(phrase, 0, 0);
	}

	/**
	 * Renders "phrase" in the center of the screen plus "xo" and "yo" in the respective
	 * dimensions.
	 */
	public void renderCentered(String phrase, int xo, int yo) {
		Dimension d = this.getPhraseDimensions(phrase);
		this.render(phrase, ((this.windowSize.width - d.width) / 2) + xo, ((this.windowSize.height - d.height) / 2) + yo);
	}

	/**
	 * Returns the Texture being used by this Font.
	 */
	public Texture getTexture() {
		return this.texture;
	}

	/**
	 * Returns the Character that corresponds with the primitive char represented by
	 * "character".
	 */
	public Character getCharacter(int character) {
		return this.characters.get(character);
	}

	/**
	 * Returns the current size of this Font.
	 */
	public float getSize() {
		return this.size;
	}

	/**
	 * Sets the size of this Font to "size".
	 */
	public void setSize(float size) {
		this.size = size;
	}

	/**
	 * Returns the RGBA components of this font's color as a float array.
	 */
	public float[] getColor() {
		return this.color;
	}

	/**
	 * Sets this Font's color to the RGBA parameters.
	 */
	public void setColor(float red, float green, float blue, float alpha) {
		this.color[0] = red;
		this.color[1] = green;
		this.color[2] = blue;
		this.color[3] = alpha;
	}

	/**
	 * Returns the size of "phrase" in this Font's current state as a Dimension.
	 */
	public Dimension getPhraseDimensions(StringBuffer phrase) {
		return this.getPhraseDimensions(phrase.toString());
	}

	/**
	 * Returns the size of "phrase" in this Font's current state as a Dimension.
	 */
	public Dimension getPhraseDimensions(String phrase) {
		Character character;

		float sizeFactor = this.getSizeFactor();
		int width = 0, height = 0;
		float xa, h;

		for (int i = 0; i < phrase.length(); i++) {
			character = this.characters.get((int) phrase.charAt(i));
			if (character != null) {
				xa = character.getXA() * sizeFactor;
				h = character.getHeight() * sizeFactor;

				width += xa;

				if (h > height) {
					height = (int) h;
				}
			}
		}

		return new Dimension(width, height);
	}

	/**
	 * Returns the width of "phrase" in this Font's current state.
	 */
	public int getPhraseWidth(StringBuffer phrase) {
		return this.getPhraseWidth(phrase.toString());
	}

	/**
	 * Returns the width of "phrase" in this Font's current state.
	 */
	public int getPhraseWidth(String phrase) {
		Character character;

		float sizeFactor = this.getSizeFactor();
		int width = 0;
		float xa;

		for (int i = 0; i < phrase.length(); i++) {
			character = this.characters.get((int) phrase.charAt(i));
			if (character != null) {
				xa = character.getXA() * sizeFactor;

				width += xa;
			}
		}

		return width;
	}

	/**
	 * Returns the height of "phrase" in this Font's current state.
	 */
	public int getPhraseHeight(StringBuffer phrase) {
		return this.getPhraseHeight(phrase.toString());
	}

	/**
	 * Returns the height of "phrase" in this Font's current state.
	 */
	public int getPhraseHeight(String phrase) {
		Character character;

		float sizeFactor = this.getSizeFactor();
		int height = 0;
		int h;

		for (int i = 0; i < phrase.length(); i++) {
			character = this.characters.get((int) phrase.charAt(i));
			if (character != null) {
				h = (int) (character.getHeight() * sizeFactor);
				if (h > height) {
					height = h;
				}
			}
		}

		return height;
	}

	/**
	 * Returns a float used for scaling the Font's original values. At size 100, the
	 * Characters' original sizes are used.
	 */
	public float getSizeFactor() {
		return this.size / 100f;
	}
}