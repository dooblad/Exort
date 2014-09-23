package com.doobs.exort.util.sat;

import org.lwjgl.util.vector.*;

/**
 * Utility class for Oriented Bounding Boxes (OBBs) that rotate around the center of their shape
 * @author Logan
 *
 */
public class BB {
	private float angle;
	
	private Vector2f[] vertices;
	
	public BB(float x, float width, float z, float length) {
		angle = 0;
		vertices = new Vector2f[4];
		vertices[0] = new Vector2f(x, z);
		vertices[1] = new Vector2f(x + width, z);
		vertices[2] = new Vector2f(x + width, z + length);
		vertices[3] = new Vector2f(x, z + length);
	}
	
	public BB() {
		this(0, 0, 0, 0);
	}
	
	public void rotate(float angle) {
		this.angle += angle;
		
	}
	
	public Vector2f[] getAxes() {
		Vector2f[] axes = new Vector2f[vertices.length];
		
		for(int i = 0; i < vertices.length; i++) {
			Vector2f p1 = vertices[i];
			Vector2f p2 = vertices[i + 1 == vertices.length ? 0 : i + 1];
			Vector2f edge = new Vector2f();
			Vector2f.sub(p1, p2, edge);
			Vector2f normal = new Vector2f();
			normal.x = -edge.y;
			normal.y = edge.x;
			axes[i] = normal;
		}
		
		return axes;
	}
	
	public Projection project(Vector2f axis) {
		double min = Vector2f.dot(axis, vertices[0]);
		double max = min;
		
		for(int i = 1; i < vertices.length; i++) {
			// The axis must be normalized to get accurate projections
			double p = Vector2f.dot(axis, vertices[i]);
			
			System.out.println(axis.x);
			
			if(p < min) {
				min = p;
			} else if(p > max) {
				max = p;
			}
		}
		
		Projection projection = new Projection(min, max);
		return projection;
	}
	
	public boolean colliding(BB bb) {
		Vector2f[] axes1 = getAxes();
		Vector2f[] axes2 = bb.getAxes();
		
		for(int i = 0; i < axes1.length; i++) {
			Vector2f axis = axes1[i];
			
			// Project both shapes onto the axis
			Projection p1 = project(axis);
			Projection p2 = bb.project(axis);

			if(!p1.overlaps(p2)) {
				return false;
			}
		}
		
		for(int i = 0; i < axes2.length; i++) {
			Vector2f axis = axes2[i];
			
			// Project both shapes onto the axis
			Projection p1 = project(axis);
			Projection p2 = bb.project(axis);
			
			if(!p1.overlaps(p2)) {
				return false;
			}
		}
		
		return true;
	}

	public static void main(String[] args) {
		BB shape1 = new BB(1, 4, 1, 4);
		BB shape2 = new BB(0, 3, 0, 3);
		
		System.out.println(shape1.colliding(shape2));
	}
	
	// Getters and setters
	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}
}
