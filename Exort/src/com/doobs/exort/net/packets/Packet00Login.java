package com.doobs.exort.net.packets;

import com.doobs.exort.net.*;

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

	public void writeData(NetComponent component) {
		super.writeData(component, getData());
	}

	@Override
	public byte[] getData() {
		return ("00" + this.username).getBytes();
	}

	public String getUsername() {
		return username;
	}
}
