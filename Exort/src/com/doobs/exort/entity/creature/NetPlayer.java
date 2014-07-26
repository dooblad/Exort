package com.doobs.exort.entity.creature;

import org.lwjgl.util.vector.*;

import com.doobs.exort.level.*;
import com.doobs.exort.net.client.*;
import com.doobs.exort.net.packets.*;

public class NetPlayer extends Player {
	private Client client;
	private String username;
	private String address;
	private int port;

	public NetPlayer(Client client, String username, String address, int port, Level level) {
		super(Player.spawn[0], Player.spawn[1], Player.spawn[2], level);
		this.client = client;
		this.username = username;
		this.address = address;
		this.port = port;
	}
	
	@Override
	public void tick(int delta) {
		super.tick(delta);
	}
	
	@Override
	public void render() {
		super.render();
	}
	
	public void move(Vector3f position) {
		super.move(position);
		client.sendData(new Packet02Move(username, position.getX(), position.getZ()).getData());
	}
	
	// Getters and setters
	public Client getClient() {
		return client;
	}
	
	public void setClient(Client client) {
		this.client = client;
	}

	public String getUsername() {
		return username;
	}

	public String getAddress() {
		return address;
	}
	
	public int getPort() {
		return port;
	}

	public void setAddress(String username) {
		this.username = username;
	}
}
