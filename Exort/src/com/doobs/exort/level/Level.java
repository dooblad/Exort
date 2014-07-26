package com.doobs.exort.level;

import static org.lwjgl.opengl.GL11.*;

import java.util.*;

import res.models.*;

import com.doobs.exort.entity.*;
import com.doobs.exort.entity.creature.*;

public class Level {
	private NetPlayer player;

	private int width, height;
	private byte[] tiles;
	private List<Entity> entities = new ArrayList<Entity>();

	public Level(NetPlayer player) {
		this.player = player;
		if(player != null)
			entities.add(player);
		
		width = 16;
		height = 14;
		tiles = new byte[width * height];
		for (int i = 0; i < tiles.length; i++) {
			int random = (int) (Math.random() * 12);
			if (random <= 8)
				tiles[i] = 0;
			else if (random == 9)
				tiles[i] = 1;
			else if (random > 9)
				tiles[i] = 2;
		}
	}
	
	public Level() {
		this(null);
	}

	public void tick(int delta) {
		for (Entity entity : entities) {
			if (entity instanceof NetPlayer) {
				((NetPlayer) entity).tick(delta);
			}
		}
	}

	public void render() {
		renderLevel();
		renderEntities();
	}
	
	public void renderLevel() {
		glColor4f(1f, 1f, 1f, 1f);
		Models.stillModels.get("arena").draw();
	}
	
	public void renderEntities() {
		for(Entity entity : entities) {
			if(entity instanceof NetPlayer)
				((NetPlayer) entity).render();
		}
	}

	// Getters and setters
	public synchronized void movePlayer(String username, float x, float z) {
		int index = getPlayerIndex(username);
		NetPlayer player = (NetPlayer) entities.get(index);
		player.setTargetPosition(x, z);
	}

	public void addEntity(Entity entity) {
		entities.add(entity);
	}
	
	public void addApplicationPlayer(NetPlayer player) {
		this.player = player;
		entities.add(player);
	}

	public void removePlayer(String name) {
		int index = 0;
		for (Entity entity : entities) {
			if (entity instanceof NetPlayer && ((NetPlayer) entity).getUsername().equals(name)) {
				break;
			}
			index++;
		}
		entities.remove(index);
	}

	private int getPlayerIndex(String name) {
		int index = 0;
		for (Entity entity : entities) {
			if (entity instanceof NetPlayer && ((NetPlayer) entity).getUsername().equals(name)) {
				break;
			}
			index++;
		}
		return index;
	}

	// Getters and Setters
	public NetPlayer getPlayer() {
		return player;
	}
	
	public void setPlayer(NetPlayer player) {
		this.player = player;
	}
}
