package com.doobs.exort.net.client;

import java.net.*;

import com.doobs.exort.entity.creature.*;
import com.doobs.exort.gfx.*;
import com.doobs.exort.level.*;
import com.doobs.exort.net.packets.*;
import com.doobs.exort.net.packets.Packet.PacketType;

public class PacketParser {
	private Client client;
	private Level level;

	public PacketParser(Client client, Level level) {
		this.client = client;
		this.level = level;
	}

	public void parsePacket(byte[] data, InetAddress address, int port) {
		String message = new String(data).trim();
		PacketType type = Packet.lookupPacket(message.substring(0, 2));
		switch (type) {
		case INVALID:
			break;
		case LOGIN:
			Packet00Login packet = new Packet00Login(data);
			GUI.addMessage(packet.getUsername() + " has joined the game.");
			NetPlayer player = new NetPlayer(client, packet.getUsername(), null, -1, level);
			level.addEntity(player);
			break;
		case DISCONNECT:
			Packet01Disconnect disconnectPacket = new Packet01Disconnect(data);
			GUI.addMessage(disconnectPacket.getUsername() + " has left the game.");
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
}
