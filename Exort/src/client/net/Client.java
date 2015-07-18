package client.net;

import java.net.*;

import shared.entity.*;
import shared.level.*;
import shared.net.*;
import shared.net.packets.*;
import client.entity.*;
import client.gui.*;
import client.state.*;
import client.util.*;

/**
 * Handles Client-side networking for the game.
 */
public class Client {
	private GUI gui;

	private Level level;

	private Player[] players;

	private PacketIO packetIO;

	// For setting input for the main Player, when they connect.
	private InputHandler input;
	// For setting the DuelState's main Player, when they connect.
	private DuelState state;
	private boolean mainPlayerConnected;

	/**
	 * Creates a Client connected to a server with "address". Uses "gui" to send messages
	 * directly to chat and uses "level" to interact with the Level according to incoming
	 * packets. "input" is used to set the main Player's input, when they connect.
	 */
	public Client(DuelState state, InetAddress address) {
		this.gui = state.getGUI();
		this.level = state.getLevel();
		this.input = state.getInput();
		this.state = state;

		// Initialize all possible slots so the Server can assign any ID between 0 and
		// MAX_PLAYERS without any IndexOutOfBoundsExceptions.
		this.players = new Player[NetVariables.MAX_PLAYERS];

		this.packetIO = new PacketIO(this, this.gui, address, this.level);

		this.mainPlayerConnected = false;
	}

	/**
	 * Moves a Player based on the data in the incoming "packet".
	 */
	public void handleMove(Packet02Move packet) {
		this.players[packet.getPlayerID()].setTargetPosition(packet.getX(), packet.getZ());
	}

	/**
	 * Adds "player" to this Level. If it's the first Player, it becomes the main Player.
	 */
	public void addPlayer(String username, int id) {
		if (id == -1) {
			this.gui.addToChat("Now ya fucked up...");
		} else {
			Player player;

			// I think there's a chance that, if another Player connects at the perfect
			// time, that Player could become the main player on the actual main
			// Player's client.
			if (!this.mainPlayerConnected) {
				player = new ClientPlayer(this, username, id, this.input, this.level);
				this.state.setPlayer((ClientPlayer) player);
				this.mainPlayerConnected = true;
			} else {
				player = new Player(username, id, this.level);
			}

			this.players[id] = player;
			this.level.addEntity(player);

			this.gui.addToChat(username + " has joined the game.");
		}
	}

	/**
	 * Adds the contents of "packet" to the chat box.
	 */
	public void addChat(Packet03Chat packet) {
		this.gui.addToChat(this.players[packet.getPlayerID()].getUsername() + ": " + packet.getMessage());
	}

	/**
	 * Removes the Player specified by "packet".
	 */
	public void removePlayer(Packet01Disconnect packet) {
		int id = packet.getID();

		this.gui.addToChat(this.players[packet.getID()].getUsername() + " has left the game.");

		// Remove from the Level and here.
		this.players[id].remove();
		this.players[id] = null;
	}

	/**
	 * Pre: Player with "id" exists. Otherwise, throws IllegalArgumentException.
	 *
	 * Returns the Player with "id".
	 */
	public Player getPlayer(int id) {
		if ((id >= this.players.length) || (this.players[id] == null)) {
			throw new IllegalArgumentException("Player with id \"" + id + "\" doesn't exist.");
		}
		return this.players[id];
	}

	/**
	 * Returns the address this Client is connected to.
	 */
	public InetAddress getAddress() {
		return this.packetIO.getAddress();
	}

	/**
	 * Sends the packet specified by "data".
	 */
	public void sendData(byte[] data) {
		this.packetIO.sendData(data);
	}

	/**
	 * Exits all networking processes.
	 */
	public void exit() {
		this.packetIO.exit();
	}
}