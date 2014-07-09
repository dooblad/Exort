package res.models;

import java.io.*;
import java.util.*;

import com.doobs.exort.util.gl.*;

public class Models {
	public static Map<String, TexturedModel> stillModels = new HashMap<String, TexturedModel>();
	public static Map<String, AnimatedModel> animatedModels = new HashMap<String, AnimatedModel>();
	
	public static void init() {
		File directory = new File("src/res/models/");
		File[] files = directory.listFiles();
		String[] temp;

		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					temp = file.getName().split("\\.");

					if (temp[1].equals("obj")) {
						stillModels.put(temp[0], OBJLoader.loadModel(temp[0] + "." + temp[1]));
					}
				} else if(file.isDirectory()) {
					List<TexturedModel> modelsTemp = new ArrayList<TexturedModel>();
					
					File[] subFiles = file.listFiles();
					for(File subFile : subFiles) {
						temp = subFile.getName().split("\\.");
						
						if(temp[1].equals("obj")) {
							modelsTemp.add(OBJLoader.loadModel(temp[0] + "." + temp[1]));
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
