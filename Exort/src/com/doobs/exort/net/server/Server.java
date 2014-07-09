package com.doobs.exort.net.server;

import java.util.*;

import com.doobs.exort.entity.creature.*;
import com.doobs.exort.level.*;
import com.doobs.exort.net.*;
import com.doobs.exort.net.packets.*;

public class Server extends NetComponent implements Runnable {
	private PacketHandler handler;

	private Level level;
	private List<NetPlayer> players;

	// Packet timing
	private long lastTime;
	private long delta;
	private long time;

	public Server() {
		level = new Level();
		players = new ArrayList<NetPlayer>();

		handler = new PacketHandler(this, null, level);
		handler.start();

		lastTime = System.currentTimeMillis();
		delta = 0;
		time = 0;
	}

	public void run() {
		while (true) {
			delta = System.currentTimeMillis() - lastTime;
			lastTime = System.currentTimeMillis();
			time += delta;
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void handleMove(Packet02Move packet) {
		level.movePlayer(packet.getUsername(), packet.getX(), packet.getZ(), packet.getTime());
		// Insert movement checking code
		packet.writeData(this);
	}

	public void addConnection(NetPlayer player, Packet00Login packet) {
		boolean alreadyConnected = false;
		for (NetPlayer p : players) {
			if (player.getUsername().equalsIgnoreCase(p.getUsername())) {
				if (p.getAddress() == null)
					p.setAddress(player.getAddress());
				if (p.getPort() == -1)
					p.setPort(player.getPort());
				alreadyConnected = true;
			} else {
				handler.sendData(packet.getData(), p.getAddress());
				handler.sendData(new Packet00Login(p.getUsername(), getPacketTime()).getData(), player.getAddress());
			}
		}
		if (!alreadyConnected) {
			players.add(player);
		}
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
			handler.sendData(data, player.getAddress());
		}
	}

	// Getters and setters
	public int getPacketTime() {
		return (int) (time % 1000);
	}
}