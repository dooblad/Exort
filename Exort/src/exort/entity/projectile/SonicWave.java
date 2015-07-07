package exort.entity.projectile;

import org.lwjgl.util.vector.*;

import com.doobs.modern.util.*;
import com.doobs.modern.util.matrix.*;

import exort.level.*;
import exort.util.*;
import exort.util.loaders.*;
import exort.util.sat.*;

/**
 * A Projectile with mysterious purposes.
 */
public class SonicWave extends Projectile {
	public static final double SPEED = 1.0 / 25.0;
	public static final int LIFE = 28;

	/**
	 * Creates a SonicWave on "level" at "position" with initial "direction" (in radians).
	 */
	public SonicWave(Vector3f position, double direction, Level level) {
		this(position.getX(), position.getY() + 1.5f, position.getZ(), SPEED * Math.cos(direction), 0, SPEED * Math.sin(direction), level);
	}

	/**
	 * Creates a SonicWave on "level" at "position" with velocity ("xa", "ya", "za).
	 */
	public SonicWave(Vector3f position, double xa, double ya, double za, Level level) {
		this(position.getX(), position.getY(), position.getZ(), xa, ya, za, level);
	}

	/**
	 * Creates a SonicWave on "level" at ("x", "y", "z") with velocity ("xa", "ya", "za").
	 */
	public SonicWave(double x, double y, double z, double xa, double ya, double za, Level level) {
		super(x, y, z, xa, ya, za, LIFE, level);
		this.bb = new OBB((float) x, 1f, (float) y, 1f);
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
