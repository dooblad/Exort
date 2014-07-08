package com.doobs.exort.state;

import static org.lwjgl.opengl.GL11.*;

import java.awt.*;

import org.lwjgl.input.*;

import res.shaders.*;
import res.textures.fonts.*;

import com.doobs.exort.*;
import com.doobs.exort.util.gl.*;

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
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		GLTools.switchToOrtho();
		Shaders.font.use();
		Dimension d = Fonts.finalFrontier.getPhraseDimensions("Welcome to Exort");
		Fonts.finalFrontier.draw("Welcome to Exort", (Main.width - d.width) / 2, (Main.height - d.height) / 2, 1f, 0f, 0f);
		Shaders.useDefault();
		GLTools.switchToPerspective();
		glDisable(GL_BLEND);
		glDisable(GL_TEXTURE_2D);
	}
}
