package com.doobs.exort.util;

import org.lwjgl.input.*;

public class InputHandler {
	public static final int NUM_OF_KEYS = 128, NUM_OF_MOUSE_BUTTONS = 3;
	
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
		
		for(int i = 0; i < keys.length; i++) {
			oldKeys[i] = keys[i];
			keys[i] = Keyboard.isKeyDown(i);
		}
		
		for(int i = 0; i < mouse.length; i++) {
			oldMouse[i] = mouse[i];
			mouse[i] = Mouse.isButtonDown(i);
		}
	}
	
	public boolean isKeyDown(int keyCode) {
		if(keyCode < keys.length)
			return keys[keyCode];
		else
			return false;
	}
	
	public boolean isKeyPressed(int keyCode) {
		if(keyCode < keys.length)
			return keys[keyCode] && !oldKeys[keyCode];
		else
			return false;
	}
	
	public boolean isMouseButtonDown(int buttonCode) {
		if(buttonCode < mouse.length)
			return mouse[buttonCode];
		else
			return false;
	}
	
	public boolean isMouseButtonPressed(int buttonCode) {
		if(buttonCode < mouse.length)
			return mouse[buttonCode] && !oldMouse[buttonCode];
		else
			return false;
	}
	
}
