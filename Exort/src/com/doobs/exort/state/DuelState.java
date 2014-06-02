package com.doobs.exort.state;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.*;

import res.shaders.*;

import com.doobs.exort.*;
import com.doobs.exort.entity.creature.*;
import com.doobs.exort.gfx.*;
import com.doobs.exort.level.*;
import com.doobs.exort.math.*;
import com.doobs.exort.util.*;

public class DuelState implements GameState {
	private Level level;
	private Player player;
	private Camera camera;
	
	public DuelState() {
		level = new Level();
		player = new Player();
		camera = new Camera(0.0f, 3.0f, 0.0f);
	}
	
	public void tick(int delta) {
		if (Main.input.isKeyPressed(Keyboard.KEY_LMENU))
			Mouse.setGrabbed(!Mouse.isGrabbed());
		else if (Main.input.isKeyPressed(Keyboard.KEY_R))
			camera.reset();
		
		camera.tick(delta);
		level.tick(delta);
		player.tick(delta);
	}

	public void render() {
		glEnable(GL_TEXTURE_2D);
		
		// Level rendering
		Shaders.lighting.use();

		camera.applyTransformations();

		Lighting.sendModelViewMatrix();

		Lighting.setTextured(true);
		level.render();
		Lighting.setTextured(false);
		
		while (Mouse.next()) {
			if (Mouse.getEventButtonState() && Mouse.getEventButton() == 1) {
				RayCast.movePlayer(camera, player);
			}
		}
		
		player.render();
		
		// GUI rendering
		GLTools.switchToOrtho();
		Shaders.gui.use();
		
		GUI.render();
		
		Shaders.gui.end();
		GLTools.switchToPerspective();

		glDisable(GL_TEXTURE_2D);
	}
	
	// Getters and setters
	public Level getLevel() {
		return level;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Camera getCamera() {
		return camera;
	}
}
