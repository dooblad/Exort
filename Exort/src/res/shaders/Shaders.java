package res.shaders;

import com.doobs.exort.util.*;

public class Shaders {
	public static ShaderProgram defaultShader;
	
	public static void init() {
		defaultShader = new ShaderProgram("default");
	}
}
