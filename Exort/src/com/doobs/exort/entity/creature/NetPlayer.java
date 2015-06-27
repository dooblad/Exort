package com.doobs.exort.entity.creature;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.*;
import org.lwjgl.util.vector.*;

import com.doobs.exort.level.*;
import com.doobs.exort.math.*;
import com.doobs.exort.net.client.*;
import com.doobs.exort.net.packets.*;
import com.doobs.exort.util.*;
import com.doobs.exort.util.loaders.*;
import com.doobs.modern.util.*;
import com.doobs.modern.util.batch.*;
import com.doobs.modern.util.matrix.*;
import com.doobs.modern.util.texture.*;

public class NetPlayer extends Player {
	private Client client;
	private String username;
	private String address;
	private int port;

	public NetPlayer(Client client, String username, String address, int port, Level level, InputHandler input) {
		super(Player.spawn[0], Player.spawn[1], Player.spawn[2], level, input);
		this.client = client;
		this.username = username;
		this.address = address;
		this.port = port;
	}

	@Override
	public void tick(int delta) {
		super.tick(delta);
		if (this.client != null) {
			if (Mouse.isButtonDown(1)) {
				this.setTarget(new Vector3f(RayCast.mouseX, 0, RayCast.mouseZ));
			}
			if (this.input.isKeyReleased(Keyboard.KEY_Q)) {
				new Packet04SonicWave(this.username, this.calculateAngle(RayCast.mouseX - this.x, RayCast.mouseZ - this.z)).sendData(this.client);
			}
			if (this.input.isKeyReleased(Keyboard.KEY_W) && !Mouse.isGrabbed()) {
				new Packet05RockWall(this.username, this.calculateAngle(RayCast.mouseX - this.x, RayCast.mouseZ - this.z), RayCast.mouseX, RayCast.mouseZ)
				.sendData(this.client);
				/*
				 * if(Main.input.isKeyReleased(Keyboard.KEY_S)) new Packet06Stop(username,
				 * );
				 */
			}
		}
	}

	@Override
	public void render() {
		super.render();
		if (this.client != null) {
			if (this.input.isKeyDown(Keyboard.KEY_Q)) {
				glEnable(GL_BLEND);
				Shaders.use("texture");
				Color.set(Shaders.current, 1f, 1f, 1f, 1f);
				Texture texture = Textures.get("qIndicator");
				texture.bind();

				Matrices.translate(this.x, 0.5f, this.z);
				Matrices.rotate((float) Math.toDegrees(this.calculateAngle(RayCast.mouseX - this.x, RayCast.mouseZ - this.z)), 0f, 1f, 0f);
				Matrices.sendMVPMatrix(Shaders.current);

				new SimpleBatch(GL_TRIANGLES, 3, new float[] { 0f, 0f, 1f, 20f, 0f, 1f, 20f, 0f, -1f,

						20f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, 1f }, null, null, new float[] { 0f, 0f, 1f, 0f, 1f, 1f,

						1f, 1f, 0f, 1f, 0f, 0f }, null).draw(Shaders.current.getAttributeLocations());

				Matrices.rotate((float) -Math.toDegrees(this.calculateAngle(RayCast.mouseX - this.x, RayCast.mouseZ - this.z)), 0f, 1f, 0f);
				Matrices.translate(-this.x, -0.5f, -this.z);

				Shaders.use("lighting");
				glDisable(GL_BLEND);
			}

		}
	}

	@Override
	public void setTarget(Vector3f position) {
		// super.move(position);
		this.client.sendData(new Packet02Move(this.username, position.getX(), position.getZ()).getData());
	}

	private float calculateAngle(double x, double z) {
		float result = (float) Math.atan(z / x);

		if (x < 0) {
			result += Math.PI;
		}

		return result;
	}

	/**
	 * Post: Returns the Client associated with this NetPlayer.
	 */
	public Client getClient() {
		return this.client;
	}

	/**
	 * Post: Sets this NetPlayer's Client to "client".
	 */
	public void setClient(Client client) {
		this.client = client;
	}

	/**
	 * Post: Returns the username of this NetPlayer.
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * Post: Returns the IP address of this NetPlayer.
	 */
	public String getAddress() {
		return this.address;
	}

	/**
	 * Post: Returns the port this Client is operating on.
	 */
	public int getPort() {
		return this.port;
	}

	/**
	 * Post: Sets this Client's username to "username".
	 */
	public void setUsername(String username) {
		this.username = username;
	}
}
