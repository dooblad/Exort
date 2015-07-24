package client.util.dae;

import java.util.*;

import client.util.*;
import client.util.obj.*;

public class ColladaLoader {
	private static Stack<XMLTag> tagStack;

	public static Model load(String URL) {
		Model model = new Model();
		// Floats.
		Number[] positions = null, normals = null;
		// Integers.
		Number[] indices = null;
		int vertexOffset = 0, normalOffset = 0;

		tagStack = new Stack<XMLTag>();
		TagReader reader = new TagReader(URL);
		XMLTag tag = reader.next();
		while (tag != null) {
			if ((tag.isOpening() || tag.isSelfClosing()) && !tagStack.isEmpty()) {
				String parentName = tagStack.peek().getName();
				String name = tag.getName();
				if (parentName.equals("source")) {
					if (name.equals("float_array")) {
						String id = tag.getAttribute("id");
						Number[] data = FileReadingUtils.extractNumbers(reader.getLine(), tag.end(), Integer.parseInt(tag.getAttribute("count")));
						if (id.contains("positions-array")) {
							positions = data;
						} else if (id.contains("normals-array")) {
							normals = data;
						}
					}
				} else if (parentName.equals("polylist")) {
					if (name.equals("input")) {
						String semantic = tag.getAttribute("semantic");
						int offset = Integer.parseInt(tag.getAttribute("offset"));
						if (semantic.equals("VERTEX")) {
							vertexOffset = offset;
						} else if (semantic.equals("NORMAL")) {
							normalOffset = offset;
						}

					} else if (name.equals("p")) {
						// TODO: Replace two with a variable that corresponds with the
						// number of vertex attributes being represented in this tag.
						int count = Integer.parseInt(tagStack.peek().getAttribute("count")) * 3 * 2;
						indices = FileReadingUtils.extractNumbers(reader.getLine(), tag.end(), count);
					}
				}
			}
			updateStack(tag);
			tag = reader.next();
		}
		reader.close();

		int stride = 2;
		model.positions = new float[3 * indices.length];
		model.normals = new float[3 * indices.length];
		for (int i = 0; i < indices.length; i += stride) {
			// Positions.
			int index = indices[i + vertexOffset].intValue();
			for (int j = 0; j < 3; j++) {
				model.positions[i + j] = positions[index + j].floatValue();
			}
			// Normals.
			index = indices[i + normalOffset].intValue();
			for (int j = 0; j < 3; j++) {
				model.normals[i + j] = normals[index + j].floatValue();
			}
		}

		model.generate();
		return model;
	}

	private static void updateStack(XMLTag tag) {
		if (tag.isOpening() && !tag.isSelfClosing()) {
			tagStack.push(tag);
		} else if (tag.isClosing() && tag.getName().equals(tagStack.peek().getName())) {
			tagStack.pop();
		}
	}

	public static void main(String[] args) {
		load("res/models/cube.dae");
	}
}
