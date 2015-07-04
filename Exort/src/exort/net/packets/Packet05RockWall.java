package exort.net.packets;

import exort.net.client.*;
import exort.net.server.*;

public class Packet05RockWall extends Packet {
	private int id;
	private float direction;
	private float x, z;

	public Packet05RockWall(byte[] data) {
		super(05);
		String[] dataArray = this.readData(data).split(",");
		this.id = Integer.parseInt(dataArray[0]);
		this.direction = Float.valueOf(dataArray[1]);
		this.x = Float.parseFloat(dataArray[2]);
		this.z = Float.parseFloat(dataArray[3]);
	}

	public Packet05RockWall(int id, float direction, float x, float z) {
		super(05);
		this.id = id;
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

	public byte[] getData() {
		return ("05" + this.id + "," + this.direction + "," + this.x + "," + this.z).getBytes();
	}

	public int getID() {
		return this.id;
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
