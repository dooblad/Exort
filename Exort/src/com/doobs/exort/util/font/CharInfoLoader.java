package com.doobs.exort.util.font;

import java.io.*;
import java.util.*;

import com.doobs.modern.util.texture.*;

public class CharInfoLoader {

	public static Map<Integer, Character> load(Texture texture, String URL) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(URL)));

			Map<Integer, Character> characters = new HashMap<Integer, Character>();

			String line;
			double[] temp;

			while ((line = reader.readLine()) != null) {
				if (line.startsWith("char ")) {
					String number = "";
					int numbers = 0;
					temp = new double[8];

					for (int i = 0; i < line.length(); i++) {
						if (line.charAt(i) >= 48 && line.charAt(i) <= 57 || line.charAt(i) == '.')
							number += line.charAt(i);
						else if (!number.equals("")) {
							temp[numbers] = Double.valueOf(number);
							number = "";
							if (numbers >= temp.length - 1)
								break;
							numbers++;
						}
					}

					characters.put((int) temp[0], new Character(texture.getWidth(), texture.getHeight(), (int) temp[1], (int) temp[2], (int) temp[3],
							(int) temp[4], (int) temp[5], (int) temp[6], (int) temp[7]));
				}
			}

			reader.close();

			return characters;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
