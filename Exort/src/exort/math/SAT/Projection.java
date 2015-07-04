package exort.math.SAT;

/**
 * Represents the Projection of a two-dimensional object onto a one-dimensional surface.
 */
public class Projection {
	// The two bounds of the Projection.
	private float min, max;

	/**
	 * Creates a Projection with the bounds "min" and "max".
	 */
	public Projection(float min, float max) {
		this.min = min;
		this.max = max;
	}

	/**
	 * Returns true if this Projection overlaps "projection".
	 */
	public boolean overlaps(Projection projection) {
		return (this.max >= projection.getMin()) && (projection.getMax() >= this.min);
	}

	/**
	 * Returns a String representation of this Projection.
	 */
	public String toString() {
		return "Projection[" + this.min + ", " + this.max + "]";
	}

	/**
	 * Returns the lower bound of this Projection.
	 */
	public float getMin() {
		return this.min;
	}

	/**
	 * Returns the upper bound of this Projection.
	 */
	public float getMax() {
		return this.max;
	}
}
