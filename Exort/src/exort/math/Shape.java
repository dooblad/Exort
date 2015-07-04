package exort.math;

import org.lwjgl.util.vector.*;

/**
 * Holds an arbitrary number of 3D vertices.
 */
public class Shape {
	private Vector3f[] vertices;

	/**
	 * Creates a Shape from "vertices" by converting every 3 entries into a Vector3f.
	 */
	public Shape(float... vertices) {
		this.vertices = new Vector3f[vertices.length / 3];
		for (int i = 0; i < vertices.length; i += 3) {
			this.vertices[i / 3] = new Vector3f(vertices[i], vertices[i + 1], vertices[i + 2]);
		}
	}

	/**
	 * Returns the Vector3f at "index".
	 */
	public Vector3f getVertex(int index) {
		return this.vertices[index];
	}

	/**
	 * Returns an array of all the vertices (Vector3f) in this Shape.
	 */
	public Vector3f[] getVertices() {
		return this.vertices;
	}
}
