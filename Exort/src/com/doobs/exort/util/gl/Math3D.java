package com.doobs.exort.util.gl;

/**
 * 
 * @author andreban
 */
public class Math3D {
	private static final float[] IDENTITY_3x3_F = { 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f };

	private static final double[] IDENTITY_3x3_D = { 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0 };

	private static final float[] IDENTITY_4x4_F = { 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f };

	private static final double[] IDENTITY_4x4_D = { 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0 };

	public static void loadIdentity3f(float[] matrix) {
		if (matrix.length != 9)
			throw new IllegalArgumentException("Matrix must be at least 3x3");
		System.arraycopy(IDENTITY_3x3_F, 0, matrix, 0, 9);
	}

	public static void loadIdentity3d(double[] matrix) {
		if (matrix.length != 9)
			throw new IllegalArgumentException("Matrix must be at least 3x3");
		System.arraycopy(IDENTITY_3x3_D, 0, matrix, 0, 9);
	}

	public static void loadIdentity4f(float[] matrix) {
		if (matrix.length != 16)
			throw new IllegalArgumentException("Matrix must be at least 4x4");
		System.arraycopy(IDENTITY_4x4_F, 0, matrix, 0, 16);
	}

	public static void loadIdentity4d(double[] matrix) {
		if (matrix.length != 16)
			throw new IllegalArgumentException("Matrix must be at least 4x4");
		System.arraycopy(IDENTITY_4x4_D, 0, matrix, 0, 16);
	}

	/**
	 * Returns the squared distance between two points.
	 * 
	 * @param u
	 * @param v
	 * @return
	 */
	public static float getDistanceSquared3(float[] u, float[] v) {
		if (u.length != 3) {
			throw new IllegalArgumentException("vector must have 3 pos");
		}
		if (v.length != 3) {
			throw new IllegalArgumentException("vector must have 3 pos");
		}

		float x = u[0] - v[0];
		x = x * x;

		float y = u[1] - v[1];
		y = y * y;

		float z = u[2] - v[2];
		z = z * z;

		return (x + y + z);
	}

	/**
	 * Returns the squared distance between two points.
	 * 
	 * @param u
	 * @param v
	 * @return
	 */
	public static double getDistanceSquared3(double[] u, double[] v) {
		if (u.length != 3) {
			throw new IllegalArgumentException("vector must have 3 pos");
		}
		if (v.length != 3) {
			throw new IllegalArgumentException("vector must have 3 pos");
		}

		double x = u[0] - v[0];
		x = x * x;

		double y = u[1] - v[1];
		y = y * y;

		double z = u[2] - v[2];
		z = z * z;

		return (x + y + z);
	}

	public static void translate4(float[] matrix, float x, float y, float z) {
		loadIdentity4f(matrix);
		matrix[12] = x;
		matrix[13] = y;
		matrix[14] = z;
	}

	public static void ortho(float[] mProjection, float xMin, float xMax, float yMin, float yMax, float zMin, float zMax) {
		loadIdentity4f(mProjection);

		mProjection[0] = 2.0f / (xMax - xMin);
		mProjection[5] = 2.0f / (yMax - yMin);
		mProjection[10] = -2.0f / (zMax - zMin);
		mProjection[12] = -((xMax + xMin) / (xMax - xMin));
		mProjection[13] = -((yMax + yMin) / (yMax - yMin));
		mProjection[14] = -((zMax + zMin) / (zMax - zMin));
		mProjection[15] = 1.0f;
	}

	public static void matrixMultiply3(float[] product, final float[] a, final float[] b) {
		for (int i = 0; i < 3; i++) {
			float ai0 = a[(0 * 3) + i], ai1 = a[(1 * 3) + i], ai2 = a[(2 * 3) + i];
			product[(0 * 3) + i] = ai0 * b[(0 * 3) + 0] + ai1 * b[(0 * 3) + 1] + ai2 * b[(0 * 3) + 2];
			product[(1 * 3) + i] = ai0 * b[(1 * 3) + 0] + ai1 * b[(1 * 3) + 1] + ai2 * b[(1 * 3) + 2];
			product[(2 * 3) + i] = ai0 * b[(2 * 3) + 0] + ai1 * b[(2 * 3) + 1] + ai2 * b[(2 * 3) + 2];
		}
	}

