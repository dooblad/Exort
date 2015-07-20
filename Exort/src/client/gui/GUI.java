package client.gui;

import static org.lwjgl.opengl.GL11.*;
import shared.*;
import client.*;
import client.state.*;
import client.util.*;
import client.util.loaders.*;

import com.doobs.modern.util.matrix.*;

/**
 * Handles the GUI for the DuelState.
 */
public class GUI implements UI {
	// Standard color for GUI components.
	public static final float[] GUI_COL = { 0f, 0f, 0f, 0.5f };

	private Chat chat;
	private PauseMenu menu;

	/**
	 * Creates a GUI using the InputHandler and Dimension from "main".
	 */
	public GUI(Main main, DuelState state) {
		this.chat = new Chat(state, main.input);
		this.menu = new PauseMenu(state, main.getSize(), main.input);
	}

	public void tick(boolean paused, int delta) {
		this.chat.tick(delta);
		this.menu.tick(delta);

		if (this.menu.isExiting()) {
			this.chat.vanish();
		}
	}

	public void render() {
		// Orthographical projection for 2D rendering.
		Matrices.switchToOrtho();
		Matrices.loadIdentity();

		// Setup font rendering.
		Shaders.use("font");
		Matrices.sendMVPMatrix(Shaders.current);
		Fonts.centuryGothic.setSize(15);
		Fonts.centuryGothic.setColor(1f, 1f, 1f, 1f);

		// Setup gui rendering.
		Shaders.use("gui");
		Matrices.sendMVPMatrix(Shaders.current);
		glEnable(GL_BLEND);
		glDisable(GL_DEPTH_TEST);

		this.chat.render();
		this.menu.render();

		glEnable(GL_DEPTH_TEST);
		glDisable(GL_BLEND);
	}

	/**
	 * Recalculates positions for GUI components.
	 */
	public void recalculatePositions() {
		this.menu.calculatePositions();
	}

	/**
	 * Adds "message" to the chat.
	 */
	public void addMessage(Message message) {
		this.chat.addMessage(message);
	}

	/**
	 * Adds "message" to the chat.
	 */
	public void addMessage(String message) {
		this.chat.addMessage(message);
	}

	/**
	 * Returns true if ready to exit this state.
	 */
	public boolean exitFlag() {
		return this.menu.exitFlag();
	}

	/**
	 * Returns true if the Player is typing.
	 */
	public boolean isTyping() {
		return this.chat.isTyping();
	}
}