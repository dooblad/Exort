package com.doobs.exort.net.server;

import java.util.*;
import java.util.Map.Entry;

import com.doobs.exort.entity.creature.*;
import com.doobs.exort.gfx.*;
import com.doobs.exort.level.*;
import com.doobs.exort.net.packets.*;
import com.doobs.exort.state.*;

public class Server {
	public static final int USERNAME_MAX_LENGTH = 16;

	private PacketIO handler;

	private DuelState duelState;

	private Level level;
	private Map<String, NetPlayer> players;

	public Server(DuelState duelState, GUI gui, Level level) {
		this.duelState = duelState;

		this.level = level;
		players = new HashMap<String, NetPlayer>();

		handler = new PacketIO(gui, this, level);
		handler.start();

		gui.addMessage("Started server on port " + handler.getPort());
	}

	public void handleMove(Packet02Move packet) {
		level.movePlayer(packet.getUsername(), packet.getX(), 0, packet.getZ());
		packet.sendData(this);
	}

	public void addConnection(NetPlayer player, Packet00Login packet) {
		// Confirm the login (to the player)
		handler.sendData(new Packet00Login(player.getUsername()).getData(), player.getAddress(), player.getPort());

		if (players.isEmpty()) {
			player.setClient(duelState.getClient());
			level.setMainPlayer(player);
		}

		if (players.containsKey(player.getUsername())) {
			NetPlayer p = players.get(player.getUsername());
			if (p.getAddress() == null)
				p.setAddress(player.getAddress());
		} else {
			Iterator<Entry<String, NetPlayer>> iterator = players.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, NetPlayer> pairs = iterator.next();
				NetPlayer p = pairs.getValue();

				// Inform new player of existing players
				handler.sendData(new Packet00Login(p.getUsername()).getData(), player.getAddress(), player.getPort());
				handler.sendData(new Packet02Move(p.getUsername(), (float) p.getX(), (float) p.getZ()).getData(), player.getAddress(), player.getPort());
				// Inform existing players of new player
				handler.sendData(new Packet00Login(player.getUsername()).getData(), p.getAddress(), p.getPort());

				// iterator.remove();
			}

			addPlayer(player);
		}
	}

	public void addPlayer(NetPlayer player) {
		players.put(player.getUsername(), player);
		level.addEntity(player);
	}

	public void removeConnection(Packet01Disconnect packet) {
		players.remove(packet.getUsername());
		packet.sendData(this);
		level.removePlayer(packet.getUsername());
	}

	public NetPlayer getPlayer(String username) {
		return players.get(username);
	}

	public void sendDataToAllClients(byte[] data) {
		Iterator<Entry<String, NetPlayer>> iterator = players.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, NetPlayer> pairs = iterator.next();
			NetPlayer player = pairs.getValue();
			handler.sendData(data, player.getAddress(), player.getPort());
			// iterator.remove();
		}
	}

	public void exit() {
		handler.exit();
	}
}