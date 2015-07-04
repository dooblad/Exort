package exort.entity;

import org.lwjgl.util.vector.*;

import exort.level.*;
import exort.util.sat.*;

/**
 * A thing with a collision box and a position in the Level.
 */
public class Entity {
	protected BB bb;
	protected double x, y, z;
	protected Level level;
	protected boolean removed;

	/**
	 * Creates an Entity at the origin with no associated Level.
	 */
	public Entity() {
		this(0, 0, 0, null);
	}

	/**
	 * Creates an Entity at ("x", 0, "z") with no associated Level.
	 */
	public Entity(double x, double z) {
		this(x, 0, z, null);
	}

	/**
	 * Creates an Entity at ("x", "y", "z") with no associated Level.
	 */
	public Entity(double x, double y, double z) {
		this(x, y, z, null);
	}

	/**
	 * Creates an Entity at ("x", 0, "z") on "level".
	 */
	public Entity(double x, double z, Level level) {
		this(x, 0, z, level);
	}

	/**
	 * Creates an Entity at ("x", "y", "z") on "level".
	 */
	public Entity(double x, double y, double z, Level level) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.level = level;
		this.removed = false;
	}

	/**
	 * Handles the behavior of this Entity.
	 */
	public void tick(int delta) {

	}

	/**
	 * Renders this Entity
	 */
	public void render() {

	}

	/**
	 * Sets a removed flag, so the Level knows to remove this Entity.
	 */
	public void remove() {
		this.removed = true;
	}

	/**
	 * Returns this Entity's bounding box.
	 */
	public BB getBB() {
		return this.bb;
	}

	/**
	 * Sets this Entity's bounding box to "bb".
	 */
	public void setBB(BB bb) {
		this.bb = bb;
	}

	/**
	 * Returns a Vector3f that represents this Entity's current position.
	 */
	public Vector3f getPosition() {
		return new Vector3f((float) this.x, (float) this.y, (float) this.z);
	}

	/**
	 * Returns this Entity's x-position.
	 */
	public double getX() {
		return this.x;
	}

	/**
	 * Sets this Entity's x-position to "x".
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Returns this Entity's y-position.
	 */
	public double getY() {
		return this.y;
	}

	/**
	 * Sets this Entity's y-position to "y".
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Returns this Entity's z-position.
	 */
	public double getZ() {
		return this.z;
	}

	/**
	 * Sets this Entity's z-position to "z".
	 */
	public void setZ(double z) {
		this.z = z;
	}

	/**
	 * Returns true if this Entity's remove flag has been set.
	 */
	public boolean isRemoved() {
		return this.removed;
	}
}
