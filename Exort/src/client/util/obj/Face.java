package client.util.obj;

import org.lwjgl.util.vector.*;

/**
 * Represents one Face of a Model for bundling the positions, texture coordinates, and
 * normal vectors together.
 */
public class Face {
	private Vector3f position, texCoord, normal;

	public Face(Vector3f position, Vector3f texCoord, Vector3f normal) {
		this.position = position;
		this.texCoord = texCoord;
		this.normal = normal;
	}

	public Vector3f getPosition() {
		return this.position;
	}

	public Vector3f getTexCoord() {
		return this.texCoord;
	}

	public Vector3f getNormal() {
		return this.normal;
	}
}
