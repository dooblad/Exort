package exort.entity.creature;

import static org.lwjgl.opengl.GL11.*;

import java.util.*;

import org.lwjgl.input.*;
import org.lwjgl.util.vector.*;

import com.doobs.modern.util.*;
import com.doobs.modern.util.batch.*;
import com.doobs.modern.util.matrix.*;

import exort.*;
import exort.entity.*;
import exort.level.*;
import exort.math.*;
import exort.net.client.*;
import exort.net.packets.*;
import exort.util.*;
import exort.util.gl.*;
import exort.util.loaders.*;
import exort.util.sat.*;

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
	private String address;
	private int port;

	/**
	 * Creates a Player at the origin with no associated Level or InputHandler.
	 */
	public Player() {
		this(0, 0, 0, null, null, null, null, -1, null, -1);
	}

	/**
	 * Creates a Player at ("x", "y", "z") on "level" with movement specified by "input".
	 */
	public Player(double x, double y, double z, InputHandler input, Level level) {
		this(x, y, z, input, level, null, null, -1, null, -1);
	}

	/**
	 * Creates a Player on "level" with "username" and "id".
	 */
	public Player(String username, int id, Level level) {
		this(0, 0, 0, null, level, null, username, id, null, -1);
	}

	/**
	 * Creates a Player on "level" with "username" and "id" at "address":"port".
	 */
	public Player(String username, int id, String address, int port, Level level) {
		this(0, 0, 0, null, level, null, username, id, address, port);
	}

	/**
	 * Creates a Player at the origin with networking capabilities.
	 */
	public Player(Client client, String username, String address, int port) {
		this(0, 0, 0, null, null, client, username, -1, address, port);
	}

	/**
	 * Creates a Player with networking capabilities at the origin on "level" with
	 * movement specified by "input".
	 */
	public Player(InputHandler input, Level level, Client client, String username, String address, int port) {
		this(0, 0, 0, input, level, client, username, -1, address, port);
	}

	/**
	 * Creates a Player with networking capabilites at ("x", "y", "z") on "level" with
	 * movement specified by "input".
	 */
	public Player(double x, double y, double z, InputHandler input, Level level, Client client, String username, int id, String address, int port) {
		this.targetX = 0;
		this.targetZ = 0;
		this.xa = 0;
		this.za = 0;
		this.moveSpeed = 1f / 50f;
		this.bb = new BB((float) x, 2.5f, (float) z, 2.5f);
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
		// Player movement.
		boolean xDone = false, zDone = false;
		if (((this.xa > 0) && ((this.x + (this.xa * delta)) > this.targetX)) || ((this.xa < 0) && ((this.x + (this.xa * delta)) < this.targetX))) {
			this.xa = this.targetX - this.x;
			xDone = true;
		}

		if (((this.za > 0) && ((this.z + (this.za * delta)) > this.targetZ)) || ((this.za < 0) && ((this.z + (this.za * delta)) < this.targetZ))) {
			this.za = this.targetZ - this.z;
			zDone = true;
		}

		// If close to target, snap Player's position to it.
		if (xDone) {
			this.x = this.targetX;
			this.xa = 0;
		}

		if (zDone) {
			this.z = this.targetZ;
			this.za = 0;
		}

		super.tick(delta);

		// Check collisions.
		Iterator<Entity> iterator = this.level.getEntities().iterator();
		while (iterator.hasNext()) {
			Entity entity = iterator.next();
			// TODO: SAT Collision detection.
			// Vector2f mtv = null;
			// if (entity != null && entity != this && (mtv =
			// bb.colliding(entity.getBB())) != null) {
			if (entity instanceof MovingEntity) {
				MovingEntity mEntity = (MovingEntity) entity;

				mEntity.stop();
				this.stop();

				// Divide by 2 so each entity is repelled by an equal amount // (no
				// momentum)
				/*
				 * if (mtv != null) { entity.x += mtv.x; entity.z += mtv.y;
				 *
				 * mEntity.x += -mtv.x; mEntity.z += -mtv.y;
				 *
				 * System.out.println("X: " + mtv.x + " Y: " + mtv.y); } }
				 */
			}
		}

		// Update lighting.
		Lighting.moveLight(new Vector3f((float) this.x, 8f, (float) this.z), false);

		// Net code.
		if ((this.client != null) && !Mouse.isGrabbed()) {
			// Move.
			if (this.input.isMouseButtonDown(1)) {
				this.setTarget(new Vector3f(RayCast.mouseX, 0, RayCast.mouseZ));
			}
			// Sonic wave.
			if (this.input.isKeyReleased(Keyboard.KEY_Q)) {
				new Packet04SonicWave(this.id, this.calculateAngle(RayCast.mouseX - this.x, RayCast.mouseZ - this.z)).sendData(this.client);
			}
			// Rock wall.
			if (this.input.isKeyReleased(Keyboard.KEY_W)) {
				new Packet05RockWall(this.id, this.calculateAngle(RayCast.mouseX - this.x, RayCast.mouseZ - this.z), RayCast.mouseX, RayCast.mouseZ)
				.sendData(this.client);
			}
		}
	}

	/**
	 * Renders this Player and all graphical entities associated with it.
	 */
	public void render() {
		if (Main.debug) {
			this.bb.render();
		}
		Shaders.use("lighting");

		// Model command.
		Matrices.translate(this.x, this.y, this.z);
		Matrices.sendMVPMatrix(Shaders.current);
		Color.set(Shaders.current, 1f, 0f, 0f, 1f);
		Models.get("player").draw();

		// Move command.
		Matrices.translate(this.targetX - this.x, 0, this.targetZ - this.z);
		Matrices.sendMVPMatrix(Shaders.current);
		if ((this.xa != 0) && (this.za != 0)) {
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
				Matrices.rotate((float) Math.toDegrees(this.calculateAngle(RayCast.mouseX - this.x, RayCast.mouseZ - this.z)), 0f, 1f, 0f);
				Matrices.sendMVPMatrix(Shaders.current);

				// TODO: Make some sense of these coordinates.
				new SimpleBatch(GL_TRIANGLES, 3, new float[] { 0f, 0f, 1f, 20f, 0f, 1f, 20f, 0f, -1f, 20f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, 1f }, null, null,
						new float[] { 0f, 0f, 1f, 0f, 1f, 1f, 1f, 1f, 0f, 1f, 0f, 0f }, null).draw(Shaders.current.getAttributeLocations());

				Matrices.rotate((float) -Math.toDegrees(this.calculateAngle(RayCast.mouseX - this.x, RayCast.mouseZ - this.z)), 0f, 1f, 0f);
				Matrices.translate(-this.x, -0.5f, -this.z);

				Shaders.use("lighting");
				glDisable(GL_BLEND);
			}

		}
	}

	/**
	 * Sets this Player's target to "position".
	 */
	public void setTarget(Vector3f position) {
		if ((position.getX() != this.x) || (position.getZ() != this.z)) {
			this.targetX = position.x;
			this.targetZ = position.z;
			this.calculateSpeeds();
		}
		this.client.sendData(new Packet02Move(this.id, position.getX(), position.getZ()).getData());
	}

	/**
	 * Calculates the component-wise velocities required to reach this Player's target.
	 */
	public void calculateSpeeds() {
		Vector3f target = new Vector3f((float) (this.targetX - this.x), 0f, (float) (this.targetZ - this.z));
		if ((target.getX() != 0) || (target.getZ() != 0)) {
			target.normalise();
			this.xa = target.getX() * this.moveSpeed;
			this.za = target.getZ() * this.moveSpeed;
		}
	}

	/**
	 * Returns the angle (in radians) of the line from the origin to ("x", 0, "z"), with
	 * respect to the y-axis.
	 */
	private float calculateAngle(double x, double z) {
		float result = (float) Math.atan(z / x);
		if (x < 0) {
			result += Math.PI;
		}
		return result;
	}

	/**
	 * Returns a String representation of this Player.
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
	 * Sets the x-coordinate of this Player's target to "x".
	 */
	public void setTargetX(float x) {
		this.targetX = x;
		this.calculateSpeeds();
	}

	/**
	 * Sets the z-coordinate of this Player's target to "z".
	 */
	public void setTargetZ(float z) {
		this.targetZ = z;
		this.calculateSpeeds();
	}

	/**
	 * Sets the coordinates of this Player's target to ("x", "z").
	 */
	public void setTargetPosition(float x, float z) {
		this.targetX = x;
		this.targetZ = z;
		this.bb.rotate(-Math.atan(x / z));
		this.calculateSpeeds();
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
	public String getAddress() {
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
