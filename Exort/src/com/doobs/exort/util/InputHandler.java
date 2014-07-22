package com.doobs.exort.util;

import org.lwjgl.input.*;

import com.doobs.exort.util.font.*;

public class InputHandler {
	public static final int NUM_OF_KEYS = 256, NUM_OF_MOUSE_BUTTONS = 3;

	public boolean[] keys, oldKeys;
	public boolean[] mouse, oldMouse;

	public InputHandler() {
		keys = new boolean[NUM_OF_KEYS];
		oldKeys = new boolean[NUM_OF_KEYS];
		mouse = new boolean[NUM_OF_MOUSE_BUTTONS];
		oldMouse = new boolean[NUM_OF_MOUSE_BUTTONS];
	}

	public void tick() {
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

	public char getChar(int keyCode) {
		char returned;

		switch (keyCode) {
		case Keyboard.KEY_0:
			returned = '0';
			break;
		case Keyboard.KEY_1:
			returned = '1';
			break;
		case Keyboard.KEY_2:
			returned = '2';
			break;
		case Keyboard.KEY_3:
			returned = '3';
			break;
		case Keyboard.KEY_4:
			returned = '4';
			break;
		case Keyboard.KEY_5:
			returned = '5';
			break;
		case Keyboard.KEY_6:
			returned = '6';
			break;
		case Keyboard.KEY_7:
			returned = '7';
			break;
		case Keyboard.KEY_8:
			returned = '8';
			break;
		case Keyboard.KEY_9:
			returned = '9';
			break;
		case Keyboard.KEY_PERIOD:
			returned = '.';
			break;
		case Keyboard.KEY_COLON:
			returned = ':';
			break;
		case Keyboard.KEY_A:
			returned = 'a';
			break;
		case Keyboard.KEY_B:
			returned = 'b';
			break;
		case Keyboard.KEY_C:
			returned = 'c';
			break;
		case Keyboard.KEY_D:
			returned = 'd';
			break;
		case Keyboard.KEY_E:
			returned = 'e';
			break;
		case Keyboard.KEY_F:
			returned = 'f';
			break;
		case Keyboard.KEY_G:
			returned = 'g';
			break;
		case Keyboard.KEY_H:
			returned = 'h';
			break;
		case Keyboard.KEY_I:
			returned = 'i';
			break;
		case Keyboard.KEY_J:
			returned = 'j';
			break;
		case Keyboard.KEY_K:
			returned = 'k';
			break;
		case Keyboard.KEY_L:
			returned = 'l';
			break;
		case Keyboard.KEY_M:
			returned = 'm';
			break;
		case Keyboard.KEY_N:
			returned = 'n';
			break;
		case Keyboard.KEY_O:
			returned = 'o';
			break;
		case Keyboard.KEY_P:
			returned = 'p';
			break;
		case Keyboard.KEY_Q:
			returned = 'q';
			break;
		case Keyboard.KEY_R:
			returned = 'r';
			break;
		case Keyboard.KEY_S:
			returned = 's';
			break;
		case Keyboard.KEY_T:
			returned = 't';
			break;
		case Keyboard.KEY_U:
			returned = 'u';
			break;
		case Keyboard.KEY_V:
			returned = 'v';
			break;
		case Keyboard.KEY_W:
			returned = 'w';
			break;
		case Keyboard.KEY_X:
			returned = 'x';
			break;
		case Keyboard.KEY_Y:
			returned = 'y';
			break;
		case Keyboard.KEY_Z:
			returned = 'z';
			break;
		case Keyboard.KEY_SPACE:
			returned = ' ';
			break;
		default:
			returned = (char) -1;
		}

		if ((keys[Keyboard.KEY_LSHIFT] | keys[Keyboard.KEY_LSHIFT]) && returned >= 97 && returned <= 122)
			return (char) (returned - 32);
		else
			return returned;
	}

	public String handleTyping(String phrase, Font font) {
		if (isKeyPressed(Keyboard.KEY_BACK) && phrase.length() != 0) {
			phrase = phrase.substring(0, phrase.length() - 1);
		}

		for (int i = 0; i < keys.length; i++) {
			if (isKeyPressed(i) && font.getCharacter(getChar(i)) != null) {
				phrase += getChar(i);
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

}
