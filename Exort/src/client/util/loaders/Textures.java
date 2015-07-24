package client.util.loaders;

import java.util.*;

import com.doobs.modern.util.texture.*;

public class Textures {
	private static final String DIRECTORY = "res/textures/";

	public static Map<String, Texture> textures = new HashMap<String, Texture>();

	public static void init() {
		System.out.println("----------------------\n" + "|  LOADING  TEXTURES |\n" + "----------------------");

		textures = new HashMap<String, Texture>();
		List<String> elements = DirectorySearcher.findElements(DIRECTORY, "png");
		for(String URL : elements) {
			textures.put(URL, TextureLoader.getTexture(DIRECTORY + URL + ".png", false));
		}
	}

	/**
	 * Returns the Texture with "name".
	 */
	public static Texture get(String name) {
		return textures.get(name);
	}
}
