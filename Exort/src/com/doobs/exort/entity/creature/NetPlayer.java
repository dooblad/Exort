package com.doobs.exort.entity.creature;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.*;
import org.lwjgl.util.vector.*;

import com.doobs.exort.*;
import com.doobs.exort.level.*;
import com.doobs.exort.math.*;
import com.doobs.exort.net.client.*;
import com.doobs.exort.net.packets.*;
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

	public NetPlayer(Client client, String username, String address, int port, Level level) {
		super(Player.spawn[0], Player.spawn[1], Player.spawn[2], level);
		this.client = client;
		this.username = username;
		this.address = address;
		this.port = port;
	}

	@Override
	public void tick(int delta) {
		super.tick(delta);
		if (client != null) {
			if (Mouse.isButtonDown(1))
				move(new Vector3f(RayCast.mouseX, 0, RayCast.mouseZ));
			if (Main.input.isKeyReleased(Keyboard.KEY_Q))
				new Packet04Q(username, calculateAngle(RayCast.mouseX - this.x, RayCast.mouseZ - this.z)).sendData(client);
		}
	}

	@Override
	public void render() {
		super.render();
		if (client != null) {
			if (Main.input.isKeyDown(Keyboard.KEY_Q)) {
				glEnable(GL_BLEND);
				Shaders.use("texture");
				Color.set(Shaders.current, 1f, 1f, 1f, 1f);
				Texture texture = Textures.get("qIndicator");
				texture.bind();

				Matrices.translate(this.x, 0.5f, this.z);
				Matrices.rotate((float) Math.toDegrees(calculateAngle(RayCast.mouseX - this.x, RayCast.mouseZ - this.z)), 0f, 1f, 0f);
				Matrices.sendMVPMatrix(Shaders.current);
				
				new SimpleBatch(GL_TRIANGLES, 3, new float[] {
					0f, 0f, 1f, 
					20f, 0f, 1f,
					20f, 0f, -1f,
					
					20f, 0f, -1f,
					0f, 0f, -1f,
					0f, 0f, 1f
				}, null, null, new float[] {
					0f, 0f,
					1f, 0f,
					1f, 1f,
					
					1f, 1f,
					0f, 1f,
					0f, 0f
				}, null).draw(Shaders.current.getAttributeLocations());
				
				Matrices.rotate((float) -Math.toDegrees(calculateAngle(RayCast.mouseX - this.x, RayCast.mouseZ - this.z)), 0f, 1f, 0f);
				Matrices.translate(-this.x, -0.5f, -this.z);
				
				Shaders.use("lighting");
				glDisable(GL_BLEND);
			}

		}
	}

	@Override
	public void move(Vector3f position) {
		// super.move(position);
		client.sendData(new Packet02Move(username, position.getX(), position.getZ()).getData());
	}

	private float calculateAngle(double x, double z) {
		float result = (float) Math.atan(z / x);

		if (x < 0)
			result += Math.PI;

		return result;
	}

	// Getters and setters
	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public String getUsername() {
		return username;
	}

	public String getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	public void setAddress(String username) {
		this.username = username;
	}
}
