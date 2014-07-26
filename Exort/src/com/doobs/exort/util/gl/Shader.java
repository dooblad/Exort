package com.doobs.exort.util.gl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.*;
import java.nio.*;
import java.util.*;

public class Shader {
	private int program;
	private int vertexShader, fragmentShader;

	private String vertexSource, fragmentSource;

	private Map<String, Integer> attributeLocations = new HashMap<String, Integer>();
	private Map<String, Integer> uniformLocations = new HashMap<String, Integer>();

	public Shader(String URL) {
		this(URL, (String[]) null);
	}

	/**
	 * 
	 * @param permutation
	 *            The string to concatenate to the front of the shader to define
	 *            a permutation.
	 * @param URL
	 *            The location of the shader program.
	 */
	public Shader(String URL, String... permutations) {
		program = glCreateProgram();
		vertexShader = glCreateShader(GL_VERTEX_SHADER);
		fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		String vertexSource = loadProgram("res/shaders/" + URL + ".vert", permutations);
		String fragmentSource = loadProgram("res/shaders/" + URL + ".frag", permutations);
		compile(vertexSource, fragmentSource);
	}

	public String loadProgram(String URL, String... permutations) {
		String source = "";

		try {
			BufferedReader reader = new BufferedReader(new FileReader(URL));
			String line;

			while ((line = reader.readLine()) != null) {
				if (permutations != null && source.equals("") && line.contains("#version")) {
					source += line + '\n';

					// Add permutations after version define
					for (String permutation : permutations) {
						source += "#define " + permutation + '\n';
					}
				} else
					source += line + '\n';
			}
			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return source;
	}

	public void compile(String vertexSource, String fragmentSource) {
		// Vertex
		glShaderSource(vertexShader, vertexSource);
		glCompileShader(vertexShader);
		if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Vertex shader not compiled.");
			System.err.println(glGetShaderInfoLog(vertexShader, 1024));
		}

		// Fragment
		glShaderSource(fragmentShader, fragmentSource);
		glCompileShader(fragmentShader);
		if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Fragment shader not compiled.");
			System.err.println(glGetShaderInfoLog(fragmentShader, 1024));
		}

		// Program
		glAttachShader(program, vertexShader);
		glAttachShader(program, fragmentShader);
		glLinkProgram(program);
		glValidateProgram(program);

		// Attributes
		int numAttributes = glGetProgrami(program, GL_ACTIVE_ATTRIBUTES);
		int maxAttributeLength = glGetProgrami(program, GL_ACTIVE_ATTRIBUTE_MAX_LENGTH);
		for (int i = 0; i < numAttributes; i++) {
			String name = glGetActiveAttrib(program, i, maxAttributeLength);
			int location = glGetAttribLocation(program, name);
			System.out.println(name + ":" + location);
			attributeLocations.put(name, location);
		}

		// Uniforms
		int numUniforms = glGetProgrami(program, GL_ACTIVE_UNIFORMS);
		int maxUniformLength = glGetProgrami(program, GL_ACTIVE_UNIFORM_MAX_LENGTH);
		for (int i = 0; i < numUniforms; i++) {
			String name = glGetActiveUniform(program, i, maxUniformLength);
			int location = glGetUniformLocation(program, name);
			uniformLocations.put(name, location);
			System.out.println(name + ":" + location);
		}
	}

	public void use() {
		glUseProgram(program);
	}

	public void cleanup() {
		glDeleteProgram(program);
		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);
	}

	public void printProgram() {
		printVertexShader();
		printFragmentShader();
	}

	public void printVertexShader() {
		System.out.println(vertexSource);
	}

	public void printFragmentShader() {
		System.out.println(fragmentSource);
	}

	// Getters and Setters
	public int getID() {
		return program;
	}

	public Map<String, Integer> getAttributeLocations() {
		return attributeLocations;
	}

	public Map<String, Integer> getUniformLocations() {
		return uniformLocations;
	}

	public void setUniformMatrix4(String uniformName, boolean transpose, FloatBuffer matrixdata) {
		int location = uniformLocations.get(uniformName);
		glUniformMatrix4(location, transpose, matrixdata);
	}

	public void setUniformMatrix3(String uniformName, boolean transpose, FloatBuffer matrixdata) {
		int location = uniformLocations.get(uniformName);
		glUniformMatrix3(location, transpose, matrixdata);
	}

	public void setUniform1i(String uniformName, int i) {
		int location = uniformLocations.get(uniformName);
		glUniform1i(location, i);
	}

	public void setUniform1f(String uniformName, float f) {
		int location = uniformLocations.get(uniformName);
		glUniform1f(location, f);
	}

	public void setUniform3f(String uniformName, float v1, float v2, float v3) {
		int location = uniformLocations.get(uniformName);
		glUniform3f(location, v1, v2, v3);
	}

	public void setUniform4f(String uniformName, float v1, float v2, float v3, float v4) {
		int location = uniformLocations.get(uniformName);
		glUniform4f(location, v1, v2, v3, v4);
	}
}
