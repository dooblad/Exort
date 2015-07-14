package exort.util;

import org.lwjgl.input.*;

import com.doobs.modern.util.*;

import exort.entity.*;

/**
 * A Camera that attaches to a specified Entity.
 */
public class EntityCamera extends Camera {
	private static final float DEFAULT_DISTANCE = 10f;

	private InputHandler input;

	private Entity entity;
	private float distance;
	private float distanceSpeed;
	private static final float SLOWDOWN_FACTOR = 0.9f;

	public EntityCamera() {
		this(DEFAULT_DISTANCE, null, null);
	}

	public EntityCamera(float distance) {
		this(distance, null, null);
	}

	public EntityCamera(Entity entity) {
		this(DEFAULT_DISTANCE, entity);
	}

	public EntityCamera(float distance, Entity entity) {
		this(distance, entity, null);
	}

	public EntityCamera(float distance, InputHandler input) {
		this(distance, null, input);
	}

	public EntityCamera(float distance, Entity entity, InputHandler input) {
		this.distance = distance;
		this.entity = entity;
		this.input = input;
		this.distanceSpeed = 0f;
	}

	public void tick() {
		if (this.entity != null) {
			if(this.input != null) {
				if(this.input.isKeyDown(Keyboard.KEY_MINUS)) {
					this.distanceSpeed += 0.1f;
				} else if(this.input.isKeyDown(Keyboard.KEY_EQUALS)) {
					this.distanceSpeed -= 0.1f;
				}
			}

			this.x = entity.getX();
			this.y = distance * (float) Math.sin(Math.toRadians(this.rotX)) + entity.getY();
			this.z = distance * (float) Math.cos(Math.toRadians(this.rotX)) + entity.getZ();
		}
		
		this.distance += this.distanceSpeed;
		this.distanceSpeed *= SLOWDOWN_FACTOR;
	}

	public Entity getEntity() {
		return this.entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public void setInput(InputHandler input) {
		this.input = input;
	}
}
