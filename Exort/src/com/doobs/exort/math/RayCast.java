package com.doobs.exort.math;

import static org.lwjgl.opengl.GL11.*;

import java.nio.*;

import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.*;

import com.doobs.exort.util.*;

public class RayCast {
	public static Vector3f getDirection(int mouseX, int mouseY) {
		// Convert mouse coordinates to normalised device coordinates
		Vector4f rayClip = new Vector4f((2.0f * mouseX) / Display.getWidth() - 1.0f, 
				1.0f - (2.0f * mouseY) / Display.getHeight(), -1.0f, 1.0f);
		
		// Store the projection matrix in a FloatBuffer
		FloatBuffer projectionTemp = BufferUtils.createFloatBuffer(16);
		glGetFloat(GL_PROJECTION_MATRIX, projectionTemp);
		
		// Load the FloatBuffer into a Matrix4f object
		Matrix4f projection = MathUtil.loadMatrix(projectionTemp);
		projection.invert();
		
		// Multiply the clipping ray by the inverted projection matrix 
		Vector4f rayEye = MathUtil.multByMatrix(projection, rayClip);
		// Make the eye ray face away from the camera
		rayEye.setZ(-1.0f);
		// Specify this vector to be a direction and not a position
		rayEye.setW(0.0f);
		
		// Store the modelview matrix in a FloatBuffer
		FloatBuffer modelViewTemp = BufferUtils.createFloatBuffer(16);
		glGetFloat(GL_MODELVIEW_MATRIX, modelViewTemp);
		
		// Load the FloatBuffer into a Matrix4f object
		Matrix4f modelView = MathUtil.loadMatrix(modelViewTemp);
		modelView.invert();
		
		// Multiply the eye ray by the modelview matrix
		Vector4f temp = MathUtil.multByMatrix(modelView, rayEye);
		
		// Return the normalised world space vector
		return (Vector3f) (new Vector3f(-temp.getX(), -temp.getY(), temp.getZ()).normalise());
	}
	
	public static Vector3f findPlane(Camera camera, Ray ray) {
		Vector3f direction = ray.getDirection();
		Vector3f position = ray.getPosition();
		
		if(direction.getY() < 0) { // Make sure the direction vector is actually pointed towards the ground plane
			// Determine how many y-component direction lengths it takes to reach the ground plane
			float factor = position.getY() / direction.getY();
			
			// Use this factor to determine how far the x and z components travel
			float deltaX = direction.getX() * factor;
			float deltaZ = direction.getZ()	* factor;
			
			float x = position.getX() + deltaX;
			float z = position.getZ() + deltaZ;
			
			// Make y = 0 so the player is always on the ground plane
			return new Vector3f(x, 0, z);
		}
		
		return null;
	}
}
