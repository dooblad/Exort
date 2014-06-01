package com.doobs.exort.entity.creature;

import com.doobs.exort.entity.*;
import com.doobs.exort.level.*;

public class Creature extends MovingEntity {
	private int health;
	
	public Creature() {
		this(0, 0, 0, null);
	}

	public Creature(double x, double z) {
		this(x, 0, z, null);
	}

	public Creature(double x, double y, double z) {
		this(x, y, z, null);
	}

	public Creature(double x, double z, Level level) {
		this(x, 0, z, level);
	}

	public Creature(double x, double y, double z, Level level) {
		super(x, y, z, level);
	}
	
	public Creature(double x, double y, double z, float xa, float ya, float za, Level level) {
		super(x, y, z, xa, ya, za, level);
	}
	
	// Getters and setters
	public int getHealth() {
		return health;
	}
	
	public void setHealth(int health) {
		this.health = health;
	}
}
