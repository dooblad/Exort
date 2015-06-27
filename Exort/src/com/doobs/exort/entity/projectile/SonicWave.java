package com.doobs.exort.entity.projectile;

import org.lwjgl.util.vector.*;

import com.doobs.exort.level.*;
import com.doobs.exort.util.loaders.*;
import com.doobs.exort.util.sat.*;
import com.doobs.modern.util.*;
import com.doobs.modern.util.matrix.*;

public class SonicWave extends Projectile {
	public static final double SPEED = 1.0 / 25.0;
	public static final int LIFE = 28;

	public SonicWave(double x, double y, double z, double xa, double ya, double za, Level level) {
		super(x, y, z, xa, ya, za, LIFE, level);
		this.bb = new BB((float) x, 1f, (float) y, 1f);
		this.direction = (float) Math.toDegrees(Math.atan(za / xa));
	}

	public SonicWave(Vector3f position, double xa, double ya, double za, Level level) {
		this(position.getX(), position.getY(), position.getZ(), xa, ya, za, level);
	}

	public SonicWave(Vector3f position, double direction, Level level) {
		this(position.getX(), position.getY() + 1.5f, position.getZ(), SPEED * Math.cos(direction), 0, SPEED * Math.sin(direction), level);
	}

	@Override
	public void tick(int delta) {
		super.tick(delta);
	}

	@Override
	public void render() {
		this.bb.render();

		Shaders.use("lighting");
		Matrices.translate(this.x, this.y, this.z);
		Matrices.rotate(this.direction, 0, 1, 0);
		Matrices.sendMVPMatrix(Shaders.current);
		Color.set(Shaders.current, 0f, 1f, 1f, 1f);
		Models.get("sonicWave").draw();

		// Reset
		Matrices.rotate(this.direction, 0, -1, 0);
		Matrices.translate(-this.x, -this.y, -this.z);
	}
}
