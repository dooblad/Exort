package server.entity.projectile;

import server.entity.*;
import server.level.*;
import shared.sat.*;
import shared.util.*;

/**
 * A Projectile with mysterious purposes.
 */
public class SonicWave extends Projectile {
	public static final float SPEED = 1f / 25f;
	public static final int LIFE = 28;

	/**
	 * Creates a SonicWave on "level" at "position" with initial "direction" (in radians)
	 * with "owner".
	 */
	public SonicWave(Vector2f position, float direction, Entity owner, Level level) {
		this(position.x, position.z, SPEED * (float) Math.cos(direction), SPEED * (float) Math.sin(direction), owner, level);
	}

	/**
	 * Creates a SonicWave on "level" at "position" with velocity ("xa", "za").
	 */
	public SonicWave(Vector2f position, float xa, float za, Level level) {
		this(position.getX(), position.getZ(), xa, za, null, level);
	}

	/**
	 * Creates a SonicWave on "level" at ("x", "z") with velocity ("xa", "za").
	 */
	public SonicWave(float x, float z, float xa, float za, Entity owner, Level level) {
		super(x, z, xa, za, LIFE, owner, level);
		this.bb = new OBB(x, 1f, z, 1f);
		this.direction = TrigUtil.calculateAngle(xa, za);
		this.bb.rotate(this.direction);
	}

	/**
	 * Handles the behavior of this RockWall.
	 */
	public void tick(int delta) {
		super.tick(delta);
	}
}
