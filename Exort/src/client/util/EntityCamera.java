package client.util;

import org.lwjgl.input.*;

import client.entity.*;

import com.doobs.modern.util.*;

/**
 * A Camera that attaches to a specified Entity.
 */
public class EntityCamera extends Camera {
	private static final float DEFAULT_DISTANCE = 10f;
	private static final float ACCELERATION = 0.2f;
	private static final float SLOWDOWN_FACTOR = 0.9f;
	// These are acceleration limits, not position limits.
	// For smoothing purposes.
	private static final float CLOSEST = 20f, FURTHEST = 30f;
	private static final float LOWEST = 7f, HIGHEST = 93f;

	private InputHandler input;

	private Entity entity;
	private float distance;
	private float distanceSpeed;
	private float rotationSpeed;
	// For preventing further input at the rotation limits.
	private boolean upLocked, downLocked;

	/**
	 * Creates an EntityCamera at distance {@value #DEFAULT_DISTANCE} with no Entity to
	 * attach to or InputHandler.
	 */
	public EntityCamera() {
		this(DEFAULT_DISTANCE, null, null);
	}

	/**
	 * Creates an EntityCamera at "distance" with no Entity to attach to or InputHandler.
	 */
	public EntityCamera(float distance) {
		this(distance, null, null);
	}

	/**
	 * Creates an EntityCamera at distance {@value #DEFAULT_DISTANCE} attached to "entity"
	 * with no InputHandler.
	 */
	public EntityCamera(Entity entity) {
		this(DEFAULT_DISTANCE, entity);
	}

	/**
	 * Creates an EntityCamera at "distance" attached to "entity" with no InputHandler.
	 */
	public EntityCamera(float distance, Entity entity) {
		this(distance, entity, null);
	}

	/**
	 * Creates an EntityCamera at "distance" with no Entity to attach to and InputHandler
	 * "input".
	 */
	public EntityCamera(float distance, InputHandler input) {
		this(distance, null, input);
	}

	/**
	 * Creates an EntityCamera at "distance" attached to "entity" and InputHandler
	 * "input".
	 */
	public EntityCamera(float distance, Entity entity, InputHandler input) {
		super();
		this.distance = distance;
		this.entity = entity;
		this.input = input;
		this.distanceSpeed = 0f;
		this.rotationSpeed = 0f;
		this.upLocked = false;
		this.downLocked = false;
	}

	/**
	 * Controls the behavior of this Entity Camera.
	 */
	public void tick() {
		if (this.entity != null) {
			if (this.input != null) {
				if ((this.distance < FURTHEST) && this.input.isKeyDown(Keyboard.KEY_MINUS)) {
					this.distanceSpeed += ACCELERATION;
				} else if ((this.distance > CLOSEST) && this.input.isKeyDown(Keyboard.KEY_EQUALS)) {
					this.distanceSpeed -= ACCELERATION;
				}

				if (this.input.isKeyDown(Keyboard.KEY_UP)) {
					if (!this.upLocked) {
						this.rotationSpeed += ACCELERATION;
					}
				} else {
					this.upLocked = false;
				}

				if (this.input.isKeyDown(Keyboard.KEY_DOWN)) {
					if (!this.downLocked) {
						this.rotationSpeed -= ACCELERATION;
					}
				} else {
					this.downLocked = false;
				}
			}

			this.x = this.entity.getX();
			this.y = (this.distance * (float) Math.sin(Math.toRadians(this.rotX)));
			this.z = (this.distance * (float) Math.cos(Math.toRadians(this.rotX))) + this.entity.getZ();
		}

		this.distance += this.distanceSpeed;
		this.distanceSpeed *= SLOWDOWN_FACTOR;

		this.rotX += this.rotationSpeed;
		this.rotationSpeed *= SLOWDOWN_FACTOR;
		if (this.rotX < LOWEST) {
			this.downLocked = true;
			this.rotationSpeed += ACCELERATION * 1.1f;
		} else if (this.rotX > HIGHEST) {
			this.upLocked = true;
			this.rotationSpeed -= ACCELERATION * 1.1f;
		}
	}

	/**
	 * Returns the Entity this EntityCamera is attached to.
	 */
	public Entity getEntity() {
		return this.entity;
	}

	/**
	 * Attaches this EntityCamera to "entity"
	 */
	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	/**
	 * Makes this EntityCamera receive commands from "input".
	 */
	public void setInput(InputHandler input) {
		this.input = input;
	}
}