package exort.util.sat;

import org.lwjgl.util.vector.*;

public class MTV {
	private Vector2f smallest;
	private double overlap;

	public MTV(Vector2f smallest, double overlap) {
		this.smallest = smallest;
		this.overlap = overlap;

		if (smallest != null) {
			if (Math.abs(smallest.x) > 2000000000) {
				smallest.x = 0;
			}
			if (Math.abs(smallest.y) > 2000000000) {
				smallest.y = 0;
			}
		}
	}

	public MTV() {
		this(null, 0);
	}

	// Getters and setters
	public Vector2f getSmallest() {
		return this.smallest;
	}

	public void setSmallest(Vector2f smallest) {
		this.smallest = smallest;
	}

	public double getOverlap() {
		return this.overlap;
	}

	public void setOverlap(double overlap) {
		this.overlap = overlap;
	}
}
