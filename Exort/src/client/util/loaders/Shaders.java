package client.util.loaders;

import static org.lwjgl.opengl.GL20.*;

import java.io.*;
import java.util.*;

import com.doobs.modern.util.shader.*;

public class Shaders {
	private static final String DIRECTORY = "res/shaders/";

	public static Shader current;
	public static Map<String, Shader> shaders;

	public static void init() {
		shaders = new HashMap<String, Shader>();
		System.err.println("----------------------\n" + "|  LOADING  SHADERS  |\n" + "----------------------");

		File directory = new File(DIRECTORY);
		File[] files = directory.listFiles();
		String[] temp;

		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					temp = file.getName().split("\\.");

					if (temp[1].equals("vert")) {
						System.out.println(DIRECTORY + temp[0]);
						shaders.put(temp[0], new Shader(DIRECTORY + temp[0]));
					}
				}
			}
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
