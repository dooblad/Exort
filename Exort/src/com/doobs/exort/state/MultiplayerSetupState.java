package com.doobs.exort.state;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.*;

import res.shaders.*;
import res.textures.*;

import com.doobs.exort.*;
import com.doobs.exort.net.*;
import com.doobs.exort.util.*;
import com.doobs.exort.util.gl.*;

public class MultiplayerSetupState implements GameState {
	private Main main;

	private boolean server, chosen;
	private boolean typingName;
	private String username, address;

	private Animation toPlayerSetup, nameAddressSwitch;

	public MultiplayerSetupState(Main main) {
		this.main = main;
		server = false;
		chosen = false;
		typingName = true;
		username = "";
		address = "";
		toPlayerSetup = new Animation(10);
		nameAddressSwitch = new Animation(10);
	}

	@Override
	public void tick(int delta) {
		if (Main.input.isKeyPressed(Keyboard.KEY_RETURN)) {
			if(chosen) {
				if((server && username != ""))
					main.changeState(new DuelState(main, server, username, NetVariables.LOCALHOST));
				else if(!server && username != "" && address != "")
					main.changeState(new DuelState(main, server, username, address));
			} else
				chosen = true;
		} else if (!chosen && (Main.input.isKeyPressed(Keyboard.KEY_LEFT) || Main.input.isKeyPressed(Keyboard.KEY_RIGHT)))
			server = !server;
		else if (chosen && (Main.input.isKeyPressed(Keyboard.KEY_UP) || Main.input.isKeyPressed(Keyboard.KEY_DOWN) || Main.input.isKeyPressed(Keyboard.KEY_TAB)))
			typingName = !typingName;
		else if (Main.input.isKeyPressed(Keyboard.KEY_ESCAPE)) {
			if (chosen)
				chosen = false;
			else
				main.changeState(new MainMenuState(main));
		}

		if (chosen) {
			toPlayerSetup.tickUp(delta);

			if (typingName) {
				username = Main.input.handleTyping(username, Fonts.centuryGothic);
				nameAddressSwitch.tickUp(delta);
			} else {
				address = Main.input.handleTyping(address, Fonts.centuryGothic);
				nameAddressSwitch.tickDown(delta);
			}
		} else
			toPlayerSetup.tickDown(delta);

	}

	@Override
	public void render() {
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		GLTools.switchToOrtho();
		Shaders.font.use();

		float percent = toPlayerSetup.getPercentage();
		int yo;
		String phrase;

		if (!toPlayerSetup.isFull()) {
			yo = (int) (Math.sin(percent * Math.PI / 2) * 200);
			phrase = "Server or Client";
			Fonts.centuryGothic.setSize(30);
			Fonts.centuryGothic.setColor(1f, 1f, 0f, 1f - percent);
			Fonts.centuryGothic.drawCentered(phrase, 0, yo);

			// Selection Arrow
			phrase = "v";
			Fonts.centuryGothic.setColor(1f, 1f, 1f, 1f - percent);
			int x;
			if (!server)
				x = (Fonts.centuryGothic.getPhraseWidth("or Clientv")) / 2;
			else
				x = (-Fonts.centuryGothic.getPhraseWidth("Server v")) / 2;
			Fonts.centuryGothic.drawCentered(phrase, x, (int) (Math.sin(System.currentTimeMillis() / 200.0) * 15) + 55 + yo);
		}

		if (!toPlayerSetup.isEmpty()) {
			yo = -(int) (Math.cos(percent * Math.PI / 2) * 200);
			
			if (server) {
				Fonts.centuryGothic.setColor(1f, 1f, 1f, percent);
				Fonts.centuryGothic.setSize(35);
				Fonts.centuryGothic.drawCentered("Username", 0, yo + 35);
				
				Fonts.centuryGothic.setColor(0.2f, 0.2f, 0.2f, percent);
				Fonts.centuryGothic.setSize(25);
				Fonts.centuryGothic.drawCentered(username, 0, yo - 35);
			} else {
				Fonts.centuryGothic.setColor(1f, 1f, 1f, percent);
				Fonts.centuryGothic.setSize(20 + nameAddressSwitch.getPercentage() * 15);
				Fonts.centuryGothic.drawCentered("Username", 0, 110 + yo + (int) (nameAddressSwitch.getPercentage() * 15));
				Fonts.centuryGothic.setSize(20 + (1 - nameAddressSwitch.getPercentage()) * 15);
				Fonts.centuryGothic.drawCentered("Server IP", 0, -5 + yo + (int) (1 - nameAddressSwitch.getPercentage() * 15));

				Fonts.centuryGothic.setColor(0.2f, 0.2f, 0.2f, percent);
				Fonts.centuryGothic.setSize(10 + nameAddressSwitch.getPercentage() * 15);
				Fonts.centuryGothic.drawCentered(username, 0, 75 + yo - (int) (nameAddressSwitch.getPercentage() * 15));
				Fonts.centuryGothic.setSize(10 + (1 - nameAddressSwitch.getPercentage()) * 15);
				Fonts.centuryGothic.drawCentered(address, 0, -75 + yo - (int) (1 - nameAddressSwitch.getPercentage() * 15));
			}
		}

		Shaders.useDefault();
		GLTools.switchToPerspective();
		glDisable(GL_BLEND);
		glDisable(GL_TEXTURE_2D);
	}

}
