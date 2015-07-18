package client.util.loaders;

import client.*;
import client.util.font.*;

public class Fonts {
	public static Font centuryGothic;

	public static void init(Main main) {
		System.err.println("----------------------\n" + "|   LOADING FONTS    |\n" + "----------------------");

		centuryGothic = new Font(main, "centuryGothic");
	}
}