	/**
	 * Multiplies two 4x4 matrices.
	 */
	public static void matrixMultiply4(float[] product, final float[] a, final float[] b) {
		for (int i = 0; i < 4; i++) {
			float ai0 = a[(0 << 2) + i], ai1 = a[(1 << 2) + i], ai2 = a[(2 << 2) + i], ai3 = a[(3 << 2) + i];
			product[(0 << 2) + i] = ai0 * b[(0 << 2) + 0] + ai1 * b[(0 << 2) + 1] + ai2 * b[(0 << 2) + 2] + ai3 * b[(0 << 2) + 3];
			product[(1 << 2) + i] = ai0 * b[(1 << 2) + 0] + ai1 * b[(1 << 2) + 1] + ai2 * b[(1 << 2) + 2] + ai3 * b[(1 << 2) + 3];
			product[(2 << 2) + i] = ai0 * b[(2 << 2) + 0] + ai1 * b[(2 << 2) + 1] + ai2 * b[(2 << 2) + 2] + ai3 * b[(2 << 2) + 3];
			product[(3 << 2) + i] = ai0 * b[(3 << 2) + 0] + ai1 * b[(3 << 2) + 1] + ai2 * b[(3 << 2) + 2] + ai3 * b[(3 << 2) + 3];
		}
	}

	/**
	 * @param a
	 *            The matrix
	 * @param b
	 *            The vector
	 * @return The resultant vector
	 */
	public static float[] matrixMultiplyByVector4(float[] a, float[] b) {
		float[] result = new float[4];

		result[0] = a[0] * b[0] + a[4] * b[1] + a[8] * b[2] + a[12] * b[3];
		result[1] = a[1] * b[0] + a[5] * b[1] + a[9] * b[2] + a[13] * b[3];
		result[2] = a[2] * b[0] + a[6] * b[1] + a[10] * b[2] + a[14] * b[3];
		result[3] = a[3] * b[0] + a[7] * b[1] + a[11] * b[2] + a[15] * b[3];

		return result;
	}

