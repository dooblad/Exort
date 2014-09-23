package com.doobs.exort.state;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.*;

import com.doobs.exort.*;
import com.doobs.exort.util.loaders.*;
import com.doobs.modern.util.matrix.*;

public class MainMenuState implements GameState {
	private Main main;

	public MainMenuState(Main main) {
		this.main = main;
		Matrices.loadIdentity();
	}

	@Override
	public void tick(int delta) {
		if (Main.input.isKeyPressed(Keyboard.KEY_ESCAPE))
			main.exit();
		else if (Main.input.isKeyPressed(Keyboard.KEY_RETURN))
			main.changeState(new MultiplayerSetupState(main));
	}

	@Override
	public void render() {
		glEnable(GL_BLEND);
		Matrices.switchToOrtho();
		Shaders.use("font");
		Matrices.sendMVPMatrix(Shaders.current);
		String phrase = "Welcome to Exort";
		Fonts.centuryGothic.setSize(50);
		Fonts.centuryGothic.setColor(1f, (float) Math.sin(System.currentTimeMillis() / 350.0) / 2f + 0.5f, 0f, 1f);
		Fonts.centuryGothic.drawCentered(phrase, 0, 0);
		Shaders.useDefault();
		glDisable(GL_BLEND);
	}
}
