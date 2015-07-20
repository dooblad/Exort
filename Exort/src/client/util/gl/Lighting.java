package client.util.gl;

import static org.lwjgl.opengl.GL20.*;
import client.util.loaders.*;

import com.doobs.modern.util.shader.*;

/**
 * A helper class for the lighting shader.
 */
public class Lighting {
	public static final float[] LIGHT_COLOR = new float[] { 1f, 0.5f, 0f, 1f };
	public static final float[] AMBIENT_COLOR = new float[] { 0.3f, 0.3f, 1f, 0.5f };
	// Quadratic attenuation.
	public static final float[] FALLOFF = new float[] { 0.25f, 0.01f, 0.005f };
	public static final float[] START_POSITION = new float[] { 0f, 1f, 0f };

	private static Shader shader;

	public static void init() {
		shader = Shaders.get("lighting");
		// Use it, so we can start setting GLSL variables.
		shader.use();

		// Initialize GLSL variables.
		shader.setUniform3f("lightPosition", START_POSITION[0], START_POSITION[1], START_POSITION[2]);
		shader.setUniform4f("lightColor", LIGHT_COLOR[0], LIGHT_COLOR[1], LIGHT_COLOR[2], LIGHT_COLOR[3]);
		shader.setUniform4f("ambientColor", AMBIENT_COLOR[0], AMBIENT_COLOR[1], AMBIENT_COLOR[2], AMBIENT_COLOR[3]);
		shader.setUniform3f("falloff", FALLOFF[0], FALLOFF[1], FALLOFF[2]);
		setTextured(false);
		setNormalMapped(false);

		Shaders.useDefault();
	}

	/**
	 * Sets the light's position to ("x", "y", "z").
	 */
	public static void setPosition(float x, float y, float z) {
		int location = glGetUniformLocation(Shaders.get("lighting").getID(), "lightPosition");
		glUniform3f(location, x, y, z);
	}

	/**
	 * Pre: OpenGL must be using the lighting shader.
	 */
	public static void setTextured(boolean textured) {
		shader.setUniform1i("textured", textured ? 1 : 0);
	}

	/**
	 * Pre: OpenGL must be using the lighting shader.
	 */
	public static void setNormalMapped(boolean normalMapped) {
		shader.setUniform1i("normalMapped", normalMapped ? 1 : 0);
	}
}