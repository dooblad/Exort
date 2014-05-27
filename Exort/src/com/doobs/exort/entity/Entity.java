package com.doobs.exort.entity;

import com.doobs.exort.level.*;

public class Entity {
	protected double x, y, z;

	protected Level level;

	public Entity() {
		this(0, 0, 0, null);
	}

	public Entity(double x, double z) {
		this(x, 0, z, null);
	}

	public Entity(double x, double y, double z) {
		this(x, y, z, null);
	}

	public Entity(double x, double z, Level level) {
		this(x, 0, z, level);
	}

	public Entity(double x, double y, double z, Level level) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.level = level;
	}

	// Getters and Setters
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}
}
