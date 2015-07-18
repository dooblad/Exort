package server.entity.projectile;

import server.entity.*;
import server.level.*;
import shared.util.*;

/**
 * A MovingEntity with a lifetime.
 */
public abstract class Projectile extends MovingEntity {
	// Whoever conjured it.
	protected Entity owner;
	protected int currentLife, maxLife;

	/**
	 * Creates a Projectile at the origin with no velocity and no associated level.
	 */
	public Projectile() {
		this(0, 0, 0, 0, 0, null, null);
	}

	/**
	 * Creates a Projectile on "level" at the origin with no velocity and "maxLife".
	 */
	public Projectile(int maxLife, Level level) {
		this(0, 0, 0, 0, maxLife, null, level);
	}

	/**
	 * Creates a Projectile on "level" at ("x", "z") with "maxLife".
	 */
	public Projectile(float x, float z, int maxLife, Level level) {
		this(x, z, 0, 0, maxLife, null, level);
	}

	/**
	 * Creates a Projectile on "level" at "position" with velocity ("xa", "ya", "za") and
	 * "maxLife".
	 */
	public Projectile(Vector2f position, float xa, float za, int maxLife, Level level) {
		this(position.getX(), position.getZ(), xa, za, maxLife, null, level);
	}

	/**
	 * Creates a Projectile on "level" at ("x", "y", "z") with velocity ("xa", "ya", "za")
	 * and "maxLife" with "owner".
	 */
	public Projectile(float x, float z, float xa, float za, int maxLife, Entity owner, Level level) {
		super(x, z, xa, za, level);
		this.currentLife = this.maxLife = maxLife;
		this.owner = owner;
	}

	/**
	 * Handles the behavior of this Projectile.
	 */
	public void tick(int delta) {
		super.tick(delta);
		if (this.currentLife-- <= 0) {
			this.remove();
		}
	}

	/**
	 * Returns the Entity that summoned this Projectile.
	 */
	public Entity getOwner() {
		return this.owner;
	}

	/**
	 * Sets the owner of this Projectile to "owner".
	 */
	public void setOwner(Entity owner) {
		this.owner = owner;
	}

	/**
	 * Returns the current life of this Projectile.
	 */
	public int getCurrentLife() {
		return this.currentLife;
	}

	/**
	 * Sets this Projectile's current life to "life".
	 */
	public void setCurrentLife(int life) {
		this.currentLife = life;
	}

	/**
	 * Returns the maximum life of this Projectile.
	 */
	public int getMaxLife() {
		return this.maxLife;
	}

	/**
	 * Sets the maximum life of this Projectile to "life".
	 */
	public void setMaxLife(int life) {
		this.maxLife = life;
	}
}