package res.textures;

import java.util.*;

import com.doobs.exort.util.texture.*;

public class Textures {
	public static Map<String, Texture> textures = new HashMap<String, Texture>();
	
	public static Texture abilityHUD;
	public static Texture arena, arenaNormal;
	public static Texture mapTest;
	
	public static void init() {
		abilityHUD = TextureLoader.getTexture("abilityHUD.png");
		textures.put("abilityHUD", abilityHUD);
		
		arena = TextureLoader.getTexture("arena.png");
		textures.put("arena", arena);
		//arenaNormal = TextureLoader.getTexture("arenaNormal.png");
		//textures.put("arenaNormal", arena);
		
		mapTest = TextureLoader.getTexture("mapTest.png");
		textures.put("mapTest", mapTest);
	}
}
