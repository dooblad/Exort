package exort.util.sat;

import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.*;

import com.doobs.modern.util.*;
import com.doobs.modern.util.batch.*;
import com.doobs.modern.util.matrix.*;

import exort.*;
import exort.util.loaders.*;

/**
 * Oriented Bounding Box (OBB).
 */
public class OBB {
	// For visual purposes.
	private static final float HEIGHT = 1.0f;

	// For the vertices to rotate around.
	private Vector2f center;
	private Vector2f[] vertices;

	private double angle;

	private float width, length;

	public OBB() {
		this(0, 0, 0, 0);
	}

	public OBB(float x, float width, float z, float length) {
		this.vertices = new Vector2f[4];
		for (int i = 0; i < this.vertices.length; i++) {
			this.vertices[i] = new Vector2f();
		}
		this.vertices[0].set(x - width / 2, z - length / 2);
		this.vertices[1].set(x + width / 2, z - length / 2);
		this.vertices[2].set(x + width / 2, z + length / 2);
		this.vertices[3].set(x - width / 2, z + length / 2);
		this.center = new Vector2f(x, z);
		this.angle = 0.0;
		this.width = width;
		this.length = length;
	}

	public void render() {
		if (Main.debug) {
			Shaders.use("color");
			Color.set(Shaders.current, 1f, 0f, 1f, 1f);
			Matrices.sendMVPMatrix(Shaders.current);
			new SimpleBatch(GL11.GL_LINES, 3, new float[] { this.vertices[0].x, HEIGHT, this.vertices[0].y, this.vertices[1].x, HEIGHT, this.vertices[1].y,
					this.vertices[1].x, HEIGHT, this.vertices[1].y, this.vertices[2].x, HEIGHT, this.vertices[2].y, this.vertices[2].x, HEIGHT,
					this.vertices[2].y, this.vertices[3].x, HEIGHT, this.vertices[3].y, this.vertices[3].x, HEIGHT, this.vertices[3].y,
					this.vertices[0].x, HEIGHT, this.vertices[0].y, }, null, null, null, null).draw(Shaders.current.getAttributeLocations());
		}
	}

	public void move(float x, float z) {
		// Find the difference between the current center and the new center.
		float xx = x - this.center.x;
		float zz = z - this.center.y;

		this.center.set(x, z);

		for (int i = 0; i < this.vertices.length; i++) {
			this.vertices[i].x += xx;
			this.vertices[i].y += zz;
		}
	}

	public void rotate(double angle) {
		double difference = this.angle - angle;
		this.angle = angle;

		// Precompute sine and cosine.
		float sin = (float) Math.sin(difference);
		float cos = (float) Math.cos(difference);

		// Translate to the origin for proper rotation.
		for (Vector2f vertex : this.vertices) {
			float x = vertex.x - this.center.x;
			float z = vertex.y - this.center.y;
			// System.out.println(x + " " + z);
			 vertex.x = (x * cos - z * sin) + this.center.x;
			 vertex.y = (x * sin + z * cos) + this.center.y;
		}
		// System.out.println("---------------");
	}

	public Vector2f[] getAxes() {
		Vector2f[] axes = new Vector2f[this.vertices.length];

		for (int i = 0; i < this.vertices.length; i++) {
			Vector2f p1 = this.vertices[i];
			Vector2f p2 = this.vertices[(i + 1) == this.vertices.length ? 0 : i + 1];
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
		double min = Vector2f.dot(axis, this.vertices[0]);
		double max = min;

		for (int i = 1; i < this.vertices.length; i++) {
			// The axis must be normalized to get accurate projections
			double p = Vector2f.dot(axis, this.vertices[i]);

			if (p < min) {
				min = p;
			} else if (p > max) {
				max = p;
			}
		}

		Projection projection = new Projection(min, max);
		return projection;
	}

	public Vector2f colliding(OBB bb) {
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

	public float getX() {
		return this.center.getX();
	}

	public float getZ() {
		return this.center.getY();
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
