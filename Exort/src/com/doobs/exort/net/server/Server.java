package com.doobs.exort.net.server;

import java.util.*;

import com.doobs.exort.entity.creature.*;
import com.doobs.exort.gfx.*;
import com.doobs.exort.level.*;
import com.doobs.exort.net.packets.*;

public class Server {
	private PacketIO handler;

	private Level level;
	private List<NetPlayer> players;

	public Server(Level level) {
		this.level = level;
		players = new ArrayList<NetPlayer>();

		handler = new PacketIO(this, level);
		handler.start();

		GUI.addMessage("Started server on port " + handler.getPort());
	}

	public void handleMove(Packet02Move packet) {
		level.movePlayer(packet.getUsername(), packet.getX(), packet.getZ(), packet.getTime());
		packet.writeData(this);
	}

	public void addConnection(NetPlayer player, Packet00Login packet) {
		// Confirm the login (to the player)
		handler.sendData(new Packet00Login(player.getUsername()).getData(), player.getAddress(), player.getPort());

		boolean alreadyConnected = false;
		for (NetPlayer p : players) {
			if (player.getUsername().equalsIgnoreCase(p.getUsername())) {
				if (p.getAddress() == null)
					p.setAddress(player.getAddress());
				alreadyConnected = true;
			} else {
				handler.sendData(packet.getData(), p.getAddress(), p.getPort());
			}
		}
		if (!alreadyConnected) {

		}
	}

	public void addPlayer(NetPlayer player) {
		players.add(player);
		level.addEntity(player);
	}

	public void removeConnection(Packet01Disconnect packet) {
		players.remove(getNetPlayerIndex(packet.getUsername()));
		packet.writeData(this);
	}

	public NetPlayer getPlayer(String username) {
		for (NetPlayer player : players) {
			if (player.getUsername().equals(username))
				return player;
		}
		return null;
	}

	public int getNetPlayerIndex(String username) {
		int index = 0;
		for (NetPlayer player : players) {
			if (player.getUsername().equals(username))
				break;
			index++;
		}
		return index;
	}

	public void sendDataToAllClients(byte[] data) {
		for (NetPlayer player : players) {
			handler.sendData(data, player.getAddress(), player.getPort());
		}
	}

	public void exit() {
		handler.exit();
	}
}