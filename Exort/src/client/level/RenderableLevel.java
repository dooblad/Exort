package client.level;

import java.util.*;

import shared.entity.*;
import shared.level.*;
import client.util.loaders.*;

/**
 * Allows the map and its Entity's to be rendered.
 */
public class RenderableLevel extends Level {
	public static final String MAP_NAME = "arena";

	public RenderableLevel() {
		super();
	}

	public void render() {
		this.renderMap();
		this.renderEntities();
	}

	/**
	 * Renders only the map.
	 */
	public void renderMap() {
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
}
