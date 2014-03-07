package com.doobs.exort.net.packets;

import com.doobs.exort.net.*;

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

	@Override
	public void writeData(Client client) {
		client.sendData(getData());
	}

	@Override
	public byte[] getData() {
		return ("01" + this.username).getBytes();
	}

	public String getUsername() {
		return username;
	}

}
