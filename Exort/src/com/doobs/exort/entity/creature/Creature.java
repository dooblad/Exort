package com.doobs.exort.entity.creature;

import com.doobs.exort.entity.*;
import com.doobs.exort.level.*;

/**
 * A step up from Entity, because it has health...
 *
 * There is no maximum health because we must teach ourselves that there is no upper bound
 * to our well-being.
 */
public class Creature extends MovingEntity {
	private int health;

	/**
	 * Post: Creates a Creature at (0, 0, 0) with no initial velocity and no associated
	 * Level.
	 */
	public Creature() {
		this(0, 0, 0, 0, 0, 0, null);
	}

	/**
	 * Post: Creates a Creature at ("x", 0, "z") with no initial velocity and no
	 * associated Level.
	 */
	public Creature(double x, double z) {
		this(x, 0, z, 0, 0, 0, null);
	}

	/**
	 * Post: Creates a Creature at ("x", "y", "z") with no initial velocity and no
	 * associated Level.
	 */
	public Creature(double x, double y, double z) {
		this(x, y, z, 0, 0, 0, null);
	}

	/**
	 * Post: Creates a Creature at ("x", 0, "z") with no initial velocity and "level".
	 */
	public Creature(double x, double z, Level level) {
		this(x, 0, z, 0, 0, 0, level);
	}

	/**
	 * Post: Creates a Creature at ("x", "y", "z") with no initial velocity and "level".
	 */
	public Creature(double x, double y, double z, Level level) {
		super(x, y, z, 0, 0, 0, level);
	}

	/**
	 * Post: Creates a Creature at ("x", "y", "z") with initial velocity ("xa", "ya",
	 * "za") and "level".
	 */
	public Creature(double x, double y, double z, float xa, float ya, float za, Level level) {
		super(x, y, z, xa, ya, za, level);
	}

	/**
	 * Post: Returns the current health of this Creature.
	 */
	public int getHealth() {
		return this.health;
	}

	/**
	 * Post: Sets the health of this Creature to "health".
	 */
	public void setHealth(int health) {
		this.health = health;
	}
}
