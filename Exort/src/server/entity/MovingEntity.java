package server.entity;

import server.level.*;

/**
 * An Entity that can move.
 */
public abstract class MovingEntity extends Entity {
	public float xv, yv, zv;
	// In radians.
	protected float direction;

	/**
	 * Creates a MovingEntity at the origin with no associated Level.
	 */
	public MovingEntity() {
		this(0, 0, 0, 0, null);
	}

	/**
	 * Creates a MovingEntity at ("x", "z") with no associated Level.
	 */
	public MovingEntity(float x, float z) {
		this(x, z, 0, 0, null);
	}

	/**
	 * Creates a MovingEntity at ("x", "z") on "level".
	 */
	public MovingEntity(float x, float z, Level level) {
		this(x, z, 0, 0, level);
	}

	/**
	 * Creates a MovingEntity at ("x", "z") with velocity ("xa", "za") on "level".
	 */
	public MovingEntity(float x, float z, float xa, float za, Level level) {
		super(x, z, level);
		this.xv = xa;
		this.zv = za;
	}

	/**
	 * Handles the behavior of this MovingEntity.
	 */
	public void tick(int delta) {
		this.x += this.xv * delta;
		this.z += this.zv * delta;

		this.bb.setPosition(this.x, this.z);
	}

	/**
	 * Sets velocity on every axis to 0.
	 */
	public void stop() {
		this.xv = 0;
		this.yv = 0;
		this.zv = 0;
	}

	/**
	 * Returns the x-velocity of this MovingEntity.
	 */
	public float getXA() {
		return this.xv;
	}

	/**
	 * Sets the x-velocity of this MovingEntity to "xa".
	 */
	public void setXA(float xa) {
		this.xv = xa;
	}

	/**
	 * Returns the y-velocity of this MovingEntity.
	 */
	public float getYA() {
		return this.yv;
	}

	/**
	 * Sets the y-velocity of this MovingEntity to "ya".
	 */
	public void setYA(float ya) {
		this.yv = ya;
	}

	/**
	 * Returns the z-velocity of this MovingEntity.
	 */
	public float getZA() {
		return this.zv;
	}

	/**
	 * Sets the z-velocity of this MovingEntity to "za".
	 */
	public void setZA(float za) {
		this.zv = za;
	}

	/**
	 * Returns the direction (in radians) of this MovingEntity.
	 */
	public float getDirection() {
		return this.direction;
	}

	/**
	 * Sets the direction (in radians) of this MovingEntity to "direction".
	 */
	public void setDirection(float direction) {
		this.direction = direction;
	}
}
