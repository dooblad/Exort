package exort.util.sat;

import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.*;

import com.doobs.modern.util.*;
import com.doobs.modern.util.batch.*;
import com.doobs.modern.util.matrix.*;

import exort.util.loaders.*;

/**
 * Utility class for Oriented Bounding Boxes (OBBs)
 *
 * @author Logan
 *
 */
public class BB {
	private Vector2f[] original, transformed;
	private float width, length;
	private double angle;

	public BB(float x, float width, float z, float length) {
		this.angle = 0;
		this.original = new Vector2f[4];
		this.transformed = new Vector2f[4];
		this.width = width;
		this.length = length;
		this.move(x, z);
		this.rotate(0);
	}

	public BB() {
		this(0, 0, 0, 0);
	}

	public void render() {
		Shaders.use("color");
		Color.set(Shaders.current, 1f, 0f, 1f, 1f);
		Matrices.sendMVPMatrix(Shaders.current);
		new SimpleBatch(GL11.GL_LINES, 3, new float[] { this.transformed[0].x - (this.width / 2), 0.3f, this.transformed[0].y - (this.length / 2),
				this.transformed[1].x - (this.width / 2), 0.3f, this.transformed[1].y - (this.length / 2), this.transformed[1].x - (this.width / 2), 0.3f,
				this.transformed[1].y - (this.length / 2), this.transformed[2].x - (this.width / 2), 0.3f, this.transformed[2].y - (this.length / 2),
				this.transformed[2].x - (this.width / 2), 0.3f, this.transformed[2].y - (this.length / 2), this.transformed[3].x - (this.width / 2), 0.3f,
				this.transformed[3].y - (this.length / 2), this.transformed[3].x - (this.width / 2), 0.3f, this.transformed[3].y - (this.length / 2),
				this.transformed[0].x - (this.width / 2), 0.3f, this.transformed[0].y - (this.length / 2), }, null, null, null, null).draw(Shaders.current
				.getAttributeLocations());
	}

	public void rotate(double angle) {
		this.angle = angle;

		/*
		 * transformed[0].x = (float) ((original[0].x - width / 2) * Math.cos(angle) -
		 * (original[0].y - length / 2) * Math.sin(angle)) + width / 2; transformed[1].x =
		 * (float) ((original[1].x - width / 2) * Math.cos(angle) - (original[1].y -
		 * length / 2) * Math.sin(angle)) + width / 2; transformed[2].x = (float)
		 * ((original[2].x - width / 2) * Math.cos(angle) - (original[2].y - length / 2) *
		 * Math.sin(angle)) + width / 2; transformed[3].x = (float) ((original[3].x -
		 * width / 2) * Math.cos(angle) - (original[3].y - length / 2) * Math.sin(angle))
		 * + width / 2;
		 * 
		 * transformed[0].y = (float) ((original[0].x - width / 2) * Math.sin(angle) +
		 * (original[0].y - length / 2) * Math.cos(angle)) + length / 2; transformed[1].y
		 * = (float) ((original[1].x - width / 2) * Math.sin(angle) + (original[1].y -
		 * length / 2) * Math.cos(angle)) + length / 2; transformed[2].y = (float)
		 * ((original[2].x - width / 2) * Math.sin(angle) + (original[2].y - length / 2) *
		 * Math.cos(angle)) + length / 2; transformed[3].y = (float) ((original[3].x -
		 * width / 2) * Math.sin(angle) + (original[3].y - length / 2) * Math.cos(angle))
		 * + length / 2;
		 */

		this.transformed[0].x = (float) (((-this.width / 2) * Math.cos(angle)) - ((-this.length / 2) * Math.sin(angle))) + (this.width / 2);
		this.transformed[1].x = (float) (((this.width / 2) * Math.cos(angle)) - ((-this.length / 2) * Math.sin(angle))) + (this.width / 2);
		this.transformed[2].x = (float) (((this.width / 2) * Math.cos(angle)) - ((this.length / 2) * Math.sin(angle))) + (this.width / 2);
		this.transformed[3].x = (float) (((-this.width / 2) * Math.cos(angle)) - ((this.length / 2) * Math.sin(angle))) + (this.width / 2);

		this.transformed[0].y = (float) (((-this.width / 2) * Math.sin(angle)) + ((-this.length / 2) * Math.cos(angle))) + (this.length / 2);
		this.transformed[1].y = (float) (((this.width / 2) * Math.sin(angle)) + ((-this.length / 2) * Math.cos(angle))) + (this.length / 2);
		this.transformed[2].y = (float) (((this.width / 2) * Math.sin(angle)) + ((this.length / 2) * Math.cos(angle))) + (this.length / 2);
		this.transformed[3].y = (float) (((-this.width / 2) * Math.sin(angle)) + ((this.length / 2) * Math.cos(angle))) + (this.length / 2);

		/*
		 * System.out.println("--------------------------");
		 * System.out.println(transformed[0].x + " " + transformed[0].y);
		 * System.out.println(transformed[1].x + " " + transformed[1].y);
		 * System.out.println(transformed[2].x + " " + transformed[2].y);
		 * System.out.println(transformed[3].x + " " + transformed[3].y);
		 */

		// System.out.println(original[3].y - length / 2);
	}

