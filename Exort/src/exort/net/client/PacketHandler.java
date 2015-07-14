package exort.net.client;

import java.net.*;

import exort.entity.projectile.*;
import exort.gui.*;
import exort.level.*;
import exort.net.packets.*;
import exort.net.packets.Packet.PacketType;

/**
 * Carries out the actions designated by incoming packets.
 */
public class PacketHandler {
	private Client client;
	private Level level;

	private PacketIO packetIO;

	/**
	 * Initializes a PacketParser for "client" with "level".
	 */
	public PacketHandler(Client client, GUI gui, String address, Level level) {
		this.client = client;
		this.level = level;
		this.packetIO = new PacketIO(gui, this, address);
	}

	/**
	 * Starts receiving and parsing packets.
	 */
	public void start() {
		this.packetIO.start();
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
				this.level.addEntity(new SonicWave(this.client.getPlayer(qPacket.getID()).getPosition(), qPacket.getDirection(), this.client.getPlayer(qPacket
						.getID()), this.level));
				break;
			case ROCK_WALL:
				Packet05RockWall wPacket = new Packet05RockWall(data);
				this.level.addEntity(new RockWall(wPacket.getX(), wPacket.getZ(), wPacket.getDirection(), this.level));
				break;
			default:
				break;
		}
	}

	/**
	 * Sends the packet specified by "data".
	 */
	public void sendData(byte[] data) {
		this.packetIO.sendData(data);
	}

	/**
	 * Returns the address this Client is connected to.
	 */
	public InetAddress getAddress() {
		return this.packetIO.getAddress();
	}

	/**
	 * Returns the port being used for networking.
	 */
	public int getPort() {
		return this.packetIO.getPort();
	}

	/**
	 * Exits all networking processes.
	 */
	public void exit() {
		this.packetIO.exit();
	}
}