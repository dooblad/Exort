package com.doobs.exort.util;

import java.util.*;

import org.lwjgl.input.*;

import res.textures.*;

import com.doobs.exort.util.font.*;

public class InputHandler {
	public static final int NUM_OF_KEYS = 256, NUM_OF_MOUSE_BUTTONS = 3;

	public List<java.lang.Character> eventChars;

	public boolean[] keys, oldKeys;
	public boolean[] mouse, oldMouse;

	public InputHandler() {
		eventChars = new ArrayList<java.lang.Character>();
		keys = new boolean[NUM_OF_KEYS];
		oldKeys = new boolean[NUM_OF_KEYS];
		mouse = new boolean[NUM_OF_MOUSE_BUTTONS];
		oldMouse = new boolean[NUM_OF_MOUSE_BUTTONS];
	}

	public void tick() {
		eventChars.clear();
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				eventChars.add(Keyboard.getEventCharacter());
			}
		}

		Keyboard.poll();
		Mouse.poll();

		for (int i = 0; i < keys.length; i++) {
			oldKeys[i] = keys[i];
			keys[i] = Keyboard.isKeyDown(i);
		}

		for (int i = 0; i < mouse.length; i++) {
			oldMouse[i] = mouse[i];
			mouse[i] = Mouse.isButtonDown(i);
		}

	}

	public String handleTyping(String phrase, Font font) {
		if (isKeyPressed(Keyboard.KEY_BACK) && phrase.length() != 0) {
			phrase = phrase.substring(0, phrase.length() - 1);
		} else {
			for (char c : eventChars) {
				if (Fonts.centuryGothic.getCharacter(c) != null)
					phrase += c;
			}
		}

		return phrase;
	}

	public boolean isKeyDown(int keyCode) {
		if (keyCode < keys.length)
			return keys[keyCode];
		else
			return false;
	}

	public boolean isKeyPressed(int keyCode) {
		if (keyCode < keys.length)
			return keys[keyCode] && !oldKeys[keyCode];
		else
			return false;
	}
	public boolean isKeyReleased(int keyCode) {
		if (keyCode < keys.length)
			return !keys[keyCode] && oldKeys[keyCode];
		else
			return false;
	}

	public boolean isMouseButtonDown(int buttonCode) {
		if (buttonCode < mouse.length)
			return mouse[buttonCode];
		else
			return false;
	}

	public boolean isMouseButtonPressed(int buttonCode) {
		if (buttonCode < mouse.length)
			return mouse[buttonCode] && !oldMouse[buttonCode];
		else
			return false;
	}

	public List<java.lang.Character> getEventCharacters() {
		return eventChars;
	}
}
