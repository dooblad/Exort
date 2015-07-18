package server.net;

import java.net.*;

import server.net.packets.*;
import server.net.packets.Packet.PacketType;
import shared.*;

/**
 * Carries out the actions designated by incoming Packets.
 */
public class PacketHandler {
	private Server server;

	/**
	 * Initializes a PacketParser for "server".
	 */
	public PacketHandler(Server server) {
		this.server = server;
	}

	/**
	 * Handles the incoming packet defined by "data" from "address":"port".
	 */
	public void parsePacket(byte[] data, InetAddress address, int port) {
		String message = new String(data).trim();
		PacketType type = Packet.lookupPacket(message.substring(0, 2));
		if (type != null) {
			switch (type) {
				case LOGIN:
					Packet00Login loginPacket = new Packet00Login(data);
					if (loginPacket.getUsername().length() > NetVariables.MAX_USERNAME_LENGTH) {
						// Send it right back without an assigned ID to show the Player
						// that they fucked up.
						loginPacket.sendData(this.server);
					} else {
						this.server.addPlayer(loginPacket.getUsername(), address, port);
					}
					break;
				case DISCONNECT:
					this.server.removePlayer(new Packet01Disconnect(data));
					break;
				case MOVE:
					this.server.handleMove(new Packet02Move(data));
					break;
				case CHAT:
					this.server.sendDataToAllClients(data);
					break;
				case SONIC_WAVE:
					this.server.sendDataToAllClients(data);
					break;
				case ROCK_WALL:
					this.server.sendDataToAllClients(data);
					break;
				default:
					break;
			}
		}
	}
}