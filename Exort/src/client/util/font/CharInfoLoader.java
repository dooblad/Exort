package client.util.font;

import java.io.*;
import java.util.*;

import client.util.*;

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
			Number[] temp;
			float width = texture.getWidth();
			float height = texture.getHeight();

			while (line != null) {
				if (line.startsWith("char ")) {
					temp = FileReadingUtils.extractNumbers(line, 8);
					characters.put(
							temp[0].intValue(),
							new Character(width, height, temp[1].floatValue(), temp[2].floatValue(), temp[3].floatValue(), temp[4].floatValue(), temp[5]
									.floatValue(), temp[6].floatValue(), temp[7].floatValue()));
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

}