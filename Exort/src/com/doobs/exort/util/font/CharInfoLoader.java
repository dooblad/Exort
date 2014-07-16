package com.doobs.exort.util.font;

import java.io.*;
import java.util.*;

import com.doobs.exort.util.texture.*;

@SuppressWarnings("rawtypes")
public class CharInfoLoader {

	public static Map<Integer, Character> load(Class c, Texture texture, String URL) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(c.getResourceAsStream(URL)));

			Map<Integer, Character> characters = new HashMap<Integer, Character>();

			String line;
			String[] temp;

			while ((line = reader.readLine()) != null) {
				if (line.startsWith("char ")) {
					String number = "";
					int numbers = 0;
					temp = new String[6];

					for (int i = 0; i < line.length(); i++) {
						if ((int) line.charAt(i) >= 48 && (int) line.charAt(i) <= 57)
							number += line.charAt(i);
						else if (!number.equals("")) {
							temp[numbers] = number;
							number = "";
							if (numbers >= 5)
								break;
							numbers++;
						}
					}

					characters.put(Integer.valueOf(temp[0]),
							new Character(texture.getWidth(), texture.getHeight(), Integer.valueOf(temp[1]), Integer.valueOf(temp[2]),
									Integer.valueOf(temp[3]), Integer.valueOf(temp[4])));
				}
			}

			return characters;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
