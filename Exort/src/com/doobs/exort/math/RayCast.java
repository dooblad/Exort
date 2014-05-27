package com.doobs.exort.math;

import java.nio.*;

import org.lwjgl.*;
import org.lwjgl.input.*;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.*;

import com.doobs.exort.entity.creature.*;
import com.doobs.exort.util.*;

import static org.lwjgl.opengl.GL11.*;

public class RayCast {
	public static void movePlayer(Camera camera, Player player) {
		Vector3f plane;
		
		if (Mouse.isGrabbed())
			plane = RayCast.getPosition(Display.getWidth() / 2, Display.getHeight() / 2);
		else
			plane = RayCast.getPosition(Mouse.getX(), Mouse.getY());

		if (plane != null) {
			player.move(plane);
		}
	}

	public static Vector3f getPosition(int mouseX, int mouseY) {
		IntBuffer viewport = BufferUtils.createIntBuffer(16);
		FloatBuffer modelViewBuffer = BufferUtils.createFloatBuffer(16);
		FloatBuffer projectionBuffer = BufferUtils.createFloatBuffer(16);
		
		glGetFloat(GL_MODELVIEW_MATRIX, modelViewBuffer);
		glGetFloat(GL_PROJECTION_MATRIX, projectionBuffer);
		glGetInteger(GL_VIEWPORT, viewport);
		
		float[] modelView = GLTools.asFloatArray(modelViewBuffer);
		float[] projection = GLTools.asFloatArray(projectionBuffer);
		float[] a = new float[16];
		float[] in = new float[4];
		float[] out;
		Vector3f coords = new Vector3f();
		
		// Find inverse of ProjectionModelViewMatrix
		Math3D.matrixMultiply4(a, projection, modelView);
		a = Math3D.matrixInverse4(a);
		
		// Get the depth component of the mouse pixel
		FloatBuffer winZ = BufferUtils.createFloatBuffer(1);
		glReadPixels(mouseX, mouseY, 1, 1, GL_DEPTH_COMPONENT, GL_FLOAT, winZ);
		
		// Normalized device coordinate transformation
		in[0] = (mouseX - (float) viewport.get(0)) / (float) viewport.get(2) * 2f - 1f; 
		in[1] = (mouseY - (float) viewport.get(1)) / (float) viewport.get(3) * 2f - 1f;
		in[2] = 2f * winZ.get(0) - 1f;
		in[3] = 1f;
		
		out = Math3D.matrixMultiplyByVector4(a, in);
		
		if(out[3] == 0f)
			return null;
		
		out[3] = 1f / out[3];
		
		coords.setX(out[0] * out[3]);
		coords.setY(out[1] * out[3]);
		coords.setZ(out[2] * out[3]);
		
		return coords;
	}
}
