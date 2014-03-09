package com.doobs.exort.entity;

import com.doobs.exort.level.*;

public class Entity {
	protected float x, y, z;

	protected Level level;

	public Entity() {
		this(0, 0, 0, null);
	}

	public Entity(int x, int z) {
		this(x, 0, z, null);
	}

	public Entity(int x, int y, int z) {
		this(x, y, z, null);
	}

	public Entity(int x, int z, Level level) {
		this(x, 0, z, level);
	}

	public Entity(int x, int y, int z, Level level) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.level = level;
	}

	// Getters and Setters
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}
}
