package com.doobs.exort.math;

import org.lwjgl.util.vector.Vector3f;

import com.doobs.exort.util.Camera;

public class Ray {
	private Vector3f position, direction;

	public Ray() {

	}

	public Ray(Camera camera, Vector3f direction) {
		position = new Vector3f(camera.getX(), camera.getY(), camera.getZ());
		this.direction = direction;
	}

	public Ray(Vector3f position, Vector3f direction) {
		this.position = position;
		this.direction = direction;
	}

	// Getters and Setters
	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getDirection() {
		return direction;
	}
}
