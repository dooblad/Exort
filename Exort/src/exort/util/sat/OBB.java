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

	private float width, length;

	/**
	 * Creates an OBB at the origin with size (0, 0).
	 */
	public OBB() {
		this(0, 0, 0, 0);
	}

	/**
	 * Creates an OBB at ("x", "z") with size("width", "length").
	 */
	public OBB(float x, float width, float z, float length) {
		this.vertices = new Vector2f[4];
		for (int i = 0; i < this.vertices.length; i++) {
			this.vertices[i] = new Vector2f();
		}
		this.vertices[0].set(x - (width / 2), z - (length / 2));
		this.vertices[1].set(x + (width / 2), z - (length / 2));
		this.vertices[2].set(x + (width / 2), z + (length / 2));
		this.vertices[3].set(x - (width / 2), z + (length / 2));
		this.center = new Vector2f(x, z);
		this.width = width;
		this.length = length;
	}

	/**
	 * Renders this OBB using GL_LINES.
	 */
	public void render() {
		if (Main.debug) {
			Shaders.use("color");
			Color.set(Shaders.current, 1f, 0f, 1f, 1f);
			Matrices.sendMVPMatrix(Shaders.current);
			new SimpleBatch(GL11.GL_LINES, 3, new float[] { this.vertices[0].x, HEIGHT, this.vertices[0].y, this.vertices[1].x, HEIGHT, this.vertices[1].y,
					this.vertices[1].x, HEIGHT, this.vertices[1].y, this.vertices[2].x, HEIGHT, this.vertices[2].y, this.vertices[2].x, HEIGHT,
					this.vertices[2].y, this.vertices[3].x, HEIGHT, this.vertices[3].y, this.vertices[3].x, HEIGHT, this.vertices[3].y, this.vertices[0].x,
					HEIGHT, this.vertices[0].y, }, null, null, null, null).draw(Shaders.current.getAttributeLocations());
		}
	}

	/**
	 * Moves this OBB to ("x", "z").
	 */
	public void setPosition(float x, float z) {
		// Find the difference between the current center and the new center.
		float xx = x - this.center.x;
		float zz = z - this.center.y;

		this.center.set(x, z);

		for (int i = 0; i < this.vertices.length; i++) {
			this.vertices[i].x += xx;
			this.vertices[i].y += zz;
		}
	}

	/**
	 * Performs an absolute rotation (relative to no rotation) by "angle" radians.
	 */
	public void rotate(float angle) {
		// Precompute sine and cosine.
		float sin = (float) Math.sin(angle);
		float cos = (float) Math.cos(angle);

		float x = -this.width / 2;
		float z = -this.length / 2;
		this.vertices[0].x = ((x * cos) - (z * sin)) + this.center.x;
		this.vertices[0].y = ((x * sin) + (z * cos)) + this.center.y;
		x = this.width / 2;
		z = -this.length / 2;
		this.vertices[1].x = ((x * cos) - (z * sin)) + this.center.x;
		this.vertices[1].y = ((x * sin) + (z * cos)) + this.center.y;
		x = this.width / 2;
		z = this.length / 2;
		this.vertices[2].x = ((x * cos) - (z * sin)) + this.center.x;
		this.vertices[2].y = ((x * sin) + (z * cos)) + this.center.y;
		x = -this.width / 2;
		z = this.length / 2;
		this.vertices[3].x = ((x * cos) - (z * sin)) + this.center.x;
		this.vertices[3].y = ((x * sin) + (z * cos)) + this.center.y;
	}

	/**
	 * Returns a minimum translation to resolve the collision between this and "bb". If
	 * there is no collision, returns null.
	 */
	public Vector2f colliding(OBB bb) {
		// Start it out at a ridiculous maximum so it can be compared with actual overlaps
		// to find a minimum.
		double overlap = 100.0;
		Vector2f smallest = new Vector2f(0, 0);

		Vector2f[][] axes = new Vector2f[2][];
		axes[0] = this.getAxes();
		axes[1] = bb.getAxes();

		for (Vector2f[] a : axes) {
			for (Vector2f axis : a) {
				// Project both shapes onto the axis.
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
		}

		// Create the minimum translation vector (MTV).
		Vector2f result = new Vector2f((float) (overlap / smallest.x), (float) (overlap / smallest.y));
		// Check for ridiculousness in the vector.
		if (Math.abs(result.x) > 100f) {
			result.x = 0;
		}
		if (Math.abs(result.y) > 100f) {
			result.y = 0;
		}
		return result;
	}

	public Vector2f[] getAxes() {
		Vector2f[] axes = new Vector2f[this.vertices.length];

		for (int i = 0; i < this.vertices.length; i++) {
			Vector2f p1 = this.vertices[i];
			Vector2f p2 = this.vertices[(i + 1) == this.vertices.length ? 0 : i + 1];
			Vector2f edge = new Vector2f();
			Vector2f.sub(p1, p2, edge);
			edge.normalise();
			float temp = edge.x;
			edge.x = -edge.y;
			edge.y = temp;
			axes[i] = edge;
		}

		return axes;
	}

	public Projection project(Vector2f axis) {
		double min = Vector2f.dot(axis, this.vertices[0]);
		double max = min;

		for (int i = 1; i < this.vertices.length; i++) {
			// The axis must be normalized to get accurate projections.
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

	public static void main(String[] args) {
		for (int i = 0; i < 21; i++) {
			OBB one = new OBB(0, 20, 0, 20);
			OBB two = new OBB(i, 20, 0, 20);
			System.out.println(one.colliding(two));
		}
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