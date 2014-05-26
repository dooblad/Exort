package res.shaders;

import com.doobs.exort.util.*;

public class Shaders {
	public static ShaderProgram lighting;
	public static ShaderProgram gui;

	public static void init() {
		lighting = new ShaderProgram("lighting");
		gui = new ShaderProgram("gui");
	}
}
