package com.doobs.exort.util.font;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import java.awt.*;
import java.util.*;

import res.textures.fonts.*;

import com.doobs.exort.*;
import com.doobs.exort.util.texture.*;

public class Font {
	public static final int SPACE = 125;

	private static final int DEFAULT_SIZE = 12;
	private static final float[] DEFAULT_COLOR = new float[] { 1f, 1f, 1f, 1f };

	private float size;
	private float[] color = new float[] { 1f, 1f, 1f, 1f };
	
	private int[] corrections;

	private Texture texture;
	private Map<Integer, Character> characters;

	public Font(String URL) {
		this.size = DEFAULT_SIZE;
		for (int i = 0; i < color.length; i++) {
			color[i] = DEFAULT_COLOR[i];
		}

		this.texture = TextureLoader.getTexture("fonts/" + URL + ".png");
		this.characters = CharInfoLoader.load(Fonts.class, texture, URL + ".txt");
	}

	public void draw(String phrase, int x, int y) {
		glColor4f(color[0], color[1], color[2], color[3]);
		glActiveTexture(GL_TEXTURE0);
		texture.bind();

		Character character;
		float[] texCoords;
		float width, height;

		float sizeFactor = getSizeFactor();

		float yy = 0;

		glBegin(GL_QUADS);
		for (int i = 0; i < phrase.length(); i++) {
			char temp = phrase.charAt(i);
			if (temp == ' ') {
				x += SPACE * sizeFactor;
			} else if ((character = characters.get(Integer.valueOf(temp))) != null) {
				texCoords = character.getTexCoords();
				width = character.getWidth();
				height = character.getHeight();

				yy = 0f;

				if (temp == 'g')
					yy = -125 * sizeFactor;
				else if(temp == 'j')
					yy = -135 * sizeFactor;
				else if(temp == 'p')
					yy = -150 * sizeFactor;
				else if(temp == 'q')
					yy = -150 * sizeFactor;
				else if(temp == 'y')
					yy = -150 * sizeFactor;
				
				for(int j = 0; i < corrections.length; i++) {
						
					// ADD FONT-SPECIFIC CORRECTIONS (NOT USING ARRAYS. THIS IS TEMPORARY)
					// MAKE IT A MANUAL CONSTRUCT YOU HAVE TO WRITE WITHIN THE FONT .PNGs ASSOCIATED
					// .TXT FILE
				}

				glTexCoord2f(texCoords[0], texCoords[1]);
				glVertex2f(x, y + yy);

				glTexCoord2f(texCoords[2], texCoords[3]);
				glVertex2f(x + width * sizeFactor, y + yy);

				glTexCoord2f(texCoords[4], texCoords[5]);
				glVertex2f(x + width * sizeFactor, y + yy + height * sizeFactor);

				glTexCoord2f(texCoords[6], texCoords[7]);
				glVertex2f(x, y + yy + height * sizeFactor);

				x += width * sizeFactor;
			}
		}
		glEnd();
	}

	public void drawCentered(String phrase, int xo, int yo) {
		Dimension d = getPhraseDimensions(phrase);
		draw(phrase, (Main.width - d.width) / 2 + xo, (Main.height - d.height) / 2 + yo);
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
		float w, h;

		for (int i = 0; i < phrase.length(); i++) {
			if (phrase.charAt(i) == ' ') {
				width += SPACE * sizeFactor;
			} else if ((character = characters.get(Integer.valueOf(phrase.charAt(i)))) != null) {
				w = character.getWidth() * sizeFactor;
				h = character.getHeight() * sizeFactor;

				width += w;

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
		float w;

		for (int i = 0; i < phrase.length(); i++) {
			if (phrase.charAt(i) == ' ') {
				width += SPACE * sizeFactor;
			} else if ((character = characters.get(Integer.valueOf(phrase.charAt(i)))) != null) {
				w = character.getWidth() * sizeFactor;

				width += w;
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
