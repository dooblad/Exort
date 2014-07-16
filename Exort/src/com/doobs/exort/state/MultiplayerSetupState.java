package com.doobs.exort.state;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.*;

import res.shaders.*;
import res.textures.fonts.*;

import com.doobs.exort.*;
import com.doobs.exort.util.gl.*;

public class MultiplayerSetupState implements GameState {
	private Main main;

	private boolean client;
	private String address;

	public MultiplayerSetupState(Main main) {
		this.main = main;
		client = false;
		address = "";
	}

	@Override
	public void tick(int delta) {
		if (Main.input.isKeyPressed(Keyboard.KEY_RETURN))
			main.changeState(new DuelState(main, client, address));
		else if (Main.input.isKeyPressed(Keyboard.KEY_LEFT) || Main.input.isKeyPressed(Keyboard.KEY_RIGHT))
			client = !client;

		address = Main.input.handleTyping(address, Fonts.finalFrontier);
	}

	@Override
	public void render() {
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		GLTools.switchToOrtho();
		Shaders.font.use();

		String phrase = "Server or Client";
		Fonts.finalFrontier.setSize(12);
		Fonts.finalFrontier.setColor(1f, 1f, 0f);
		Fonts.finalFrontier.drawCentered(phrase, 0, 50);

		phrase = "v";
		Fonts.finalFrontier.setColor(1f, 1f, 1f);
		int x;
		if (client)
			x = (Fonts.finalFrontier.getPhraseWidth("or Clientv")) / 2 + 24;
		else
			x = (-Fonts.finalFrontier.getPhraseWidth("Server v")) / 2 + 13;
		Fonts.finalFrontier.drawCentered(phrase, x, (int) (Math.sin(System.currentTimeMillis() / 200.0) * 30) + 130);

		Fonts.finalFrontier.setSize(7);
		Fonts.finalFrontier.setColor(1f, 1f, 1f);
		Fonts.finalFrontier.drawCentered(address, 0, -35);

		Shaders.useDefault();
		GLTools.switchToPerspective();
		glDisable(GL_BLEND);
		glDisable(GL_TEXTURE_2D);
	}

}
