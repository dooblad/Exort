package client;

import java.awt.*;

import org.lwjgl.input.*;

import shared.*;
import client.state.*;
import client.util.*;
import client.util.gl.*;
import client.util.loaders.*;

import com.doobs.modern.*;
import com.doobs.modern.util.*;

/**
 * Initializes and manages the game's loop.
 */
public class Main implements GameLoop {
	public static final String TITLE = "Exort Test";

	public GraphicsContext context;
	public InputHandler input;

	private GameState state;

	/**
	 * Initializes a GraphicsContext, loads all game assets, then runs the game.
	 */
	public Main() {
		this.context = new GraphicsContext(this);
		this.input = new InputHandler();

		Resources.init(this);
		Lighting.init();

		this.state = new MainMenuState(this);

		this.context.run();
	}

	/**
	 * Handles the majority of the per-frame game logic, taking into account the time
	 * elapsed ("delta").
	 */
	public void tick(int delta) {
		if (GLTools.wasResized()) {
			this.resize();
		}

		this.input.tick();

		if (this.input.isKeyPressed(Keyboard.KEY_F4)) {
			GlobalVariables.debug = !GlobalVariables.debug;
		}
		if (this.input.isKeyPressed(Keyboard.KEY_F11)) {
			GLTools.toggleFullscreen();
		}

		this.state.tick(delta);
	}

	/**
	 * Renders the contents of the game's "state".
	 */
	public void render() {
		this.state.render();
	}

	/**
	 * Performs any necessary recalculations if the window is resized.
	 */
	private void resize() {
		if (this.state instanceof DuelState) {
			((DuelState) this.state).getGUI().recalculatePositions();
		}
	}

	/**
	 * Changes the current GameState to "state".
	 */
	public void changeState(GameState state) {
		this.state = state;
	}

	/**
	 * Attempts to exit the game.
	 */
	public void exit() {
		this.context.requestExit();
	}

	/**
	 * Returns the width of this game's GraphicsContext.
	 */
	public int getWidth() {
		return this.context.getWidth();
	}

	/**
	 * Returns the height of this game's GraphicsContext.
	 */
	public int getHeight() {
		return this.context.getHeight();
	}

	/**
	 * Returns the size of the GraphicsContext as a Dimension.
	 */
	public Dimension getSize() {
		return this.context.getSize();
	}

	/**
	 * Returns the current GameState.
	 */
	public GameState getCurrentState() {
		return this.state;
	}

	public static void main(String[] args) {
		new Main();
	}
}
