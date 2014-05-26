package com.doobs.exort.gfx;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.*;

import org.lwjgl.*;
import org.lwjgl.util.vector.*;

import res.shaders.*;

public class Lighting {
	public static final float[] lightColor = new float[] {1f, 0.5f, 0f, 1f};
	public static final float[] ambientColor = new float[] {0.6f, 0.6f, 1f, 0.2f};
	public static final float[] falloff = new float[] {0.2f, 0.05f, 0.05f};
	
	// Shader locations
	private static int lightPositionLocation, lightColorLocation, ambientColorLocation, falloffLocation;
	
	private static Vector3f position;
	
	public static void setupLighting() {
		Shaders.lighting.use();
		
		lightPositionLocation = glGetUniformLocation(Shaders.lighting.getID(), "lightPosition");
		position = new Vector3f(0f, 1f, 0f);
		position.normalise();
		glUniform3f(lightPositionLocation, position.getX(), position.getY(), position.getZ());
		
		lightColorLocation = glGetUniformLocation(Shaders.lighting.getID(), "lightColor");
		glUniform4f(lightColorLocation, lightColor[0], lightColor[1], lightColor[2], lightColor[3]);
		
		ambientColorLocation = glGetUniformLocation(Shaders.lighting.getID(), "ambientColor");
		glUniform4f(ambientColorLocation, ambientColor[0], ambientColor[1], ambientColor[2], ambientColor[3]);
		
		falloffLocation = glGetUniformLocation(Shaders.lighting.getID(), "falloff");
		glUniform3f(falloffLocation, falloff[0], falloff[1], falloff[2]);
		
		Shaders.lighting.end();
	}

	public static void moveLight(Vector3f position, boolean usingShader) {
		if (!usingShader)
			Shaders.lighting.use();

		int location = glGetUniformLocation(Shaders.lighting.getID(), "lightPosition");
		glUniform3f(location, position.getX(), position.getY(), position.getZ());

		if (!usingShader)
			Shaders.lighting.end();
	}
	
	//public static void drawLight()

	public static void sendModelViewMatrix() {
		int location = glGetUniformLocation(Shaders.lighting.getID(), "modelViewMatrix");
		FloatBuffer modelViewMatrix = BufferUtils.createFloatBuffer(16);
		glGetFloat(GL_MODELVIEW_MATRIX, modelViewMatrix);
		glUniformMatrix4(location, false, modelViewMatrix);
	}
}
