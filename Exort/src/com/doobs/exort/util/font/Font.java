package com.doobs.exort.util.font;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import java.awt.*;
import java.util.*;

import com.doobs.exort.*;
import com.doobs.exort.util.loaders.*;
import com.doobs.modern.util.Color;
import com.doobs.modern.util.batch.*;
import com.doobs.modern.util.texture.*;

public class Font {
	private static final String DIRECTORY = "res/textures/fonts/";
	private static final int DEFAULT_SIZE = 12;
	private static final float[] DEFAULT_COLOR = new float[] { 1f, 1f, 1f, 1f };

	private float size;
	private float[] color = new float[] { 1f, 1f, 1f, 1f };

	private Texture texture;
	private Map<Integer, Character> characters;

	public Font(String URL) {
		this.size = DEFAULT_SIZE;
		for (int i = 0; i < color.length; i++) {
			color[i] = DEFAULT_COLOR[i];
		}

		this.texture = TextureLoader.getTexture(DIRECTORY + URL + ".png", true);
		this.characters = CharInfoLoader.load(texture, DIRECTORY + URL + ".txt");
	}

	public void draw(String phrase, int x, int y) {
		Shaders.use("font");
		Color.set(Shaders.current, color[0], color[1], color[2], color[3]);
		glActiveTexture(GL_TEXTURE0);
		Shaders.current.setUniform1i("texture", 0);
		texture.bind();

		Character character;
		float[] texCoords = new float[phrase.length() * 8];
		float[] vertexData = new float[phrase.length() * 16];
		short[] indexData = new short[phrase.length() * 6];

		float width, height;

		float sizeFactor = getSizeFactor();

		float xo = 0, yo = 0;

		for (int i = 0; i < phrase.length(); i++) {
			char temp = phrase.charAt(i);
			if ((character = characters.get((int) temp)) != null) {
				width = character.getWidth() * sizeFactor;
				height = character.getHeight() * sizeFactor;

				xo = character.getXO() * sizeFactor;
				yo = character.getYO() * sizeFactor;

				// Tex Coords
				for (int j = 0; j < 8; j++) {
					texCoords[i * 8 + j] = character.getTexCoords()[j];
				}

				// Vertices
				vertexData[i * 16] = x + xo;
				vertexData[i * 16 + 1] = y + yo;
				vertexData[i * 16 + 2] = 0f;
				vertexData[i * 16 + 3] = 1f;

				vertexData[i * 16 + 4] = x + xo + width;
				vertexData[i * 16 + 5] = y + yo;
				vertexData[i * 16 + 6] = 0f;
				vertexData[i * 16 + 7] = 1f;

				vertexData[i * 16 + 8] = x + xo + width;
				vertexData[i * 16 + 9] = y + yo + height;
				vertexData[i * 16 + 10] = 0f;
				vertexData[i * 16 + 11] = 1f;

				vertexData[i * 16 + 12] = x + xo;
				vertexData[i * 16 + 13] = y + yo + height;
				vertexData[i * 16 + 14] = 0f;
				vertexData[i * 16 + 15] = 1f;

				// Indices
				indexData[i * 6] = (short) (i * 4);
				indexData[i * 6 + 1] = (short) (i * 4 + 1);
				indexData[i * 6 + 2] = (short) (i * 4 + 2);

				indexData[i * 6 + 3] = (short) (i * 4);
				indexData[i * 6 + 4] = (short) (i * 4 + 2);
				indexData[i * 6 + 5] = (short) (i * 4 + 3);

				x += character.getXA() * sizeFactor;
			}
		}
		new SimpleBatch(GL_TRIANGLES, 4, vertexData, null, null, texCoords, indexData).draw(Shaders.current.getAttributeLocations());
	}

	public void drawCentered(String phrase, int xo, int yo) {
		Dimension d = getPhraseDimensions(phrase);
		draw(phrase, (Main.getWidth() - d.width) / 2 + xo, (Main.getHeight() - d.height) / 2 + yo);
	}

	// Getters and setters
	public Texture getTexture() {
		return texture;
	}

	public Character getCharacter(int character) {
		return characters.get(character);
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}

	public float[] getColor() {
		return color;
	}

	public void setColor(float red, float green, float blue, float alpha) {
		color[0] = red;
		color[1] = green;
		color[2] = blue;
		color[3] = alpha;
	}

	public Dimension getPhraseDimensions(String phrase) {
		Character character;

		float sizeFactor = getSizeFactor();
		int width = 0, height = 0;
		float xa, h;

		for (int i = 0; i < phrase.length(); i++) {
			if ((character = characters.get(Integer.valueOf(phrase.charAt(i)))) != null) {
				xa = character.getXA() * sizeFactor;
				h = character.getHeight() * sizeFactor;

				width += xa;

				if (h > height)
					height = (int) h;
			}
		}

		return new Dimension(width, height);
	}

	public int getPhraseWidth(String phrase) {
		Character character;

		float sizeFactor = getSizeFactor();
		int width = 0;
		float xa;

		for (int i = 0; i < phrase.length(); i++) {
			if ((character = characters.get(Integer.valueOf(phrase.charAt(i)))) != null) {
				xa = character.getXA() * sizeFactor;

				width += xa;
			}
		}

		return width;
	}

	public int getPhraseHeight(String phrase) {
		Character character;

		float sizeFactor = getSizeFactor();
		int height = 0;
		int h;

		for (int i = 0; i < phrase.length(); i++) {
			if ((character = characters.get(Integer.valueOf(phrase.charAt(i)))) != null) {
				h = (int) (character.getHeight() * sizeFactor);
				if (h > height)
					height = h;
			}
		}

		return height;
	}

	public float getSizeFactor() {
		return size / 100f;
	}
}
