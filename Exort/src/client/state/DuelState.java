package client.state;

import static org.lwjgl.opengl.GL11.*;

import java.net.*;

import org.lwjgl.input.*;

import shared.*;
import shared.level.*;
import shared.net.packets.*;
import client.*;
import client.entity.*;
import client.gui.*;
import client.level.*;
import client.net.*;
import client.util.*;
import client.util.gl.*;
import client.util.loaders.*;

import com.doobs.modern.util.*;
import com.doobs.modern.util.matrix.*;

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
	private RenderableLevel level;
	private Client client;
	private EntityCamera camera;

	private ClientPlayer player;

	private boolean paused;

	/**
	 * Creates a DuelState for connecting a Player with "username" to "address".
	 */
	public DuelState(Main main, String username, InetAddress address) {
		this.main = main;
		this.input = main.input;
		this.gui = new GUI(main, this);
		this.level = new RenderableLevel();
		this.client = new Client(this, address);
		// TODO: Implement a sendUntilACK method to make sure certain messages get sent.
		// Login.
		new Packet00Login(username).sendData(this.client);
		this.camera = new EntityCamera(CAMERA_DISTANCE, this.input);
		this.camera.rotX = CAMERA_ANGLE;

		Mouse.setGrabbed(false);
	}

	public void tick(int delta) {
		if (this.gui.exitFlag()) {
			if (this.player != null) {
				this.client.sendData(new Packet01Disconnect(this.player.getID()).getData());
			}
			this.client.exit();
			this.main.changeState(new MainMenuState(this.main));
		}

		this.gui.tick(this.paused, delta);

		if (this.input.isKeyPressed(Keyboard.KEY_LMENU)) {
			Mouse.setGrabbed(!Mouse.isGrabbed());
		} else if (this.input.isKeyPressed(Keyboard.KEY_R) && !this.gui.isTyping()) {
			this.camera.reset();
		} else if (GlobalVariables.debug && this.input.isKeyPressed(Keyboard.KEY_F5)) {
			// For dynamically loading in models.
			Models.init();
		}

		this.level.tick(delta);

		if (!this.gui.isTyping()) {
			this.camera.tick();
		}
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

		this.level.renderMap();

		Lighting.setTextured(false);

		this.level.renderEntities();

		if (!this.paused) {
			RayCast.tick(this.camera);
		}

		// GUI rendering.
		glEnable(GL_BLEND);
		this.gui.render();
		Shaders.useDefault();
		glDisable(GL_BLEND);
	}

	/**
	 * Sends "message" as a Packet03Chat.
	 */
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

	public ClientPlayer getPlayer() {
		return this.player;
	}

	/**
	 * Sets this DuelState's ClientPlayer to "player" and attaches the EntityCamera to it.
	 */
	public void setPlayer(ClientPlayer player) {
		this.player = player;
		this.camera.setEntity(player);
	}

	public boolean isPaused() {
		return this.paused;
	}

	public void togglePause() {
		this.paused = !this.paused;
	}
}