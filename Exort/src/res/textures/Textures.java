package res.textures;

import java.io.*;
import java.util.*;

import com.doobs.exort.util.texture.*;

public class Textures {
	public static Map<String, Texture> textures = new HashMap<String, Texture>();

	public static void init() {
		File directory = new File("src/res/textures/");
		File[] files = directory.listFiles();
		String[] temp;

		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					temp = file.getName().split("\\.");

					if (temp[1].equals("png")) {
						System.out.println(temp[0]);
						textures.put(temp[0], TextureLoader.getTexture(temp[0] + "." + temp[1]));
					}
				}
			}
		}
	}

	public static Texture getTexture(String key) {
		return textures.get(key);
	}
}
