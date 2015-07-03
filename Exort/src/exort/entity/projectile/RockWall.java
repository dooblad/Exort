package exort.entity.projectile;

import org.lwjgl.util.vector.*;

import com.doobs.modern.util.*;
import com.doobs.modern.util.matrix.*;

import exort.*;
import exort.entity.*;
import exort.level.*;
import exort.util.loaders.*;
import exort.util.sat.*;

/**
 * A wall with a limited life time that can be conjured by Players.
 */
public class RockWall extends Entity {
	public static final int LIFE = 60;

	private int currentLife;

	// Angle (in degrees) of the line perpendicular to this RockWall, with respect to the
	// xz-plane.
	private float direction;

	/**
	 * Creates a RockWall on "level" at "position" facing "direction".
	 */
	public RockWall(Vector3f position, float direction, Level level) {
		this(position.getX(), position.getZ(), direction, level);
	}

	/**
	 * Creates a RockWall on "level" at ("x", 0, "z") facing "direction".
	 */
	public RockWall(double x, double z, float direction, Level level) {
		super(x, z, level);
		this.bb = new BB((float) x, 0.7f, (float) z, 2f);
		this.bb.rotate(direction);
		this.currentLife = LIFE;
		this.direction = (float) (Math.toDegrees(direction));
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

	/**
	 * Renders this RockWall.
	 */
	public void render() {
		if (Main.debug) {
			this.bb.render();
		}

		Shaders.use("lighting");
		Matrices.translate(this.x, this.y, this.z);
		Matrices.rotate(this.direction, 0, 1, 0);
		Matrices.sendMVPMatrix(Shaders.current);
		Color.set(Shaders.current, 0f, 1f, 1f, 1f);
		Models.get("rockWall").draw();

		// Reset
		Matrices.rotate(this.direction, 0, -1, 0);
		Matrices.translate(-this.x, -this.y, -this.z);
	}
}