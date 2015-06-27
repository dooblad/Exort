package exort.util.loaders;

import java.io.*;
import java.util.*;

import com.doobs.modern.util.texture.*;

public class Textures {
	private static final String DIRECTORY = "res/textures/";

	public static Map<String, Texture> textures = new HashMap<String, Texture>();

	public static void init() {
		System.err.println("----------------------\n" + "|  LOADING  TEXTURES |\n" + "----------------------");

		File directory = new File(DIRECTORY);
		File[] files = directory.listFiles();
		String[] temp;

		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					temp = file.getName().split("\\.");

					if (temp[1].equals("png")) {
						System.out.println(DIRECTORY + file.getName());
						textures.put(temp[0], TextureLoader.getTexture(DIRECTORY + file.getName(), false));
					}
				}
			}
		}
	}

	public static Texture get(String key) {
		return textures.get(key);
	}
}
