package res.shaders;

import com.doobs.exort.util.*;

public class Shaders {
	public static Shader lighting;
	public static Shader bloom;
	public static Shader gui;

	public static void init() {
		lighting = new Shader(Shaders.class, "lighting");
		bloom = new Shader(Shaders.class, "bloom");
		gui = new Shader(Shaders.class, "gui");
	}
}
