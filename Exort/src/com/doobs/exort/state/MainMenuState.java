package com.doobs.exort.state;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.*;

import res.shaders.*;
import res.textures.fonts.*;

import com.doobs.exort.*;
import com.doobs.exort.util.gl.*;

public class MainMenuState implements GameState {
	private Main main;

	public MainMenuState(Main main) {
		this.main = main;
	}

	public void tick(int delta) {
		if(Main.input.isKeyPressed(Keyboard.KEY_ESCAPE))
			main.exit();
		else if (Main.input.isKeyPressed(Keyboard.KEY_RETURN))
			main.changeState(new MultiplayerSetupState(main));
	}

	public void render() {
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		GLTools.switchToOrtho();
		Shaders.font.use();
		String phrase = "Welcome to Exort";
		Fonts.finalFrontier.setColor(1f, (float) Math.sin(System.currentTimeMillis() / 1000.0) / 2f + 0.5f, 0f, 1f);
		Fonts.finalFrontier.drawCentered(phrase, 0, 0);
		Shaders.useDefault();
		GLTools.switchToPerspective();
		glDisable(GL_BLEND);
		glDisable(GL_TEXTURE_2D);
	}
}
