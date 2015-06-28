package exort.util.sat;

import org.lwjgl.util.vector.*;

/**
 * Data structure to contain information for a minimum translation vector.
 */
public class MTV {
	private Vector2f smallest;
	private double overlap;

	/**
	 * Creates an empty MTV.
	 */
	public MTV() {
		this(null, 0);
	}

	/**
	 * Post: Creates an MTV defined by the vector "smallest" with "overlap".
	 */
	public MTV(Vector2f smallest, double overlap) {
		this.smallest = smallest;
		this.overlap = overlap;
	}
	
	/**
	 * Post: Returns the vector for this MTV.
	 */
	public Vector2f getSmallest() {
		return this.smallest;
	}

	/**
	 * Post: Sets the vector for this MTV to "smallest".
	 */
	public void setSmallest(Vector2f smallest) {
		this.smallest = smallest;
	}

	/**
	 * Post: Returns the overlap between the two objects that define this MTV.
	 */
	public double getOverlap() {
		return this.overlap;
	}

	/**
	 * Post: Sets the overlap for this MTV to "overlap".
	 */
	public void setOverlap(double overlap) {
		this.overlap = overlap;
	}
}
