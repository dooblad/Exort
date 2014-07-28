package com.doobs.exort.entity.projectile;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.vector.*;

import res.models.*;

import com.doobs.exort.level.*;

public class QSpell extends Projectile {
	public static final double SPEED = 1.0 / 25.0;
	public static final int LIFE = 120;

	public QSpell(double x, double y, double z, double xa, double ya, double za, Level level) {
		super(x, y, z, xa, ya, za, LIFE, level);
	}

	public QSpell(Vector3f position, double xa, double ya, double za, Level level) {
		this(position.getX(), position.getY(), position.getZ(), xa, ya, za, level);
	}

	public QSpell(Vector3f position, double direction, Level level) {
		this(position.getX(), position.getY() + 1.5f, position.getZ(), SPEED * Math.cos(direction), 0, SPEED * Math.sin(direction), level);
	}

	public void tick(int delta) {
		super.tick(delta);
	}

	public void render() {
		glTranslated(this.x, this.y, this.z);
		glColor3f(0.3f, 0.3f, 1.0f);
		glCallList(Models.stillModels.get("q").getHandle());

		// Reset
		glTranslated(-this.x, -this.y, -this.z);
	}
}
