package com.doobs.exort;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import res.models.*;
import res.shaders.*;
import res.textures.*;
import res.textures.cursors.*;
import res.textures.fonts.*;

import com.doobs.exort.gfx.*;
import com.doobs.exort.state.*;
import com.doobs.exort.util.*;
import com.doobs.exort.util.gl.*;

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
		Fonts.init();
		Models.init();
		
		input = new InputHandler();
		
		closeRequested = false;
		
		state = new MainMenuState(this);

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
	
	public void changeState(GameState state) {
		this.state = state;
	}

	public static void main(String[] args) {
		new Main();
	}
	
	// Getters and setters
	public GameState getCurrentState() {
		return state;
	}
}
