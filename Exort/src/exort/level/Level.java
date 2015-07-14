package exort.level;

import java.util.*;

import exort.entity.*;
import exort.entity.creature.*;
import exort.util.loaders.*;

public class Level {
	// The main Player.
	private Player player;

	private List<Entity> entities;

	// Prevent ConcurrentModificationExceptions.
	private volatile boolean entitiesLocked;

	/**
	 * Creates a Level with no associated Player.
	 */
	public Level() {
		this(null);
	}

	/**
	 * Creates a Level for "player".
	 */
	public Level(Player player) {
		this.player = player;
		if (player != null) {
			this.entities.add(player);
		}
		this.entities = new ArrayList<Entity>();
		this.entitiesLocked = false;
	}

	/**
	 * Handles the behavior of the Entities on this Level.
	 */
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
			} else {
				// Remove null Entities.
				iterator.remove();
			}
		}
		this.entitiesLocked = false;
	}

	/**
	 * Renders this Level and the Entities on it.
	 */
	public void render() {
		this.renderLevel();
		this.renderEntities();
	}

	/**
	 * Renders the Model labelled 'arena'.
	 */
	public void renderLevel() {
		Models.get("arena").draw();
	}

	/**
	 * Iterates through all Entities on this Level and renders them.
	 */
	public void renderEntities() {
		// Wait until it's safe to add to "entities".
		while (this.entitiesLocked) {
			;
		}
		this.entitiesLocked = true;
		Iterator<Entity> iterator = this.entities.iterator();
		while (iterator.hasNext()) {
			iterator.next().render();
		}
		this.entitiesLocked = false;
	}

	/**
	 * Adds "entity" to this Level.
	 */
	public synchronized void addEntity(Entity entity) {
		// Wait until it's safe to add to "entities".
		while (this.entitiesLocked) {
			;
		}

		this.entities.add(entity);
	}

	/**
	 * Adds "player" to this Level and makes it the main Player.
	 */
	public synchronized void addMainPlayer(Player player) {
		// Wait until it's safe to add to "entities".
		while (this.entitiesLocked) {
			;
		}

		this.player = player;
		this.entities.add(player);
	}

	/**
	 * Returns the main Player of this Level.
	 */
	public Player getMainPlayer() {
		return this.player;
	}

	/**
	 * Sets the main Player of this Level to "player".
	 */
	public void setMainPlayer(Player player) {
		this.player = player;
	}

	/**
	 * Returns the List of all Entities on this Level.
	 */
	public List<Entity> getEntities() {
		return this.entities;
	}

	/**
	 * Returns true if the List of Entities is being used by another method.
	 */
	public synchronized boolean entitiesLocked() {
		return this.entitiesLocked;
	}
}
