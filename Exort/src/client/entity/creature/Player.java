package client.entity.creature;

import static org.lwjgl.opengl.GL11.*;

import java.net.*;
import java.util.*;

import org.lwjgl.input.*;

import shared.sat.*;
import shared.util.*;
import client.entity.*;
import client.entity.projectile.*;
import client.level.*;
import client.net.client.*;
import client.net.packets.*;
import client.util.*;
import client.util.gl.*;
import client.util.loaders.*;

import com.doobs.modern.util.*;
import com.doobs.modern.util.batch.*;
import com.doobs.modern.util.matrix.*;

/**
 * A Player with networking capabilities.
 */
public class Player extends MovingEntity {
	private InputHandler input;

	private float targetX, targetZ;
	private float moveSpeed;

	// Net variables.
	private Client client;
	private String username;
	private int id;
	private InetAddress address;
	private int port;

	/**
	 * Creates a Player at the origin with no associated Level or InputHandler.
	 */
	public Player() {
		this(0, 0, null, null, null, null, -1, null, -1);
	}

	/**
	 * Creates a Player at ("x", "z") on "level" with movement specified by "input".
	 */
	public Player(float x, float z, InputHandler input, Level level) {
		this(x, z, input, level, null, null, -1, null, -1);
	}

	/**
	 * Creates a Player on "level" with "username" and "id".
	 */
	public Player(String username, int id, Level level) {
		this(0, 0, null, level, null, username, id, null, -1);
	}

	/**
	 * Creates a Player on "level" with "username" and "id" at "address":"port".
	 */
	public Player(String username, int id, InetAddress address, int port, Level level) {
		this(0, 0, null, level, null, username, id, address, port);
	}

	/**
	 * Creates a Player at the origin with networking capabilities.
	 */
	public Player(Client client, String username, InetAddress address, int port) {
		this(0, 0, null, null, client, username, -1, address, port);
	}

	/**
	 * Creates a Player with networking capabilities at the origin on "level" with
	 * movement specified by "input".
	 */
	public Player(InputHandler input, Level level, Client client, String username, InetAddress address, int port) {
		this(0, 0, input, level, client, username, -1, address, port);
	}

	/**
	 * Creates a Player with networking capabilites at ("x", "z") on "level" with movement
	 * specified by "input".
	 */
	public Player(float x, float z, InputHandler input, Level level, Client client, String username, int id, InetAddress address, int port) {
		// TODO: Figure out why we're not using the super constructor here.
		this.targetX = 0;
		this.targetZ = 0;
		this.xv = 0;
		this.zv = 0;
		this.moveSpeed = 1f / 50f;
		this.bb = new OBB(x, 2.5f, z, 2.5f);
		this.input = input;
		this.level = level;
		// Net variables.
		this.client = client;
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

		// Net code.
		if ((this.client != null) && !Mouse.isGrabbed()) {
			// Move.
			if (this.input.isMouseButtonPressed(1)) {
				this.client.sendData(new Packet02Move(this.id, RayCast.mouseX, RayCast.mouseZ).getData());
			}
			// Sonic wave.
			if (this.input.isKeyReleased(Keyboard.KEY_Q)) {
				new Packet04SonicWave(this.id, TrigUtil.calculateAngle(RayCast.mouseX - this.x, RayCast.mouseZ - this.z)).sendData(this.client);
			}
			// Rock wall.
			if (this.input.isKeyReleased(Keyboard.KEY_W)) {
				new Packet05RockWall(this.id, TrigUtil.calculateAngle(RayCast.mouseX - this.x, RayCast.mouseZ - this.z), RayCast.mouseX, RayCast.mouseZ)
						.sendData(this.client);
			}
		}
	}

	/**
	 * Renders this Player and all graphical entities associated with it.
	 */
	public void render() {
		this.bb.render();

		Shaders.use("lighting");

		// Update lighting.
		Lighting.setPosition(this.x, 8f, this.z);

		// Player.
		Matrices.translate(this.x, this.y, this.z);
		Matrices.rotate(Math.toDegrees(this.direction), 0.0, 1.0, 0.0);
		Matrices.sendMVPMatrix(Shaders.current);
		Color.set(Shaders.current, 1f, 0f, 0f, 1f);
		Models.get("player").draw();
		Matrices.rotate(Math.toDegrees(this.direction), 0.0, -1.0, 0.0);
		Matrices.translate(-this.x, -this.y, -this.z);

		// Move command.
		Matrices.translate(this.targetX, 0, this.targetZ);
		Matrices.sendMVPMatrix(Shaders.current);
		if ((this.xv != 0) || (this.zv != 0)) {
			Color.set(Shaders.current, 0f, 1f, 0f, 1f);
			Models.get("move").draw();
		}

		// Reset matrix and color.
		Matrices.translate(-this.targetX, -this.y, -this.targetZ);
		Matrices.sendMVPMatrix(Shaders.current);
		Color.set(Shaders.current, 1f, 1f, 1f, 1f);

		// Ability indicators.
		if (this.client != null) {
			if (this.input.isKeyDown(Keyboard.KEY_Q)) {
				glEnable(GL_BLEND);
				Shaders.use("texture");
				Color.set(Shaders.current, 1f, 1f, 1f, 1f);
				Textures.get("qIndicator").bind();

				// Move to Player's position and raise slightly off the ground.
				Matrices.translate(this.x, 0.5f, this.z);
				Matrices.rotate((float) Math.toDegrees(TrigUtil.calculateAngle(RayCast.mouseX - this.x, RayCast.mouseZ - this.z)), 0f, 1f, 0f);
				Matrices.sendMVPMatrix(Shaders.current);

				new SimpleBatch(GL_TRIANGLES, 3, new float[] { 0f, 0f, 1f, 20f, 0f, 1f, 20f, 0f, -1f, 20f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, 1f }, null, null,
						new float[] { 0f, 0f, 1f, 0f, 1f, 1f, 1f, 1f, 0f, 1f, 0f, 0f }, null).draw(Shaders.current.getAttributeLocations());

				Matrices.rotate((float) -Math.toDegrees(TrigUtil.calculateAngle(RayCast.mouseX - this.x, RayCast.mouseZ - this.z)), 0f, 1f, 0f);
				Matrices.translate(-this.x, -0.5f, -this.z);

				Shaders.use("lighting");
				glDisable(GL_BLEND);
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
	 * Makes this Player accept input from "input".
	 */
	public void setInput(InputHandler input) {
		this.input = input;
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
	 * Returns the Client associated with this Player.
	 */
	public Client getClient() {
		return this.client;
	}

	/**
	 * Sets this Player's Client to "client".
	 */
	public void setClient(Client client) {
		this.client = client;
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
	public void setID(int id) {
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
