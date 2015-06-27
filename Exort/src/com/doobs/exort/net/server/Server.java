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
		this.players = new HashMap<String, NetPlayer>();

		this.handler = new PacketIO(gui, this, level);
		this.handler.start();

		gui.addMessage("Started server on port " + this.handler.getPort());
	}

	public void handleMove(Packet02Move packet) {
		this.level.movePlayer(packet.getUsername(), packet.getX(), 0, packet.getZ());
		packet.sendData(this);
	}

	public void addConnection(NetPlayer player, Packet00Login packet) {
		// Confirm the login (to the player)
		this.handler.sendData(new Packet00Login(player.getUsername()).getData(), player.getAddress(), player.getPort());

		if (this.players.isEmpty()) {
			player.setClient(this.duelState.getClient());
			this.level.setMainPlayer(player);
		}

		if (this.players.containsKey(player.getUsername())) {
			NetPlayer p = this.players.get(player.getUsername());
			if (p.getAddress() == null) {
				p.setUsername(player.getAddress());
			}
		} else {
			Iterator<Entry<String, NetPlayer>> iterator = this.players.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, NetPlayer> pairs = iterator.next();
				NetPlayer p = pairs.getValue();

				// Inform new player of existing players
				this.handler.sendData(new Packet00Login(p.getUsername()).getData(), player.getAddress(), player.getPort());
				this.handler.sendData(new Packet02Move(p.getUsername(), (float) p.getX(), (float) p.getZ()).getData(), player.getAddress(), player.getPort());
				// Inform existing players of new player
				this.handler.sendData(new Packet00Login(player.getUsername()).getData(), p.getAddress(), p.getPort());

				// iterator.remove();
			}

			this.addPlayer(player);
		}
	}

	public void addPlayer(NetPlayer player) {
		this.players.put(player.getUsername(), player);
		this.level.addEntity(player);
	}

	public void removeConnection(Packet01Disconnect packet) {
		this.players.remove(packet.getUsername());
		packet.sendData(this);
		this.level.removePlayer(packet.getUsername());
	}

	public NetPlayer getPlayer(String username) {
		return this.players.get(username);
	}

	public void sendDataToAllClients(byte[] data) {
		Iterator<Entry<String, NetPlayer>> iterator = this.players.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, NetPlayer> pairs = iterator.next();
			NetPlayer player = pairs.getValue();
			this.handler.sendData(data, player.getAddress(), player.getPort());
			// iterator.remove();
		}
	}

	public void exit() {
		this.handler.exit();
	}
}