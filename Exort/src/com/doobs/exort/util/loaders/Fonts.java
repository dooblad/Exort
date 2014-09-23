package com.doobs.exort.util.loaders;

import com.doobs.exort.util.font.*;

public class Fonts {
	public static Font centuryGothic;

	public static void init() {
		System.err.println("----------------------\n" +
				   		   "|   LOADING FONTS    |\n" +
				           "----------------------");
		
		centuryGothic = new Font("centuryGothic");
	}
}
