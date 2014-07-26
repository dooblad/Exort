package com.doobs.exort.net.client;

import java.net.*;
import java.util.*;

import com.doobs.exort.*;
import com.doobs.exort.entity.creature.*;
import com.doobs.exort.gfx.*;
import com.doobs.exort.level.*;
import com.doobs.exort.net.packets.*;
import com.doobs.exort.state.*;

public class Client {
	private Main main;
	
	private boolean isServer;
	
	private Level level;
	private List<NetPlayer> players;

	private PacketIO handler;

	public Client(Main main, boolean isServer, GUI gui, Level level, String address) {
		this.main = main;
		
		this.isServer = isServer;
		
		this.level = level;
		players = new ArrayList<NetPlayer>();

		handler = new PacketIO(this, gui, address, level);
		handler.start();
	}

	public void handleMove(Packet02Move packet) {
		if (main.getCurrentState() instanceof DuelState)
			((DuelState) main.getCurrentState()).getLevel().movePlayer(packet.getUsername(), packet.getX(), packet.getZ());
	}
	
	public void addConnection(NetPlayer player, Packet00Login packet) {
		if(players.isEmpty()) {
			level.setPlayer(player);
		}

		boolean alreadyConnected = false;
		for (NetPlayer p : players) {
			if (player.getUsername().equalsIgnoreCase(p.getUsername())) {
				if (p.getAddress() == null)
					p.setAddress(player.getAddress());
				alreadyConnected = true;
			}
		}
		if (!alreadyConnected) {
			addPlayer(player);
		}
	}
	
	public void removeConnection(Packet01Disconnect packet) {
		players.remove(getNetPlayerIndex(packet.getUsername()));
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
	
	public void addPlayer(NetPlayer player) {
		players.add(player);
		if(!isServer)
			level.addEntity(player);
	}

	public void sendData(byte[] data) {
		handler.sendData(data);
	}

	public void exit() {
		handler.exit();
	}

	// Getters and Setters
	public PacketIO getHandler() {
		return handler;
	}

	public InetAddress getAddress() {
		return handler.getAddress();
	}

	public void setAddress(InetAddress address) {
		handler.setAddress(address);
	}

	public int getPort() {
		return handler.getPort();
	}
}