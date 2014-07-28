package com.doobs.exort.net.client;

import java.net.*;

import com.doobs.exort.entity.creature.*;
import com.doobs.exort.entity.projectile.*;
import com.doobs.exort.gfx.*;
import com.doobs.exort.level.*;
import com.doobs.exort.net.packets.*;
import com.doobs.exort.net.packets.Packet.PacketType;

public class PacketParser {
	private GUI gui;
	private Client client;
	private Level level;

	public PacketParser(GUI gui, Client client, Level level) {
		this.gui = gui;
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
			gui.addMessage(packet.getUsername() + " has joined the game.");
			NetPlayer player = new NetPlayer(null, packet.getUsername(), null, -1, level);
			client.addConnection(player, packet);
			break;
		case DISCONNECT:
			Packet01Disconnect disconnectPacket = new Packet01Disconnect(data);
			gui.addMessage(disconnectPacket.getUsername() + " has left the game.");
			client.removeConnection(disconnectPacket);
			break;
		case MOVE:
			Packet02Move movePacket = new Packet02Move(data);
			client.handleMove(movePacket);
			break;
		case CHAT:
			Packet03Chat chatPacket = new Packet03Chat(data);
			gui.addMessage(chatPacket.getUsername() + ": " + chatPacket.getMessage());
			break;
		case Q:
			Packet04Q qPacket = new Packet04Q(data);
			level.addEntity(new QSpell(level.getPlayer(qPacket.getUsername()).getPosition(), qPacket.getDirection(), level));
		default:
			break;
		}
	}
}
