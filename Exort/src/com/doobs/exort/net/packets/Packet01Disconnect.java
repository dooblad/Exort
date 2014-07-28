package com.doobs.exort.net.packets;

import com.doobs.exort.net.client.*;
import com.doobs.exort.net.server.*;

public class Packet01Disconnect extends Packet {

	private String username;

	public Packet01Disconnect(byte[] data) {
		super(01);
		this.username = readData(data);
	}

	public Packet01Disconnect(String username) {
		super(01);
		this.username = username;
	}

	public void sendData(Client client) {
		super.sendData(client, getData());
	}

	public void sendData(Server server) {
		super.sendData(server, getData());
	}

	@Override
	public byte[] getData() {
		return ("01" + this.username).getBytes();
	}

	// Getters and setters
	public String getUsername() {
		return username;
	}

}
