package com.doobs.exort.net.packets;

import com.doobs.exort.net.client.*;
import com.doobs.exort.net.server.*;

public class Packet04Q extends Packet {

	private String username;
	private float direction;

	public Packet04Q(byte[] data) {
		super(04);
		String[] dataArray = readData(data).split(",");
		this.username = dataArray[0];
		this.direction = Float.valueOf(dataArray[1]);
	}

	public Packet04Q(String username, float direction) {
		super(04);
		this.username = username;
		this.direction = direction;
	}

	public void sendData(Client client) {
		super.sendData(client, getData());
	}

	public void sendData(Server server) {
		super.sendData(server, getData());
	}

	@Override
	public byte[] getData() {
		return ("04" + this.username + "," + this.direction).getBytes();
	}

	// Getters and setters
	public String getUsername() {
		return username;
	}

	public float getDirection() {
		return direction;
	}
}
