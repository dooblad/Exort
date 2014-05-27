package res.textures;

import java.util.*;

import com.doobs.exort.util.*;

public class Textures {
	public static Map<String, Texture> textures = new HashMap<String, Texture>();
	
	public static Texture abilityHUD;
	public static Texture mapTest;
	
	public static void init() {
		abilityHUD = TextureLoader.getTexture("abilityHUD.png");
		textures.put("abilityHUD", abilityHUD);
		
		mapTest = TextureLoader.getTexture("mapTest.png");
		textures.put("mapTest", mapTest);
	}
}
