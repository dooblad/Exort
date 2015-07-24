package client.util.obj;

import java.io.*;
import java.util.*;

import org.lwjgl.util.vector.*;

import client.util.loaders.*;

public class OBJLoader {
	public static Model load(String URL) {
		try {
			Model model = new Model();

			BufferedReader reader = new BufferedReader(new FileReader(URL));

			List<Vector3f> positions = new ArrayList<Vector3f>();
			List<Vector3f> positionsTemp = new ArrayList<Vector3f>();

			List<Vector2f> texCoords = new ArrayList<Vector2f>();
			List<Vector2f> texCoordsTemp = new ArrayList<Vector2f>();

			List<Vector3f> normals = new ArrayList<Vector3f>();
			List<Vector3f> normalsTemp = new ArrayList<Vector3f>();

			List<Face> faces = new ArrayList<Face>();
			List<Face> facesTemp = new ArrayList<Face>();

			String line;

			while ((line = reader.readLine()) != null) {
				if (line.startsWith("v ")) {
					float x = Float.valueOf(line.split(" ")[1]);
					float y = Float.valueOf(line.split(" ")[2]);
					float z = Float.valueOf(line.split(" ")[3]);
					positionsTemp.add(new Vector3f(x, y, z));
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
					Vector3f positionIndices = new Vector3f(Float.valueOf(line.split(" ")[1].split("/")[0]), Float.valueOf(line.split(" ")[2].split("/")[0]),
							Float.valueOf(line.split(" ")[3].split("/")[0]));

					// Check if model uses textures
					Vector3f texCoordIndices = null;
					if (line.split(" ")[1].split("/")[1].length() != 0) {
						texCoordIndices = new Vector3f(Float.valueOf(line.split(" ")[1].split("/")[1]), Float.valueOf(line.split(" ")[2].split("/")[1]),
								Float.valueOf(line.split(" ")[3].split("/")[1]));
					}

					Vector3f normalIndices = new Vector3f(Float.valueOf(line.split(" ")[1].split("/")[2]), Float.valueOf(line.split(" ")[2].split("/")[2]),
							Float.valueOf(line.split(" ")[3].split("/")[2]));

					facesTemp.add(new Face(positionIndices, texCoordIndices, normalIndices));
				} else if (line.startsWith("o ")) {
					dumpModelData(URL, positions, positionsTemp, texCoords, texCoordsTemp, normals, normalsTemp, faces, facesTemp);
				}
			}
			reader.close();

			dumpModelData(URL, positions, positionsTemp, texCoords, texCoordsTemp, normals, normalsTemp, faces, facesTemp);

			// Convert Lists to Vector Arrays
			Vector3f[] positionArray = new Vector3f[positions.size()];
			for (int i = 0; i < positions.size(); i++) {
				positionArray[i] = positions.get(i);
			}

			Vector3f[] normalArray = new Vector3f[normals.size()];
			for (int i = 0; i < normals.size(); i++) {
				normalArray[i] = normals.get(i);
			}

			Vector2f[] texCoordArray = new Vector2f[texCoords.size()];
			for (int i = 0; i < texCoords.size(); i++) {
				texCoordArray[i] = texCoords.get(i);
			}

			Face[] faceArray = new Face[faces.size()];
			for (int i = 0; i < faces.size(); i++) {
				faceArray[i] = faces.get(i);
			}

			sortData(model, positionArray, normalArray, texCoordArray, faceArray);
			model.generate();

			// Find texture (if available)
			String[] temp = URL.split("/");
			model.setTexture(Textures.get(temp[temp.length - 1].split("\\.")[0]));

			return model;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static void dumpModelData(String URL, List<Vector3f> positions, List<Vector3f> positionsTemp, List<Vector2f> texCoords, List<Vector2f> texCoordsTemp,
			List<Vector3f> normals, List<Vector3f> normalsTemp, List<Face> faces, List<Face> facesTemp) {

		for (int i = 0; i < positionsTemp.size(); i++) {
			positions.add(positionsTemp.get(i));
		}
		positionsTemp.clear();

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
	}

	private static void sortData(Model model, Vector3f[] positions, Vector3f[] normals, Vector2f[] texCoords, Face[] faces) {
		model.positions = new float[faces.length * 9];
		model.normals = new float[faces.length * 9];
		model.texCoords = null;
		if ((texCoords != null) && (texCoords.length > 0)) {
			model.texCoords = new float[faces.length * 6];
		}

		int i = 0;
		for (Face face : faces) {
			{
				Vector3f v1 = positions[(int) face.getPosition().x - 1];
				model.positions[i * 9] = v1.x;
				model.positions[(i * 9) + 1] = v1.y;
				model.positions[(i * 9) + 2] = v1.z;

				Vector3f n1 = normals[(int) face.getNormal().x - 1];
				model.normals[i * 9] = n1.x;
				model.normals[(i * 9) + 1] = n1.y;
				model.normals[(i * 9) + 2] = n1.z;

				if (model.texCoords != null) {
					Vector2f t1 = texCoords[(int) face.getTexCoord().x - 1];
					model.texCoords[i * 6] = t1.x;
					model.texCoords[(i * 6) + 1] = t1.y;
				}
			}

			{
				Vector3f v2 = positions[(int) face.getPosition().y - 1];
				model.positions[(i * 9) + 3] = v2.x;
				model.positions[(i * 9) + 4] = v2.y;
				model.positions[(i * 9) + 5] = v2.z;

				Vector3f n2 = normals[(int) face.getNormal().y - 1];
				model.normals[(i * 9) + 3] = n2.x;
				model.normals[(i * 9) + 4] = n2.y;
				model.normals[(i * 9) + 5] = n2.z;

				if (model.texCoords != null) {
					Vector2f t2 = texCoords[(int) face.getTexCoord().y - 1];
					model.texCoords[(i * 6) + 2] = t2.x;
					model.texCoords[(i * 6) + 3] = t2.y;
				}
			}

			{
				Vector3f v3 = positions[(int) face.getPosition().z - 1];
				model.positions[(i * 9) + 6] = v3.x;
				model.positions[(i * 9) + 7] = v3.y;
				model.positions[(i * 9) + 8] = v3.z;

				Vector3f n3 = normals[(int) face.getNormal().z - 1];
				model.normals[(i * 9) + 6] = n3.x;
				model.normals[(i * 9) + 7] = n3.y;
				model.normals[(i * 9) + 8] = n3.z;

				if (model.texCoords != null) {
					Vector2f t3 = texCoords[(int) face.getTexCoord().z - 1];
					model.texCoords[(i * 6) + 4] = t3.x;
					model.texCoords[(i * 6) + 5] = t3.y;
				}
			}

			i++;
		}
	}
}
