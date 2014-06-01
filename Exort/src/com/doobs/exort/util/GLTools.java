package com.doobs.exort.util;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import java.nio.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

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

	public static boolean fullscreen = false;

	public static FloatBuffer perspectiveProjectionMatrix = BufferUtils.createFloatBuffer(16);
	public static FloatBuffer orthographicProjectionMatrix = BufferUtils.createFloatBuffer(16);

	public static void init() {
		getDelta();
		lastFPS = getTime();
		initDisplay();
		initGL();
		aspectRatio = (float) Main.width / (float) Main.height;
	}

	public static void initDisplay() {
		try {
			setDisplayMode(Main.width, Main.height, fullscreen);
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
		gluPerspective(fov, ((float) Main.width / (float) Main.height), zNear, zFar);
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

		glClearColor(0f, 0f, 0f, 0f);
		glColor4f(1f, 1f, 1f, 1f);
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
		gluPerspective(fov, (float) Main.width / (float) Main.height, zNear, zFar);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		// Ortho
		glGetFloat(GL_PROJECTION_MATRIX, perspectiveProjectionMatrix);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Main.width, 0, Main.height, 1, -1);
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
		// THIS METHOD SHOULD BE CREATING THE PROPER ORTHOGRAPHICAL ENVIRONMENT FOR USE IN
		// THE GUI RENDER() FUNCTION, BUT NOPE
		
		glMatrixMode(GL_PROJECTION);
		glLoadMatrix(orthographicProjectionMatrix);
		glMatrixMode(GL_MODELVIEW);
		glPushMatrix();
		glLoadIdentity();
	}

	public static void switchToPerspective() {
		glPopMatrix();
		glMatrixMode(GL_PROJECTION);
		glLoadMatrix(perspectiveProjectionMatrix);
		glMatrixMode(GL_MODELVIEW);
	}

	public static void toggleMouseGrabbed() {
		mouseGrabbed = !mouseGrabbed;
		Mouse.setGrabbed(mouseGrabbed);
	}

	public static void setDisplayMode(int width, int height, boolean fullscreen) {
		// return if requested DisplayMode is already set
		if ((Display.getDisplayMode().getWidth() == width) && (Display.getDisplayMode().getHeight() == height) && (Display.isFullscreen() == fullscreen)) {
			return;
		}

		try {
			DisplayMode targetDisplayMode = null;

			if (fullscreen) {
				DisplayMode[] modes = Display.getAvailableDisplayModes();
				int freq = 0;

				for (int i = 0; i < modes.length; i++) {
					DisplayMode current = modes[i];

					if ((current.getWidth() == width) && (current.getHeight() == height)) {
						if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
							if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
								targetDisplayMode = current;
								freq = targetDisplayMode.getFrequency();
							}
						}

						// if we've found a match for bpp and frequence against
						// the
						// original display mode then it's probably best to go
						// for this one
						// since it's most likely compatible with the monitor
						if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel())
								&& (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
							targetDisplayMode = current;
							break;
						}
					}
				}
			} else {
				targetDisplayMode = new DisplayMode(width, height);
			}

			if (targetDisplayMode == null) {
				System.out.println("Failed to find value mode: " + width + "x" + height + " fs=" + fullscreen);
				return;
			}

			Display.setDisplayMode(targetDisplayMode);
			Display.setFullscreen(fullscreen);

		} catch (LWJGLException e) {
			System.out.println("Unable to setup mode " + width + "x" + height + " fullscreen=" + fullscreen + e);
		}
	}

	public static void toggleFullscreen() {
		fullscreen = !fullscreen;
		setDisplayMode(Main.width, Main.height, fullscreen);
	}

	public static FloatBuffer asFloatBuffer(float... values) {
		FloatBuffer result = BufferUtils.createFloatBuffer(values.length);
		result.put(values);
		result.flip();
		return result;
	}

	public static IntBuffer asIntBuffer(int... values) {
		IntBuffer result = BufferUtils.createIntBuffer(values.length);
		result.put(values);
		result.flip();
		return result;
	}
	
	public static float[] asFloatArray(FloatBuffer buffer) {
		float[] result = new float[16];

		for(int i = 0; i < result.length; i++) {
			result[i] = buffer.get(i);
		}
		
		return result;
	}
}
