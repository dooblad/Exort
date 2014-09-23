package com.doobs.exort.util.obj;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.vector.*;

import com.doobs.exort.util.loaders.*;
import com.doobs.modern.util.batch.*;
import com.doobs.modern.util.texture.*;

public class Model {
	private Texture texture;

	private SimpleBatch batch;
	
	public float[] vertices, normals, texCoords;

	public Model() {

	}
	

	public Model(Vector3f[] vertices, Vector3f[] normals, Vector2f[] texCoords) {
		generate();
	}
	
	public void generate() {
		batch = new SimpleBatch(GL_TRIANGLES, 3, vertices, null, normals, texCoords, null);
	}

	public void draw() {
		Shaders.current.setUniform1i("texture", 0);
		if(texture != null)
			texture.bind();
		batch.draw(Shaders.current.getAttributeLocations());
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	// Getter and setters
	public Texture getTexture() {
		return texture;
	}
	
	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public SimpleBatch getBatch() {
		return batch;
	}

	public void setBatch(SimpleBatch batch) {
		this.batch = batch;
	}

	public float[] getVertices() {
		return vertices;
	}

	public void setVertices(float[] vertices) {
		this.vertices = vertices;
	}

	public float[] getNormals() {
		return normals;
	}

	public void setNormals(float[] normals) {
		this.normals = normals;
	}

	public float[] getTexCoords() {
		return texCoords;
	}

	public void setTexCoords(float[] texCoords) {
		this.texCoords = texCoords;
	}
}
