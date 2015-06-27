package exort.util.obj;

import org.lwjgl.util.vector.*;

public class Face {
	private Vector3f vertex, texCoord, normal;

	public Face(Vector3f vertex, Vector3f texCoord, Vector3f normal) {
		this.normal = normal;
		this.texCoord = texCoord;
		this.vertex = vertex;
	}

	public Vector3f getVertex() {
		return this.vertex;
	}

	public Vector3f getTexCoord() {
		if (this.texCoord == null) {
			return new Vector3f(0f, 0f, 0f);
		} else {
			return this.texCoord;
		}
	}

	public Vector3f getNormal() {
		return this.normal;
	}
}
