package com.doobs.exort.util;

import java.util.*;

import org.lwjgl.input.*;

import com.doobs.exort.util.font.*;
import com.doobs.exort.util.loaders.*;

/**
 * Retrieves information every tick about the state of user input.
 */
public class InputHandler {
	public static final int NUM_OF_KEYS = 256, NUM_OF_MOUSE_BUTTONS = 3;

	// Requires inline import because of our custom Character class.
	public List<java.lang.Character> eventChars;

	public boolean[] keys, oldKeys;
	public boolean[] mouse, oldMouse;

	public InputHandler() {
		this.eventChars = new ArrayList<java.lang.Character>();
		this.keys = new boolean[NUM_OF_KEYS];
		this.oldKeys = new boolean[NUM_OF_KEYS];
		this.mouse = new boolean[NUM_OF_MOUSE_BUTTONS];
		this.oldMouse = new boolean[NUM_OF_MOUSE_BUTTONS];
	}

	public void tick() {
		// Clear all events from last tick.
		this.eventChars.clear();
		// Capture all events from this tick in order to reference the event characters in
		// multiple places.
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				this.eventChars.add(Keyboard.getEventCharacter());
			}
		}

		Keyboard.poll();
		Mouse.poll();

		for (int i = 0; i < this.keys.length; i++) {
			this.oldKeys[i] = this.keys[i];
			this.keys[i] = Keyboard.isKeyDown(i);
		}

		for (int i = 0; i < this.mouse.length; i++) {
			this.oldMouse[i] = this.mouse[i];
			this.mouse[i] = Mouse.isButtonDown(i);
		}

	}

	public void handleTyping(StringBuilder phrase, Font font) {
		if (this.isKeyPressed(Keyboard.KEY_BACK) && (phrase.length() != 0)) {
			phrase.deleteCharAt(phrase.length() - 1);
		} else {
			for (char c : this.eventChars) {
				if (Fonts.centuryGothic.getCharacter(c) != null) {
					phrase.append(c);
				}
			}
		}
	}

	public boolean isKeyDown(int keyCode) {
		if (keyCode < this.keys.length) {
			return this.keys[keyCode];
		} else {
			return false;
		}
	}

	public boolean isKeyPressed(int keyCode) {
		if (keyCode < this.keys.length) {
			return this.keys[keyCode] && !this.oldKeys[keyCode];
		} else {
			return false;
		}
	}

	public boolean isKeyReleased(int keyCode) {
		if (keyCode < this.keys.length) {
			return !this.keys[keyCode] && this.oldKeys[keyCode];
		} else {
			return false;
		}
	}

	public boolean isMouseButtonDown(int buttonCode) {
		if (buttonCode < this.mouse.length) {
			return this.mouse[buttonCode];
		} else {
			return false;
		}
	}

	public boolean isMouseButtonPressed(int buttonCode) {
		if (buttonCode < this.mouse.length) {
			return this.mouse[buttonCode] && !this.oldMouse[buttonCode];
		} else {
			return false;
		}
	}

	public List<java.lang.Character> getEventCharacters() {
		return this.eventChars;
	}
}
