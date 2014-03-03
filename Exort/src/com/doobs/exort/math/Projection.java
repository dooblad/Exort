package com.doobs.exort.math;

public class Projection {
	private float min, max;

	public Projection(float min, float max) {
		this.min = min;
		this.max = max;
	}

	public boolean overlaps(Projection projection) {
		return max >= projection.getMin() && projection.getMax() >= min;
	}
	
	public void print() {
		System.out.println("Projection[" + min + ", " + max + "]");
	}

	// Getters and Setters
	public float getMin() {
		return min;
	}

	public float getMax() {
		return max;
	}
}
