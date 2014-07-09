package com.doobs.exort.net;

import com.doobs.exort.entity.creature.*;
import com.doobs.exort.level.*;
import com.doobs.exort.net.client.*;
import com.doobs.exort.net.packets.*;
import com.doobs.exort.net.packets.Packet.*;
import com.doobs.exort.net.server.*;

public class PacketParser {
	private NetComponent component;
	private Level level;

	public PacketParser(NetComponent component, Level level) {
		this.component = component;
		this.level = level;
	}

	public void parsePacket(byte[] data, String address, int port) {
		String message = new String(data).trim();
		PacketType type = Packet.lookupPacket(message.substring(0, 2));
		boolean client = component instanceof Client;
		
		switch (type) {
		case INVALID:
			break;
		case LOGIN:
			Packet00Login loginPacket = new Packet00Login(data);
			// System.out.println(packet.getUsername() +
			// " has joined the game.");
			NetPlayer player = new NetPlayer(null, loginPacket.getUsername(), address.toString(), port, level);
			level.addEntity(player);
			if(!client)
				((Server) component).addConnection(player, loginPacket);
			break;
		case DISCONNECT:
			Packet01Disconnect disconnectPacket = new Packet01Disconnect(data);
			// System.out.println(disconnectPacket.getUsername() + " has left the game.");
			level.removePlayer(disconnectPacket.getUsername());
			
			if (!client && ((Server) component).getPlayer(disconnectPacket.getUsername()) != null) {
				/*
				 * UI.addMessage("[" + address.getHostAddress() + "] " +
				 * disconnectPacket.getUsername() + " has disconnected.");
				 */
				((Server) component).removeConnection(disconnectPacket);
			}
			break;
		case MOVE:
			Packet02Move movePacket = new Packet02Move(data);
			if(client) {
				((Client) component).handleMove(movePacket);
			} else {
				((Server) component).handleMove(movePacket);
			}
			break;
		default:
			break;
		}
	}
}
