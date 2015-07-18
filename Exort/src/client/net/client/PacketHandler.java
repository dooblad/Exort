package client.net.client;

import java.net.*;

import client.entity.projectile.*;
import client.level.*;
import client.net.packets.*;
import client.net.packets.Packet.PacketType;

/**
 * Carries out the actions designated by incoming Packets.
 */
public class PacketHandler {
	private Client client;
	private Level level;

	/**
	 * Initializes a PacketHandler for "client" with "level".
	 */
	public PacketHandler(Client client, Level level) {
		this.client = client;
		this.level = level;
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
					Packet00Login packet = new Packet00Login(data);
					this.client.addPlayer(packet.getUsername(), packet.getPlayerID());
					break;
				case DISCONNECT:
					this.client.removePlayer(new Packet01Disconnect(data));
					break;
				case MOVE:
					this.client.handleMove(new Packet02Move(data));
					break;
				case CHAT:
					this.client.addChat(new Packet03Chat(data));
					break;
				case SONIC_WAVE:
					Packet04SonicWave qPacket = new Packet04SonicWave(data);
					this.level.addEntity(new SonicWave(this.client.getPlayer(qPacket.getPlayerID()).getPosition(), qPacket.getDirection(), this.client
							.getPlayer(qPacket.getPlayerID()), this.level));
					break;
				case ROCK_WALL:
					Packet05RockWall wPacket = new Packet05RockWall(data);
					this.level.addEntity(new RockWall(wPacket.getX(), wPacket.getZ(), wPacket.getOrientation(), this.level));
					break;
				default:
					break;
			}
		}
	}
}