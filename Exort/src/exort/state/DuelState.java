package exort.state;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.*;

import com.doobs.modern.util.*;
import com.doobs.modern.util.matrix.*;

import exort.*;
import exort.entity.creature.*;
import exort.gui.*;
import exort.level.*;
import exort.net.client.*;
import exort.net.packets.*;
import exort.net.server.*;
import exort.util.*;
import exort.util.gl.*;
import exort.util.loaders.*;

/**
 * GameState where two Players fight to the death.
 */
public class DuelState implements GameState {
	// For rotation about the x-axis.
	public static final float CAMERA_ANGLE = 60f;
	public static final float CAMERA_DISTANCE = 25f;

	private Main main;
	private InputHandler input;
	private GUI gui;
	private Level level;
	private Player player;
	private EntityCamera camera;

	private Client client;
	private Server server;

	private boolean paused;

	public DuelState(Main main, boolean isServer, String username, String address) {
		this.main = main;
		this.input = main.input;

		this.gui = new GUI(main, this);

		this.level = new Level();

		this.client = new Client(this, address);

		if (isServer) {
			this.server = new Server(this.gui, new Level());
		}

		new Packet00Login(username).sendData(this.client);

		this.camera = new EntityCamera(CAMERA_DISTANCE, this.input);
		this.camera.rotX = CAMERA_ANGLE;

		Mouse.setGrabbed(false);
	}

	public void tick(int delta) {
		if (this.gui.exitFlag()) {
			if (this.server != null) {
				this.server.exit();
			}
			this.client.sendData(new Packet01Disconnect(this.player.getID()).getData());
			this.client.exit();
			this.main.changeState(new MainMenuState(this.main));
		}

		this.gui.tick(this.paused, delta);

		if (this.input.isKeyPressed(Keyboard.KEY_LMENU)) {
			Mouse.setGrabbed(!Mouse.isGrabbed());
		} else if (this.input.isKeyPressed(Keyboard.KEY_R) && !this.gui.isTyping()) {
			this.camera.reset();
		} else if (Main.debug && this.input.isKeyPressed(Keyboard.KEY_F5)) {
			// For dynamically loading in models.
			Models.init();
		}

		this.level.tick(delta);

		this.camera.tick();
	}

	public void render() {
		// Level rendering.
		Matrices.switchToPerspective();
		Shaders.use("lighting");
		Color.set(Shaders.current, 1f, 1f, 1f, 1f);
		Matrices.loadIdentity();

		this.camera.applyTransformations();

		Lighting.setTextured(true);
		Matrices.sendMVPMatrix(Shaders.current);
		Matrices.sendMVMatrix(Shaders.current);
		this.level.renderLevel();

		Lighting.setTextured(false);

		this.level.renderEntities();

		if (!this.paused) {
			RayCast.tick(this.camera);
		}

		// GUI rendering
		glEnable(GL_BLEND);
		this.gui.render();
		Shaders.useDefault();
		glDisable(GL_BLEND);
	}

	public void sendMessage(String message) {
		new Packet03Chat(this.player.getID(), message).sendData(this.client);
	}

	public InputHandler getInput() {
		return this.input;
	}

	public GUI getGUI() {
		return this.gui;
	}

	public Level getLevel() {
		return this.level;
	}

	public Player getPlayer() {
		return this.player;
	}

	public void setPlayer(Player player) {
		this.player = player;
		this.camera.setEntity(player);
	}

	public Camera getCamera() {
		return this.camera;
	}

	public Client getClient() {
		return this.client;
	}

	public boolean isPaused() {
		return this.paused;
	}

	public void togglePause() {
		this.paused = !this.paused;
	}
}