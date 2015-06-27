package com.doobs.exort.entity.creature;

import java.util.*;

import org.lwjgl.util.vector.*;

import com.doobs.exort.entity.*;
import com.doobs.exort.gfx.*;
import com.doobs.exort.level.*;
import com.doobs.exort.util.*;
import com.doobs.exort.util.loaders.*;
import com.doobs.exort.util.sat.*;
import com.doobs.modern.util.*;
import com.doobs.modern.util.matrix.*;

public class Player extends MovingEntity {
	protected InputHandler input;

	public static double[] spawn = new double[] { 0, 0, 0 };

	private float targetX, targetZ;

	private float moveSpeed;

	/**
	 * Post: Initializes a Player at the origin with no associated Level or InputHandler.
	 */
	public Player() {
		this(0, 0, 0, null, null);
	}

	/**
	 * Post: Initializes a Player at ("x", "y", "z") on "level" with movement specified by
	 * "input".
	 */
	public Player(double x, double y, double z, Level level, InputHandler input) {
		super(x, y, z, level);
		this.targetX = 0;
		this.targetZ = 0;
		this.xa = 0;
		this.za = 0;
		this.moveSpeed = 1f / 50f;
		this.bb = new BB((float) x, 2.5f, (float) z, 2.5f);
		this.input = input;
	}

	/**
	 * Post: Handles the majority of the logic of this Player, adjusting certain values in
	 * accordance with "delta".
	 */
	public void tick(int delta) {
		// Player movement.
		boolean xDone = false, zDone = false;
		if ((this.xa > 0) && ((this.x + (this.xa * delta)) > this.targetX)) {
			this.xa = this.targetX - this.x;
			xDone = true;
		} else if ((this.xa < 0) && ((this.x + (this.xa * delta)) < this.targetX)) {
			this.xa = this.targetX - this.x;
			xDone = true;
		}

		if ((this.za > 0) && ((this.z + (this.za * delta)) > this.targetZ)) {
			this.za = this.targetZ - this.z;
			zDone = true;
		} else if ((this.za < 0) && ((this.z + (this.za * delta)) < this.targetZ)) {
			this.za = this.targetZ - this.z;
			zDone = true;
		}

		if (xDone) {
			this.x = this.targetX;
			this.xa = 0;
		}

		if (zDone) {
			this.z = this.targetZ;
			this.za = 0;
		}

		super.tick(delta);

		// Check collisions
		Iterator<Entity> iterator = this.level.getEntities().iterator();
		while (iterator.hasNext()) {
			Entity entity = iterator.next();
			// Vector2f mtv = null;
			// if (entity != null && entity != this && (mtv =
			// bb.colliding(entity.getBB())) != null) {
			if (entity instanceof MovingEntity) {
				// MovingEntity mEntity = (MovingEntity) entity;
				/*
				 * mEntity.stop(); stop();
				 *
				 * // Divide by 2 so each entity is repelled by an equal amount // (no
				 * momentum) if (mtv != null) { entity.x += mtv.x; entity.z += mtv.y;
				 *
				 * mEntity.x += -mtv.x; mEntity.z += -mtv.y;
				 *
				 * //System.out.println("X: " + mtv.x + " Y: " + mtv.y); }
				 */
				// }
			}
		}

		// Ability handling
		// TODO: Abilities, m8.

		// Update lighting
		Lighting.moveLight(new Vector3f((float) this.x, 8f, (float) this.z), false);
	}

	/**
	 * Post: Renders this Player and all graphical entities associated with it.
	 */
	public void render() {
		this.bb.render();
		Shaders.use("lighting");

		// Draw model command
		Matrices.translate(this.x, this.y, this.z);
		Matrices.sendMVPMatrix(Shaders.current);
		Color.set(Shaders.current, 1f, 0f, 0f, 1f);
		Models.get("player").draw();

		// Draw move command
		Matrices.translate(this.targetX - this.x, 0, this.targetZ - this.z);
		Matrices.sendMVPMatrix(Shaders.current);
		if ((this.xa != 0) && (this.za != 0)) {
			Color.set(Shaders.current, 0f, 1f, 0f, 1f);
			Models.get("move").draw();
		}

		// Reset
		Matrices.translate(-this.targetX, -this.y, -this.targetZ);
		Matrices.sendMVPMatrix(Shaders.current);
		Color.set(Shaders.current, 1f, 1f, 1f, 1f);
	}

	/**
	 * Post: Sets this Player's target to "position".
	 */
	public void setTarget(Vector3f position) {
		if ((position.getX() != this.x) || (position.getZ() != this.z)) {
			this.targetX = position.x;
			this.targetZ = position.z;
			this.calculateSpeeds();
		}
	}

	/**
	 * Post: Calculates the component-wise velocities required to reach this Player's
	 * target.
	 */
	public void calculateSpeeds() {
		Vector3f target = new Vector3f((float) (this.targetX - this.x), 0f, (float) (this.targetZ - this.z));
		if ((target.getX() != 0) || (target.getZ() != 0)) {
			target.normalise();
			this.xa = target.getX() * this.moveSpeed;
			this.za = target.getZ() * this.moveSpeed;
		}
	}

	/**
	 * Post: Returns a Vector3f that represents this Player's current position.
	 */
	public Vector3f getPosition() {
		return new Vector3f((float) this.x, (float) this.y, (float) this.z);
	}

	/**
	 * Post: Sets the x-coordinate of this Player's target to "x".
	 */
	public void setTargetX(float x) {
		this.targetX = x;
		this.calculateSpeeds();
	}

	/**
	 * Post: Sets the z-coordinate of this Player's target to "z".
	 */
	public void setTargetZ(float z) {
		this.targetZ = z;
		this.calculateSpeeds();
	}

	/**
	 * Post: Sets the coordinates of this Player's target to ("x", "z").
	 */
	public void setTargetPosition(float x, float z) {
		this.targetX = x;
		this.targetZ = z;
		this.bb.rotate(-Math.atan(x / z));
		this.calculateSpeeds();
	}

	/**
	 * Post: Returns this Player's current move speed.
	 */
	public float getMoveSpeed() {
		return this.moveSpeed;
	}

	/**
	 * Post: Sets this Player's move speed to "moveSpeed".
	 */
	public void setMoveSpeed(float moveSpeed) {
		this.moveSpeed = moveSpeed;
	}
}
