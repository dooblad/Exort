package com.doobs.exort.state;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.*;

import res.shaders.*;
import res.textures.fonts.*;

import com.doobs.exort.*;
import com.doobs.exort.entity.creature.*;
import com.doobs.exort.gfx.*;
import com.doobs.exort.level.*;
import com.doobs.exort.math.*;
import com.doobs.exort.net.client.*;
import com.doobs.exort.net.packets.*;
import com.doobs.exort.net.server.*;
import com.doobs.exort.util.gl.*;

public class DuelState implements GameState {
	private Main main;
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

		this.client = new Client(main, level, address);
		new Packet00Login(username).writeData(this.client);

		if (isServer)
			server = new Server(level);

		level = new Level();
		player = new NetPlayer(client, username, address, client.getPort(), level);
		level.addApplicationPlayer(player);
		camera = new Camera(0.0f, 3.0f, 0.0f);

		typing = false;
		message = "";
	}

	public void tick(int delta) {
		if(GUI.exitDuel.isFull()) {
			if(server != null) server.exit();
			client.exit();
			main.changeState(new MainMenuState(main));
		}
		
		GUI.tick(delta);
		
		if (Main.input.isKeyPressed(Keyboard.KEY_ESCAPE)) {
			paused = !paused;
			GUI.chatFade.empty();
			Mouse.setGrabbed(false);
		} else if (Main.input.isKeyPressed(Keyboard.KEY_LMENU))
			Mouse.setGrabbed(!Mouse.isGrabbed());
		else if (Main.input.isKeyPressed(Keyboard.KEY_R) && !typing)
			camera.reset();
		else if (Main.input.isKeyPressed(Keyboard.KEY_RETURN)) {
			if (typing && message.length() != 0) {
				GUI.addMessage(message);
				message = "";
			}
			typing = !typing;
		}

		if (!paused) {
			if (typing)
				message = Main.input.handleTyping(message, Fonts.centuryGothic);
			else
				camera.tick(delta);

			level.tick(delta);
		} else
			typing = false;
	}

	public void render() {
		glEnable(GL_TEXTURE_2D);

		// Level rendering
		Shaders.lighting.use();

		camera.applyTransformations();

		Lighting.sendModelViewMatrix();

		Lighting.setTextured(true);
		level.renderLevel();
		if (!paused && Main.input.isMouseButtonDown(1))
			RayCast.movePlayer(camera, player);
		level.renderEntities();
		Lighting.setTextured(false);

		// GUI rendering
		glEnable(GL_BLEND);
		Shaders.font.use();
		GLTools.switchToOrtho();
		GUI.render(message, paused, typing);
		Shaders.useDefault();
		GLTools.switchToPerspective();
		glDisable(GL_BLEND);
		glDisable(GL_TEXTURE_2D);
	}

	// Getters and setters
	public Level getLevel() {
		return level;
	}

	public Player getPlayer() {
		return player;
	}

	public Camera getCamera() {
		return camera;
	}
}
