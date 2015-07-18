package shared.net;

import java.net.*;

import shared.net.packets.*;
import shared.net.packets.Packet.PacketType;

/**
 * Carries out the actions designated by incoming Packets.
 */
public class PacketHandler {
	private Networker networker;

	/**
	 * Initializes a PacketParser for "server".
	 */
	public PacketHandler(Networker networker) {
		this.networker = networker;
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
					Packet00Login login = new Packet00Login(data);
					this.networker.addPlayer(login, address, port);
					break;
				case DISCONNECT:
					this.networker.removePlayer(new Packet01Disconnect(data));
					break;
				case MOVE:
					this.networker.handleMove(new Packet02Move(data));
					break;
				case CHAT:
					this.networker.handleChat(new Packet03Chat(data));
					break;
				case SONIC_WAVE:
					this.networker.handleSonicWave(new Packet04SonicWave(data));
					break;
				case ROCK_WALL:
					this.networker.handleRockWall(new Packet05RockWall(data));
					break;
				default:
					break;
			}
		}
	}
}