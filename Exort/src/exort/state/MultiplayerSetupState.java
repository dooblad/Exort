package exort.state;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.*;

import com.doobs.modern.util.matrix.*;

import exort.*;
import exort.net.*;
import exort.util.*;
import exort.util.loaders.*;

public class MultiplayerSetupState implements GameState {
	private Main main;

	private boolean server, chosen;
	private boolean typingName;
	private StringBuilder username, address;

	private Animation toPlayerSetup, nameAddressSwitch;

	public MultiplayerSetupState(Main main) {
		this.main = main;
		this.server = false;
		this.chosen = false;
		this.typingName = true;
		this.username = new StringBuilder();
		this.address = new StringBuilder();
		this.toPlayerSetup = new Animation(10);
		this.nameAddressSwitch = new Animation(10);

		Matrices.loadIdentity();
	}

	public void tick(int delta) {
		if (this.main.input.isKeyPressed(Keyboard.KEY_RETURN)) {
			if (this.chosen) {
				if (this.username.length() != 0) {
					if (this.server) {
						this.main.changeState(new DuelState(this.main, this.server, this.username.toString(), NetVariables.LOCALHOST));
					} else if (this.address.length() != 0) {
						this.main.changeState(new DuelState(this.main, this.server, this.username.toString(), this.address.toString()));
					}
				}
			} else {
				this.chosen = true;
			}
		} else if (!this.chosen && (this.main.input.isKeyPressed(Keyboard.KEY_LEFT) || this.main.input.isKeyPressed(Keyboard.KEY_RIGHT))) {
			this.server = !this.server;
		} else if (this.chosen
				&& (this.main.input.isKeyPressed(Keyboard.KEY_UP) || this.main.input.isKeyPressed(Keyboard.KEY_DOWN) || this.main.input
						.isKeyPressed(Keyboard.KEY_TAB))) {
			this.typingName = !this.typingName;
		} else if (this.main.input.isKeyPressed(Keyboard.KEY_ESCAPE)) {
			if (this.chosen) {
				this.chosen = false;
			} else {
				this.main.changeState(new MainMenuState(this.main));
			}
		}

		if (this.chosen) {
			this.toPlayerSetup.tickUp(delta);

			if (this.typingName) {
				this.main.input.handleTyping(this.username, Fonts.centuryGothic);
				this.nameAddressSwitch.tickUp(delta);
			} else {
				this.main.input.handleTyping(this.address, Fonts.centuryGothic);
				this.nameAddressSwitch.tickDown(delta);
			}
		} else {
			this.toPlayerSetup.tickDown(delta);
		}

	}

	public void render() {
		glEnable(GL_BLEND);
		Matrices.switchToOrtho();
		Shaders.use("font");

		float percent = this.toPlayerSetup.getPercentage();
		int yo;
		String phrase;

		if (!this.toPlayerSetup.isFull()) {
			yo = (int) (Math.sin((percent * Math.PI) / 2) * 200);
			phrase = "Server or Client";
			Fonts.centuryGothic.setSize(30);
			Fonts.centuryGothic.setColor(1f, 1f, 0f, 1f - percent);
			Fonts.centuryGothic.drawCentered(phrase, 0, yo);

			// Selection Arrow
			phrase = "v";
			Fonts.centuryGothic.setColor(1f, 1f, 1f, 1f - percent);
			int x;
			if (!this.server) {
				x = (Fonts.centuryGothic.getPhraseWidth("or Clientv")) / 2;
			} else {
				x = (-Fonts.centuryGothic.getPhraseWidth("Server v")) / 2;
			}
			Fonts.centuryGothic.drawCentered(phrase, x, (int) (Math.sin(System.currentTimeMillis() / 200.0) * 15) + 55 + yo);
		}

		if (!this.toPlayerSetup.isEmpty()) {
			yo = -(int) (Math.cos((percent * Math.PI) / 2) * 200);

			if (this.server) {
				Fonts.centuryGothic.setColor(1f, 1f, 1f, percent);
				Fonts.centuryGothic.setSize(35);
				Fonts.centuryGothic.drawCentered("Username", 0, yo + 35);

				Fonts.centuryGothic.setColor(0.2f, 0.2f, 0.2f, percent);
				Fonts.centuryGothic.setSize(25);
				Fonts.centuryGothic.drawCentered(this.username.toString(), 0, yo - 35);
			} else {
				Fonts.centuryGothic.setColor(1f, 1f, 1f, percent);
				Fonts.centuryGothic.setSize(20 + (this.nameAddressSwitch.getPercentage() * 15));
				Fonts.centuryGothic.drawCentered("Username", 0, 110 + yo + (int) (this.nameAddressSwitch.getPercentage() * 15));
				Fonts.centuryGothic.setSize(20 + ((1 - this.nameAddressSwitch.getPercentage()) * 15));
				Fonts.centuryGothic.drawCentered("Server IP", 0, -5 + yo + (int) (1 - (this.nameAddressSwitch.getPercentage() * 15)));

				Fonts.centuryGothic.setColor(0.2f, 0.2f, 0.2f, percent);
				Fonts.centuryGothic.setSize(10 + (this.nameAddressSwitch.getPercentage() * 15));
				Fonts.centuryGothic.drawCentered(this.username.toString(), 0, (75 + yo) - (int) (this.nameAddressSwitch.getPercentage() * 15));
				Fonts.centuryGothic.setSize(10 + ((1 - this.nameAddressSwitch.getPercentage()) * 15));
				Fonts.centuryGothic.drawCentered(this.address.toString(), 0, (-75 + yo) - (int) (1 - (this.nameAddressSwitch.getPercentage() * 15)));
			}
		}

		Shaders.useDefault();
		Matrices.switchToPerspective();
		glDisable(GL_BLEND);
	}

}
