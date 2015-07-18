package server.entity.creature;

import java.net.*;
import java.util.*;

import server.entity.*;
import server.entity.projectile.*;
import server.level.*;
import shared.sat.*;
import shared.util.*;

/**
 * A Player with networking capabilities.
 */
public class Player extends MovingEntity {
	private float targetX, targetZ;
	private float moveSpeed;

	// Net variables.
	private String username;
	// Allows null values to be represented.
	private Integer id;
	private InetAddress address;
	private int port;

	/**
	 * Creates a 'blank' Player at the origin with no associated Level.
	 */
	public Player() {
		this(0, 0, 0, null, null, null, -1, null);
	}

	/**
	 * Creates a Player at ("x", "y", "z") on "level".
	 */
	public Player(float x, float y, float z, Level level) {
		this(x, y, z, null, null, null, -1, null);
	}

	/**
	 * Creates a Player on "level" with "username" and "id".
	 */
	public Player(String username, Integer id, Level level) {
		this(0, 0, 0, username, id, null, -1, level);
	}

	/**
	 * Creates a Player at the origin with "username" residing at "address":"port".
	 */
	public Player(String username, InetAddress address, int port) {
		this(0, 0, 0, username, null, address, port, null);
	}

	/**
	 * Creates a Player with networking capabilities at the origin on "level".
	 */
	public Player(String username, InetAddress address, int port, Level level) {
		this(0, 0, 0, username, null, address, port, level);
	}

	/**
	 * Creates a Player on "level" with "username" and "id" residing at "address":"port".
	 */
	public Player(String username, Integer id, InetAddress address, int port, Level level) {
		this(0, 0, 0, username, id, address, port, level);
	}

	/**
	 * Creates a Player at ("x", "y", "z") on "level" with "username" and "id" residing at
	 * "address":"port".
	 */
	public Player(float x, float y, float z, String username, Integer id, InetAddress address, int port, Level level) {
		this.targetX = 0;
		this.targetZ = 0;
		this.xv = 0;
		this.zv = 0;
		this.moveSpeed = 1f / 50f;
		this.bb = new OBB(x, 2.5f, z, 2.5f);
		this.level = level;
		// Net variables.
		this.username = username;
		this.id = id;
		this.address = address;
		this.port = port;
	}

	/**
	 * Handles the majority of the logic of this Player.
	 */
	public void tick(int delta) {
		// If close to target, snap Player's position to it.
		if (Math.abs((this.x + this.xv) - this.targetX) > Math.abs(this.x - this.targetX)) {
			this.x = this.targetX;
			this.xv = 0;
		}
		if (Math.abs((this.z + this.zv) - this.targetZ) > Math.abs(this.z - this.targetZ)) {
			this.z = this.targetZ;
			this.zv = 0;
		}

		// Position and BB update.
		super.tick(delta);

		// Check collisions.
		Iterator<Entity> iterator = this.level.getEntities().iterator();
		while (iterator.hasNext()) {
			Entity entity = iterator.next();
			Vector2f mtv = null;
			if ((entity != null) && (entity != this) && ((mtv = this.bb.colliding(entity.getBB())) != null)) {
				if (entity instanceof Projectile) {
					Projectile p = (Projectile) entity;
					// Replace this with hit reaction.
					if (p.getOwner() != this) {
						System.out.println(this);
					}
				} else if ((entity instanceof RockWall) || (entity instanceof Player)) {
					this.move(mtv);
					this.calculateSpeeds();
				}
			}
		}
	}

	/**
	 * Sets the coordinates of this Player's target to ("x", "z").
	 */
	public void setTargetPosition(float x, float z) {
		if ((x != this.targetX) || (z != this.targetZ)) {
			this.targetX = x;
			this.targetZ = z;
			this.calculateSpeeds();
			this.bb.rotate(this.direction);
		}
	}

	/**
	 * Calculates the component-wise velocities required to reach this Player's target and
	 * updates the Player's direction.
	 */
	public void calculateSpeeds() {
		Vector2f target = new Vector2f(this.targetX - this.x, this.targetZ - this.z);
		if ((target.getX() != 0) || (target.getZ() != 0)) {
			target.normalize();
			this.xv = target.getX() * this.moveSpeed;
			this.zv = target.getZ() * this.moveSpeed;
		}
		this.direction = TrigUtil.calculateAngle(this.targetX - this.x, this.targetZ - this.z);
	}

	/**
	 * Returns a String representing the username and id of this Player.
	 */
	public String toString() {
		return "[\"" + this.username + "\":" + this.id + "]";
	}

	/**
	 * Returns this Player's current move speed.
	 */
	public float getMoveSpeed() {
		return this.moveSpeed;
	}

	/**
	 * Sets this Player's move speed to "moveSpeed".
	 */
	public void setMoveSpeed(float moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

	/**
	 * Returns the username of this Player.
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * Returns the ID of this Player.
	 */
	public int getID() {
		return this.id;
	}

	/**
	 * Sets the ID of this Player to "id".
	 */
	public void setID(Integer id) {
		this.id = id;
	}

	/**
	 * Returns the IP address of this Player.
	 */
	public InetAddress getAddress() {
		return this.address;
	}

	/**
	 * Returns the port this Client is operating on.
	 */
	public int getPort() {
		return this.port;
	}

	/**
	 * Sets this Client's username to "username".
	 */
	public void setUsername(String username) {
		this.username = username;
	}
}
