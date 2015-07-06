package exort;

import java.awt.*;

import org.lwjgl.input.*;

import com.doobs.modern.*;
import com.doobs.modern.util.*;

import exort.state.*;
import exort.util.*;
import exort.util.gl.*;
import exort.util.gl.Cursor;
import exort.util.loaders.*;

/**
 * Initializes and manages the game's loop.
 */
public class Main implements GameLoop {
	public static final String TITLE = "Exort Test";

	public static boolean debug = true;

	public GraphicsContext context;
	public InputHandler input;

	private GameState state;

	/**
	 * Initializes a GraphicsContext, loads all game assets, then runs the game.
	 */
	public Main() {
		this.context = new GraphicsContext(this);

		// Initialization in specific order.
		Shaders.init();
		Lighting.init();
		Cursor.init();
		Textures.init();
		Fonts.init(this);
		Models.init();

		this.input = new InputHandler();
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
			debug = !debug;
		}
		if (this.input.isKeyPressed(Keyboard.KEY_F11)) {
			GLTools.toggleFullscreen();
		}

		this.state.tick(delta);
	}

	/**
	 * Renders the contents of the current game "state".
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
