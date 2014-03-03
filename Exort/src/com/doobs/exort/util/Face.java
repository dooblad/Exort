package com.doobs.exort.util;

import org.lwjgl.util.vector.Vector3f;

public class Face {
	private Vector3f normal, vertex;

	public Face(Vector3f vertex, Vector3f normal) {
		this.normal = normal;
		this.vertex = vertex;
	}

	public Vector3f getNormal() {
		return normal;
	}

	public Vector3f getVertex() {
		return vertex;
	}
}
