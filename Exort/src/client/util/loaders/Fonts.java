package client.util.loaders;

import java.util.*;

import client.*;
import client.util.font.*;

public class Fonts {
	private static final String DIRECTORY = "res/textures/fonts/";

	public static Font current;
	public static Map<String, Font> fonts;

	public static void init(Main main) {
		System.out.println("----------------------\n" + "|   LOADING FONTS    |\n" + "----------------------");

		fonts = new HashMap<String, Font>();
		List<String> elements = DirectorySearcher.findElements(DIRECTORY, "png");
		for (String URL : elements) {
			fonts.put(URL, new Font(main, DIRECTORY + URL));
		}
		current = fonts.get("centuryGothic");
	}

	public static Font get(String font) {
		return fonts.get(font);
	}
}
