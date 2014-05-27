package com.doobs.exort.util;

import org.lwjgl.input.*;

public class InputHandler {
	public static final int NUM_OF_KEYS = 128;
	
	public boolean[] keys, oldKeys;
	
	public InputHandler() {
		keys = new boolean[NUM_OF_KEYS];
		oldKeys = new boolean[NUM_OF_KEYS];
	}
	
	public void tick() {
		Keyboard.poll();
		
		for(int i = 0; i < keys.length; i++) {
			oldKeys[i] = keys[i];
			keys[i] = Keyboard.isKeyDown(i);
		}
	}
	
	public boolean isKeyDown(int keyCode) {
		return keys[keyCode];
	}
	
	public boolean isKeyPressed(int keyCode) {
		return keys[keyCode] && !oldKeys[keyCode];
	}
}
