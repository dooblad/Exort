package exort.util;

import org.lwjgl.util.vector.*;

import exort.util.loaders.*;

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
		Fonts.centuryGothic.setColor(this.color.x, this.color.y, this.color.z, this.color.w);
		Fonts.centuryGothic.draw(this.text, x, y);
	}

	// Getters and setters
	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Vector4f getColors() {
		return this.color;
	}

	public void setColors(Vector4f color) {
		this.color = color;
	}
}
