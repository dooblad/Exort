package com.doobs.exort.level;

import res.models.OBJLoader;

import com.doobs.exort.util.Model;

import static org.lwjgl.opengl.GL11.*;

public class Map {
	private Model model;

	public Map() {
		this.model = OBJLoader.loadModel("map.obj");
	}

	public void tick(int delta) {
		
	}

	public void render() {
		glColor3f(1.0f, 0.0f, 0.0f);
		glCallList(model.getHandle());
		glColor3f(1.0f, 1.0f, 1.0f);
	}

	// Getters and setters
	public Model getModel() {
		return model;
	}
}
