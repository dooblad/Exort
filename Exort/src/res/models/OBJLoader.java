package res.models;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import java.io.*;
import java.util.*;

import org.lwjgl.util.vector.*;

import res.textures.*;

import com.doobs.exort.util.gl.*;

public class OBJLoader {
	public static TexturedModel loadModel(String URL) {
		try {
			TexturedModel model = new TexturedModel();
			model.setHandle(glGenLists(1));
			BufferedReader reader = new BufferedReader(new FileReader("res/models/" + URL));

			List<Vector3f> vertices = new ArrayList<Vector3f>();
			List<Vector3f> verticesTemp = new ArrayList<Vector3f>();

			List<Vector2f> texCoords = new ArrayList<Vector2f>();
			List<Vector2f> texCoordsTemp = new ArrayList<Vector2f>();

			List<Vector3f> normals = new ArrayList<Vector3f>();
			List<Vector3f> normalsTemp = new ArrayList<Vector3f>();

			List<Face> faces = new ArrayList<Face>();
			List<Face> facesTemp = new ArrayList<Face>();

			String line;

			glEnable(GL_TEXTURE_2D);

			// Create Display List
			glNewList(model.getHandle(), GL_COMPILE);
			glBegin(GL_TRIANGLES);

			while ((line = reader.readLine()) != null) {
				if (line.startsWith("v ")) {
					float x = Float.valueOf(line.split(" ")[1]);
					float y = Float.valueOf(line.split(" ")[2]);
					float z = Float.valueOf(line.split(" ")[3]);
					verticesTemp.add(new Vector3f(x, y, z));
				} else if (line.startsWith("vt ")) {
					float u = Float.valueOf(line.split(" ")[1]);
					float v = Float.valueOf(line.split(" ")[2]);
					texCoordsTemp.add(new Vector2f(u, v));
				} else if (line.startsWith("vn ")) {
					float x = Float.valueOf(line.split(" ")[1]);
					float y = Float.valueOf(line.split(" ")[2]);
					float z = Float.valueOf(line.split(" ")[3]);
					normalsTemp.add(new Vector3f(x, y, z));
				} else if (line.startsWith("f ")) {
					Vector3f vertexIndices = new Vector3f(Float.valueOf(line.split(" ")[1].split("/")[0]), Float.valueOf(line.split(" ")[2].split("/")[0]),
							Float.valueOf(line.split(" ")[3].split("/")[0]));

					// Check if model uses textures
					Vector3f texCoordIndices = null;
					if (line.split(" ")[1].split("/")[1].length() != 0) {
						texCoordIndices = new Vector3f(Float.valueOf(line.split(" ")[1].split("/")[1]), Float.valueOf(line.split(" ")[2].split("/")[1]),
								Float.valueOf(line.split(" ")[3].split("/")[1]));
					}

					Vector3f normalIndices = new Vector3f(Float.valueOf(line.split(" ")[1].split("/")[2]), Float.valueOf(line.split(" ")[2].split("/")[2]),
							Float.valueOf(line.split(" ")[3].split("/")[2]));

					facesTemp.add(new Face(vertexIndices, texCoordIndices, normalIndices));
				} else if (line.startsWith("o ")) {
					dumpModelData(URL, vertices, verticesTemp, texCoords, texCoordsTemp, normals, normalsTemp, faces, facesTemp);
				}
			}

			dumpModelData(URL, vertices, verticesTemp, texCoords, texCoordsTemp, normals, normalsTemp, faces, facesTemp);

			reader.close();

			glEnd();
			glEndList();

			glBindTexture(GL_TEXTURE_2D, 0);
			glDisable(GL_TEXTURE_2D);

			// Convert Lists to FloatBuffers
			Vector3f[] vertArray = new Vector3f[vertices.size() * 3];
			for (int i = 0; i < vertices.size(); i++) {
				vertArray[i] = vertices.get(i);
			}
			model.vertices = vertArray;

			Vector2f[] texCoordArray = new Vector2f[texCoords.size() * 2];
			for (int i = 0; i < texCoords.size(); i++) {
				texCoordArray[i] = texCoords.get(i);
			}
			model.texCoords = texCoordArray;

			Vector3f[] normArray = new Vector3f[normals.size() * 3];
			for (int i = 0; i < normals.size(); i++) {
				normArray[i] = normals.get(i);
			}
			model.normals = normArray;

			Face[] faceArray = new Face[faces.size()];
			for (int i = 0; i < faces.size(); i++) {
				faceArray[i] = faces.get(i);
			}
			model.faces = faceArray;

			System.out.println(URL + ": #V: " + vertices.size());

			return model;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static void dumpModelData(String URL, List<Vector3f> vertices, List<Vector3f> verticesTemp, List<Vector2f> texCoords, List<Vector2f> texCoordsTemp,
			List<Vector3f> normals, List<Vector3f> normalsTemp, List<Face> faces, List<Face> facesTemp) {
		
		for (int i = 0; i < verticesTemp.size(); i++) {
			vertices.add(verticesTemp.get(i));
		}
		verticesTemp.clear();

		boolean textured = texCoordsTemp.size() > 0;

		if (textured) {
			for (int i = 0; i < texCoordsTemp.size(); i++) {
				texCoords.add(texCoordsTemp.get(i));
			}
			texCoordsTemp.clear();
		}

		for (int i = 0; i < normalsTemp.size(); i++) {
			normals.add(normalsTemp.get(i));
		}
		normalsTemp.clear();

		for (int i = 0; i < facesTemp.size(); i++) {
			faces.add(facesTemp.get(i));
		}
		facesTemp.clear();

		// If textured, bind the corresponding texture
		if (textured) {
			glActiveTexture(GL_TEXTURE1);
			try {
				glBindTexture(GL_TEXTURE_2D, Textures.textures.get(URL.split("\\.")[0] + "Normal").getID());
			} catch (NullPointerException e) {
				System.err.println("No corresponding normal map found for model " + URL);
			}

			glActiveTexture(GL_TEXTURE0);
			try {
				glBindTexture(GL_TEXTURE_2D, Textures.getTexture(URL.split("\\.")[0]).getID());
			} catch (NullPointerException e) {
				System.err.println("No corresponding diffuse texture found for model \"" + URL + "\"");
				glBindTexture(GL_TEXTURE_2D, Textures.getTexture("missing").getID());
			}
		}

		for (Face face : faces) {
			if (textured) {
				Vector2f t1 = texCoords.get((int) face.getTexCoord().getX() - 1);
				glTexCoord2f(t1.getX(), t1.getY());
			}
			Vector3f n1 = normals.get((int) face.getNormal().getX() - 1);
			glNormal3f(n1.getX(), n1.getY(), n1.getZ());
			Vector3f v1 = vertices.get((int) face.getVertex().getX() - 1);
			glVertex3f(v1.getX(), v1.getY(), v1.getZ());

			if (textured) {
				Vector2f t2 = texCoords.get((int) face.getTexCoord().getY() - 1);
				glTexCoord2f(t2.getX(), t2.getY());
			}
			Vector3f n2 = normals.get((int) face.getNormal().getY() - 1);
			glNormal3f(n2.getX(), n2.getY(), n2.getZ());
			Vector3f v2 = vertices.get((int) face.getVertex().getY() - 1);
			glVertex3f(v2.getX(), v2.getY(), v2.getZ());

			if (textured) {
				Vector2f t3 = texCoords.get((int) face.getTexCoord().getZ() - 1);
				glTexCoord2f(t3.getX(), t3.getY());
			}
			Vector3f n3 = normals.get((int) face.getNormal().getZ() - 1);
			glNormal3f(n3.getX(), n3.getY(), n3.getZ());
			Vector3f v3 = vertices.get((int) face.getVertex().getZ() - 1);
			glVertex3f(v3.getX(), v3.getY(), v3.getZ());
		}
	}
}