	public Vector2f[] getAxes() {
		Vector2f[] axes = new Vector2f[this.original.length];

		for (int i = 0; i < this.original.length; i++) {
			Vector2f p1 = this.original[i];
			Vector2f p2 = this.original[(i + 1) == this.original.length ? 0 : i + 1];
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
		double min = Vector2f.dot(axis, this.original[0]);
		double max = min;

		for (int i = 1; i < this.original.length; i++) {
			// The axis must be normalized to get accurate projections
			double p = Vector2f.dot(axis, this.original[i]);

			if (p < min) {
				min = p;
			} else if (p > max) {
				max = p;
			}
		}

		Projection projection = new Projection(min, max);
		return projection;
	}

	public Vector2f colliding(BB bb) {
		double overlap = 2000000000;
		Vector2f smallest = null;
		Vector2f[] axes1 = this.getAxes();
		Vector2f[] axes2 = bb.getAxes();

		for (Vector2f axis : axes1) {
			// Project both shapes onto the axis
			Projection p1 = this.project(axis);
			Projection p2 = bb.project(axis);

			if (!p1.overlaps(p2)) {
				return null;
			} else {
				double o = p1.getOverlap(p2);
				if (o < overlap) {
					overlap = o;
					smallest = axis;
				}
			}
		}

		for (Vector2f axis : axes2) {
			// Project both shapes onto the axis
			Projection p1 = this.project(axis);
			Projection p2 = bb.project(axis);

			if (!p1.overlaps(p2)) {
				return null;
			} else {
				double o = p1.getOverlap(p2);
				if (o < overlap) {
					overlap = o;
					smallest = axis;
				}
			}
		}

		Vector2f result = new Vector2f((float) (overlap / smallest.x), (float) (overlap / smallest.y));
		if (Math.abs(result.x) > 2000000000) {
			result.x = 0;
		}
		if (Math.abs(result.y) > 2000000000) {
			result.y = 0;
		}
		return result;
	}

	public void move(float x, float z) {
		this.original[0] = new Vector2f(x, z);
		this.original[1] = new Vector2f(x + this.width, z);
		this.original[2] = new Vector2f(x + this.width, z + this.length);
		this.original[3] = new Vector2f(x, z + this.length);

		this.transformed[0] = new Vector2f(x, z);
		this.transformed[1] = new Vector2f(x + this.width, z);
		this.transformed[2] = new Vector2f(x + this.width, z + this.length);
		this.transformed[3] = new Vector2f(x, z + this.length);

		this.rotate(this.angle);
	}

	public static void main(String[] args) {
		BB one = new BB(0, 40, 0, 40);
		BB two = new BB(0, 40, 0, 40);

		// MTV pls = two.colliding(one);
		Vector2f pls = two.colliding(one);

		if (pls != null) {
			System.out.println(pls.x + " " + pls.y);
		} else {
			System.out.println("null");
		}
	}

	// Getters and setters
	public double getAngle() {
		return this.angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public float getWidth() {
		return this.width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getLength() {
		return this.length;
	}

	public void setLength(float length) {
		this.length = length;
	}
}
