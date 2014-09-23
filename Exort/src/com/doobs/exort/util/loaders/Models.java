package com.doobs.exort.util.loaders;

import java.io.*;
import java.util.*;

import com.doobs.exort.util.obj.*;

public class Models {
	private static final String DIRECTORY = "res/models/";
	
	public static Map<String, Model> models = new HashMap<String, Model>();
	
	public static void init() {
		System.err.println("----------------------\n" +
						   "|   LOADING MODELS   |\n" +
						   "----------------------");
		File directory = new File(DIRECTORY);
		File[] files = directory.listFiles();
		String[] temp;

		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					temp = file.getName().split("\\.");

					if (temp[1].equals("obj")) {
						models.put(temp[0], OBJLoader.load(DIRECTORY + file.getName()));
					}
				} else if(file.isDirectory()) {
					// ANIMATIONS
				}
			}
		}
	}
	
	public static Model get(String key) {
		return models.get(key);
	}
}