	/**
	 * Calculates the inverse of a 4x4 matrix.
	 * 
	 * @param result
	 *            The resultant matrix
	 * @param m
	 *            The matrix to be inverted
	 */
	public static boolean matrixInverse4(float[] result, float[] m) {
		float[] tempInverse = new float[16];
		float det;

		tempInverse[0] = m[5] * m[10] * m[15] - m[5] * m[11] * m[14] - m[9] * m[6] * m[15] + m[9] * m[7] * m[14] + m[13] * m[6] * m[11] - m[13] * m[7] * m[10];

		tempInverse[4] = -m[4] * m[10] * m[15] + m[4] * m[11] * m[14] + m[8] * m[6] * m[15] - m[8] * m[7] * m[14] - m[12] * m[6] * m[11] + m[12] * m[7] * m[10];

		tempInverse[8] = m[4] * m[9] * m[15] - m[4] * m[11] * m[13] - m[8] * m[5] * m[15] + m[8] * m[7] * m[13] + m[12] * m[5] * m[11] - m[12] * m[7] * m[9];

		tempInverse[12] = -m[4] * m[9] * m[14] + m[4] * m[10] * m[13] + m[8] * m[5] * m[14] - m[8] * m[6] * m[13] - m[12] * m[5] * m[10] + m[12] * m[6] * m[9];

		tempInverse[1] = -m[1] * m[10] * m[15] + m[1] * m[11] * m[14] + m[9] * m[2] * m[15] - m[9] * m[3] * m[14] - m[13] * m[2] * m[11] + m[13] * m[3] * m[10];

		tempInverse[5] = m[0] * m[10] * m[15] - m[0] * m[11] * m[14] - m[8] * m[2] * m[15] + m[8] * m[3] * m[14] + m[12] * m[2] * m[11] - m[12] * m[3] * m[10];

		tempInverse[9] = -m[0] * m[9] * m[15] + m[0] * m[11] * m[13] + m[8] * m[1] * m[15] - m[8] * m[3] * m[13] - m[12] * m[1] * m[11] + m[12] * m[3] * m[9];

		tempInverse[13] = m[0] * m[9] * m[14] - m[0] * m[10] * m[13] - m[8] * m[1] * m[14] + m[8] * m[2] * m[13] + m[12] * m[1] * m[10] - m[12] * m[2] * m[9];

		tempInverse[2] = m[1] * m[6] * m[15] - m[1] * m[7] * m[14] - m[5] * m[2] * m[15] + m[5] * m[3] * m[14] + m[13] * m[2] * m[7] - m[13] * m[3] * m[6];

		tempInverse[6] = -m[0] * m[6] * m[15] + m[0] * m[7] * m[14] + m[4] * m[2] * m[15] - m[4] * m[3] * m[14] - m[12] * m[2] * m[7] + m[12] * m[3] * m[6];

		tempInverse[10] = m[0] * m[5] * m[15] - m[0] * m[7] * m[13] - m[4] * m[1] * m[15] + m[4] * m[3] * m[13] + m[12] * m[1] * m[7] - m[12] * m[3] * m[5];

		tempInverse[14] = -m[0] * m[5] * m[14] + m[0] * m[6] * m[13] + m[4] * m[1] * m[14] - m[4] * m[2] * m[13] - m[12] * m[1] * m[6] + m[12] * m[2] * m[5];

		tempInverse[3] = -m[1] * m[6] * m[11] + m[1] * m[7] * m[10] + m[5] * m[2] * m[11] - m[5] * m[3] * m[10] - m[9] * m[2] * m[7] + m[9] * m[3] * m[6];

		tempInverse[7] = m[0] * m[6] * m[11] - m[0] * m[7] * m[10] - m[4] * m[2] * m[11] + m[4] * m[3] * m[10] + m[8] * m[2] * m[7] - m[8] * m[3] * m[6];

		tempInverse[11] = -m[0] * m[5] * m[11] + m[0] * m[7] * m[9] + m[4] * m[1] * m[11] - m[4] * m[3] * m[9] - m[8] * m[1] * m[7] + m[8] * m[3] * m[5];

		tempInverse[15] = m[0] * m[5] * m[10] - m[0] * m[6] * m[9] - m[4] * m[1] * m[10] + m[4] * m[2] * m[9] + m[8] * m[1] * m[6] - m[8] * m[2] * m[5];

		det = m[0] * tempInverse[0] + m[1] * tempInverse[4] + m[2] * tempInverse[8] + m[3] * tempInverse[12];

		if (det == 0)
			return false;

		det = 1f / det;

		float[] inverse = new float[16];

		for (int i = 0; i < 16; i++)
			inverse[i] = tempInverse[i] * det;

		return true;
	}

