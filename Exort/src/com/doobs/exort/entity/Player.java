package com.doobs.exort.entity;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.vector.Vector3f;

import res.models.OBJLoader;

import com.doobs.exort.util.Model;

public class Player {
	private Model model;
	private float x, y, z;
	
	public Player() {
		model = OBJLoader.loadModel("player.obj");
		x = 0;
		y = 0;
		z = 0;
	}
	
	public void tick(int delta) {
		
	}
	
	public void render() {
		glPushMatrix();
		glTranslatef(x, y, -z);
		glColor3f(0.0f, 1.0f, 0.0f);
		glCallList(model.getHandle());
		glColor3f(1.0f, 1.0f, 1.0f);
		glPopMatrix();
	}
	
	public void move(Vector3f position) {
		x = position.getX();
		y = position.getY();
		z = position.getZ();
	}
	
	// Getters and Setters
	public Model getModel() {
		return model;
	}
}
