package com.doobs.exort.math;

public class Projection {
	private float min, max;

	public Projection(float min, float max) {
		this.min = min;
		this.max = max;
	}

	public boolean overlaps(Projection projection) {
		return (this.max >= projection.getMin()) && (projection.getMax() >= this.min);
	}

	public void print() {
		System.out.println("Projection[" + this.min + ", " + this.max + "]");
	}

	// Getters and Setters
	public float getMin() {
		return this.min;
	}

	public float getMax() {
		return this.max;
	}
}
