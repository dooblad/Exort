package exort.net.client;

import java.net.*;

import exort.entity.creature.*;
import exort.entity.projectile.*;
import exort.gui.*;
import exort.level.*;
import exort.net.packets.*;
import exort.net.packets.Packet.PacketType;

public class PacketParser {
	private GUI gui;
	private Client client;
	private Level level;

	/**
	 * Initializes a PacketParser for "client" with "level".
	 */
	public PacketParser(GUI gui, Client client, Level level) {
		this.gui = gui;
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
				if (packet.getID() == -1) {
					this.gui.addToChat("Now ya fucked up...");
				} else {
					this.gui.addToChat(packet.getUsername() + " has joined the game.");
					Player player = new Player(packet.getUsername(), packet.getID(), this.level);
					this.client.addPlayer(player, packet.getID());
				}
				break;
			case DISCONNECT:
				Packet01Disconnect disconnectPacket = new Packet01Disconnect(data);
				this.client.removePlayer(disconnectPacket);
				break;
			case MOVE:
				Packet02Move movePacket = new Packet02Move(data);
				this.client.handleMove(movePacket);
				break;
			case CHAT:
				Packet03Chat chatPacket = new Packet03Chat(data);
				this.gui.addToChat(chatPacket.getID() + ": " + chatPacket.getMessage());
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