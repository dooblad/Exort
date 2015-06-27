package exort.entity;

import exort.level.*;

public class MovingEntity extends Entity {
	public double xa, ya, za;
	protected float direction;

	public MovingEntity() {
		this(0, 0, 0, null);
	}

	public MovingEntity(double x, double z) {
		this(x, 0, z, null);
	}

	public MovingEntity(double x, double y, double z) {
		this(x, y, z, null);
	}

	public MovingEntity(double x, double z, Level level) {
		this(x, 0, z, level);
	}

	public MovingEntity(double x, double y, double z, Level level) {
		super(x, y, z, level);
	}

	public MovingEntity(double x, double y, double z, double xa, double ya, double za, Level level) {
		super(x, y, z, level);
		this.xa = xa;
		this.ya = ya;
		this.za = za;
	}

	public void tick(int delta) {
		this.x += this.xa * delta;
		this.y += this.ya * delta;
		this.z += this.za * delta;

		this.bb.move((float) this.x, (float) this.z);
	}

	public void render() {

	}

	public void stop() {
		this.xa = 0;
		this.ya = 0;
		this.za = 0;
	}

	// Getters and setters
	public double getXA() {
		return this.xa;
	}

	public void setXA(double xa) {
		this.xa = xa;
	}

	public double getYA() {
		return this.ya;
	}

	public void setYA(double ya) {
		this.ya = ya;
	}

	public double getZA() {
		return this.za;
	}

	public void setZA(double za) {
		this.za = za;
	}

	public float getDirection() {
		return this.direction;
	}

	public void setDirection(float direction) {
		this.direction = direction;
	}
}
