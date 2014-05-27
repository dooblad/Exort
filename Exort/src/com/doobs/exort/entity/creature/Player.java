package com.doobs.exort.entity.creature;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.vector.Vector3f;

import res.models.OBJLoader;

import com.doobs.exort.entity.*;
import com.doobs.exort.gfx.*;
import com.doobs.exort.level.*;
import com.doobs.exort.net.*;
import com.doobs.exort.util.Model;

public class Player extends MovingEntity {
	private Model model, move;

	private Client client;
	private String username;

	private float targetX, targetZ;

	private static float moveSpeed = 1 / 50f;

	public Player(Client client, String username, int x, int z, Level level) {
		super(x, z, level);
		this.client = client;
		this.username = username;
	}

	public Player() {
		super();
		model = OBJLoader.loadModel("player.obj");
		move = OBJLoader.loadModel("move.obj");
		targetX = 0;
		targetZ = 0;
		xa = 0;
		za = 0;
	}

	public void tick(int delta) {
		// Player movement
		boolean xDone = false, zDone = false;
		if (xa > 0 && this.x + xa * delta > targetX) {
			xa = targetX - this.x;
			xDone = true;
		} else if (xa < 0 && this.x + xa * delta < targetX) {
			xa = targetX - this.x;
			xDone = true;
		}

		if (za > 0 && this.z + za * delta > targetZ) {
			za = targetZ - this.z;
			zDone = true;
		} else if (za < 0 && this.z + za * delta < targetZ) {
			za = targetZ - this.z;
			zDone = true;
		}

		if (xDone) {
			this.x = targetX;
			xa = 0;
		}

		if (zDone) {
			this.z = targetZ;
			za = 0;
		}

		this.x += xa * delta;
		this.z += za * delta;
		
		// Ability handling
		
		// Update lighting
		Lighting.moveLight(new Vector3f((float) this.x, 4f, (float) this.z), false);
	}

	public void render() {
		glTranslated(x, y, z);
		glColor3f(1.0f, 0.0f, 0.0f);
		glCallList(model.getHandle());

		glTranslated(targetX - x, 0, targetZ - z);
		glColor3f(0.0f, 1.0f, 0.0f);
		glCallList(move.getHandle());

		glTranslated(-targetX, -y, -targetZ);
		glColor3f(1.0f, 1.0f, 1.0f);
	}

	public void move(Vector3f position) {
		if (position.getX() != this.x || position.getZ() != this.z) {
			targetX = position.getX();
			targetZ = position.getZ();
			Vector3f target = new Vector3f((float) (targetX - this.x), 0f, (float) (targetZ - this.z));
			target.normalise();
			xa = target.getX() * moveSpeed;
			za = target.getZ() * moveSpeed;
		}
	}

	// Getters and Setters
	public Model getModel() {
		return model;
	}

	public Client getClient() {
		return client;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}