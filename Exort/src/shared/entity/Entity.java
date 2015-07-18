package shared.entity;

import shared.level.*;
import shared.sat.*;
import shared.util.*;

/**
 * A thing with a collision box and a position in the Level.
 */
public abstract class Entity {
	protected OBB bb;
	protected float x, z;
	protected Level level;
	protected boolean removed;

	/**
	 * Creates an Entity at the origin with no associated Level.
	 */
	public Entity() {
		this(0, 0, null);
	}

	/**
	 * Creates an Entity at ("x", "z") with no associated Level.
	 */
	public Entity(float x, float z) {
		this(x, z, null);
	}

	/**
	 * Creates an Entity at ("x", "z") on "level".
	 */
	public Entity(float x, float z, Level level) {
		this.x = x;
		this.z = z;
		this.level = level;
		this.removed = false;
	}

	/**
	 * Handles the behavior of this Entity.
	 */
	public abstract void tick(int delta);

	/**
	 * Renders this Entity.
	 */
	public void render() {
		// Does nothing, but shouldn't be made abstract, or else Server-side Entity
		// classes will need to implement a render() method.
	}

	/**
	 * Adds "position" to this Entity's current position.
	 */
	public void move(Vector2f position) {
		this.move(position.getX(), position.getZ());
	}

	/**
	 * Moves this Entity by ("x", "z").
	 */
	public void move(float x, float z) {
		this.x += x;
		this.z += z;

		this.bb.setPosition(this.x, this.z);
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
	public OBB getBB() {
		return this.bb;
	}

	/**
	 * Sets this Entity's bounding box to "bb".
	 */
	public void setBB(OBB bb) {
		this.bb = bb;
	}

	/**
	 * Returns a Vector2f that represents this Entity's current position.
	 */
	public Vector2f getPosition() {
		return new Vector2f(this.x, this.z);
	}

	/**
	 * Returns this Entity's x-position.
	 */
	public float getX() {
		return this.x;
	}

	/**
	 * Sets this Entity's x-position to "x".
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Returns this Entity's z-position.
	 */
	public float getZ() {
		return this.z;
	}

	/**
	 * Sets this Entity's z-position to "z".
	 */
	public void setZ(float z) {
		this.z = z;
	}

	/**
	 * Returns true if this Entity's remove flag has been set.
	 */
	public boolean isRemoved() {
		return this.removed;
	}
}