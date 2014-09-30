package com.doobs.exort.entity.projectile;

import org.lwjgl.util.vector.*;

import com.doobs.exort.entity.*;
import com.doobs.exort.level.*;
import com.doobs.exort.util.loaders.*;
import com.doobs.exort.util.sat.*;
import com.doobs.modern.util.*;
import com.doobs.modern.util.matrix.*;

public class RockWall extends Entity {
	public static final int LIFE = 120;
	
	private int currentLife;

	public RockWall(double x, double y, double z, Level level) {
		super(x, y, z, level);
		bb = new BB((float) x, 1f, (float) y, 1f);
		currentLife = LIFE;
	}

	public RockWall(Vector3f position, Level level) {
		this(position.getX(), position.getY(), position.getZ(), level);
	}

	@Override
	public void tick(int delta) {
		if(--currentLife < 0)
			remove();
	}

	@Override
	public void render() {
		bb.render();
		
		Shaders.use("lighting");
		Matrices.translate(this.x, this.y, this.z);
		Matrices.sendMVPMatrix(Shaders.current);
		Color.set(Shaders.current, 0.3f, 0.3f, 1.0f, 1.0f);
		Models.get("rockWall").draw();

		// Reset
		Matrices.translate(-this.x, -this.y, -this.z);
	}
}