	/**
	 * Calculates the inverse of a 4x4 matrix.
	 * 
	 * @param result
	 *            The resultant matrix
	 * @param m
	 *            The matrix to be inverted
	 */
	public static float[] matrixInverse4(float[] m) {
		float[] tempInverse = new float[16];
		float det;

		tempInverse[0] = m[5] * m[10] * m[15] - m[5] * m[11] * m[14] - m[9] * m[6] * m[15] + m[9] * m[7] * m[14] + m[13] * m[6] * m[11] - m[13] * m[7] * m[10];

		tempInverse[4] = -m[4] * m[10] * m[15] + m[4] * m[11] * m[14] + m[8] * m[6] * m[15] - m[8] * m[7] * m[14] - m[12] * m[6] * m[11] + m[12] * m[7] * m[10];

		tempInverse[8] = m[4] * m[9] * m[15] - m[4] * m[11] * m[13] - m[8] * m[5] * m[15] + m[8] * m[7] * m[13] + m[12] * m[5] * m[11] - m[12] * m[7] * m[9];

		tempInverse[12] = -m[4] * m[9] * m[14] + m[4] * m[10] * m[13] + m[8] * m[5] * m[14] - m[8] * m[6] * m[13] - m[12] * m[5] * m[10] + m[12] * m[6] * m[9];

		tempInverse[1] = -m[1] * m[10] * m[15] + m[1] * m[11] * m[14] + m[9] * m[2] * m[15] - m[9] * m[3] * m[14] - m[13] * m[2] * m[11] + m[13] * m[3] * m[10];

		tempInverse[5] = m[0] * m[10] * m[15] - m[0] * m[11] * m[14] - m[8] * m[2] * m[15] + m[8] * m[3] * m[14] + m[12] * m[2] * m[11] - m[12] * m[3] * m[10];

		tempInverse[9] = -m[0] * m[9] * m[15] + m[0] * m[11] * m[13] + m[8] * m[1] * m[15] - m[8] * m[3] * m[13] - m[12] * m[1] * m[11] + m[12] * m[3] * m[9];

		tempInverse[13] = m[0] * m[9] * m[14] - m[0] * m[10] * m[13] - m[8] * m[1] * m[14] + m[8] * m[2] * m[13] + m[12] * m[1] * m[10] - m[12] * m[2] * m[9];

		tempInverse[2] = m[1] * m[6] * m[15] - m[1] * m[7] * m[14] - m[5] * m[2] * m[15] + m[5] * m[3] * m[14] + m[13] * m[2] * m[7] - m[13] * m[3] * m[6];

		tempInverse[6] = -m[0] * m[6] * m[15] + m[0] * m[7] * m[14] + m[4] * m[2] * m[15] - m[4] * m[3] * m[14] - m[12] * m[2] * m[7] + m[12] * m[3] * m[6];

		tempInverse[10] = m[0] * m[5] * m[15] - m[0] * m[7] * m[13] - m[4] * m[1] * m[15] + m[4] * m[3] * m[13] + m[12] * m[1] * m[7] - m[12] * m[3] * m[5];

		tempInverse[14] = -m[0] * m[5] * m[14] + m[0] * m[6] * m[13] + m[4] * m[1] * m[14] - m[4] * m[2] * m[13] - m[12] * m[1] * m[6] + m[12] * m[2] * m[5];

		tempInverse[3] = -m[1] * m[6] * m[11] + m[1] * m[7] * m[10] + m[5] * m[2] * m[11] - m[5] * m[3] * m[10] - m[9] * m[2] * m[7] + m[9] * m[3] * m[6];

		tempInverse[7] = m[0] * m[6] * m[11] - m[0] * m[7] * m[10] - m[4] * m[2] * m[11] + m[4] * m[3] * m[10] + m[8] * m[2] * m[7] - m[8] * m[3] * m[6];

		tempInverse[11] = -m[0] * m[5] * m[11] + m[0] * m[7] * m[9] + m[4] * m[1] * m[11] - m[4] * m[3] * m[9] - m[8] * m[1] * m[7] + m[8] * m[3] * m[5];

		tempInverse[15] = m[0] * m[5] * m[10] - m[0] * m[6] * m[9] - m[4] * m[1] * m[10] + m[4] * m[2] * m[9] + m[8] * m[1] * m[6] - m[8] * m[2] * m[5];

		det = m[0] * tempInverse[0] + m[1] * tempInverse[4] + m[2] * tempInverse[8] + m[3] * tempInverse[12];

		if (det == 0)
			return null;

		det = 1f / det;

		float[] inverse = new float[16];

		for (int i = 0; i < 16; i++)
			inverse[i] = tempInverse[i] * det;

		return inverse;
	}

