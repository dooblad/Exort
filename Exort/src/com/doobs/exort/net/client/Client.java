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
	// all connected players.

	private PacketIO handler;

	public Client(Main main, GUI gui, Level level, String address) {
		this.main = main;

		this.level = level;
		this.players = new HashMap<String, NetPlayer>();

		this.handler = new PacketIO(main, this, gui, address, level);
		this.handler.start();
	}

	public void handleMove(Packet02Move packet) {
		if (this.main.getCurrentState() instanceof DuelState) {
			((DuelState) this.main.getCurrentState()).getLevel().movePlayer(packet.getUsername(), packet.getX(), 0, packet.getZ());
		}
	}

	public void addConnection(NetPlayer player, Packet00Login packet) {
		if (this.players.isEmpty()) {
			player.setClient(this);
			this.level.setMainPlayer(player);
		}

		if (this.players.containsKey(player.getUsername())) {
			NetPlayer p = this.players.get(packet.getUsername());
			if (p.getAddress() == null) {
				p.setUsername(player.getAddress());
			}
		} else {
			this.addPlayer(player);
		}
	}

	public void removeConnection(Packet01Disconnect packet) {
		this.players.remove(packet.getUsername());
		this.level.removePlayer(packet.getUsername());
	}

	public NetPlayer getNetPlayer(String username) {
		return this.players.get(username);
	}

	public void addPlayer(NetPlayer player) {
		this.players.put(player.getUsername(), player);
		this.level.addEntity(player);
	}

	public void sendData(byte[] data) {
		this.handler.sendData(data);
	}

	public void exit() {
		this.handler.exit();
	}

	// Getters and Setters
	public PacketIO getHandler() {
		return this.handler;
	}

	public InetAddress getAddress() {
		return this.handler.getAddress();
	}

	public void setAddress(InetAddress address) {
		this.handler.setAddress(address);
	}

	public int getPort() {
		return this.handler.getPort();
	}
}