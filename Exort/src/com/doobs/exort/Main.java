package com.doobs.exort;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import res.shaders.*;
import res.textures.*;

import com.doobs.exort.gfx.*;
import com.doobs.exort.state.*;
import com.doobs.exort.util.*;

public class Main {
	public static final String TITLE = "Exort";
	public static int width = 800, height = 600;

	public static InputHandler input;

	private boolean closeRequested;
	
	private GameState state;

	public Main() {
		GLTools.init();
		Shaders.init();
		Lighting.init();
		Cursor.init();
		Textures.init();
		
		input = new InputHandler();
		
		closeRequested = false;
		
		state = new DuelState();

		run();
	}
	
	public void run() {
		while (!closeRequested) {
			tick(GLTools.getDelta());
			render();

			Display.update();
			Display.sync(60);
		}
	}

	public void tick(int delta) {
		input.tick();
		
		if (input.isKeyDown(Keyboard.KEY_ESCAPE) || Display.isCloseRequested())
			closeRequested = true;

		
		else if (input.isKeyPressed(Keyboard.KEY_F11))
			GLTools.toggleFullscreen();

		GLTools.tick();

		state.tick(delta);
	}

	public void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glLoadIdentity();

		state.render();
	}

	public static void main(String[] args) {
		new Main();
	}
	
	// Getters and setters
	public GameState getCurrentState() {
		return state;
	}
}
