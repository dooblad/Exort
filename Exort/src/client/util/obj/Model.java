package client.util.obj;

import static org.lwjgl.opengl.GL11.*;
import client.util.loaders.*;

import com.doobs.modern.util.batch.*;
import com.doobs.modern.util.texture.*;

public class Model {
	public static int POSITION_COMPONENTS = 3;

	private Texture texture;

	private SimpleBatch batch;

	public float[] positions, normals, texCoords;

	public Model() {

	}

	/**
	 * Creates the drawing batch for this Model.
	 */
	public void generate() {
		this.batch = new SimpleBatch(GL_TRIANGLES, POSITION_COMPONENTS, this.positions, null, this.normals, this.texCoords, null);
	}

	/**
	 * Pre: OpenGL is using a Shader with textures.
	 */
	public void draw() {
		// TODO: Figure out the correct way to bind textures in OpenGL 3+.
		Shaders.current.setUniform1i("texture", 0);
		if (this.texture != null) {
			this.texture.bind();
		}
		this.batch.draw(Shaders.current.getAttributeLocations());
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}
}