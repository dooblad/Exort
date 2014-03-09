package com.doobs.exort.net.packets;

import com.doobs.exort.net.*;

public class Packet00Login extends Packet {

	private String username;
	private int x, y;
	private int time;

	public Packet00Login(byte[] data) {
		super(00);
		String[] dataArray = readData(data).split(",");
		this.username = dataArray[0];
		this.x = Integer.parseInt(dataArray[1]);
		this.y = Integer.parseInt(dataArray[2]);
		this.time = Integer.parseInt(dataArray[3]);
	}

	public Packet00Login(String username, int x, int y, int time) {
		super(00);
		this.username = username;
		this.x = x;
		this.y = y;
		this.time = time;
	}

	@Override
	public void writeData(Client client) {
		client.sendData(getData());
	}

	@Override
	public byte[] getData() {
		return ("00" + this.username + "," + this.x + "," + this.y + "," + this.time)
				.getBytes();
	}

	public String getUsername() {
		return username;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getTime() {
		return time;
	}
}
