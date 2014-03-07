package com.doobs.exort.net;

import java.net.InetAddress;

import com.doobs.exort.entity.*;
import com.doobs.exort.level.*;
import com.doobs.exort.net.packets.*;
import com.doobs.exort.net.packets.Packet.*;

public class PacketParser {
	private Client client;
	private Level level;
	
	// Used to determine client-server latency
	private long pingStart;
	private int ping;

	public PacketParser(Client client, Level level) {
		this.client = client;
		this.level = level;
		pingStart = 0;
		ping = 0;
	}

	public void parsePacket(byte[] data, InetAddress address, int port) {
		String message = new String(data).trim();
		PacketType type = Packet.lookupPacket(message.substring(0, 2));
		switch (type) {
		case INVALID:
			break;
		case LOGIN:
			stopPingTimer();
			Packet00Login packet = new Packet00Login(data);
			System.out.println(packet.getUsername() + " has joined the game.");
			Player player = new Player(null, packet.getUsername(),
					packet.getX(), packet.getY(), level);
			level.addEntity(player);
			client.setTime(getAdjustedTime(packet.getTime(), ping));
			break;
		case DISCONNECT:
			Packet01Disconnect disconnectPacket = new Packet01Disconnect(data);
			System.out.println(disconnectPacket.getUsername()
					+ " has left the game.");
			level.removePlayer(disconnectPacket.getUsername());
			break;
		case MOVE:
			Packet02Move movePacket = new Packet02Move(data);
			client.handleMove(movePacket);
			break;
		default:
			break;
		}
	}
	
	public void startPingTimer() {
		pingStart = System.currentTimeMillis();
	}
	
	public void stopPingTimer() {
		ping = (int) (System.currentTimeMillis() - pingStart);
	}
	
	// For adjusting the packet time based on latency
	public int getAdjustedTime(int time, int offset) {
		return (time + offset) % 1000;
	}
}
