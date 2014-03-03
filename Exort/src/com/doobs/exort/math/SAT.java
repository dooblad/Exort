package com.doobs.exort.math;

import org.lwjgl.util.vector.Vector3f;

public class SAT {

	private static Vector3f[] axes1, axes2;
	private static Shape shape1 = new Shape(0.0f, 0.0f, -3.0f,
											1.0f, 0.0f, -3.0f,
											0.0f, 1.0f, -3.0f);
	private static Shape shape2 = new Shape(0.5f, 0.0f, -3.0f,
											1.5f, 0.0f, -3.0f,
											0.5f, 1.0f, -3.0f);

	private static Vector3f[] calculateAxes(Shape shape) {
		Vector3f[] axes = new Vector3f[shape.getVertices().length];
		for (int i = 0; i < shape.getVertices().length; i++) {
			Vector3f edge = new Vector3f();
			Vector3f.sub(shape.getVertex(i), shape.getVertex(i + 1 == shape
					.getVertices().length ? 0 : i + 1), edge);
			axes[i] = (Vector3f) MathUtil.perpendicular(edge).normalise();
			//RIDDENCE
			System.out.println("Axis[" + axes[i].getX() + ", " + axes[i].getY() + ", " + axes[i].getZ() + "]");
		}
		
		return axes;
	}

	private static Projection project(Vector3f axis, Shape shape) {
		float min = Vector3f.dot(axis, shape.getVertex(0));
		float max = min;

		for (int i = 1; i < shape.getVertices().length; i++) {
			float p = Vector3f.dot(axis, shape.getVertex(i));
			if (p < min)
				min = p;
			else if (p > max)
				max = p;
		}

		Projection projection = new Projection(min, max);
		
		//RIDDENCE
		projection.print();

		return projection;
	}

	private static boolean colliding() {
		for (int i = 0; i < axes1.length; i++) {
			//RIDDENCE
			System.out.println("Check 1[" + i + "]");
			
			Projection proj1 = project(axes1[i], shape1);
			Projection proj2 = project(axes1[i], shape2);
			
			//RIDDENCE
			System.out.println();
			
			if (!proj1.overlaps(proj2))
				return false;
		}
		
		//RIDDENCE
		System.out.println();

		for (int i = 0; i < axes2.length; i++) {
			//RIDDENCE
			System.out.println("Check 2[" + i + "]");
			
			Projection proj1 = project(axes2[i], shape1);
			Projection proj2 = project(axes2[i], shape2);
			
			//RIDDENCE
			System.out.println();
			
			if (!proj1.overlaps(proj2))
				return false;
		}
		return true;
	}

	public static void main(String[] args) {
		axes1 = calculateAxes(shape1);
		//RIDDENCE
		System.out.println();
		axes2 = calculateAxes(shape2);
		//RIDDENCE
		System.out.println();
		
		System.out.println(colliding());
	}
}
