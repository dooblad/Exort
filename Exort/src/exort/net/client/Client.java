package exort.net.client;

import java.net.*;

import exort.entity.creature.*;
import exort.gui.*;
import exort.level.*;
import exort.net.*;
import exort.net.packets.*;
import exort.state.*;
import exort.util.*;

/**
 * Handles Client-side networking for the game.
 */
public class Client {
	private GUI gui;

	private Level level;

	private Player[] players;

	private PacketIO handler;

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
	public Client(DuelState state, String address) {
		this.gui = state.getGUI();
		this.level = state.getLevel();
		this.input = state.getInput();
		this.state = state;

		// Initialize all possible slots so the Server can assign any ID between 0 and
		// MAX_PLAYERS without any IndexOutOfBoundsExceptions.
		this.players = new Player[NetVariables.MAX_PLAYERS];

		this.handler = new PacketIO(this, this.gui, address, this.level);
		this.handler.start();

		this.mainPlayerConnected = false;
	}

	/**
	 * Moves a Player based on the data in the incoming "packet".
	 */
	public void handleMove(Packet02Move packet) {
		this.players[packet.getID()].setTargetPosition(packet.getX(), packet.getZ());
	}

	/**
	 * Adds "player" to this Level. If it's the first Player, it becomes the main Player.
	 */
	public void addPlayer(String username, int id) {
		if (id == -1) {
			this.gui.addToChat("Now ya fucked up...");
		} else {
			Player player = new Player(username, id, this.level);

			// I think there's a chance that, if another Player connects at the perfect
			// time,
			// another Player could become the main player on the actual main Player's
			// client.
			if (!this.mainPlayerConnected) {
				player.setClient(this);
				player.setInput(this.input);
				this.state.setPlayer(player);
				this.level.setMainPlayer(player);
				this.mainPlayerConnected = true;
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
		this.gui.addToChat(this.players[packet.getID()].getUsername() + ": " + packet.getMessage());
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
	 * Sends the packet specified by "data".
	 */
	public void sendData(byte[] data) {
		this.handler.sendData(data);
	}

	/**
	 * Exits all networking processes.
	 */
	public void exit() {
		this.handler.exit();
	}

	/**
	 * Returns the packet handler.
	 */
	public PacketIO getHandler() {
		return this.handler;
	}

	/**
	 * Returns the address this Client is connected to.
	 */
	public InetAddress getAddress() {
		return this.handler.getAddress();
	}

	/**
	 * Returns the port being used for networking.
	 */
	public int getPort() {
		return this.handler.getPort();
	}
}