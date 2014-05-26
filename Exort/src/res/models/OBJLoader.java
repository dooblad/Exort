package res.models;

import static org.lwjgl.opengl.GL11.GL_COMPILE;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glEndList;
import static org.lwjgl.opengl.GL11.glGenLists;
import static org.lwjgl.opengl.GL11.glNewList;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import com.doobs.exort.util.Face;
import com.doobs.exort.util.Model;

public class OBJLoader {
	public static Model loadModel(String URL) {
		try {
			Model model = new Model();
			model.setHandle(glGenLists(1));
			BufferedReader reader = new BufferedReader(new InputStreamReader(OBJLoader.class.getResourceAsStream(URL)));
			List<Vector3f> vertices = new ArrayList<Vector3f>();
			List<Vector3f> normals = new ArrayList<Vector3f>();
			List<Face> faces = new ArrayList<Face>();
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("v ")) {
					float x = Float.valueOf(line.split(" ")[1]);
					float y = Float.valueOf(line.split(" ")[2]);
					float z = Float.valueOf(line.split(" ")[3]);
					vertices.add(new Vector3f(x, y, z));
				} else if (line.startsWith("vn ")) {
					float x = Float.valueOf(line.split(" ")[1]);
					float y = Float.valueOf(line.split(" ")[2]);
					float z = Float.valueOf(line.split(" ")[3]);
					normals.add(new Vector3f(x, y, z));
				} else if (line.startsWith("f ")) {
					Vector3f vertexIndices = new Vector3f(Float.valueOf(line.split(" ")[1].split("/")[0]), Float.valueOf(line.split(" ")[2].split("/")[0]),
							Float.valueOf(line.split(" ")[3].split("/")[0]));
					Vector3f normalIndices = new Vector3f(Float.valueOf(line.split(" ")[1].split("/")[2]), Float.valueOf(line.split(" ")[2].split("/")[2]),
							Float.valueOf(line.split(" ")[3].split("/")[2]));
					faces.add(new Face(vertexIndices, normalIndices));
				}
			}
			reader.close();

			// Convert Lists to FloatBuffers
			Vector3f[] vertArray = new Vector3f[vertices.size() * 3];
			for (int i = 0; i < vertices.size(); i++) {
				vertArray[i] = vertices.get(i);
			}
			model.setVertices(vertArray);

			Vector3f[] normArray = new Vector3f[normals.size() * 3];
			for (int i = 0; i < normals.size(); i++) {
				normArray[i] = normals.get(i);
			}
			model.setNormals(normArray);

			Face[] faceArray = new Face[faces.size()];
			for (int i = 0; i < faces.size(); i++) {
				faceArray[i] = faces.get(i);
			}
			model.setFaces(faceArray);

			// Create Display List
			glNewList(model.getHandle(), GL_COMPILE);
			glBegin(GL_TRIANGLES);
			for (Face face : faces) {
				Vector3f n1 = normals.get((int) face.getNormal().x - 1);
				glNormal3f(n1.x, n1.y, n1.z);
				Vector3f v1 = vertices.get((int) face.getVertex().x - 1);
				glVertex3f(v1.x, v1.y, v1.z);
				Vector3f n2 = normals.get((int) face.getNormal().y - 1);
				glNormal3f(n2.x, n2.y, n2.z);
				Vector3f v2 = vertices.get((int) face.getVertex().y - 1);
				glVertex3f(v2.x, v2.y, v2.z);
				Vector3f n3 = normals.get((int) face.getNormal().z - 1);
				glNormal3f(n3.x, n3.y, n3.z);
				Vector3f v3 = vertices.get((int) face.getVertex().z - 1);
				glVertex3f(v3.x, v3.y, v3.z);
			}
			glEnd();
			glEndList();
			System.out.println(URL + ": #V: " + vertices.size());
			return model;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
