package shared.net.packets;

import server.net.*;
import client.net.*;

/**
 * An arbitrary size of information that can be encoded as a byte array and sent over a
 * network.
 */
public abstract class Packet {
	public static enum PacketType {
		LOGIN("00"), DISCONNECT("01"), MOVE("02"), CHAT("03"), SONIC_WAVE("04"), ROCK_WALL("05");

		public String id;

		private PacketType(String id) {
			this.id = id;
		}

		public int getID() {
			return Integer.parseInt(this.id);
		}
	}

	public int packetID;
	// -1 represents no assigned ID.
	public int playerID;

	/**
	 * Creates an empty Packet.
	 */
	public Packet() {
		this(-1, -1);
	}

	/**
	 * Creates a Packet with "id".
	 */
	public Packet(int packetID, int playerID) {
		this.packetID = packetID;
		this.playerID = playerID;
	}

	/**
	 * Sends the contents of this Packet to the Server that "client" is connected to.
	 */
	public void sendData(Client client) {
		client.sendData(this.getData());
	}

	/**
	 * Sends the contents of this Packet to all Clients connected to "server".
	 */
	public void sendData(Server server) {
		server.sendDataToAllClients(this.getData());
	}

	/**
	 * Returns a String from "data" that has been prepared to be parsed.
	 */
	public static String readData(byte[] data) {
		String message = new String(data).trim();
		// Remove packet identifier.
		return message.substring(2);
	}

	/**
	 * Returns the contents of this Packet as a byte array.
	 */
	public abstract byte[] getData();

	/**
	 * Returns the PacketType associated with the int specified by "id".
	 */
	public static PacketType lookupPacket(String id) {
		return lookupPacket(Integer.parseInt(id));
	}

	/**
	 * Returns the PacketType associated with "id".
	 */
	public static PacketType lookupPacket(int id) {
		if ((id < 0) || (id >= PacketType.values().length)) {
			return null;
		}
		return PacketType.values()[id];
	}

	/**
	 * Returns the ID of the Player who created this Packet.
	 */
	public int getPlayerID() {
		return this.playerID;
	}
}