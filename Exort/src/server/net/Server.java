package server.net;

import java.net.*;

import server.ui.*;
import shared.entity.*;
import shared.level.*;
import shared.net.*;
import shared.net.packets.*;

/**
 * Handles Server-side networking for the game.
 */
public class Server {
	private UI ui;

	private Level level;

	private Player[] players;

	private PacketIO handler;

	/**
	 * Creates a Server using "ui" to send messages directly to chat and uses "level" to
	 * keep track of the master gamestate.
	 */
	public Server(UI ui, Level level) {
		this.ui = ui;

		this.level = level;

		// Initialize all possible slots so we can assign any ID between 0 and
		// MAX_PLAYERS without any IndexOutOfBoundsExceptions.
		this.players = new Player[NetVariables.MAX_PLAYERS];

		this.handler = new PacketIO(ui, this, level);

		ui.addMessage("Started server on port " + this.handler.getPort());
	}

	/**
	 * Moves a Player based on the data in the incoming "packet", then forwards "packet"
	 * to all other clients.
	 */
	public void handleMove(Packet02Move packet) {
		this.players[packet.getPlayerID()].setTargetPosition(packet.getX(), packet.getZ());
		packet.sendData(this);
	}

	/**
	 * Assigns an ID to "player", synchronizes the world state between all other Players,
	 * and adds "player" to the game.
	 */
	public void addPlayer(String username, InetAddress address, int port) {
		// Assign an ID while creating the Player.
		Player player = new Player(username, this.findID(), address, port, this.level);

		// Confirm the login (to the player).
		this.handler.sendData(new Packet00Login(player.getUsername(), player.getID()).getData(), player.getAddress(), player.getPort());

		for (Player other : this.players) {
			if (other != null) {
				// Inform "player" of existing Players.
				this.handler.sendData(new Packet00Login(other.getUsername(), other.getID()).getData(), player.getAddress(), player.getPort());
				this.handler.sendData(new Packet02Move(other.getID(), other.getX(), other.getZ()).getData(), player.getAddress(), player.getPort());
				// Inform existing Players of "player".
				this.handler.sendData(new Packet00Login(player.getUsername(), player.getID()).getData(), other.getAddress(), other.getPort());
			}
		}

		this.players[player.getID()] = player;
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
		int id = packet.getID();

		// Tell everybody he's gone.
		packet.sendData(this);

		this.ui.addMessage(this.players[packet.getID()].getUsername() + " has left the game.");

		// Remove from Level and here.
		this.players[id].remove();
		this.players[id] = null;
	}

	/**
	 * Returns the Player with "id".
	 */
	public Player getPlayer(int id) {
		return this.players[id];
	}

	/**
	 * Sends "data" to all connected Clients.
	 */
	public void sendDataToAllClients(byte[] data) {
		for (Player player : this.players) {
			if (player != null) {
				this.handler.sendData(data, player.getAddress(), player.getPort());
			}
		}
	}

	/**
	 * Exits all networking processes.
	 */
	public void exit() {
		this.handler.exit();
	}
}