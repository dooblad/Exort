package com.doobs.exort.state;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.*;

import com.doobs.exort.*;
import com.doobs.exort.entity.creature.*;
import com.doobs.exort.gfx.*;
import com.doobs.exort.level.*;
import com.doobs.exort.math.*;
import com.doobs.exort.net.client.*;
import com.doobs.exort.net.packets.*;
import com.doobs.exort.net.server.*;
import com.doobs.exort.util.gl.*;
import com.doobs.exort.util.loaders.*;
import com.doobs.modern.util.*;
import com.doobs.modern.util.matrix.*;

public class DuelState implements GameState {
	private Main main;
	private GUI gui;
	private Level level;
	private NetPlayer player;
	private Camera camera;

	private Client client;
	private Server server;

	private boolean paused;

	private boolean typing;
	private String message;

	public DuelState(Main main, boolean isServer, String username, String address) {
		this.main = main;

		gui = new GUI();

		level = new Level();

		if (isServer)
			server = new Server(this, gui, new Level());

		client = new Client(main, gui, level, address);

		if (!isServer) {
			level.addMainPlayer(player);
			player = new NetPlayer(client, username, address, client.getPort(), level);
		}

		new Packet00Login(username).sendData(this.client);

		camera = new Camera(0.0f, 6.5f, 0.0f);

		typing = false;
		message = "";

		Mouse.setGrabbed(true);
	}

	@Override
	public void tick(int delta) {
		if (this.player == null && level.getMainPlayer() != null) {
			this.player = level.getMainPlayer();
			player.setClient(client);
		}

		if (gui.exitDuel.isFull()) {
			if (server != null) ;
				server.exit();
			client.sendData(new Packet01Disconnect(player.getUsername()).getData());
			client.exit();
			main.changeState(new MainMenuState(main));
		}

		gui.tick(paused, delta);

		if (Main.input.isKeyPressed(Keyboard.KEY_ESCAPE)) {
			paused = !paused;
			gui.chatFade.fill();
			Mouse.setGrabbed(false);
		} else if (Main.input.isKeyPressed(Keyboard.KEY_LMENU))
			Mouse.setGrabbed(!Mouse.isGrabbed());
		else if (Main.input.isKeyPressed(Keyboard.KEY_R) && !typing)
			camera.reset();
		else if (Main.input.isKeyPressed(Keyboard.KEY_RETURN)) {
			if (typing && message.length() != 0) {
				new Packet03Chat(player.getUsername(), message).sendData(client);
				message = "";
			}
			typing = !typing;
		} else if(Main.input.isKeyPressed(Keyboard.KEY_F5))
			Models.init();
		
		if(Main.input.isKeyDown(Keyboard.KEY_V))
			Camera.moveSpeed = 0.01f;
		else
			Camera.moveSpeed = 0.0015f;

		if (!paused) {
			if (typing)
				message = Main.input.handleTyping(message, Fonts.centuryGothic);
			else
				camera.tick(delta);

			level.tick(delta);
		} else
			typing = false;
	}

	@Override
	public void render() {
		// Level rendering
		Matrices.switchToPerspective();
		Shaders.use("lighting");
		Color.set(Shaders.current, 1f, 1f, 1f, 1f);
		Matrices.loadIdentity();
		
		camera.applyTransformations();

		Lighting.setTextured(true);
		Matrices.sendMVPMatrix(Shaders.current);
		Matrices.sendMVMatrix(Shaders.current);
		level.renderLevel();
		
		if (!paused)
			RayCast.tick(camera);
		Lighting.setTextured(false);
		level.renderEntities();

		// GUI rendering
		glEnable(GL_BLEND);
		gui.render(message, paused, typing);
		Shaders.useDefault();
		glDisable(GL_BLEND);
	}

	// Getters and setters
	public GUI getGUI() {
		return gui;
	}

	public Level getLevel() {
		return level;
	}

	public Player getPlayer() {
		return player;
	}

	public Camera getCamera() {
		return camera;
	}

	public Client getClient() {
		return client;
	}
}
