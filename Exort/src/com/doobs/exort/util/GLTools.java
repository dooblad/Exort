package com.doobs.exort.util;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import res.Shaders;

import com.doobs.exort.Main;

public class GLTools {
	public static long lastFrame;
	public static int fps, perFrameFPS;
	public static long lastFPS;

	public static float zNear = 0.0001f, zFar = 1000.0f;
	
	public static float aspectRatio;
	public static float fov = 90.0f;
	
	public static boolean vSync = true;
	public static boolean mouseGrabbed = false;

	public static FloatBuffer perspectiveProjectionMatrix = BufferUtils
			.createFloatBuffer(16);
	public static FloatBuffer orthographicProjectionMatrix = BufferUtils
			.createFloatBuffer(16);

	public static void init() {
		getDelta();
		lastFPS = getTime();
		initDisplay();
		initGL();
		Shaders.init();
		
		aspectRatio = (float) Main.width / (float) Main.height;
	}

	public static void initDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(Main.width, Main.height));
			Display.setTitle(Main.TITLE);
			Display.setVSyncEnabled(vSync);
			Display.setResizable(true);
			Mouse.setGrabbed(mouseGrabbed);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

	public static void initGL() {
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(fov, ((float) Main.width / (float) Main.height), zNear,
				zFar);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
		glMatrixMode(GL_MODELVIEW);

		glGetFloat(GL_PROJECTION_MATRIX, perspectiveProjectionMatrix);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Main.width, Main.height, 0, 1, -1);
		glGetFloat(GL_PROJECTION_MATRIX, orthographicProjectionMatrix);
		glLoadMatrix(perspectiveProjectionMatrix);
		glMatrixMode(GL_MODELVIEW);

		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	}

	public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	public static int getDelta() {
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;

		return delta;
	}

	public static void tick() {
		if (Display.wasResized())
			resizeGL();
		updateFPS();
		Display.setTitle(Main.TITLE + " FPS: " + perFrameFPS);
	}

	public static void resizeGL() {
		Main.width = Display.getWidth();
		Main.height = Display.getHeight();
		// Perspective
		glViewport(0, 0, Main.width, Main.height);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(fov, (float) Main.width / (float) Main.height, zNear,
				zFar);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		// Ortho
		glGetFloat(GL_PROJECTION_MATRIX, perspectiveProjectionMatrix);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Main.width, Main.height, 0, 1, -1);
		glGetFloat(GL_PROJECTION_MATRIX, orthographicProjectionMatrix);
		glLoadMatrix(perspectiveProjectionMatrix);
		glMatrixMode(GL_MODELVIEW);
	}

	public static void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			perFrameFPS = fps;
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}

	public static void switchToOrtho() {
		glMatrixMode(GL_PROJECTION);
		glLoadMatrix(GLTools.orthographicProjectionMatrix);
		glMatrixMode(GL_MODELVIEW);
		glPushMatrix();
		glLoadIdentity();
	}

	public static void switchToPerspective() {
		glPopMatrix();
		glMatrixMode(GL_PROJECTION);
		glLoadMatrix(GLTools.perspectiveProjectionMatrix);
		glMatrixMode(GL_MODELVIEW);
	}

	public static void toggleMouseGrabbed() {
		mouseGrabbed = !mouseGrabbed;
		Mouse.setGrabbed(mouseGrabbed);
	}

	public static FloatBuffer asFloatBuffer(float... values) {
		FloatBuffer result = BufferUtils.createFloatBuffer(values.length);
		result.put(values);
		result.flip();
		return result;
	}
}
