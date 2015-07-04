package exort.net.packets;

import exort.net.client.*;
import exort.net.server.*;

public class Packet02Move extends Packet {
	private int id;
	private float x, z;

	public Packet02Move(byte[] data) {
		super(02);
		String[] dataArray = this.readData(data).split(",");
		this.id = Integer.parseInt(dataArray[0]);
		this.x = Float.parseFloat(dataArray[1]);
		this.z = Float.parseFloat(dataArray[2]);
	}

	public Packet02Move(int id, float x, float z) {
		super(02);
		this.id = id;
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
		return ("02" + this.id + "," + this.x + "," + this.z).getBytes();
	}

	public int getID() {
		return this.id;
	}

	public float getX() {
		return this.x;
	}

	public float getZ() {
		return this.z;
	}
}
