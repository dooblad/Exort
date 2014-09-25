package com.doobs.exort.entity.creature;

import java.util.*;

import org.lwjgl.util.vector.*;

import com.doobs.exort.entity.*;
import com.doobs.exort.gfx.*;
import com.doobs.exort.level.*;
import com.doobs.exort.util.loaders.*;
import com.doobs.exort.util.sat.*;
import com.doobs.modern.util.*;
import com.doobs.modern.util.matrix.*;

public class Player extends MovingEntity {
	public static double[] spawn = new double[] { 0, 0, 0 };
	
	private float targetX, targetZ;

	private float moveSpeed;

	public Player(double x, double y, double z, Level level) {
		super(x, y, z, level);
		targetX = 0;
		targetZ = 0;
		xa = 0;
		za = 0;
		moveSpeed = 1f / 50f;
		bb = new BB((float) x, 2.5f, (float) z, 2.5f);
	}

	public Player() {
		this(0, 0, 0, null);
	}

	@Override
	public void tick(int delta) {
		// Player movement
		boolean xDone = false, zDone = false;
		if (xa > 0 && this.x + xa * delta > targetX) {
			xa = targetX - this.x;
			xDone = true;
		} else if (xa < 0 && this.x + xa * delta < targetX) {
			xa = targetX - this.x;
			xDone = true;
		}

		if (za > 0 && this.z + za * delta > targetZ) {
			za = targetZ - this.z;
			zDone = true;
		} else if (za < 0 && this.z + za * delta < targetZ) {
			za = targetZ - this.z;
			zDone = true;
		}

		if (xDone) {
			this.x = targetX;
			xa = 0;
		}

		if (zDone) {
			this.z = targetZ;
			za = 0;
		}
		
		super.tick(delta);

		//bb.move((float) x - 1.25f, (float) z - 1.25f);

		// Check collisions
		//while(level.entitiesLocked()) ;
		Iterator<Entity> iterator = level.getEntities().iterator();
		while(iterator.hasNext()) {
			Entity entity = iterator.next();
			
			if(entity != null && entity.getBB().colliding(bb) && entity != this) {
				if(entity instanceof MovingEntity) {
					((MovingEntity) entity).stop();
					stop();
				}
			}
		}
		
		// Ability handling

		// Update lighting
		Lighting.moveLight(new Vector3f((float) this.x, 8f, (float) this.z), false);
	}

	@Override
	public void render() {
		bb.render();
		Shaders.use("lighting");
		
		// Draw model command
		Matrices.translate(x, y, z);
		Matrices.sendMVPMatrix(Shaders.current);
		Color.set(Shaders.current, 1f, 0f, 0f, 1f);
		Models.get("player").draw();

		// Draw move command
		Matrices.translate(targetX - x, 0, targetZ - z);
		Matrices.sendMVPMatrix(Shaders.current);
		Color.set(Shaders.current, 0f, 1f, 0f, 1f);
		Models.get("move").draw();
		
		// Reset
		Matrices.translate(-targetX, -y, -targetZ);
		Matrices.sendMVPMatrix(Shaders.current);
		Color.set(Shaders.current, 1f, 1f, 1f, 1f);
	}

	/**
	 * Sets a new destination for the player
	 * @param position the vector position for the player's new destination
	 */
	public void move(Vector3f position) {
		if (position.getX() != this.x || position.getZ() != this.z) {
			targetX = position.x;
			targetZ = position.z;
			calculateSpeeds();
		}
	}

	public void calculateSpeeds() {
		Vector3f target = new Vector3f((float) (targetX - this.x), 0f, (float) (targetZ - this.z));
		if (target.getX() != 0 || target.getZ() != 0) {
			target.normalise();
			xa = target.getX() * moveSpeed;
			za = target.getZ() * moveSpeed;
		}
	}

	// Getters and setters
	public Vector3f getPosition() {
		return new Vector3f((float) this.x, (float) this.y, (float) this.z);
	}

	public void setTargetX(float x) {
		this.targetX = x;
		calculateSpeeds();
	}

	public void setTargetZ(float z) {
		this.targetZ = z;
		calculateSpeeds();
	}

	public void setTargetPosition(float x, float z) {
		this.targetX = x;
		this.targetZ = z;
		calculateSpeeds();
	}
	
	public float getMoveSpeed() {
		return moveSpeed;
	}
	
	public void setMoveSpeed(float moveSpeed) {
		this.moveSpeed = moveSpeed;
	}
}
