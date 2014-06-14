package com.doobs.exort.util.font;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import java.util.*;

import res.textures.fonts.*;

import com.doobs.exort.util.texture.*;

public class Font {
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
		
		// HASHMAP IS NOT WORKING CORRECTLY. LEARN ABOUT DEM
		
		glBegin(GL_QUADS);
		for(int i = 0; i < phrase.length(); i++) {
			if((character = characters.get(Integer.valueOf(phrase.charAt(i)))) != null) {
				texCoords = character.getTexCoords();
				width = character.getWidth();
				height = character.getHeight();
				
				glTexCoord2f(texCoords[0], texCoords[1]);
				glVertex2f(x, y);
				
				glTexCoord2f(texCoords[2], texCoords[3]);
				glVertex2f(x + width, y);
				
				glTexCoord2f(texCoords[4], texCoords[5]);
				glVertex2f(x + width, y + height);
				
				glTexCoord2f(texCoords[6], texCoords[7]);
				glVertex2f(x, y + height);
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
}
