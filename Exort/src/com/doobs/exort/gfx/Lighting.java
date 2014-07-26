package com.doobs.exort.gfx;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.*;

import org.lwjgl.*;
import org.lwjgl.util.vector.*;

import res.shaders.*;

/**
 * A helper class for lighting
 * 
 * @author Logan
 */
public class Lighting {
	public static final float[] lightColor = new float[] { 1f, 0.5f, 0f, 1f };
	public static final float[] ambientColor = new float[] { 0.3f, 0.3f, 1f, 0.5f };
	public static final float[] falloff = new float[] { 0.25f, 0.01f, 0.005f };

	private static boolean textured;
	private static boolean normalMapped;

	private static Vector3f position;

	public static void init() {
		Shaders.lighting.use();

		position = new Vector3f(0f, 1f, 0f);
		position.normalise();
		Shaders.lighting.setUniform3f("lightPosition", position.getX(), position.getY(), position.getZ());
		Shaders.lighting.setUniform4f("lightColor", lightColor[0], lightColor[1], lightColor[2], lightColor[3]);
		Shaders.lighting.setUniform4f("ambientColor", ambientColor[0], ambientColor[1], ambientColor[2], ambientColor[3]);
		Shaders.lighting.setUniform3f("falloff", falloff[0], falloff[1], falloff[2]);
		setTextured(false);
		setNormalMapped(false);
		Shaders.lighting.setUniform1i("diffuseTexture", 0);
		Shaders.lighting.setUniform1i("normalMap", 1);

		Shaders.useDefault();
	}

	public static void moveLight(Vector3f position, boolean usingShader) {
		if (!usingShader)
			Shaders.lighting.use();

		int location = glGetUniformLocation(Shaders.lighting.getID(), "lightPosition");
		glUniform3f(location, position.getX(), position.getY(), position.getZ());

		if (!usingShader)
			Shaders.useDefault();
	}

	public static void setTextured(boolean textured) {
		Lighting.textured = textured;
		Shaders.lighting.setUniform1i("textured", textured ? 1 : 0);
	}

	public static void setNormalMapped(boolean normalMapped) {
		Lighting.normalMapped = normalMapped;
		Shaders.lighting.setUniform1i("normalMapped", normalMapped ? 1 : 0);
	}

	public static void sendModelViewMatrix() {
		FloatBuffer modelViewMatrix = BufferUtils.createFloatBuffer(16);
		glGetFloat(GL_MODELVIEW_MATRIX, modelViewMatrix);
		Shaders.lighting.setUniformMatrix4("modelViewMatrix", false, modelViewMatrix);
	}

	// Getters and setters
	public static boolean isTextured() {
		return textured;
	}

	public static boolean isNormalMapped() {
		return normalMapped;
	}
}
