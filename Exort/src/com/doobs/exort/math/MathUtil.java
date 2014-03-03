package com.doobs.exort.math;

import java.nio.FloatBuffer;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class MathUtil {

	public static Vector3f perpendicular(Vector3f input) {
		return new Vector3f(-input.getY(), input.getX(), input.getZ());
	}
	
	public static Vector4f multByMatrix(Matrix4f matrix, Vector4f vector) {
		float x = (matrix.m00 * vector.getX()) + (matrix.m01 * vector.getY() + (matrix.m02 * vector.getZ()) + (matrix.m03 * vector.getW()));
		float y = (matrix.m10 * vector.getX()) + (matrix.m11 * vector.getY() + (matrix.m12 * vector.getZ()) + (matrix.m13 * vector.getW()));
		float z = (matrix.m20 * vector.getX()) + (matrix.m21 * vector.getY() + (matrix.m22 * vector.getZ()) + (matrix.m23 * vector.getW()));
		float w = (matrix.m30 * vector.getX()) + (matrix.m31 * vector.getY() + (matrix.m32 * vector.getZ()) + (matrix.m33 * vector.getW()));
		return new Vector4f(x, y, z, w);
	}
	
	public static void loadMatrix(Matrix4f matrix, FloatBuffer data) {
		matrix.m00 = data.get(0);
		matrix.m01 = data.get(1);
		matrix.m02 = data.get(2);
		matrix.m03 = data.get(3);
		matrix.m10 = data.get(4);
		matrix.m11 = data.get(5);
		matrix.m12 = data.get(6);
		matrix.m13 = data.get(7);
		matrix.m20 = data.get(8);
		matrix.m21 = data.get(9);
		matrix.m22 = data.get(10);
		matrix.m23 = data.get(11);
		matrix.m30 = data.get(12);
		matrix.m31 = data.get(13);
		matrix.m32 = data.get(14);
		matrix.m33 = data.get(15);
	}
	
	public static Matrix4f loadMatrix(FloatBuffer data) {
		Matrix4f result = new Matrix4f();
		result.m00 = data.get(0);
		result.m01 = data.get(1);
		result.m02 = data.get(2);
		result.m03 = data.get(3);
		result.m10 = data.get(4);
		result.m11 = data.get(5);
		result.m12 = data.get(6);
		result.m13 = data.get(7);
		result.m20 = data.get(8);
		result.m21 = data.get(9);
		result.m22 = data.get(10);
		result.m23 = data.get(11);
		result.m30 = data.get(12);
		result.m31 = data.get(13);
		result.m32 = data.get(14);
		result.m33 = data.get(15);
		return result;
	}
}
