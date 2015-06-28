package exort.entity.projectile;

import org.lwjgl.util.vector.*;

import exort.entity.*;
import exort.level.*;

/**
 * A MovingEntity with a lifetime.
 */
public class Projectile extends MovingEntity {
	protected int currentLife, maxLife;

	/**
	 * Post: Creates a Projectile at the origin with no velocity and no associated level.
	 */
	public Projectile() {
		this(0, 0, 0, 0, 0, 0, 0, null);
	}

	/**
	 * Post: Creates a Projectile on "level" at the origin with no velocity and "maxLife".
	 */
	public Projectile(int maxLife, Level level) {
		this(0, 0, 0, 0, 0, 0, maxLife, level);
	}

	/**
	 * Post: Creates a Projectile on "level" at ("x", "y", "z") with "maxLife".
	 */
	public Projectile(double x, double y, double z, int maxLife, Level level) {
		this(x, y, z, 0, 0, 0, maxLife, level);
	}

	/**
	 * Post: Creates a Projectile on "level" at "position" with velocity ("xa", "ya",
	 * "za") and "maxLife".
	 */
	public Projectile(Vector3f position, double xa, double ya, double za, int maxLife, Level level) {
		this(position.getX(), position.getY(), position.getZ(), xa, ya, za, maxLife, level);
	}

	/**
	 * Post: Creates a Projectile on "level" at ("x", "y", "z") with velocity ("xa", "ya",
	 * "za") and "maxLife".
	 */
	public Projectile(double x, double y, double z, double xa, double ya, double za, int maxLife, Level level) {
		super(x, y, z, xa, ya, za, level);
		this.currentLife = this.maxLife = maxLife;
	}

	/**
	 * Post: Handles the behavior of this Projectile, adjusting certain values in
	 * accordance with "delta".
	 */
	public void tick(int delta) {
		super.tick(delta);
		if (this.currentLife-- <= 0) {
			this.remove();
		}
	}

	// Getters and setters
	public int getCurrentLife() {
		return this.currentLife;
	}

	public void setCurrentLife(int life) {
		this.currentLife = life;
	}

	public int getMaxLife() {
		return this.maxLife;
	}

	public void setMaxLife(int life) {
		this.maxLife = life;
	}
}
