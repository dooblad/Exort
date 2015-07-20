package client.util.loaders;

import java.util.*;

import client.util.obj.*;

public class Models {
	private static final String DIRECTORY = "res/models/";

	public static Map<String, Model> models;

	public static void init() {
		// TODO: ANIMATIONS FOR DIRECTORIES OF .OBJs

		System.out.println("----------------------\n" + "|   LOADING MODELS   |\n" + "----------------------");

		models = new HashMap<String, Model>();

		List<String> elements = DirectorySearcher.findElements(DIRECTORY, "obj");
		for (String URL : elements) {
			// TODO: Standardize on where to include the file extension (e.g. adding the
			// ".obj" here).
			models.put(URL, OBJLoader.load(DIRECTORY + URL + ".obj"));
		}
	}

	public static Model get(String key) {
		return models.get(key);
	}
}
