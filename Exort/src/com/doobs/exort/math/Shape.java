package com.doobs.exort.math;

import org.lwjgl.util.vector.Vector3f;

public class Shape {
	private Vector3f[] vertices;

	public Shape(float... vertices) {
		this.vertices = new Vector3f[vertices.length / 3];
		for (int i = 0; i < vertices.length; i += 3) {
			this.vertices[i / 3] = new Vector3f(vertices[i], vertices[i + 1],
					vertices[i + 2]);
		}
	}

	// Getters and Setters
	public Vector3f getVertex(int vertex) {
		return vertices[vertex];
	}

	public Vector3f[] getVertices() {
		return vertices;
	}
}
