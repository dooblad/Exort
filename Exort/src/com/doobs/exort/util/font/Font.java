package com.doobs.exort.util.font;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import java.awt.*;
import java.util.*;

import res.textures.fonts.*;

import com.doobs.exort.util.texture.*;

public class Font {
	public static final int SPACE = 125;
	
	private static int size = 12;
	
	private Texture texture;
	private Map<Integer, Character> characters;
	
	public Font(String URL) {
		this.texture = TextureLoader.getTexture("fonts/" + URL + ".png");
		this.characters = CharInfoLoader.load(Fonts.class, texture, URL + ".txt");
	}
	
	public void draw(String phrase, int x, int y, float red, float green, float blue) {
		glColor4f(red, green, blue, 1f);
		glActiveTexture(GL_TEXTURE0);
		texture.bind();
		
		Character character;
		float[] texCoords;
		float width, height;
		
		float sizeFactor = getSizeFactor();
		
		glBegin(GL_QUADS);
		for(int i = 0; i < phrase.length(); i++) {
			if(phrase.charAt(i) == ' ') {
				x += SPACE * sizeFactor;
			} else if((character = characters.get(Integer.valueOf(phrase.charAt(i)))) != null) {
				texCoords = character.getTexCoords();
				width = character.getWidth();
				height = character.getHeight();
				
				glTexCoord2f(texCoords[0], texCoords[1]);
				glVertex2f(x, y);
				
				glTexCoord2f(texCoords[2], texCoords[3]);
				glVertex2f(x + width * sizeFactor, y);
				
				glTexCoord2f(texCoords[4], texCoords[5]);
				glVertex2f(x + width * sizeFactor, y + height * sizeFactor);
				
				glTexCoord2f(texCoords[6], texCoords[7]);
				glVertex2f(x, y + height * sizeFactor);
				
				x += width * sizeFactor;
			}
		}
		glEnd();
	}
	
	// Getters and setters
	public Texture getTexture() {
		return texture;
	}
	
	public Character getCharacter(int character) {
		return characters.get(character);
	}
	
	public void setSize(int size) {
		Font.size = size;
	}
	
	public Dimension getPhraseDimensions(String phrase) {
		Character character;
		
		float sizeFactor = getSizeFactor();
		int width = 0, height = 0;
		float w, h;
		
		for(int i = 0; i < phrase.length(); i++) {
			if(phrase.charAt(i) == ' ') {
				width += SPACE * sizeFactor;
			} else if((character = characters.get(Integer.valueOf(phrase.charAt(i)))) != null) {
				w = character.getWidth() * sizeFactor;
				h = character.getHeight() * sizeFactor;
				
				width += w;
				
				if(h > height)
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
		
		for(int i = 0; i < phrase.length(); i++) {
			if(phrase.charAt(i) == ' ') {
				width += SPACE * sizeFactor;
			} else if((character = characters.get(Integer.valueOf(phrase.charAt(i)))) != null) {
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
		
		for(int i = 0; i < phrase.length(); i++) {
			if((character = characters.get(Integer.valueOf(phrase.charAt(i)))) != null) {
				h = (int) (character.getHeight() * sizeFactor);
				if(h > height)
					height = h;
			}
		}
		
		return height;
	}
	
	public float getSizeFactor() {
		return size / 100f;
	}
}
