package com.doobs.exort.entity;

import com.doobs.exort.level.*;

public class MovingEntity extends Entity {
	protected double xa, ya, za;

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

	protected void tick(int delta) {
		this.x += xa * delta;
		this.y += ya * delta;
		this.z += za * delta;
		
		bb.move((float) x, (float) z);
	}

	protected void render() {

	}
	
	public void stop() {
		xa = 0;
		ya = 0;
		za = 0;
	}

	// Getters and setters
	public double getXA() {
		return xa;
	}

	public void setXA(double xa) {
		this.xa = xa;
	}

	public double getYA() {
		return ya;
	}

	public void setYA(double ya) {
		this.ya = ya;
	}

	public double getZA() {
		return za;
	}

	public void setZA(double za) {
		this.za = za;
	}
}
