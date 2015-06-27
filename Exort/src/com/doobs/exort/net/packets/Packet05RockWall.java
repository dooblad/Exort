package com.doobs.exort.net.packets;

import com.doobs.exort.net.client.*;
import com.doobs.exort.net.server.*;

public class Packet05RockWall extends Packet {

	private String username;
	private float direction;
	private float x, z;

	public Packet05RockWall(byte[] data) {
		super(05);
		String[] dataArray = this.readData(data).split(",");
		this.username = dataArray[0];
		this.direction = Float.valueOf(dataArray[1]);
		this.x = Float.parseFloat(dataArray[2]);
		this.z = Float.parseFloat(dataArray[3]);
	}

	public Packet05RockWall(String username, float direction, float x, float z) {
		super(05);
		this.username = username;
		this.direction = direction;
		this.x = x;
		this.z = z;
	}

	public void sendData(Client client) {
		super.sendData(client, this.getData());
	}

	public void sendData(Server server) {
		super.sendData(server, this.getData());
	}

	@Override
	public byte[] getData() {
		return ("05" + this.username + "," + this.direction + "," + this.x + "," + this.z).getBytes();
	}

	// Getters and setters
	public String getUsername() {
		return this.username;
	}

	public float getDirection() {
		return this.direction;
	}

	public float getX() {
		return this.x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getZ() {
		return this.z;
	}

	public void setZ(float z) {
		this.z = z;
	}
}
