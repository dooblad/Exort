package com.doobs.exort.net.packets;

import com.doobs.exort.net.client.*;
import com.doobs.exort.net.server.*;

public class Packet02Move extends Packet {

	private String username;
	private float x, z;

	public Packet02Move(byte[] data) {
		super(02);
		String[] dataArray = readData(data).split(",");
		this.username = dataArray[0];
		this.x = Float.parseFloat(dataArray[1]);
		this.z = Float.parseFloat(dataArray[2]);
	}

	public Packet02Move(String username, float x, float z) {
		super(02);
		this.username = username;
		this.x = x;
		this.z = z;
	}

	public void writeData(Client client) {
		super.writeData(client, getData());
	}

	public void writeData(Server server) {
		super.writeData(server, getData());
	}
	
	@Override
	public byte[] getData() {
		return ("02" + this.username + "," + this.x + "," + this.z).getBytes();
	}

	// Getters and setters
	public String getUsername() {
		return username;
	}

	public float getX() {
		return this.x;
	}

	public float getZ() {
		return this.z;
	}
}
