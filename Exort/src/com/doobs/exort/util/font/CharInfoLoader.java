package com.doobs.exort.util.font;

import java.io.*;

import com.doobs.exort.util.texture.*;

@SuppressWarnings("rawtypes")
public class CharInfoLoader {
	public static Character[] load(Class c, Texture texture, String URL) {
		Character[] characters = null;

		System.out.println((int) '0' + " " + (int) '9');
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(c.getResourceAsStream(URL)));
			
			String line;
			String[] temp;

			while ((line = reader.readLine()) != null) {
				if (line.startsWith("chars ")) {
					temp = line.split("=");
					characters = new Character[255];//Integer.valueOf(temp[1])];
				} else if (line.startsWith("char ")) {
					String number = "";
					int numbers = 0;
					temp = new String[6];
					
					for(int i = 0; i < line.length(); i++) {
						if((int) line.charAt(i) >= 48 && (int) line.charAt(i) <= 57)
							number += line.charAt(i);
						else if(!number.equals("")) {
							temp[numbers] = number;
							number = "";
							if(numbers >= 5)
								break;
							numbers++;
						}
					}
					
					characters[Integer.valueOf(temp[0])] = new Character(texture.getWidth(), texture.getHeight(), Integer.valueOf(temp[1]),
							Integer.valueOf(temp[2]), Integer.valueOf(temp[3]), Integer.valueOf(temp[4]));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return characters;
	}
}
