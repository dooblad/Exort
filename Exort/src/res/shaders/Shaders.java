package res.shaders;

import com.doobs.exort.util.*;

public class Shaders {
	public static ShaderProgram lightingShader;
	
	public static void init() {
		lightingShader = new ShaderProgram("lighting");
	}
}
