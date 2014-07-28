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

	private Level level;
	private Map<String, NetPlayer> players; // Keeping this here just for the
											// sake of having the usernames of
											// all connected players

	private PacketIO handler;

	public Client(Main main, GUI gui, Level level, String address) {
		this.main = main;

		this.level = level;
		players = new HashMap<String, NetPlayer>();

		handler = new PacketIO(this, gui, address, level);
		handler.start();
	}

	public void handleMove(Packet02Move packet) {
		if (main.getCurrentState() instanceof DuelState)
			((DuelState) main.getCurrentState()).getLevel().movePlayer(packet.getUsername(), packet.getX(), 0, packet.getZ());
	}

	public void addConnection(NetPlayer player, Packet00Login packet) {
		if (players.isEmpty()) {
			player.setClient(this);
			level.setMainPlayer(player);
		}

		if (players.containsKey(player.getUsername())) {
			NetPlayer p = players.get(packet.getUsername());
			if (p.getAddress() == null)
				p.setAddress(player.getAddress());
		} else
			addPlayer(player);
	}

	public void removeConnection(Packet01Disconnect packet) {
		players.remove(packet.getUsername());
		level.removePlayer(packet.getUsername());
	}

	public NetPlayer getNetPlayer(String username) {
		return players.get(username);
	}

	public void addPlayer(NetPlayer player) {
		players.put(player.getUsername(), player);
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