package com.doobs.exort.state;

import org.lwjgl.input.*;

import res.shaders.*;
import res.textures.fonts.*;

import com.doobs.exort.*;

public class MainMenuState implements GameState{
	private Main main;
	
	public MainMenuState(Main main) {
		this.main = main;
	}
	
	public void tick(int delta) {
		if(Main.input.isKeyDown(Keyboard.KEY_RETURN))
			main.changeState(new DuelState());
	}

	public void render() {
		Shaders.font.use();
		Fonts.automati.draw("7777", 300, 300, 1f, 1f, 1f);
		Shaders.useDefault();
	}
}
