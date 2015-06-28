package exort.level;

import java.util.*;

import exort.entity.*;
import exort.entity.creature.*;
import exort.util.loaders.*;

public class Level {
	private Player player;

	private List<Entity> entities;

	private volatile boolean entitiesLocked;

	public Level(Player player) {
		this.player = player;
		if (player != null) {
			this.entities.add(player);
		}
		this.entities = new ArrayList<Entity>();
		this.entitiesLocked = false;
	}

	public Level() {
		this(null);
	}

	public void tick(int delta) {
		this.entitiesLocked = true;
		Iterator<Entity> iterator = this.entities.iterator();
		while (iterator.hasNext()) {
			Entity entity = iterator.next();

			if (entity != null) {
				if (entity.isRemoved()) {
					iterator.remove();
				}
				entity.tick(delta);
			}
		}
		this.entitiesLocked = false;
	}

	public void render() {
		this.renderLevel();
		this.renderEntities();
	}

	public void renderLevel() {
		Models.get("arena").draw();
	}

	public void renderEntities() {
		this.entitiesLocked = true;
		Iterator<Entity> iterator = this.entities.iterator();
		while (iterator.hasNext()) {
			iterator.next().render();
		}
		this.entitiesLocked = false;
	}

	// Getters and setters
	public synchronized void movePlayer(String username, float x, float y, float z) {
		int index = this.getPlayerIndex(username);
		Player player = (Player) this.entities.get(index);
		player.setTargetPosition(x, z);
	}

	public synchronized void addEntity(Entity entity) {
		while (this.entitiesLocked) {
			;
		}
		this.entities.add(entity);
	}

	public synchronized void addMainPlayer(Player player) {
		while (this.entitiesLocked) {
			;
		}
		this.player = player;
		this.entities.add(player);
	}

	public Player getPlayer(String username) {
		int index = 0;
		for (Entity entity : this.entities) {
			if ((entity instanceof Player) && ((Player) entity).getUsername().equals(username)) {
				break;
			}
			index++;
		}
		return (Player) this.entities.get(index);
	}

	public synchronized void removePlayer(String username) {
		int index = 0;
		for (Entity entity : this.entities) {
			if ((entity instanceof Player) && ((Player) entity).getUsername().equals(username)) {
				break;
			}
			index++;
		}
		this.entities.remove(index);
	}

	private int getPlayerIndex(String name) {
		int index = 0;
		for (Entity entity : this.entities) {
			if ((entity instanceof Player) && ((Player) entity).getUsername().equals(name)) {
				break;
			}
			index++;
		}
		return index;
	}

	// Getters and Setters
	public Player getMainPlayer() {
		return this.player;
	}

	public void setMainPlayer(Player player) {
		this.player = player;
	}

	public List<Entity> getEntities() {
		return this.entities;
	}

	public boolean entitiesLocked() {
		return this.entitiesLocked;
	}
}
