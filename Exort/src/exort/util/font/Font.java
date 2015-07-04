package exort.util.font;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import java.awt.*;
import java.util.*;

import com.doobs.modern.util.Color;
import com.doobs.modern.util.batch.*;
import com.doobs.modern.util.texture.*;

import exort.*;
import exort.util.loaders.*;

public class Font {
	private static final String DIRECTORY = "res/textures/fonts/";
	private static final int DEFAULT_SIZE = 12;
	private static final float[] DEFAULT_COLOR = new float[] { 1f, 1f, 1f, 1f };

	private Main main;

	private float size;
	private float[] color = new float[] { 1f, 1f, 1f, 1f };

	private Texture texture;
	private Map<Integer, Character> characters;

	public Font(Main main, String URL) {
		this.main = main;
		this.size = DEFAULT_SIZE;
		for (int i = 0; i < this.color.length; i++) {
			this.color[i] = DEFAULT_COLOR[i];
		}

		this.texture = TextureLoader.getTexture(DIRECTORY + URL + ".png", true);
		this.characters = CharInfoLoader.load(this.texture, DIRECTORY + URL + ".txt");
	}

	public void draw(StringBuffer phrase, int x, int y) {
		this.draw(phrase.toString(), x, y);
	}

	public void draw(String phrase, int x, int y) {
		Shaders.use("font");
		Color.set(Shaders.current, this.color[0], this.color[1], this.color[2], this.color[3]);
		glActiveTexture(GL_TEXTURE0);
		Shaders.current.setUniform1i("texture", 0);
		this.texture.bind();

		Character character;
		float[] texCoords = new float[phrase.length() * 8];
		float[] vertexData = new float[phrase.length() * 16];
		short[] indexData = new short[phrase.length() * 6];

		float width, height;

		float sizeFactor = this.getSizeFactor();

		float xo = 0, yo = 0;

		for (int i = 0; i < phrase.length(); i++) {
			char temp = phrase.charAt(i);
			if ((character = this.characters.get((int) temp)) != null) {
				width = character.getWidth() * sizeFactor;
				height = character.getHeight() * sizeFactor;

				xo = character.getXO() * sizeFactor;
				yo = character.getYO() * sizeFactor;

				// Tex Coords
				for (int j = 0; j < 8; j++) {
					texCoords[(i * 8) + j] = character.getTexCoords()[j];
				}

				// Vertices
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

				// Indices
				indexData[i * 6] = (short) (i * 4);
				indexData[(i * 6) + 1] = (short) ((i * 4) + 1);
				indexData[(i * 6) + 2] = (short) ((i * 4) + 2);

				indexData[(i * 6) + 3] = (short) (i * 4);
				indexData[(i * 6) + 4] = (short) ((i * 4) + 2);
				indexData[(i * 6) + 5] = (short) ((i * 4) + 3);

				x += character.getXA() * sizeFactor;
			}
		}
		new SimpleBatch(GL_TRIANGLES, 4, vertexData, null, null, texCoords, indexData).draw(Shaders.current.getAttributeLocations());
	}

	public void drawCentered(String phrase, int xo, int yo) {
		Dimension d = this.getPhraseDimensions(phrase);
		this.draw(phrase, ((this.main.getWidth() - d.width) / 2) + xo, ((this.main.getHeight() - d.height) / 2) + yo);
	}

	// Getters and setters
	public Texture getTexture() {
		return this.texture;
	}

	public Character getCharacter(int character) {
		return this.characters.get(character);
	}

	public float getSize() {
		return this.size;
	}

	public void setSize(float size) {
		this.size = size;
	}

	public float[] getColor() {
		return this.color;
	}

	public void setColor(float red, float green, float blue, float alpha) {
		this.color[0] = red;
		this.color[1] = green;
		this.color[2] = blue;
		this.color[3] = alpha;
	}

	public Dimension getPhraseDimensions(StringBuffer phrase) {
		return this.getPhraseDimensions(phrase.toString());
	}

	public Dimension getPhraseDimensions(String phrase) {
		Character character;

		float sizeFactor = this.getSizeFactor();
		int width = 0, height = 0;
		float xa, h;

		for (int i = 0; i < phrase.length(); i++) {
			if ((character = this.characters.get(Integer.valueOf(phrase.charAt(i)))) != null) {
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

	public int getPhraseWidth(StringBuffer phrase) {
		return this.getPhraseWidth(phrase.toString());
	}

	public int getPhraseWidth(String phrase) {
		Character character;

		float sizeFactor = this.getSizeFactor();
		int width = 0;
		float xa;

		for (int i = 0; i < phrase.length(); i++) {
			if ((character = this.characters.get(Integer.valueOf(phrase.charAt(i)))) != null) {
				xa = character.getXA() * sizeFactor;

				width += xa;
			}
		}

		return width;
	}

	public int getPhraseHeight(StringBuffer phrase) {
		return this.getPhraseHeight(phrase.toString());
	}

	public int getPhraseHeight(String phrase) {
		Character character;

		float sizeFactor = this.getSizeFactor();
		int height = 0;
		int h;

		for (int i = 0; i < phrase.length(); i++) {
			if ((character = this.characters.get(Integer.valueOf(phrase.charAt(i)))) != null) {
				h = (int) (character.getHeight() * sizeFactor);
				if (h > height) {
					height = h;
				}
			}
		}

		return height;
	}

	public float getSizeFactor() {
		return this.size / 100f;
	}
}
