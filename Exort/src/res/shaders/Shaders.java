package res.shaders;

import static org.lwjgl.opengl.GL20.*;

import com.doobs.exort.util.gl.*;

public class Shaders {
	public static Shader lighting;
	public static Shader font;
	public static Shader gui;

	public static void init() {
		lighting = new Shader(Shaders.class, "lighting");
		font = new Shader(Shaders.class, "font");
		gui = new Shader(Shaders.class, "gui");
	}

	public static void useDefault() {
		glUseProgram(0);
	}
}
