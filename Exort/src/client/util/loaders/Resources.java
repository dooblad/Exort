package client.util.loaders;

import client.*;

public class Resources {
	/**
	 * Initializes Exort's resources in the proper order.
	 */
	public static void init(Main main) {
		Shaders.init();
		Textures.init();
		Fonts.init(main);
		Models.init();
		Cursor.init();

		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("| FINISHED LOADING! LET'S DO THIS! |");
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}
}
