package server.net;

import java.net.*;

import shared.*;
import shared.entity.creature.*;
import shared.level.*;
import shared.net.*;
import shared.net.packets.*;

/**
 * Handles Server-side networking for the game.
 */
public class Server extends Networker {
	/**
	 * Creates a Server using "ui" to send messages directly to chat and uses "level" to
	 * keep track of the master gamestate.
	 */
	public Server(UI ui, Level level) {
		super(ui, level);
	}

	/**
	 * Moves a Player based on the data in the incoming "packet", then forwards "packet"
	 * to all other clients.
	 */
	public void handleMove(Packet02Move packet) {
		super.handleMove(packet);
		packet.sendData(this);
	}

	public void handleChat(Packet03Chat packet) {
		super.handleChat(packet);
		this.sendData(packet.getData());
	}

	public void handleSonicWave(Packet04SonicWave packet) {
		super.handleSonicWave(packet);
		this.sendData(packet.getData());
	}

	public void handleRockWall(Packet05RockWall packet) {
		super.handleRockWall(packet);
		this.sendData(packet.getData());
	}

	/**
	 * Assigns an ID to "player", synchronizes the world state between all other Players,
	 * and adds "player" to the game.
	 */
	public void addPlayer(Packet00Login packet, InetAddress address, int port) {
		String username = packet.getUsername();

		// Assign an ID while creating the Player.
		int id = this.findID();
		Player player = new Player(username, id, address, port, this.level);

		// Confirm the login (to the player).
		this.packetIO.sendData(new Packet00Login(username, id).getData(), address, player.getPort());

		for (Player other : this.players) {
			if (other != null) {
				// Inform "player" of existing Players.
				this.packetIO.sendData(new Packet00Login(other.getUsername(), other.getID()).getData(), address, player.getPort());
				this.packetIO.sendData(new Packet02Move(other.getID(), other.getX(), other.getZ()).getData(), address, player.getPort());
				// Inform existing Players of "player".
				this.packetIO.sendData(new Packet00Login(username, id).getData(), other.getAddress(), other.getPort());
			}
		}

		this.players[id] = player;
		this.level.addEntity(player);

		this.ui.addMessage(username + " has joined the game.");
	}

	/**
	 * Returns the first null index in "this.players". If no index is found, returns -1.
	 */
	private int findID() {
		for (int i = 0; i < this.players.length; i++) {
			if (this.players[i] == null) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Removes the Player designated by "packet" from the game.
	 */
	public void removePlayer(Packet01Disconnect packet) {
		super.removePlayer(packet);
		// Tell everybody he's gone.
		packet.sendData(this);
	}

	/**
	 * Sends "data" to all connected Clients.
	 */
	public void sendData(byte[] data) {
		for (Player player : this.players) {
			if (player != null) {
				this.packetIO.sendData(data, player.getAddress(), player.getPort());
			}
		}
	}
}