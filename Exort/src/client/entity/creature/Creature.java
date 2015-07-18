package client.entity.creature;

import client.entity.*;
import client.level.*;

/**
 * A step up from Entity, because it has health...
 *
 * There is no maximum health because we must teach ourselves that there is no upper bound
 * to our well-being.
 */
public abstract class Creature extends MovingEntity {
	protected int health;

	/**
	 * Creates a Creature at (0, 0, 0) with no initial velocity and no associated Level.
	 */
	public Creature() {
		this(0, 0, 0, 0, 0, 0, null);
	}

	/**
	 * Creates a Creature at ("x", 0, "z") with no initial velocity and no associated
	 * Level.
	 */
	public Creature(float x, float z) {
		this(x, 0, z, 0, 0, 0, null);
	}

	/**
	 * Creates a Creature at ("x", "y", "z") with no initial velocity and no associated
	 * Level.
	 */
	public Creature(float x, float y, float z) {
		this(x, y, z, 0, 0, 0, null);
	}

	/**
	 * Creates a Creature at ("x", 0, "z") with no initial velocity and "level".
	 */
	public Creature(float x, float z, Level level) {
		this(x, 0, z, 0, 0, 0, level);
	}

	/**
	 * Creates a Creature at ("x", "y", "z") with no initial velocity and "level".
	 */
	public Creature(float x, float y, float z, Level level) {
		super(x, y, z, 0, 0, 0, level);
	}

	/**
	 * Creates a Creature at ("x", "y", "z") with initial velocity ("xa", "ya", "za") and
	 * "level".
	 */
	public Creature(float x, float y, float z, float xa, float ya, float za, Level level) {
		super(x, y, z, xa, ya, za, level);
	}

	/**
	 * Returns the current health of this Creature.
	 */
	public int getHealth() {
		return this.health;
	}

	/**
	 * Sets the health of this Creature to "health".
	 */
	public void setHealth(int health) {
		this.health = health;
	}
}
