package com.doobs.exort.math;

import java.nio.*;

import org.lwjgl.util.vector.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

public class RayCast {
	public static Vector3f getDirection(int mouseX, int mouseY) {
		IntBuffer viewport = ByteBuffer.allocateDirect((Integer.SIZE/8)*16).order(ByteOrder.nativeOrder()).asIntBuffer();
        FloatBuffer modelview = ByteBuffer.allocateDirect((Float.SIZE/8)*16).order(ByteOrder.nativeOrder()).asFloatBuffer();
        FloatBuffer projection = ByteBuffer.allocateDirect((Float.SIZE/8)*16).order(ByteOrder.nativeOrder()).asFloatBuffer();
        FloatBuffer pickingRayBuffer = ByteBuffer.allocateDirect((Float.SIZE/8)*3).order(ByteOrder.nativeOrder()).asFloatBuffer();
        glGetFloat(GL_MODELVIEW_MATRIX, modelview);
        glGetFloat(GL_PROJECTION_MATRIX, projection);
        glGetInteger(GL_VIEWPORT, viewport);
        
        // convert window coordinates to opengl coordinates (top left to bottom left for (0,0)
        float winX = (float) mouseX;
        float winY = (float) mouseY;
        
        // now unproject this to get the  vector in to the screen
        // take the frustum and unproject in to the screen
        // frustrum has a near plane and a far plane
        
        // first the near vector
        gluUnProject(winX, winY,  0, modelview, projection, viewport, pickingRayBuffer);        
        Vector3f nearVector = new Vector3f(pickingRayBuffer.get(0),pickingRayBuffer.get(1),pickingRayBuffer.get(2));
        
        pickingRayBuffer.rewind();
        
        // now the far vector
        gluUnProject(winX, winY,  1, modelview, projection, viewport, pickingRayBuffer);
        Vector3f farVector = new Vector3f(pickingRayBuffer.get(0),pickingRayBuffer.get(1),pickingRayBuffer.get(2));
        
        //save the results in a vector, far-near
        Vector3f result = new Vector3f();
        Vector3f.sub(farVector, nearVector, result);
        result.x = -result.x;
        result.z = -result.z;
        return result;
        //return farVector.subtractVector(nearVector).normalise();
	}
	
	public static Vector3f findPlane(Ray ray) {
		Vector3f direction = ray.getDirection();
		Vector3f position = ray.getPosition();
		
		if(direction.getY() < 0) { // Make sure the direction vector is actually pointed towards the ground plane
			// Determine how many y-component direction lengths it takes to reach the ground plane
			float factor = position.getY() / direction.getY();
			
			// Use this factor to determine how far the x and z components travel
			float deltaX = direction.getX() * factor;
			float deltaZ = direction.getZ()	* factor;
			
			float x =  position.getX() + deltaX;
			float z = -position.getZ() + deltaZ;
			
			// Make y = 0 so the vector is always on the ground plane
			return new Vector3f(x, 0, z);
		}
		
		return null;
	}
}








