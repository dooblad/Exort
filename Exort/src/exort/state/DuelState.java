package exort.state;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.*;

import com.doobs.modern.util.*;
import com.doobs.modern.util.matrix.*;

import exort.*;
import exort.entity.creature.*;
import exort.gfx.*;
import exort.level.*;
import exort.math.*;
import exort.net.client.*;
import exort.net.packets.*;
import exort.net.server.*;
import exort.util.gl.*;
import exort.util.loaders.*;

public class DuelState implements GameState {
	private static final int CHAT_CHAR_LIMIT = 256;

	private Main main;
	private GUI gui;
	private Level level;
	private Player player;
	private Camera camera;

	private Client client;
	private Server server;

	private boolean paused;

	private boolean typing;
	private StringBuilder message;

	public DuelState(Main main, boolean isServer, String username, String address) {
		this.main = main;

		this.gui = new GUI(main);

		this.level = new Level();

		if (isServer) {
			this.server = new Server(this, this.gui, new Level());
		}

		this.client = new Client(main, this.gui, this.level, address);

		if (!isServer) {
			this.level.addMainPlayer(this.player);
			this.player = new Player(main.input, this.level, this.client, username, address, this.client.getPort());
		}

		new Packet00Login(username).sendData(this.client);

		this.camera = new Camera(0.0f, 6.5f, 0.0f);

		this.typing = false;
		this.message = new StringBuilder(CHAT_CHAR_LIMIT);

		Mouse.setGrabbed(true);
	}

	public void tick(int delta) {
		if ((this.player == null) && (this.level.getMainPlayer() != null)) {
			this.player = this.level.getMainPlayer();
			this.player.setClient(this.client);
		}

		if (this.gui.exitDuel.isFull()) {
			if (this.server != null) {
				;
			}
			this.server.exit();
			this.client.sendData(new Packet01Disconnect(this.player.getUsername()).getData());
			this.client.exit();
			this.main.changeState(new MainMenuState(this.main));
		}

		this.gui.tick(this.paused, delta);

		if (this.main.input.isKeyPressed(Keyboard.KEY_ESCAPE)) {
			this.paused = !this.paused;
			this.gui.chatFade.fill();
			Mouse.setGrabbed(false);
		} else if (this.main.input.isKeyPressed(Keyboard.KEY_LMENU)) {
			Mouse.setGrabbed(!Mouse.isGrabbed());
		} else if (this.main.input.isKeyPressed(Keyboard.KEY_R) && !this.typing) {
			this.camera.reset();
		} else if (this.main.input.isKeyPressed(Keyboard.KEY_RETURN)) {
			if (this.typing && (this.message.length() != 0)) {
				new Packet03Chat(this.player.getUsername(), this.message.toString()).sendData(this.client);
				this.message = new StringBuilder(CHAT_CHAR_LIMIT);
			}
			this.typing = !this.typing;
		} else if (this.main.input.isKeyPressed(Keyboard.KEY_F5)) {
			Models.init();
		}

		if (this.main.input.isKeyDown(Keyboard.KEY_V)) {
			Camera.moveSpeed = 0.01f;
		} else {
			Camera.moveSpeed = 0.0015f;
		}

		if (!this.paused) {
			if (this.typing) {
				this.main.input.handleTyping(this.message, Fonts.centuryGothic);
			} else {
				this.camera.tick(delta);
			}
			this.level.tick(delta);
		} else {
			this.typing = false;
		}
	}

	public void render() {
		// Level rendering
		Matrices.switchToPerspective();
		Shaders.use("lighting");
		Color.set(Shaders.current, 1f, 1f, 1f, 1f);
		Matrices.loadIdentity();

		this.camera.applyTransformations();

		Lighting.setTextured(true);
		Matrices.sendMVPMatrix(Shaders.current);
		Matrices.sendMVMatrix(Shaders.current);
		this.level.renderLevel();

		// Raycast before rendering entities to pick position by terrain only.
		if (!this.paused) {
			RayCast.tick(this.camera);
		}
		Lighting.setTextured(false);
		this.level.renderEntities();

		// GUI rendering
		glEnable(GL_BLEND);
		this.gui.render(this.message.toString(), this.paused, this.typing);
		Shaders.useDefault();
		glDisable(GL_BLEND);
	}

	// Getters and setters
	public GUI getGUI() {
		return this.gui;
	}

	public Level getLevel() {
		return this.level;
	}

	public Player getPlayer() {
		return this.player;
	}

	public Camera getCamera() {
		return this.camera;
	}

	public Client getClient() {
		return this.client;
	}
}
