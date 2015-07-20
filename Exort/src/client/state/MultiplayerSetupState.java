package client.state;

import static org.lwjgl.opengl.GL11.*;

import java.net.*;

import org.lwjgl.input.*;

import client.*;
import client.util.*;
import client.util.font.*;
import client.util.loaders.*;

import com.doobs.modern.util.matrix.*;

/**
 * Prompts the user for a username and IP address to connect to.
 */
public class MultiplayerSetupState implements GameState {
	private static final int ANIMATION_SPEED = 10;

	private Main main;

	private boolean typingName;
	private StringBuffer username, address;

	private Animation toPlayerSetup, nameAddressSwitch;

	public MultiplayerSetupState(Main main) {
		this.main = main;
		this.typingName = true;
		this.username = new StringBuffer();
		this.address = new StringBuffer();

		// TODO: Remove these.
		this.username.append("Doobs");
		this.address.append("127.0.0.1");

		this.toPlayerSetup = new Animation(ANIMATION_SPEED);
		this.nameAddressSwitch = new Animation(ANIMATION_SPEED);

		Matrices.loadIdentity();
	}

	public void tick(int delta) {
		if (this.main.input.isKeyPressed(Keyboard.KEY_RETURN)) {
			if (this.username.length() != 0 && this.address.length() != 0) {
				try {
					this.main.changeState(new DuelState(this.main, this.username.toString(), InetAddress.getByName(this.address.toString())));
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
			this.main.input.handleTyping(this.username, Fonts.current);
			this.nameAddressSwitch.tickUp(delta);
		} else {
			this.main.input.handleTyping(this.address, Fonts.current);
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

			Font font = Fonts.current;
			font.setColor(1f, 1f, 1f, percent);
			font.setSize(20 + (this.nameAddressSwitch.getPercentage() * 15));
			font.renderCentered("Username", 0, 110 + yo + (int) (this.nameAddressSwitch.getPercentage() * 15));
			font.setSize(20 + ((1 - this.nameAddressSwitch.getPercentage()) * 15));
			font.renderCentered("Server IP", 0, -5 + yo + (int) (1 - (this.nameAddressSwitch.getPercentage() * 15)));

			font.setColor(0.2f, 0.2f, 0.2f, percent);
			font.setSize(10 + (this.nameAddressSwitch.getPercentage() * 15));
			font.renderCentered(this.username.toString(), 0, (75 + yo) - (int) (this.nameAddressSwitch.getPercentage() * 15));
			font.setSize(10 + ((1 - this.nameAddressSwitch.getPercentage()) * 15));
			font.renderCentered(this.address.toString(), 0, (-75 + yo) - (int) (1 - (this.nameAddressSwitch.getPercentage() * 15)));
		}

		Shaders.useDefault();
		Matrices.switchToPerspective();
		glDisable(GL_BLEND);
	}
}
