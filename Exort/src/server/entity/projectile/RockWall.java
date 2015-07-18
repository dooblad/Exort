package server.entity.projectile;

import server.entity.*;
import server.level.*;
import shared.sat.*;
import shared.util.*;

/**
 * A wall with a limited life time that can be conjured by Players.
 */
public class RockWall extends Entity {
	public static final int LIFE = 500; // 60;

	private int currentLife;

	/**
	 * Creates a RockWall on "level" at "position" facing "direction".
	 */
	public RockWall(Vector2f position, float direction, Level level) {
		this(position.getX(), position.getZ(), direction, level);
	}

	/**
	 * Creates a RockWall on "level" at ("x", 0, "z") facing "direction".
	 */
	public RockWall(float x, float z, float direction, Level level) {
		super(x, z, level);
		this.bb = new OBB(x, 2f, z, 6f);
		this.bb.rotate(direction);
		this.currentLife = LIFE;
	}

	/**
	 * Handles the behavior of this RockWall.
	 */
	public void tick(int delta) {
		this.currentLife -= 1;
		if (this.currentLife < 0) {
			this.remove();
		}
	}
}