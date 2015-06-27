package com.doobs.exort.util.loaders;

import com.doobs.exort.*;
import com.doobs.exort.util.font.*;

public class Fonts {
	public static Font centuryGothic;

	public static void init(Main main) {
		System.err.println("----------------------\n" + "|   LOADING FONTS    |\n" + "----------------------");

		centuryGothic = new Font(main, "centuryGothic");
	}
}
