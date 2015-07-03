package exort.net.client;

import java.net.*;

import exort.*;
import exort.entity.creature.*;
import exort.entity.projectile.*;
import exort.gui.*;
import exort.level.*;
import exort.net.packets.*;
import exort.net.packets.Packet.*;

public class PacketParser {
	private Main main;
	private GUI gui;
	private Client client;
	private Level level;

	/**
	 * Initializes a PacketParser for "client" with "level".
	 */
	public PacketParser(Main main, GUI gui, Client client, Level level) {
		this.main = main;
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
				this.gui.addToChat(packet.getUsername() + " has joined the game.");
				Player player = new Player(this.main.input, this.level, null, packet.getUsername(), null, -1);
				this.client.addConnection(player, packet);
				break;
			case DISCONNECT:
				Packet01Disconnect disconnectPacket = new Packet01Disconnect(data);
				this.gui.addToChat(disconnectPacket.getUsername() + " has left the game.");
				this.client.removeConnection(disconnectPacket);
				break;
			case MOVE:
				Packet02Move movePacket = new Packet02Move(data);
				this.client.handleMove(movePacket);
				break;
			case CHAT:
				Packet03Chat chatPacket = new Packet03Chat(data);
				this.gui.addToChat(chatPacket.getUsername() + ": " + chatPacket.getMessage());
				break;
			case SONIC_WAVE:
				Packet04SonicWave qPacket = new Packet04SonicWave(data);
				this.level.addEntity(new SonicWave(this.level.getPlayer(qPacket.getUsername()).getPosition(), qPacket.getDirection(), this.level));
				System.out.println(qPacket.getDirection());
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
