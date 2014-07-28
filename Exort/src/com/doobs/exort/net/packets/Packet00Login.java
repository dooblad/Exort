package com.doobs.exort.net.packets;

import com.doobs.exort.net.client.*;
import com.doobs.exort.net.server.*;

public class Packet00Login extends Packet {

	private String username;

	public Packet00Login(byte[] data) {
		super(00);
		String[] dataArray = readData(data).split(",");
		this.username = dataArray[0];
	}

	public Packet00Login(String username) {
		super(00);
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
		return ("00" + this.username).getBytes();
	}

	// Getters and setters
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
