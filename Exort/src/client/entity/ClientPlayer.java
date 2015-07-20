package client.entity;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.*;

import shared.entity.creature.*;
import shared.level.*;
import shared.net.packets.*;
import shared.util.*;
import client.net.*;
import client.util.*;
import client.util.loaders.*;

import com.doobs.modern.util.*;
import com.doobs.modern.util.batch.*;
import com.doobs.modern.util.matrix.*;

/**
 * A Player that can accept input from the user.
 */
public class ClientPlayer extends Player {
	private Client client;
	private InputHandler input;

	/**
	 * Creates a ClientPlayer with no Client, ID, or InputHandler yet.
	 */
	public ClientPlayer(String username, Level level) {
		super(username, level);
	}

	/**
	 * Creates a ClientPlayer with no Client or InputHandler yet.
	 */
	public ClientPlayer(String username, Integer id, Level level) {
		super(username, id, level);
	}

	/**
	 * Creates a ClientPlayer at the origin.
	 */
	public ClientPlayer(Client client, String username, Integer id, InputHandler input, Level level) {
		this(0, 0, client, username, id, input, level);
	}

	/**
	 * Creates a ClientPlayer at ("x", "z").
	 */
	public ClientPlayer(float x, float z, Client client, String username, Integer id, InputHandler input, Level level) {
		super(username, id, level);
		this.client = client;
		this.input = input;
	}

	public void tick(int delta) {
		super.tick(delta);

		// Net code.
		if (!Mouse.isGrabbed()) {
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

	public void render() {
		super.render();

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
	 * Makes this Player accept input from "input".
	 */
	public void setInput(InputHandler input) {
		this.input = input;
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
}
