package com.doobs.exort.util;

import java.nio.*;

import static org.lwjgl.opengl.GL11.*;
import static com.doobs.exort.util.GLTools.asFloatBuffer;
import static org.lwjgl.input.Keyboard.*;

public class Lighting {
	
	public static float[] lightPosition = {0.0f, 2.0f, 0.0f, 1.0f};
	private static FloatBuffer ambientLight = asFloatBuffer(0.2f, 0.2f, 0.2f, 1.0f);
	
	public static void setupLighting() {
		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);
		glLightModel(GL_LIGHT_MODEL_AMBIENT, ambientLight);
		glLight(GL_LIGHT0, GL_POSITION, asFloatBuffer(lightPosition));
		glEnable(GL_COLOR_MATERIAL);
		glColorMaterial(GL_FRONT, GL_DIFFUSE);
	}
	
	public static void updateLight(Camera camera) {
		if (isKeyDown(KEY_G))
			glLight(GL_LIGHT0,
					GL_POSITION,
					asFloatBuffer((float) camera.getX(), (float) camera.getY(),
							(float) camera.getZ(), 1));
	}
}
