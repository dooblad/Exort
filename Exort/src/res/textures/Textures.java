package res.textures;

import java.io.*;
import java.util.*;

import com.doobs.exort.util.texture.*;

public class Textures {
	public static Map<String, Texture> textures = new HashMap<String, Texture>();

	public static void init() {
		File directory = new File("res/textures/");
		File[] files = directory.listFiles();
		String[] temp;

		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					temp = file.getName().split("\\.");

					if (temp[1].equals("png")) {
						System.out.println(file.getName());
						textures.put(temp[0], TextureLoader.getTexture("res/textures/" + file.getName(), false));
					}
				}
			}
		}
	}

	public static Texture get(String key) {
		return textures.get(key);
	}
}
