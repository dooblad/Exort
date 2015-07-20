package client.util.loaders;

import java.io.*;
import java.util.*;

/**
 * Locates resources with a specific extension within a specified directory.
 */
public class DirectorySearcher {
	/**
	 * Returns a List<String> of all files with "extension" in the directory at
	 * "URL".
	 */
	public static List<String> findElements(String URL, String extension) {
		List<String> elements = new ArrayList<String>();

		File directory = new File(URL);
		File[] files = directory.listFiles();
		String[] extensionSplitter;

		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					extensionSplitter = file.getName().split("\\.");

					// If the extension matches.
					if (extensionSplitter[1].equals(extension)) {
						// Print the name.
						System.out.println(extensionSplitter[0]);
						elements.add(extensionSplitter[0]);
					}
				}
			}
		}
		return elements;
	}
}
