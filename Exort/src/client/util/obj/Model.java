package client.util.obj;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.vector.*;

import client.util.loaders.*;

import com.doobs.modern.util.batch.*;
import com.doobs.modern.util.texture.*;

public class Model {
	private Texture texture;

	private SimpleBatch batch;

	public float[] vertices, normals, texCoords;

	public Model() {

	}

	public Model(Vector3f[] vertices, Vector3f[] normals, Vector2f[] texCoords) {
		this.generate();
	}

	public void generate() {
		this.batch = new SimpleBatch(GL_TRIANGLES, 3, this.vertices, null, this.normals, this.texCoords, null);
	}

	public void draw() {
		Shaders.current.setUniform1i("texture", 0);
		if (this.texture != null) {
			this.texture.bind();
		}
		this.batch.draw(Shaders.current.getAttributeLocations());
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	// Getter and setters
	public Texture getTexture() {
		return this.texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public SimpleBatch getBatch() {
		return this.batch;
	}

	public void setBatch(SimpleBatch batch) {
		this.batch = batch;
	}

	public float[] getVertices() {
		return this.vertices;
	}

	public void setVertices(float[] vertices) {
		this.vertices = vertices;
	}

	public float[] getNormals() {
		return this.normals;
	}

	public void setNormals(float[] normals) {
		this.normals = normals;
	}

	public float[] getTexCoords() {
		return this.texCoords;
	}

	public void setTexCoords(float[] texCoords) {
		this.texCoords = texCoords;
	}
}
