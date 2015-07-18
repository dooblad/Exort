package shared.level;

import java.util.*;

import shared.entity.*;

/**
 * Keeps track of Entities.
 */
public class Level {
	protected List<Entity> entities;

	// Prevent ConcurrentModificationExceptions.
	protected volatile boolean entitiesLocked;

	/**
	 * Creates an empty Level.
	 */
	public Level() {
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