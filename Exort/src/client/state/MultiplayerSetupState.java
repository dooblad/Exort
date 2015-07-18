package client.state;

import static org.lwjgl.opengl.GL11.*;

import java.net.*;

import org.lwjgl.input.*;

import client.*;
import client.util.*;
import client.util.loaders.*;

import com.doobs.modern.util.matrix.*;

public class MultiplayerSetupState implements GameState {
	private Main main;

	private boolean typingName;
	private StringBuffer username, address;

	private Animation toPlayerSetup, nameAddressSwitch;

	public MultiplayerSetupState(Main main) {
		this.main = main;
		this.typingName = true;
		this.username = new StringBuffer();
		this.address = new StringBuffer();
		this.toPlayerSetup = new Animation(10);
		this.nameAddressSwitch = new Animation(10);

		Matrices.loadIdentity();
	}

	public void tick(int delta) {
		if (this.main.input.isKeyPressed(Keyboard.KEY_RETURN)) {
			if (this.username.length() != 0) {
				try {
					if (this.address.length() != 0) {
						this.main.changeState(new DuelState(this.main, this.username.toString(), InetAddress.getByName(this.address.toString())));
					}
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			}
		} else if (this.main.input.isKeyPressed(Keyboard.KEY_UP) || this.main.input.isKeyPressed(Keyboard.KEY_DOWN)
				|| this.main.input.isKeyPressed(Keyboard.KEY_TAB)) {
			this.typingName = !this.typingName;
		} else if (this.main.input.isKeyPressed(Keyboard.KEY_ESCAPE)) {
			this.main.changeState(new MainMenuState(this.main));
		}

		this.toPlayerSetup.tickUp(delta);

		if (this.typingName) {
			this.main.input.handleTyping(this.username, Fonts.centuryGothic);
			this.nameAddressSwitch.tickUp(delta);
		} else {
			this.main.input.handleTyping(this.address, Fonts.centuryGothic);
			this.nameAddressSwitch.tickDown(delta);
		}
	}

	public void render() {
		glEnable(GL_BLEND);
		Matrices.switchToOrtho();
		Shaders.use("font");

		float percent = this.toPlayerSetup.getPercentage();

		if (!this.toPlayerSetup.isEmpty()) {
			int yo = -(int) (Math.cos((percent * Math.PI) / 2) * 200);

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

		Shaders.useDefault();
		Matrices.switchToPerspective();
		glDisable(GL_BLEND);
	}

}
