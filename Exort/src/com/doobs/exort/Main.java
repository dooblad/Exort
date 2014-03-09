package com.doobs.exort;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import res.shaders.*;
import res.textures.*;

import com.doobs.exort.entity.Player;
import com.doobs.exort.level.Level;
import com.doobs.exort.math.RayCast;
import com.doobs.exort.util.*;

public class Main {
	public static final String TITLE = "Exort";
	public static int width = 800, height = 600;

	private Level level;
	private Player player;
	private Camera camera;

	private boolean closeRequested;

	public Main() {
		GLTools.init();
		Cursor.init();

		level = new Level();
		player = new Player();
		camera = new Camera(0.0f, 3.0f, 0.0f);

		closeRequested = false;

		while (!closeRequested) {
			tick(GLTools.getDelta());
			render();

			Display.update();
			Display.sync(60);
		}
	}

	public void tick(int delta) {
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)
				|| Display.isCloseRequested())
			closeRequested = true;

		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == Keyboard.KEY_LMENU)
					Mouse.setGrabbed(!Mouse.isGrabbed());
				if (Keyboard.getEventKey() == Keyboard.KEY_R)
					camera.resetRotation();
				if (Keyboard.getEventKey() == Keyboard.KEY_F11)
					GLTools.toggleFullscreen();
			}
		}

		GLTools.tick();

		camera.tick(delta);
		level.tick(delta);
		player.tick(delta);
	}

	public void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		Shaders.lightingShader.use();
		
		glEnable(GL_TEXTURE_2D);
		
		glPushMatrix();
		camera.applyTransformations();

		while (Mouse.next()) {
			if (Mouse.getEventButtonState() && Mouse.getEventButton() == 1) {
				RayCast.movePlayer(camera, player);
			}
		}
		
		// Send modelViewMatrix to GLSL
		Lighting.sendModelViewMatrix();

		level.render();
		player.render();

		glDisable(GL_TEXTURE_2D);
		Shaders.lightingShader.end();

		
		glPopMatrix();
	}

	public static void main(String[] args) {
		new Main();
	}

	// Getters and Setters
	public Level getLevel() {
		return level;
	}
}
