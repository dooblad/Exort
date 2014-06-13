package com.doobs.exort.util.gl;

import java.util.List;

import org.lwjgl.util.vector.*;

import static org.lwjgl.opengl.GL11.*;

public class Model {
	private int handle; // Display List Handle

	public Vector3f[] vertices, normals;
	public Vector2f[] texCoords;
	public Face[] faces;

	public Model() {

	}

	public Model(List<Vector3f> vertices, List<Vector2f> texCoords, List<Vector3f> normals) {
		this.vertices = new Vector3f[vertices.size()];
		for (int i = 0; i < vertices.size(); i++) {
			this.vertices[i] = vertices.get(i);
		}
		
		this.texCoords = new Vector2f[texCoords.size()];
		for(int i = 0; i < texCoords.size(); i++) {
			this.texCoords[i] = texCoords.get(i);
		}

		this.normals = new Vector3f[normals.size()];
		for (int i = 0; i < normals.size(); i++) {
			this.normals[i] = normals.get(i);
		}

		generateDisplayList();
	}

	public Model(Vector3f[] vertices, Vector2f[] texCoords, Vector3f[] normals) {
		this.vertices = vertices;
		this.texCoords = texCoords;
		this.normals = normals;

		generateDisplayList();
	}

	public void generateDisplayList() {
		handle = glGenLists(1);

		/*
		 * glNewList(handle, GL_COMPILE_AND_EXECUTE); glBegin(GL_TRIANGLES); for
		 * (int i = 0; i < vertices.capacity(); i += 3) {
		 * glVertex3f(vertices[i), vertices[i + 1], vertices[i + 2)); } glEnd();
		 * glEndList();
		 */

		glNewList(handle, GL_COMPILE);
		glBegin(GL_TRIANGLES);
		for (Face face : faces) {
			Vector3f n1 = normals[(int) face.getNormal().x - 1];
			glNormal3f(n1.x, n1.y, n1.z);
			Vector3f v1 = vertices[(int) face.getVertex().x - 1];
			glVertex3f(v1.x, v1.y, v1.z);
			Vector3f n2 = normals[(int) face.getNormal().y - 1];
			glNormal3f(n2.x, n2.y, n2.z);
			Vector3f v2 = vertices[(int) face.getVertex().y - 1];
			glVertex3f(v2.x, v2.y, v2.z);
			Vector3f n3 = normals[(int) face.getNormal().z - 1];
			glNormal3f(n3.x, n3.y, n3.z);
			Vector3f v3 = vertices[(int) face.getVertex().z - 1];
			glVertex3f(v3.x, v3.y, v3.z);
		}
		glEnd();
		glEndList();
	}

	// Getters and setters
	public int getHandle() {
		return handle;
	}

	public void setHandle(int handle) {
		this.handle = handle;
	}

	public Face[] getFaces() {
		return faces;
	}

	public void setFaces(Face[] faces) {
		this.faces = faces;
	}
}
