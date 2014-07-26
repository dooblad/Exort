package res.models;

import java.io.*;
import java.util.*;

import com.doobs.exort.util.gl.*;

public class Models {
	public static Map<String, TexturedModel> stillModels = new HashMap<String, TexturedModel>();
	public static Map<String, AnimatedModel> animatedModels = new HashMap<String, AnimatedModel>();

	public static void init() {
		File directory = new File("res/models/");
		File[] files = directory.listFiles();
		String[] temp;

		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					// Static models
					temp = file.getName().split("\\.");

					if (temp[1].equals("obj")) {
						stillModels.put(temp[0], OBJLoader.loadModel(file.getName()));
					}
				} else if (file.isDirectory()) {
					// Animated models
					List<TexturedModel> modelsTemp = new ArrayList<TexturedModel>();

					File[] subFiles = file.listFiles();
					for (File subFile : subFiles) {
						temp = subFile.getName().split("\\.");

						if (temp[1].equals("obj")) {
							modelsTemp.add(OBJLoader.loadModel(file.getName() + "/" + subFile.getName()));
						}
					}

					animatedModels.put(file.getName().split("\\.")[0], new AnimatedModel(modelsTemp));
				}
			}
		}
	}

	public AnimatedModel getModel(String key) {
		return animatedModels.get(key);
	}
}
