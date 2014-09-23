package com.doobs.exort.util.gl;

import static org.lwjgl.input.Keyboard.KEY_LSHIFT;
import static org.lwjgl.input.Keyboard.KEY_SPACE;
import static org.lwjgl.input.Keyboard.isKeyDown;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.*;

import com.doobs.modern.util.matrix.*;

public class Camera {
	private float x, y, z, xa, ya, za;
	public double rotX, rotY, rotZ;

	private static float rotModifier = 75.0f;
	public static float moveSpeed = 0.0015f;
	private static float slowFactor = 0.7f;

	public Camera() {
		this(0, 0, 0, 0, 0, 0);
	}

	public Camera(float x, float y, float z) {
		this(0, 0, 0, x, y, z);
	}

	public Camera(float rotX, float rotY, float rotZ, float x, float y, float z) {
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void tick(int delta) {
		if (Mouse.isGrabbed()) {
			rotX -= Mouse.getDY() / rotModifier * delta;
			rotY += Mouse.getDX() / rotModifier * delta;
			if (rotX < -90)
				rotX = -90;
			else if (rotX > 90)
				rotX = 90;
			if (rotY <= 0)
				rotY += 360.0f;
			else if (rotY >= 360)
				rotY -= 360.0f;

			if ((Keyboard.isKeyDown(Keyboard.KEY_W) && !Keyboard.isKeyDown(Keyboard.KEY_S) && !Keyboard.isKeyDown(Keyboard.KEY_A) && !Keyboard
					.isKeyDown(Keyboard.KEY_D))
					|| (Keyboard.isKeyDown(Keyboard.KEY_W) && !Keyboard.isKeyDown(Keyboard.KEY_S) && Keyboard.isKeyDown(Keyboard.KEY_A) && Keyboard
							.isKeyDown(Keyboard.KEY_D))) { // W || W + A + D
				xa += (float) (Math.sin(Math.toRadians(rotY)) * moveSpeed * delta);
				za -= (float) (Math.cos(Math.toRadians(rotY)) * moveSpeed * delta);
			} else if ((!Keyboard.isKeyDown(Keyboard.KEY_W) && Keyboard.isKeyDown(Keyboard.KEY_S) && !Keyboard.isKeyDown(Keyboard.KEY_A) && !Keyboard
					.isKeyDown(Keyboard.KEY_D))
					|| (!Keyboard.isKeyDown(Keyboard.KEY_W) && Keyboard.isKeyDown(Keyboard.KEY_S) && Keyboard.isKeyDown(Keyboard.KEY_A) && Keyboard
							.isKeyDown(Keyboard.KEY_D))) { // S || S + A + D
				xa += (float) (Math.sin(Math.toRadians(rotY + 180.0f)) * moveSpeed * delta);
				za -= (float) (Math.cos(Math.toRadians(rotY + 180.0f)) * moveSpeed * delta);
			} else if (!Keyboard.isKeyDown(Keyboard.KEY_W) && !Keyboard.isKeyDown(Keyboard.KEY_S) && Keyboard.isKeyDown(Keyboard.KEY_A)
					&& !Keyboard.isKeyDown(Keyboard.KEY_D)) { // A
				xa += (float) (Math.sin(Math.toRadians(rotY - 90.0f)) * moveSpeed * delta);
				za -= (float) (Math.cos(Math.toRadians(rotY - 90.0f)) * moveSpeed * delta);
			} else if (!Keyboard.isKeyDown(Keyboard.KEY_W) && !Keyboard.isKeyDown(Keyboard.KEY_S) && !Keyboard.isKeyDown(Keyboard.KEY_A)
					&& Keyboard.isKeyDown(Keyboard.KEY_D)) { // D
				xa += (float) (Math.sin(Math.toRadians(rotY + 90.0f)) * moveSpeed * delta);
				za -= (float) (Math.cos(Math.toRadians(rotY + 90.0f)) * moveSpeed * delta);
			} else if (Keyboard.isKeyDown(Keyboard.KEY_W) && !Keyboard.isKeyDown(Keyboard.KEY_S) && Keyboard.isKeyDown(Keyboard.KEY_A)
					&& !Keyboard.isKeyDown(Keyboard.KEY_D)) { // W + A
				xa += (float) (Math.sin(Math.toRadians(rotY - 45.0f)) * moveSpeed * delta);
				za -= (float) (Math.cos(Math.toRadians(rotY - 45.0f)) * moveSpeed * delta);
			} else if (Keyboard.isKeyDown(Keyboard.KEY_W) && !Keyboard.isKeyDown(Keyboard.KEY_S) && !Keyboard.isKeyDown(Keyboard.KEY_A)
					&& Keyboard.isKeyDown(Keyboard.KEY_D)) { // W + D
				xa += (float) (Math.sin(Math.toRadians(rotY + 45.0f)) * moveSpeed * delta);
				za -= (float) (Math.cos(Math.toRadians(rotY + 45.0f)) * moveSpeed * delta);
			} else if (!Keyboard.isKeyDown(Keyboard.KEY_W) && Keyboard.isKeyDown(Keyboard.KEY_S) && Keyboard.isKeyDown(Keyboard.KEY_A)
					&& !Keyboard.isKeyDown(Keyboard.KEY_D)) { // S + A
				xa += (float) (Math.sin(Math.toRadians(rotY - 135.0f)) * moveSpeed * delta);
				za -= (float) (Math.cos(Math.toRadians(rotY - 135.0f)) * moveSpeed * delta);
			} else if (!Keyboard.isKeyDown(Keyboard.KEY_W) && Keyboard.isKeyDown(Keyboard.KEY_S) && !Keyboard.isKeyDown(Keyboard.KEY_A)
					&& Keyboard.isKeyDown(Keyboard.KEY_D)) { // S + D
				xa += (float) (Math.sin(Math.toRadians(rotY + 135.0f)) * moveSpeed * delta);
				za -= (float) (Math.cos(Math.toRadians(rotY + 135.0f)) * moveSpeed * delta);
			}

			if (isKeyDown(KEY_SPACE))
				ya += moveSpeed * delta;
			else if (isKeyDown(KEY_LSHIFT))
				ya -= moveSpeed * delta;
		}
		x += xa;
		y += ya;
		z += za;

		xa *= slowFactor;
		ya *= slowFactor;
		za *= slowFactor;
	}

	public void applyTransformations() {
		Matrices.rotate((float) -rotX, 1, 0, 0);
		Matrices.rotate((float) -rotY, 0, 1, 0);
		Matrices.rotate((float) -rotZ, 0, 0, 1);
		Matrices.translate(-x, -y, -z);
	}

	public void reset() {
		resetRotation();
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	public void resetRotation() {
		this.rotX = 0;
		this.rotY = 0;
		this.rotZ = 0;
	}

	// Getters and Setters
	public Vector3f getPosition() {
		return new Vector3f(x, y, z);
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public float getXA() {
		return xa;
	}

	public float getYA() {
		return ya;
	}

	public float getZA() {
		return za;
	}
}
