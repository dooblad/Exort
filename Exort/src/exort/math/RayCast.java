package exort.math;

import static org.lwjgl.opengl.GL11.*;

import java.nio.*;

import org.lwjgl.*;
import org.lwjgl.input.*;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.*;

import com.doobs.modern.util.*;
import com.doobs.modern.util.matrix.*;

import exort.util.gl.*;

public class RayCast {
	// In world coordinates
	public static float mouseX, mouseZ;

	// Only call after rendering the level
	public static void tick(Camera camera) {
		Vector3f position;

		if (!Mouse.isGrabbed()) {
			position = getPosition(Mouse.getX(), Mouse.getY());
		} else {
			position = getPosition(Display.getWidth() / 2, Display.getHeight() / 2);
		}

		if (position != null) {
			mouseX = position.getX();
			mouseZ = position.getZ();
		}
	}

	public static Vector3f getPosition(int mouseX, int mouseY) {
		IntBuffer viewport = BufferUtils.createIntBuffer(16);
		glGetInteger(GL_VIEWPORT, viewport);

		float[] modelView = Matrices.transform.getModelViewMatrix();
		float[] projection = Matrices.perspective.getProjectionMatrix();
		float[] a = new float[16];
		float[] in = new float[4];
		float[] out;
		Vector3f coords = new Vector3f();

		// Find inverse of ProjectionModelViewMatrix
		Math3D.matrixMultiply4f(a, projection, modelView);
		a = Math3D.matrixInverse4f(a);

		// Get the depth component of the mouse pixel
		FloatBuffer winZ = BufferUtils.createFloatBuffer(1);
		glReadPixels(mouseX, mouseY, 1, 1, GL_DEPTH_COMPONENT, GL_FLOAT, winZ);

		// Normalized device coordinate transformation
		in[0] = (((mouseX - (float) viewport.get(0)) / viewport.get(2)) * 2f) - 1f;
		in[1] = (((mouseY - (float) viewport.get(1)) / viewport.get(3)) * 2f) - 1f;
		in[2] = (2f * winZ.get(0)) - 1f;
		in[3] = 1f;

		out = Math3D.matrixMultiplyByVector4f(a, in);

		if (out[3] == 0f) {
			return null;
		}

		out[3] = 1f / out[3];

		coords.setX(out[0] * out[3]);
		coords.setY(out[1] * out[3]);
		coords.setZ(out[2] * out[3]);

		return coords;
	}
}
