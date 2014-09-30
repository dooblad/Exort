package com.doobs.exort.level;

import java.util.*;

import com.doobs.exort.entity.*;
import com.doobs.exort.entity.creature.*;
import com.doobs.exort.entity.projectile.*;
import com.doobs.exort.util.loaders.*;

public class Level {
	private NetPlayer player;

	private List<Entity> entities;

	private volatile boolean entitiesLocked;

	public Level(NetPlayer player) {
		this.player = player;
		if (player != null)
			entities.add(player);
		entities = new ArrayList<Entity>();
		entitiesLocked = false;
	}

	public Level() {
		this(null);
	}

	public void tick(int delta) {
		entitiesLocked = true;
		Iterator<Entity> iterator = entities.iterator();
		while (iterator.hasNext()) {
			Entity entity = iterator.next();

			if (entity != null) {
				if (entity.isRemoved())
					iterator.remove();
				else if (entity instanceof NetPlayer)
					((NetPlayer) entity).tick(delta);
				else if (entity instanceof SonicWave)
					((SonicWave) entity).tick(delta);
			}
		}
		entitiesLocked = false;
	}

	public void render() {
		renderLevel();
		renderEntities();
	}

	public void renderLevel() {
		Models.get("arena").draw();
	}

	public void renderEntities() {
		entitiesLocked = true;
		Iterator<Entity> iterator = entities.iterator();
		while (iterator.hasNext()) {
			Entity entity = iterator.next();

			if (entity instanceof NetPlayer)
				((NetPlayer) entity).render();
			else if (entity instanceof SonicWave)
				((SonicWave) entity).render();
		}
		entitiesLocked = false;
	}

	// Getters and setters
	public synchronized void movePlayer(String username, float x, float y, float z) {
		int index = getPlayerIndex(username);
		NetPlayer player = (NetPlayer) entities.get(index);
		player.setTargetPosition(x, z);
	}

	public synchronized void addEntity(Entity entity) {
		while (entitiesLocked)
			;
		entities.add(entity);
	}

	public synchronized void addMainPlayer(NetPlayer player) {
		while (entitiesLocked)
			;
		this.player = player;
		entities.add(player);
	}

	public NetPlayer getPlayer(String username) {
		int index = 0;
		for (Entity entity : entities) {
			if (entity instanceof NetPlayer && ((NetPlayer) entity).getUsername().equals(username)) {
				break;
			}
			index++;
		}
		return (NetPlayer) entities.get(index);
	}

	public synchronized void removePlayer(String username) {
		int index = 0;
		for (Entity entity : entities) {
			if (entity instanceof NetPlayer && ((NetPlayer) entity).getUsername().equals(username)) {
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
	public NetPlayer getMainPlayer() {
		return player;
	}

	public void setMainPlayer(NetPlayer player) {
		this.player = player;
	}
	
	public List<Entity> getEntities() {
		return entities;
	}
	
	public boolean entitiesLocked() {
		return entitiesLocked;
	}
}
