package client.state;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.*;

import client.*;
import client.util.loaders.*;

import com.doobs.modern.util.matrix.*;

/**
 * The starting point for Exort.
 */
public class MainMenuState implements GameState {
	private Main main;

	public MainMenuState(Main main) {
		this.main = main;
		// No transformations in this GameState, so only one identity load should be
		// necessary.
		Matrices.loadIdentity();
	}

	public void tick(int delta) {
		if (this.main.input.isKeyPressed(Keyboard.KEY_ESCAPE)) {
			this.main.exit();
		} else if (this.main.input.isKeyPressed(Keyboard.KEY_RETURN)) {
			this.main.changeState(new MultiplayerSetupState(this.main));
		}
	}

	public void render() {
		glEnable(GL_BLEND);
		Matrices.switchToOrtho();
		Shaders.use("font");
		Matrices.sendMVPMatrix(Shaders.current);
		String phrase = "Welcome to Exort";
		Fonts.centuryGothic.setSize(35);
		Fonts.centuryGothic.setColor(1f, ((float) Math.sin(System.currentTimeMillis() / 350.0) / 2f) + 0.5f, 0f, 1f);
		Fonts.centuryGothic.renderCentered(phrase, 0, 0);
		Shaders.useDefault();
		glDisable(GL_BLEND);
	}
}
