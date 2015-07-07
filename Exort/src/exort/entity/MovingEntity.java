package exort.entity;

import exort.level.*;

/**
 * An Entity that can move.
 */
public class MovingEntity extends Entity {
	public float xa, ya, za;
	// In radians.
	protected float direction;

	/**
	 * Creates a MovingEntity at the origin with no associated Level.
	 */
	public MovingEntity() {
		this(0, 0, 0, null);
	}

	/**
	 * Creates a MovingEntity at ("x", 0, "z") with no associated Level.
	 */
	public MovingEntity(float x, float z) {
		this(x, 0, z, null);
	}

	/**
	 * Creates a MovingEntity at ("x", "y", "z") with no associated Level.
	 */
	public MovingEntity(float x, float y, float z) {
		this(x, y, z, null);
	}

	/**
	 * Creates a MovingEntity at ("x", 0, "z") on "level".
	 */
	public MovingEntity(float x, float z, Level level) {
		this(x, 0, z, level);
	}

	/**
	 * Creates a MovingEntity at ("x", "y", "z") on "level".
	 */
	public MovingEntity(float x, float y, float z, Level level) {
		super(x, y, z, level);
	}

	/**
	 * Creates a MovingEntity at ("x", "y", "z") with velocity ("xa", "ya", "za") on
	 * "level".
	 */
	public MovingEntity(float x, float y, float z, float xa, float ya, float za, Level level) {
		super(x, y, z, level);
		this.xa = xa;
		this.ya = ya;
		this.za = za;
	}

	/**
	 * Handles the behavior of this MovingEntity.
	 */
	public void tick(int delta) {
		this.x += this.xa * delta;
		this.y += this.ya * delta;
		this.z += this.za * delta;

		this.bb.move((float) this.x, (float) this.z);
	}

	/**
	 * Renders this MovingEntity.
	 */
	public void render() {

	}

	/**
	 * Sets acceleration on every axis to 0.
	 */
	public void stop() {
		this.xa = 0;
		this.ya = 0;
		this.za = 0;
	}

	/**
	 * Returns the x-acceleration of this MovingEntity.
	 */
	public float getXA() {
		return this.xa;
	}

	/**
	 * Sets the x-acceleration of this MovingEntity to "xa".
	 */
	public void setXA(float xa) {
		this.xa = xa;
	}

	/**
	 * Returns the y-acceleration of this MovingEntity.
	 */
	public float getYA() {
		return this.ya;
	}

	/**
	 * Sets the y-acceleration of this MovingEntity to "ya".
	 */
	public void setYA(float ya) {
		this.ya = ya;
	}

	/**
	 * Returns the z-acceleration of this MovingEntity.
	 */
	public float getZA() {
		return this.za;
	}

	/**
	 * Sets the z-acceleration of this MovingEntity to "za".
	 */
	public void setZA(float za) {
		this.za = za;
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
