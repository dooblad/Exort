package shared.net;

import java.net.*;

import server.net.*;
import shared.*;
import shared.entity.*;
import shared.entity.creature.*;
import shared.entity.projectile.*;
import shared.level.*;
import shared.net.packets.*;

/**
 * Handles Server-side networking for the game.
 */
public abstract class Networker {
	protected UI ui;
	protected Level level;
	protected Player[] players;
	protected PacketIO packetIO;

	/**
	 * Creates a Server using "ui" to send messages directly to chat and uses "level" to
	 * keep track of the master gamestate.
	 */
	public Networker(UI ui, Level level) {
		this.ui = ui;

		this.level = level;

		// Initialize all possible slots so we can assign any ID between 0 and
		// MAX_PLAYERS without any IndexOutOfBoundsExceptions.
		this.players = new Player[NetVariables.MAX_PLAYERS];

		this.packetIO = new PacketIO(ui, this);

		if (this instanceof Server) {
			ui.addMessage("Started server on port " + this.packetIO.getPort());
		}
	}

	/**
	 * Moves a Player based on the data in the incoming "packet".
	 */
	public void handleMove(Packet02Move packet) {
		this.players[packet.getPlayerID()].setTargetPosition(packet.getX(), packet.getZ());
	}

	public void handleChat(Packet03Chat packet) {

		this.ui.addMessage(this.getPlayer(packet.getPlayerID()).getUsername() + ": " + packet.getMessage());
	}

	public void handleSonicWave(Packet04SonicWave packet) {
		this.level.addEntity(new SonicWave(this.getPlayer(packet.getPlayerID()).getPosition(), packet.getDirection(), this.getPlayer(packet.getPlayerID()),
				this.level));
	}

	public void handleRockWall(Packet05RockWall packet) {
		this.level.addEntity(new RockWall(packet.getX(), packet.getZ(), packet.getOrientation(), this.level));
	}

	/**
	 * Assigns an ID to "player", synchronizes the world state between all other Players,
	 * and adds "player" to the game.
	 */
	public abstract void addPlayer(Packet00Login packet, InetAddress address, int port);

	/**
	 * Removes the Player designated by "packet" from the game.
	 */
	public void removePlayer(Packet01Disconnect packet) {
		int id = packet.getID();

		this.ui.addMessage(this.players[id].getUsername() + " has left the game.");

		// Remove from Level and here.
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

	public abstract void sendData(byte[] data);

	/**
	 * Exits all networking processes.
	 */
	public void exit() {
		this.packetIO.exit();
	}
}