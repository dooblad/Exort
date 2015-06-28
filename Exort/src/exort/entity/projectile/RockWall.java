package exort.entity.projectile;

import org.lwjgl.util.vector.*;

import com.doobs.modern.util.*;
import com.doobs.modern.util.matrix.*;

import exort.*;
import exort.entity.*;
import exort.level.*;
import exort.util.loaders.*;
import exort.util.sat.*;

public class RockWall extends Entity {
	public static final int LIFE = 60;

	private int currentLife;

	private float direction;

	public RockWall(double x, double z, float direction, Level level) {
		super(x, z, level);
		this.bb = new BB((float) x, 0.7f, (float) z, 2f);
		this.bb.rotate(direction);
		this.currentLife = LIFE;
		this.direction = (float) (Math.toDegrees(direction));
	}

	public RockWall(Vector3f position, float direction, Level level) {
		this(position.getX(), position.getZ(), direction, level);
	}

	public void tick(int delta) {
		if (--this.currentLife < 0) {
			this.remove();
		}
	}

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
