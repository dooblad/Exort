package com.doobs.exort.state;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.*;

import res.shaders.*;
import res.textures.fonts.*;

import com.doobs.exort.*;
import com.doobs.exort.util.*;
import com.doobs.exort.util.gl.*;

public class MultiplayerSetupState implements GameState {
	private Main main;

	private boolean server, chosen;
	private boolean typingName;
	private String username, address;

	private Animation toClientSetup, nameAddressSwitch;

	public MultiplayerSetupState(Main main) {
		this.main = main;
		server = false;
		chosen = false;
		typingName = true;
		username = "";
		address = "";
		toClientSetup = new Animation(10);
		nameAddressSwitch = new Animation(10);
	}

	@Override
	public void tick(int delta) {
		if (Main.input.isKeyPressed(Keyboard.KEY_RETURN)) {
			if (server || (chosen && username != "" && address != ""))
				main.changeState(new DuelState(main, server, username, address));
			else
				chosen = true;

		} else if (Main.input.isKeyPressed(Keyboard.KEY_LEFT) || Main.input.isKeyPressed(Keyboard.KEY_RIGHT))
			server = !server;
		else if (chosen && (Main.input.isKeyPressed(Keyboard.KEY_UP) || Main.input.isKeyPressed(Keyboard.KEY_DOWN)))
			typingName = !typingName;
		else if (Main.input.isKeyPressed(Keyboard.KEY_ESCAPE)) {
			if (chosen)
				chosen = false;
			else
				main.changeState(new MainMenuState(main));
		}

		if (chosen) {
			toClientSetup.tickUp(delta);

			if (typingName) {
				username = Main.input.handleTyping(username, Fonts.finalFrontier);
				nameAddressSwitch.tickUp(delta);
			} else {
				address = Main.input.handleTyping(address, Fonts.finalFrontier);
				nameAddressSwitch.tickDown(delta);
			}
		} else
			toClientSetup.tickDown(delta);

	}

	@Override
	public void render() {
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		GLTools.switchToOrtho();
		Shaders.font.use();

		float percent = toClientSetup.getPercentage();
		int yo;
		String phrase;

		if (!toClientSetup.isFull()) {
			yo = (int) (Math.sin(percent * Math.PI / 2) * 200);
			phrase = "Server or Client";
			Fonts.finalFrontier.setSize(18);
			Fonts.finalFrontier.setColor(1f, 1f, 0f, 1f - percent);
			Fonts.finalFrontier.drawCentered(phrase, 0, yo);

			// Selection Arrow
			phrase = "v";
			Fonts.finalFrontier.setColor(1f, 1f, 1f, 1f - percent);
			int x;
			if (!server)
				x = (Fonts.finalFrontier.getPhraseWidth("or Clientv")) / 2 + 24;
			else
				x = (-Fonts.finalFrontier.getPhraseWidth("Server v")) / 2 + 13;
			Fonts.finalFrontier.drawCentered(phrase, x, (int) (Math.sin(System.currentTimeMillis() / 200.0) * 15) + 55 + yo);
		}

		if (!toClientSetup.isEmpty()) {
			yo = -(int) (Math.cos(percent * Math.PI / 2) * 200);

			Fonts.finalFrontier.setColor(1f, 1f, 1f, percent);
			Fonts.finalFrontier.setSize(9 + nameAddressSwitch.getPercentage() * 5);
			Fonts.finalFrontier.drawCentered("Username", 0, 110 + yo + (int) (nameAddressSwitch.getPercentage() * 15));
			Fonts.finalFrontier.setSize(9 + (1 - nameAddressSwitch.getPercentage()) * 5);
			Fonts.finalFrontier.drawCentered("Server IP", 0, -5 + yo + (int) (1 - nameAddressSwitch.getPercentage() * 15));

			Fonts.finalFrontier.setColor(0.2f, 0.2f, 0.2f, percent);
			Fonts.finalFrontier.setSize(6 + nameAddressSwitch.getPercentage() * 5);
			Fonts.finalFrontier.drawCentered(username, 0, 55 + yo - (int) (nameAddressSwitch.getPercentage() * 5));
			Fonts.finalFrontier.setSize(6 + (1 - nameAddressSwitch.getPercentage()) * 5);
			Fonts.finalFrontier.drawCentered(address, 0, -75 + yo - (int) (1 - nameAddressSwitch.getPercentage() * 5));
		}

		Shaders.useDefault();
		GLTools.switchToPerspective();
		glDisable(GL_BLEND);
		glDisable(GL_TEXTURE_2D);
	}

}
