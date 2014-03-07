package com.doobs.exort.net.packets;

import com.doobs.exort.net.*;

public class Packet02Move extends Packet {

	private String username;
	private int x, z;
	private int time;

	public Packet02Move(byte[] data) {
		super(02);
		String[] dataArray = readData(data).split(",");
		this.username = dataArray[0];
		this.x = Integer.parseInt(dataArray[1]);
		this.z = Integer.parseInt(dataArray[2]);
		this.time = Integer.parseInt(dataArray[3]);
	}

	public Packet02Move(String username, int x, int z, int time) {
		super(02);
		this.username = username;
		this.x = x;
		this.z = z;
		this.time = time;
	}

	@Override
	public void writeData(Client client) {
		client.sendData(getData());
	}

	@Override
	public byte[] getData() {
		return ("02" + this.username + "," + this.x + "," + this.z + "," + this.time)
				.getBytes();
	}

	public String getUsername() {
		return username;
	}

	public int getX() {
		return this.x;
	}

	public int getZ() {
		return this.z;
	}

	public int getTime() {
		return time;
	}
}
