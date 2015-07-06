package exort.net.client;

import java.net.*;

import exort.entity.projectile.*;
import exort.level.*;
import exort.net.packets.*;
import exort.net.packets.Packet.PacketType;

/**
 * Carries out the actions designated by incoming packets.
 */
public class PacketParser {
	private Client client;
	private Level level;

	/**
	 * Initializes a PacketParser for "client" with "level".
	 */
	public PacketParser(Client client, Level level) {
		this.client = client;
		this.level = level;
	}

	/**
	 * Handles the incoming packet defined by "data" from "address" on "port".
	 */
	public void parsePacket(byte[] data, InetAddress address, int port) {
		String message = new String(data).trim();
		PacketType type = Packet.lookupPacket(message.substring(0, 2));
		switch (type) {
			case INVALID:
				break;
			case LOGIN:
				Packet00Login packet = new Packet00Login(data);
				this.client.addPlayer(packet.getUsername(), packet.getID());
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
				this.level.addEntity(new SonicWave(this.client.getPlayer(qPacket.getID()).getPosition(), qPacket.getDirection(), this.level));
				break;
			case ROCK_WALL:
				Packet05RockWall wPacket = new Packet05RockWall(data);
				this.level.addEntity(new RockWall(wPacket.getX(), wPacket.getZ(), wPacket.getDirection(), this.level));
				break;
			default:
				break;
		}
	}
}