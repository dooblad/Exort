package client.util.loaders;

import static org.lwjgl.opengl.GL20.*;

import java.util.*;

import com.doobs.modern.util.shader.*;

public class Shaders {
	private static final String DIRECTORY = "res/shaders/";

	public static Shader current;
	public static Map<String, Shader> shaders;

	public static void init() {
		System.out.println("----------------------\n" + "|  LOADING  SHADERS  |\n" + "----------------------");

		shaders = new HashMap<String, Shader>();
		List<String> elements = DirectorySearcher.findElements(DIRECTORY, "vert");
		for(String URL : elements) {
			shaders.put(URL, new Shader(DIRECTORY + URL));
		}
	}

	public static Shader get(String shader) {
		return shaders.get(shader);
	}

	public static void use(String shader) {
		current = shaders.get(shader);
		current.use();
	}

	public static void useDefault() {
		glUseProgram(0);
	}
}
