package com.doobs.exort.util.sat;

public class Axis {
	private float x1, x2;
	private float z1, z2;
	
	public Axis(float x1, float z1, float x2, float z2) {
		this.x1 = x1;
		this.z1 = z1;
		this.x2 = x2;
		this.z2 = z2;
	}
	
	public Axis() {
		this(0, 0, 0, 0);
	}

	// Getters and setters
	public float getX1() {
		return x1;
	}

	public void setX1(float x1) {
		this.x1 = x1;
	}

	public float getX2() {
		return x2;
	}

	public void setX2(float x2) {
		this.x2 = x2;
	}

	public float getZ1() {
		return z1;
	}

	public void setZ1(float z1) {
		this.z1 = z1;
	}

	public float getZ2() {
		return z2;
	}

	public void setZ2(float z2) {
		this.z2 = z2;
	}
}
