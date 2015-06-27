package com.doobs.exort.util.gl;

import static org.lwjgl.input.Keyboard.*;

import org.lwjgl.input.*;
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
			this.rotX -= (Mouse.getDY() / rotModifier) * delta;
			this.rotY += (Mouse.getDX() / rotModifier) * delta;
			if (this.rotX < -90) {
				this.rotX = -90;
			} else if (this.rotX > 90) {
				this.rotX = 90;
			}
			if (this.rotY <= 0) {
				this.rotY += 360.0f;
			} else if (this.rotY >= 360) {
				this.rotY -= 360.0f;
			}

			if ((Keyboard.isKeyDown(Keyboard.KEY_W) && !Keyboard.isKeyDown(Keyboard.KEY_S) && !Keyboard.isKeyDown(Keyboard.KEY_A) && !Keyboard
					.isKeyDown(Keyboard.KEY_D))
					|| (Keyboard.isKeyDown(Keyboard.KEY_W) && !Keyboard.isKeyDown(Keyboard.KEY_S) && Keyboard.isKeyDown(Keyboard.KEY_A) && Keyboard
							.isKeyDown(Keyboard.KEY_D))) { // W || W + A + D
				this.xa += (float) (Math.sin(Math.toRadians(this.rotY)) * moveSpeed * delta);
				this.za -= (float) (Math.cos(Math.toRadians(this.rotY)) * moveSpeed * delta);
			} else if ((!Keyboard.isKeyDown(Keyboard.KEY_W) && Keyboard.isKeyDown(Keyboard.KEY_S) && !Keyboard.isKeyDown(Keyboard.KEY_A) && !Keyboard
					.isKeyDown(Keyboard.KEY_D))
					|| (!Keyboard.isKeyDown(Keyboard.KEY_W) && Keyboard.isKeyDown(Keyboard.KEY_S) && Keyboard.isKeyDown(Keyboard.KEY_A) && Keyboard
							.isKeyDown(Keyboard.KEY_D))) { // S || S + A + D
				this.xa += (float) (Math.sin(Math.toRadians(this.rotY + 180.0f)) * moveSpeed * delta);
				this.za -= (float) (Math.cos(Math.toRadians(this.rotY + 180.0f)) * moveSpeed * delta);
			} else if (!Keyboard.isKeyDown(Keyboard.KEY_W) && !Keyboard.isKeyDown(Keyboard.KEY_S) && Keyboard.isKeyDown(Keyboard.KEY_A)
					&& !Keyboard.isKeyDown(Keyboard.KEY_D)) { // A
				this.xa += (float) (Math.sin(Math.toRadians(this.rotY - 90.0f)) * moveSpeed * delta);
				this.za -= (float) (Math.cos(Math.toRadians(this.rotY - 90.0f)) * moveSpeed * delta);
			} else if (!Keyboard.isKeyDown(Keyboard.KEY_W) && !Keyboard.isKeyDown(Keyboard.KEY_S) && !Keyboard.isKeyDown(Keyboard.KEY_A)
					&& Keyboard.isKeyDown(Keyboard.KEY_D)) { // D
				this.xa += (float) (Math.sin(Math.toRadians(this.rotY + 90.0f)) * moveSpeed * delta);
				this.za -= (float) (Math.cos(Math.toRadians(this.rotY + 90.0f)) * moveSpeed * delta);
			} else if (Keyboard.isKeyDown(Keyboard.KEY_W) && !Keyboard.isKeyDown(Keyboard.KEY_S) && Keyboard.isKeyDown(Keyboard.KEY_A)
					&& !Keyboard.isKeyDown(Keyboard.KEY_D)) { // W + A
				this.xa += (float) (Math.sin(Math.toRadians(this.rotY - 45.0f)) * moveSpeed * delta);
				this.za -= (float) (Math.cos(Math.toRadians(this.rotY - 45.0f)) * moveSpeed * delta);
			} else if (Keyboard.isKeyDown(Keyboard.KEY_W) && !Keyboard.isKeyDown(Keyboard.KEY_S) && !Keyboard.isKeyDown(Keyboard.KEY_A)
					&& Keyboard.isKeyDown(Keyboard.KEY_D)) { // W + D
				this.xa += (float) (Math.sin(Math.toRadians(this.rotY + 45.0f)) * moveSpeed * delta);
				this.za -= (float) (Math.cos(Math.toRadians(this.rotY + 45.0f)) * moveSpeed * delta);
			} else if (!Keyboard.isKeyDown(Keyboard.KEY_W) && Keyboard.isKeyDown(Keyboard.KEY_S) && Keyboard.isKeyDown(Keyboard.KEY_A)
					&& !Keyboard.isKeyDown(Keyboard.KEY_D)) { // S + A
				this.xa += (float) (Math.sin(Math.toRadians(this.rotY - 135.0f)) * moveSpeed * delta);
				this.za -= (float) (Math.cos(Math.toRadians(this.rotY - 135.0f)) * moveSpeed * delta);
			} else if (!Keyboard.isKeyDown(Keyboard.KEY_W) && Keyboard.isKeyDown(Keyboard.KEY_S) && !Keyboard.isKeyDown(Keyboard.KEY_A)
					&& Keyboard.isKeyDown(Keyboard.KEY_D)) { // S + D
				this.xa += (float) (Math.sin(Math.toRadians(this.rotY + 135.0f)) * moveSpeed * delta);
				this.za -= (float) (Math.cos(Math.toRadians(this.rotY + 135.0f)) * moveSpeed * delta);
			}

			if (isKeyDown(KEY_SPACE)) {
				this.ya += moveSpeed * delta;
			} else if (isKeyDown(KEY_LSHIFT)) {
				this.ya -= moveSpeed * delta;
			}
		}
		this.x += this.xa;
		this.y += this.ya;
		this.z += this.za;

		this.xa *= slowFactor;
		this.ya *= slowFactor;
		this.za *= slowFactor;
	}

	public void applyTransformations() {
		Matrices.rotate((float) -this.rotX, 1, 0, 0);
		Matrices.rotate((float) -this.rotY, 0, 1, 0);
		Matrices.rotate((float) -this.rotZ, 0, 0, 1);
		Matrices.translate(-this.x, -this.y, -this.z);
	}

	public void reset() {
		this.resetRotation();
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
		return new Vector3f(this.x, this.y, this.z);
	}

	public float getX() {
		return this.x;
	}

	public float getY() {
		return this.y;
	}

	public float getZ() {
		return this.z;
	}

	public float getXA() {
		return this.xa;
	}

	public float getYA() {
		return this.ya;
	}

	public float getZA() {
		return this.za;
	}
}
