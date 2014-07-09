package com.doobs.exort.net.packets;

import com.doobs.exort.net.*;

public class Packet00Login extends Packet {

	private String username;
	private int time;

	public Packet00Login(byte[] data) {
		super(00);
		String[] dataArray = readData(data).split(",");
		this.username = dataArray[0];
		this.time = Integer.parseInt(dataArray[1]);
	}

	public Packet00Login(String username, int time) {
		super(00);
		this.username = username;
		this.time = time;
	}

	public void writeData(NetComponent component) {
		super.writeData(component, getData());
	}

	@Override
	public byte[] getData() {
		return ("00" + this.username + "," + this.time).getBytes();
	}

	public String getUsername() {
		return username;
	}

	public int getTime() {
		return time;
	}
}
