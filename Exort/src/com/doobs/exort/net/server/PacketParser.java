package com.doobs.exort.net.server;

import com.doobs.exort.entity.creature.*;
import com.doobs.exort.gfx.*;
import com.doobs.exort.level.*;
import com.doobs.exort.net.packets.*;
import com.doobs.exort.net.packets.Packet.PacketType;

public class PacketParser {
	private GUI gui;
	
	private Server server;
	private Level level;

	public PacketParser(GUI gui, Server server, Level level) {
		this.gui = gui;
		this.server = server;
		this.level = level;
	}

	public void parsePacket(byte[] data, String address, int port) {
		String message = new String(data).trim();
		PacketType type = Packet.lookupPacket(message.substring(0, 2));
		switch (type) {
		case INVALID:
			break;
		case LOGIN:
			Packet00Login loginPacket = new Packet00Login(data);
			gui.addMessage(loginPacket.getUsername() + " has joined the game.");
			NetPlayer player = new NetPlayer(null, loginPacket.getUsername(), address, port, level);
			server.addConnection(player, loginPacket);
			break;
		case DISCONNECT:
			Packet01Disconnect disconnectPacket = new Packet01Disconnect(data);
			if (server.getPlayer(disconnectPacket.getUsername()) != null) {
				gui.addMessage(disconnectPacket.getUsername() + " has left the game.");
				server.removeConnection(disconnectPacket);
			}
			break;
		case MOVE:
			server.handleMove(new Packet02Move(data));
			break;
		case CHAT:
			Packet03Chat chatPacket = new Packet03Chat(data);
			server.sendDataToAllClients(chatPacket.getData());
		default:
			break;
		}
	}
}
