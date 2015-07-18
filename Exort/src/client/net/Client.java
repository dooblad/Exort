package client.net;

import java.net.*;

import shared.entity.creature.*;
import shared.net.*;
import shared.net.packets.*;
import client.entity.*;
import client.state.*;
import client.util.*;

/**
 * Handles Client-side networking for the game.
 */
public class Client extends Networker {
	// The ones we're connecting to.
	private InetAddress address;
	private int port;

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
		super(state.getGUI(), state.getLevel());
		this.address = address;
		this.port = NetVariables.PORT;

		this.input = state.getInput();
		this.state = state;

		this.mainPlayerConnected = false;
	}

	/**
	 * Adds "player" to this Level. If it's the first Player, it becomes the main Player.
	 */
	public void addPlayer(Packet00Login packet, InetAddress address, int port) {
		if (packet.getPlayerID() == -1) { // No assigned ID.
			this.ui.addMessage("Now ya fucked up...");
		} else {
			Player player;

			// I think there's a chance that, if another Player connects at the perfect
			// time, that Player could become the main player on the actual main
			// Player's client.
			if (!this.mainPlayerConnected) {
				player = new ClientPlayer(this, packet.getUsername(), packet.getPlayerID(), this.input, this.level);
				this.state.setPlayer((ClientPlayer) player);
				this.mainPlayerConnected = true;
			} else {
				player = new Player(packet.getUsername(), packet.getPlayerID(), address, port, this.level);
			}

			this.players[packet.getPlayerID()] = player;
			this.level.addEntity(player);

			this.ui.addMessage(packet.getUsername() + " has joined the game.");
		}
	}

	/**
	 * Sends the packet specified by "data" to "address":"port".
	 */
	public void sendData(byte[] data) {
		this.packetIO.sendData(data, this.address, this.port);
	}
}