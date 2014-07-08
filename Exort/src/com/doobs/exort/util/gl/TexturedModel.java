package com.doobs.exort.util.gl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import java.util.*;

import org.lwjgl.util.vector.*;

public class TexturedModel extends Model {
	public int texture;
	public Vector2f[] texCoords;
	
	public TexturedModel() {
		
	}
	
	public TexturedModel(List<Vector3f> vertices, List<Vector3f> normals, List<Vector2f> texCoords, int texture) {
		this.vertices = new Vector3f[vertices.size()];
		for (int i = 0; i < vertices.size(); i++) {
			this.vertices[i] = vertices.get(i);
		}
		
		this.normals = new Vector3f[normals.size()];
		for (int i = 0; i < normals.size(); i++) {
			this.normals[i] = normals.get(i);
		}

		this.texCoords = new Vector2f[texCoords.size()];
		for(int i = 0; i < texCoords.size(); i++) {
			this.texCoords[i] = texCoords.get(i);
		}
		
		this.texture = texture;
		
		generateDisplayList();
	}

	public TexturedModel(Vector3f[] vertices, Vector3f[] normals, Vector2f[] texCoords, int texture) {
		this.vertices = vertices;
		this.normals = normals;
		this.texCoords = texCoords;
		
		this.texture = texture;

		generateDisplayList();
	}
	
	public void generateDisplayList() {
		handle = glGenLists(1);

		glNewList(handle, GL_COMPILE);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, texture);
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
	
	public void draw() {
		
	}
}
