package exort.gui;

import static exort.gui.GUI.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import java.awt.*;

import org.lwjgl.input.*;

import com.doobs.modern.util.Color;
import com.doobs.modern.util.batch.*;

import exort.state.*;
import exort.util.*;
import exort.util.loaders.*;

/**
 * GUI component for handling the menu presented when the current DuelState is paused.
 */
public class PauseMenu {
	private static final int PAUSE_HOVER_SPEED = 8;
	private static final int EXIT_DUEL_SPEED = 15;
	private static final Dimension EXIT_BUTTON_SIZE = new Dimension(250, 150);

	// Uses for determining if the DuelState is paused and for toggling whether it's
	// paused.
	private DuelState state;

	// The size of the game window.
	private Dimension size;

	private InputHandler input;

	// Animations.
	public Animation exitDuel;
	public Animation pauseHover;
	private boolean exiting;
	private Rectangle exitBounds;

	/**
	 * Creates a PauseMenu using "state" for information about whether it's paused, "size"
	 * to determine the size and position of components, and "input".
	 */
	public PauseMenu(DuelState state, Dimension size, InputHandler input) {
		this.state = state;
		this.size = size;
		this.input = input;
		this.pauseHover = new Animation(PAUSE_HOVER_SPEED);
		this.exitDuel = new Animation(EXIT_DUEL_SPEED);
		this.exiting = false;
		this.calculatePositions();
	}

	/**
	 * Handles the behavior of this PauseMenu.
	 */
	public void tick(int delta) {
		if (this.input.isKeyPressed(Keyboard.KEY_ESCAPE)) {
			this.state.togglePause();
			Mouse.setGrabbed(false);
		}

		if (this.state.isPaused() && this.exitBounds.intersects(Mouse.getX(), Mouse.getY(), 1, 1)) {
			this.pauseHover.tickUp(delta);

			if (this.input.isMouseButtonPressed(0)) {
				this.exiting = true;
			}
		} else {
			this.pauseHover.tickDown(delta);
		}

		if (this.exiting) {
			this.pauseHover.fill();
			this.exitDuel.tickUp(delta);
		}
	}

	/**
	 * Renders this PauseMenu.
	 */
	public void render() {
		if (this.state.isPaused()) {
			Shaders.use("gui");
			glActiveTexture(GL_TEXTURE0);
			Textures.get("white").bind();
			Color.set(Shaders.current, GUI_COL[0], GUI_COL[1], GUI_COL[2], GUI_COL[3] + (this.exitDuel.getSmoothedPercentage() * (1 - GUI_COL[3])));

			// Animation percentages.
			float exit = this.exitDuel.getSmoothedPercentage();
			float inverseExit = 1 - this.exitDuel.getSmoothedPercentage();
			float hover = this.pauseHover.getSmoothedPercentage();

			new SimpleBatch(GL_TRIANGLES, 3, new float[] { (this.exitBounds.x - (hover * 100f)) * inverseExit, // 0
					this.exitBounds.y * inverseExit, 0f,

					(this.size.width * exit) + ((this.exitBounds.x + this.exitBounds.width) * inverseExit), // 1
					this.exitBounds.y * inverseExit, 0f,

					(this.size.width * exit) + ((this.exitBounds.x + this.exitBounds.width + (hover * 100f)) // 2
							* inverseExit), (this.size.height * exit) + ((this.exitBounds.y + this.exitBounds.height) * inverseExit), 0f,

					(this.exitBounds.x - (hover * 100f)) * inverseExit, // 0
					this.exitBounds.y * inverseExit, 0f,

					(this.size.width * exit) + ((this.exitBounds.x + this.exitBounds.width + (hover * 100f)) // 2
							* inverseExit), (this.size.height * exit) + ((this.exitBounds.y + this.exitBounds.height) * inverseExit), 0f,

					this.exitBounds.x * inverseExit, // 3
					(this.size.height * exit) + ((this.exitBounds.y + this.exitBounds.height) * inverseExit), 0f }, null, null, null, null)
					.draw(Shaders.current.getAttributeLocations());

			Shaders.use("font");
			Fonts.centuryGothic.setColor(1f, 1f, 1f, 1f);
			Fonts.centuryGothic.setSize(40 + (this.pauseHover.getSmoothedPercentage() * 10));
			Fonts.centuryGothic.drawCentered("EXIT", 0, 0);
		}
	}

	/**
	 * Calculates the positions of components according to the size of the game window.
	 */
	public void calculatePositions() {
		this.exitBounds = new Rectangle((this.size.width - EXIT_BUTTON_SIZE.width) / 2, (this.size.height - EXIT_BUTTON_SIZE.height) / 2,
				EXIT_BUTTON_SIZE.width, EXIT_BUTTON_SIZE.height);
	}

	/**
	 * Returns true if in the process of exiting (for animations).
	 */
	public boolean isExiting() {
		return this.exiting;
	}

	/**
	 * Returns true if ready to exit this state.
	 */
	public boolean exitFlag() {
		return this.exitDuel.isFull();
	}
}