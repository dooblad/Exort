package com.doobs.exort;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import com.doobs.exort.entity.Player;
import com.doobs.exort.level.Map;
import com.doobs.exort.math.Ray;
import com.doobs.exort.math.RayCast;
import com.doobs.exort.util.Camera;
import com.doobs.exort.util.GLTools;

public class Main {
	public static final String TITLE = "Exort";
	public static int width = 800, height = 600;

	private Map map;
	private Player player;
	private Camera camera;
	
	private boolean closeRequested;

	public Main() {
		GLTools.init();

		map = new Map();
		player = new Player();
		camera = new Camera();

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
			}
		}
		
		GLTools.tick();

		camera.tick(delta);
		map.tick(delta);
		player.tick(delta);
	}

	public void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		glPushMatrix();
		camera.applyTransformations();
		
		//while(Mouse.next()) {
			//if(Mouse.getEventButtonState() && Mouse.getEventButton() == 0) {
		Ray ray;
		if(Mouse.isGrabbed()) // Assume the mouse is in the middle of the screen if it is grabbed
			ray = new Ray(camera, RayCast.getDirection(Display.getWidth() / 2, Display.getHeight() / 2));
		else
			ray = new Ray(camera, RayCast.getDirection(Mouse.getX(), Mouse.getY()));
		
		// Find the location where the ray intersects the ground plane
		Vector3f plane = RayCast.findPlane(camera, ray);
		
		if(plane!= null) {
			player.move(plane);
			System.out.println(camera.rotX + " " + camera.rotY + " " + camera.rotZ);
		}
		//	}
		//}
		
		
		
		map.render();
		player.render();
		glPopMatrix();
		
		glBegin(GL_LINES);
		glVertex3f( 0.0f,  0.1f, -1.0f);
		glVertex3f( 0.0f, -0.1f, -1.0f);
		glVertex3f(-0.1f,  0.0f, -1.0f);
		glVertex3f( 0.1f,  0.0f, -1.0f); 
		glEnd();
	}

	public static void main(String[] args) {
		new Main();
	}
}
