package shared.util;

public class Vector2f {
	public float x, z;

	/**
	 * Creates a Vector2f with entries (0, 0).
	 */
	public Vector2f() {
		this(0, 0);
	}

	/**
	 * Creates a Vector2f with entries ("x", "z").
	 */
	public Vector2f(float x, float z) {
		this.x = x;
		this.z = z;
	}

	/**
	 * Scales each component of this Vector2f such that the length is 1.
	 */
	public void normalize() {
		float length = (float) Math.sqrt((this.x * this.x) + (this.z * this.z));
		this.x /= length;
		this.z /= length;
	}

	/**
	 * Returns the dot product of this Vector2f and "other".
	 */
	public float dot(Vector2f other) {
		return (this.x * other.x) + (this.z * other.z);
	}

	/**
	 * Returns a Vector2f that represents the subtraction of this Vector2f and "other".
	 */
	public Vector2f subtract(Vector2f other) {
		return new Vector2f(this.x - other.x, this.z - other.z);
	}

	/**
	 * Returns the x-component of this Vector2f.
	 */
	public float getX() {
		return this.x;
	}

	/**
	 * Sets the x-component of this Vector2f to "x".
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Returns the z-component of this Vector2f.
	 */
	public float getZ() {
		return this.z;
	}

	/**
	 * Sets the z-component of this Vector2f to "z".
	 */
	public void setZ(float z) {
		this.z = z;
	}

	/**
	 * Sets the entries of this Vector2f to ("x", "z").
	 */
	public void set(float x, float z) {
		this.x = x;
		this.z = z;
	}
}