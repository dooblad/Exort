package exort.net.server;

import java.util.*;
import java.util.Map.Entry;

import exort.entity.creature.*;
import exort.gui.*;
import exort.level.*;
import exort.net.packets.*;
import exort.state.*;

public class Server {
	public static final int USERNAME_MAX_LENGTH = 16;

	private PacketIO handler;

	private DuelState duelState;

	private Level level;
	private Map<String, Player> players;

	public Server(DuelState duelState, GUI gui, Level level) {
		this.duelState = duelState;

		this.level = level;
		this.players = new HashMap<String, Player>();

		this.handler = new PacketIO(gui, this, level);
		this.handler.start();

		gui.addToChat("Started server on port " + this.handler.getPort());
	}

	public void handleMove(Packet02Move packet) {
		this.level.movePlayer(packet.getUsername(), packet.getX(), 0, packet.getZ());
		packet.sendData(this);
	}

	public void addConnection(Player player, Packet00Login packet) {
		// Confirm the login (to the player)
		this.handler.sendData(new Packet00Login(player.getUsername()).getData(), player.getAddress(), player.getPort());

		if (this.players.isEmpty()) {
			player.setClient(this.duelState.getClient());
			this.level.setMainPlayer(player);
		}

		if (this.players.containsKey(player.getUsername())) {
			Player p = this.players.get(player.getUsername());
			if (p.getAddress() == null) {
				p.setUsername(player.getAddress());
			}
		} else {
			Iterator<Entry<String, Player>> iterator = this.players.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, Player> pairs = iterator.next();
				Player p = pairs.getValue();

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

	public void addPlayer(Player player) {
		this.players.put(player.getUsername(), player);
		this.level.addEntity(player);
	}

	public void removeConnection(Packet01Disconnect packet) {
		this.players.remove(packet.getUsername());
		packet.sendData(this);
		this.level.removePlayer(packet.getUsername());
	}

	public Player getPlayer(String username) {
		return this.players.get(username);
	}

	public void sendDataToAllClients(byte[] data) {
		Iterator<Entry<String, Player>> iterator = this.players.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Player> pairs = iterator.next();
			Player player = pairs.getValue();
			this.handler.sendData(data, player.getAddress(), player.getPort());
			// iterator.remove();
		}
	}

	public void exit() {
		this.handler.exit();
	}
}