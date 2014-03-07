package com.doobs.exort;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import res.textures.*;

import com.doobs.exort.entity.Player;
import com.doobs.exort.level.Level;
import com.doobs.exort.math.Ray;
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
				if(Keyboard.getEventKey() == Keyboard.KEY_R)
					camera.resetRotation();
				if(Keyboard.getEventKey() == Keyboard.KEY_F11)
					GLTools.toggleFullscreen();
			}
		}
		
		GLTools.tick();
		
		Lighting.updateLight(camera);

		camera.tick(delta);
		level.tick(delta);
		player.tick(delta);
	}

	public void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		//glUseProgram(Shaders.defaultShader.getID());

		glPushMatrix();
		camera.applyTransformations();
		
		Ray ray = new Ray();
		
		while(Mouse.next()) {
			if(Mouse.getEventButtonState() && Mouse.getEventButton() == 1) {
				if(Mouse.isGrabbed())
					ray = new Ray(camera.getPosition(), RayCast.getDirection(Display.getWidth() / 2, Display.getHeight() / 2));
				else
					ray = new Ray(camera.getPosition(), RayCast.getDirection(Mouse.getX(), Mouse.getY()));
				
				// Find the location where the ray intersects the ground plane
				Vector3f plane = RayCast.findPlane(ray);
				
				if(plane!= null) 
					player.move(plane);
			}
		}
		
		level.render();
		
		//glUseProgram(0);
		
		player.render();
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
