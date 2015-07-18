package client.entity.projectile;

import shared.sat.*;
import shared.util.*;
import client.entity.*;
import client.level.*;
import client.util.loaders.*;

import com.doobs.modern.util.*;
import com.doobs.modern.util.matrix.*;

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
	public SonicWave(Vector2f vector2f, float direction, Entity owner, Level level) {
		this(vector2f.x, vector2f.z, SPEED * (float) Math.cos(direction), SPEED * (float) Math.sin(direction), owner, level);
	}

	/**
	 * Creates a SonicWave on "level" at "position" with velocity ("xa", "za).
	 */
	public SonicWave(Vector2f position, float xa, float za, Level level) {
		this(position.getX(), position.getZ(), xa, za, null, level);
	}

	/**
	 * Creates a SonicWave on "level" at ("x", "z") with velocity ("xa", "za").
	 */
	public SonicWave(float x, float z, float xa, float za, Entity owner, Level level) {
		super(x, z, xa, za, LIFE, owner, level);
		this.bb = new OBB(x, 1f, y, 1f);
		this.direction = TrigUtil.calculateAngle(xa, za);
		this.bb.rotate(this.direction);
	}

	/**
	 * Handles the behavior of this RockWall.
	 */
	public void tick(int delta) {
		super.tick(delta);
	}

	/**
	 * Renders this SonicWave.
	 */
	public void render() {
		this.bb.render();

		Shaders.use("lighting");
		Matrices.translate(this.x, this.y, this.z);
		Matrices.rotate(Math.toDegrees(this.direction), 0, 1, 0);
		Matrices.sendMVPMatrix(Shaders.current);
		Color.set(Shaders.current, 0f, 1f, 1f, 1f);
		Models.get("sonicWave").draw();

		// Reset
		Matrices.rotate(Math.toDegrees(this.direction), 0, -1, 0);
		Matrices.translate(-this.x, -this.y, -this.z);
	}
}
