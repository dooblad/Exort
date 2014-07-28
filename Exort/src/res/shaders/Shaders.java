package res.shaders;

import static org.lwjgl.opengl.GL20.*;

import com.doobs.exort.util.gl.*;

public class Shaders {
	public static Shader color;
	public static Shader texture;
	public static Shader lighting;
	public static Shader font;
	public static Shader gui;

	public static void init() {
		color = new Shader("color");
		texture = new Shader("texture");
		lighting = new Shader("lighting");
		font = new Shader("font");
		gui = new Shader("gui");
	}

	public static void useDefault() {
		glUseProgram(0);
	}
}
