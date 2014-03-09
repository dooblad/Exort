package com.doobs.exort.util;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.*;

import org.lwjgl.*;
import org.lwjgl.util.vector.*;

import res.shaders.*;

public class Lighting {

	public static void setupLighting() {
		Shaders.lightingShader.use();
		int location = glGetUniformLocation(Shaders.lightingShader.getID(),
				"lightPosition");
		Vector3f lightPosition = new Vector3f(0f, 1f, 0f);
		lightPosition.normalise();
		glUniform3f(location, lightPosition.getX(), lightPosition.getY(),
				lightPosition.getZ());
		location = glGetUniformLocation(Shaders.lightingShader.getID(),
				"lightColor");
		glUniform4f(location, 1f, 0.5f, 0f, 1f);
		location = glGetUniformLocation(Shaders.lightingShader.getID(),
				"ambientColor");
		glUniform4f(location, 0.6f, 0.6f, 1f, 0.2f);
		location = glGetUniformLocation(Shaders.lightingShader.getID(),
				"falloff");
		glUniform3f(location, 0.2f, 0.05f, 0.05f);
		Shaders.lightingShader.end();
	}

	public static void moveLight(Vector3f position, boolean usingShader) {
		if (!usingShader)
			Shaders.lightingShader.use();

		int location = glGetUniformLocation(Shaders.lightingShader.getID(),
				"lightPosition");
		glUniform3f(location, position.getX(), 1f, position.getZ());

		if (!usingShader)
			Shaders.lightingShader.end();
	}
	
	public static void sendModelViewMatrix() {
		int location = glGetUniformLocation(Shaders.lightingShader.getID(), "modelViewMatrix");
		FloatBuffer modelViewMatrix = BufferUtils.createFloatBuffer(16);
		glGetFloat(GL_MODELVIEW_MATRIX, modelViewMatrix);
		glUniformMatrix4(location, false, modelViewMatrix);
	}
}
