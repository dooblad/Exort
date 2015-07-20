package client.util.font;

import java.io.*;
import java.util.*;

import com.doobs.modern.util.texture.*;

/**
 * Loads the attributes of every Character in a Font texture from a File.
 */
public class CharInfoLoader {
	/**
	 * Returns a Map from a int/char to a renderable Character.
	 */
	public static Map<Integer, Character> load(Texture texture, String URL) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(URL)));
			Map<Integer, Character> characters = new HashMap<Integer, Character>();
			String line = reader.readLine();
			float[] temp;
			float width = texture.getWidth();
			float height = texture.getHeight();

			while (line != null) {
				if (line.startsWith("char ")) {
					String number = "";
					int index = 0;
					temp = new float[8];

					// Extract numbers until the end of the line or capacity is reached.
					for (int i = 0; i < line.length() && index < temp.length; i++) {
						char ch = line.charAt(i);
						if (isNumber(ch)) {
							number += ch;
						} else if (!number.isEmpty()) {
							temp[index] = Float.parseFloat(number);
							number = "";
							index++;
						}
					}

					characters.put((int) temp[0], new Character(width, height, temp[1], temp[2], temp[3], temp[4], temp[5], temp[6], temp[7]));
				}
				line = reader.readLine();
			}
			reader.close();
			return characters;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Returns true if "ch" is a numeric character or a period (for decimals).
	 */
	private static boolean isNumber(char ch) {
		return (ch >= 48 && ch <= 57) || ch == '.';
	}
}