package com.doobs.exort.util.font;

import res.textures.fonts.*;

import com.doobs.exort.util.texture.*;

public class Font {
	private Texture texture;
	private Character[] characters;
	
	public Font(String URL) {
		this.texture = TextureLoader.getTexture("fonts/" + URL + ".png");
		this.characters = CharInfoLoader.load(Fonts.class, texture, URL + ".txt");
	}
	
	// Getters and setters
	public Texture getTexture() {
		return texture;
	}
	
	public int numOfCharacters() {
		return characters.length;
	}
}
