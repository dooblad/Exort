package exort.entity;

import exort.level.*;
import exort.util.sat.*;

public class Entity {
	protected BB bb;

	public double x, y, z;

	protected Level level;

	protected boolean removed;

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
		this.removed = false;
	}

	public void tick(int delta) {

	}

	public void render() {

	}

	public void remove() {
		this.removed = true;
	}

	// Getters and Setters
	public BB getBB() {
		return this.bb;
	}

	public void setBB(BB bb) {
		this.bb = bb;
	}

	public double getX() {
		return this.x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return this.y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return this.z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public boolean isRemoved() {
		return this.removed;
	}
}