	/**
	 * 
	 * @param m
	 *            Resultant matrix
	 * @param angle
	 *            angle in degrees
	 * @param x
	 *            rotation through x axis
	 * @param y
	 *            rotation through y axis
	 * @param z
	 *            rotation through z axis
	 */
	public static void rotate4(float[] m, float angle, float x, float y, float z) {
		float mag, s, c;
		float xx, yy, zz, xy, yz, zx, xs, ys, zs, one_c;

		angle = (float) Math.toRadians(angle);

		s = (float) Math.sin(angle);
		c = (float) Math.cos(angle);

		mag = (float) Math.sqrt(x * x + y * y + z * z);

		// Identity matrix
		if (mag == 0.0f) {
			loadIdentity4f(m);
			return;
		}

		// Rotation matrix is normalized
		x /= mag;
		y /= mag;
		z /= mag;

		xx = x * x;
		yy = y * y;
		zz = z * z;
		xy = x * y;
		yz = y * z;
		zx = z * x;
		xs = x * s;
		ys = y * s;
		zs = z * s;
		one_c = 1.0f - c;

		// m[col*4+row]
		m[0 * 4 + 0] = (one_c * xx) + c;
		m[0 * 4 + 1] = (one_c * xy) - zs;
		m[0 * 4 + 2] = (one_c * zx) + ys;
		m[0 * 4 + 3] = 0.0f;

		m[1 * 4 + 0] = (one_c * xy) + zs;
		m[1 * 4 + 1] = (one_c * yy) + c;
		m[1 * 4 + 2] = (one_c * yz) - xs;
		m[1 * 4 + 3] = 0.0f;

		m[2 * 4 + 0] = (one_c * zx) - ys;
		m[2 * 4 + 1] = (one_c * yz) + xs;
		m[2 * 4 + 2] = (one_c * zz) + c;
		m[2 * 4 + 3] = 0.0f;

		m[3 * 4 + 0] = 0.0f;
		m[3 * 4 + 1] = 0.0f;
		m[3 * 4 + 2] = 0.0f;
		m[3 * 4 + 3] = 1.0f;
	}

	public static void printMatrix4(float[] product) {
		for (int i = 0; i < 4; i++) {
			System.out.println(product[(0 << 2) + i] + ", " + product[(1 << 2) + i] + ", " + product[(2 << 2) + i] + ", " + product[(3 << 2) + i]);
		}
	}

	public static float getVectorLengthSquared3(float[] vec) {
		return getVectorLengthSquared3(vec, 0);
	}

	public static float getVectorLengthSquared3(float[] vec, int start) {
		return (vec[start] * vec[start]) + (vec[start + 1] * vec[start + 1]) + (vec[start + 2] * vec[start + 2]);
	}

	public static void scaleVector3(float[] vec, float scale, int start) {
		vec[start] *= scale;
		vec[start + 1] *= scale;
		vec[start + 2] *= scale;
	}

	public static void scaleVector3(float[] vec, float scale) {
		scaleVector3(vec, scale, 0);
	}

	public static float getVectorLength3(float[] vec, int start) {
		return (float) Math.sqrt(getVectorLengthSquared3(vec, start));
	}

	public static float getVectorLength3(float[] vec) {
		return (float) Math.sqrt(getVectorLengthSquared3(vec));
	}

	public static void normalizeVector3(float[] vec, int start) {
		scaleVector3(vec, 1.0f / getVectorLength3(vec, start), start);
	}

	public static void normalizeVector3(float[] vec) {
		normalizeVector3(vec, 0);
	}

	public static void crossProduct3(float[] result, float[] u, float[] v) {
		result[0] = u[1] * v[2] - v[1] * u[2];
		result[1] = -u[0] * v[2] + v[0] * u[2];
		result[2] = u[0] * v[1] - v[0] * u[1];
	}

	public static void crossProduct3(double[] result, double[] u, double[] v) {
		result[0] = u[1] * v[2] - v[1] * u[2];
		result[1] = -u[0] * v[2] + v[0] * u[2];
		result[2] = u[0] * v[1] - v[0] * u[1];
	}

	public static float dotProduct3(float[] u, float[] v) {
		return u[0] * v[0] + u[1] * v[1] + u[2] * v[2];
	}

	public static double dotProduct3(double[] u, double[] v) {
		return u[0] * v[0] + u[1] * v[1] + u[2] * v[2];
	}

	public static void extractRotationMatrix3(float[] result, float[] matrix) {
		System.arraycopy(matrix, 0, result, 0, 3);
		System.arraycopy(matrix, 4, result, 3, 3);
		System.arraycopy(matrix, 8, result, 6, 3);
	}

	public static void extractRotationMatrix3(double[] result, double[] matrix) {
		System.arraycopy(matrix, 0, result, 0, 3);
		System.arraycopy(matrix, 4, result, 3, 3);
		System.arraycopy(matrix, 8, result, 6, 3);
	}
}
