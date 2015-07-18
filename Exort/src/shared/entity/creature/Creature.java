package shared.entity.creature;

import shared.entity.*;
import shared.level.*;

/**
 * A step up from Entity, because it has health...
 *
 * There is no maximum health because we must teach ourselves that there is no upper bound
 * to our well-being.
 */
public abstract class Creature extends MovingEntity {
	protected int health;

	/**
	 * Creates a Creature at (0, 0) with no initial velocity and no associated Level.
	 */
	public Creature() {
		this(0, 0, 0, 0, null);
	}

	/**
	 * Creates a Creature at ("x", "z") with no initial velocity and no associated Level.
	 */
	public Creature(float x, float z) {
		this(x, z, 0, 0, null);
	}

	/**
	 * Creates a Creature at ("x", "z") with no initial velocity and "level".
	 */
	public Creature(float x, float z, Level level) {
		this(x, z, 0, 0, level);
	}

	/**
	 * Creates a Creature at ("x", "z") with initial velocity ("xa", "za") and "level".
	 */
	public Creature(float x, float z, float xa, float za, Level level) {
		super(x, z, xa, za, level);
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
