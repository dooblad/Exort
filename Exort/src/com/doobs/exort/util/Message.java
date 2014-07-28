package com.doobs.exort.util;

import org.lwjgl.util.vector.*;

import res.textures.*;

public class Message {
	private static final Vector4f DEFAULT_COLOR = new Vector4f(1f, 1f, 1f, 1f);

	private String text;
	private Vector4f color;

	public Message(String text, Vector4f color) {
		this.text = text;
		this.color = color;
	}

	public Message(String text) {
		this(text, DEFAULT_COLOR);
	}

	public Message() {
		this(null, DEFAULT_COLOR);
	}

	public void draw(int x, int y) {
		Fonts.centuryGothic.setColor(color.x, color.y, color.z, color.w);
		Fonts.centuryGothic.draw(text, x, y);
	}

	// Getters and setters
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Vector4f getColors() {
		return color;
	}

	public void setColors(Vector4f color) {
		this.color = color;
	}
}
