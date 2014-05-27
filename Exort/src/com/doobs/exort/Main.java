package com.doobs.exort;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import res.shaders.*;
import res.textures.*;

import com.doobs.exort.entity.creature.*;
import com.doobs.exort.gfx.*;
import com.doobs.exort.level.Level;
import com.doobs.exort.math.*;
import com.doobs.exort.util.*;

public class Main {
	public static final String TITLE = "Exort";
	public static int width = 800, height = 600;

	private InputHandler input;
	private Level level;
	private Player player;
	private Camera camera;

	private boolean closeRequested;

	public Main() {
		init();
		run();
	}
	
	public void init() {
		GLTools.init();
		Cursor.init();
		Textures.init();
		
		input = new InputHandler();
		level = new Level();
		player = new Player();
		camera = new Camera(0.0f, 3.0f, 0.0f);
	}
	
	public void run() {
		closeRequested = false;
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

		if (input.isKeyPressed(Keyboard.KEY_LMENU))
			Mouse.setGrabbed(!Mouse.isGrabbed());
		else if (input.isKeyPressed(Keyboard.KEY_R))
			camera.reset();
		else if (input.isKeyPressed(Keyboard.KEY_F11))
			GLTools.toggleFullscreen();

		GLTools.tick();

		camera.tick(delta);
		level.tick(delta);
		player.tick(delta);
	}

	public void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glLoadIdentity();

		glEnable(GL_TEXTURE_2D);
		
		// Level rendering
		Shaders.lighting.use();

		camera.applyTransformations();

		Lighting.sendModelViewMatrix();

		level.render();
		
		while (Mouse.next()) {
			if (Mouse.getEventButtonState() && Mouse.getEventButton() == 1) {
				RayCast.movePlayer(camera, player);
			}
		}
		
		player.render();
		
		Shaders.lighting.end();
		
		// GUI rendering
		GLTools.switchToOrtho();
		Shaders.gui.use();
		
		GUI.render();
		
		Shaders.gui.end();
		GLTools.switchToPerspective();

		glDisable(GL_TEXTURE_2D);
	}

	public static void main(String[] args) {
		new Main();
	}

	// Getters and Setters
	public Level getLevel() {
		return level;
	}
}
