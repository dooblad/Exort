package exort.entity.projectile;

import org.lwjgl.util.vector.*;

import com.doobs.modern.util.*;
import com.doobs.modern.util.matrix.*;

import exort.entity.*;
import exort.level.*;
import exort.util.*;
import exort.util.loaders.*;
import exort.util.sat.*;

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
	public SonicWave(Vector3f position, float direction, Entity owner, Level level) {
		this(position.x, position.y + 1.5f, position.z, SPEED * (float) Math.cos(direction), 0, SPEED * (float) Math.sin(direction), owner, level);
	}

	/**
	 * Creates a SonicWave on "level" at "position" with velocity ("xa", "ya", "za).
	 */
	public SonicWave(Vector3f position, float xa, float ya, float za, Level level) {
		this(position.getX(), position.getY(), position.getZ(), xa, ya, za, null, level);
	}

	/**
	 * Creates a SonicWave on "level" at ("x", "y", "z") with velocity ("xa", "ya", "za").
	 */
	public SonicWave(float x, float y, float z, float xa, float ya, float za, Entity owner, Level level) {
		super(x, y, z, xa, ya, za, LIFE, owner, level);
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
