package exort.net.packets;

import exort.net.client.*;
import exort.net.server.*;

public class Packet03Chat extends Packet {
	private int id;
	private String message;

	public Packet03Chat(byte[] data) {
		super(03);
		String[] dataArray = this.readData(data).split(",");
		this.id = Integer.parseInt(dataArray[0]);
		this.message = dataArray[1];
	}

	public Packet03Chat(int id, String message) {
		super(03);
		this.id = id;
		this.message = message;
	}

	public void sendData(Client client) {
		super.sendData(client, this.getData());
	}

	public void sendData(Server server) {
		super.sendData(server, this.getData());
	}

	public byte[] getData() {
		return ("03" + this.id + "," + this.message).getBytes();
	}

	public int getID() {
		return this.id;
	}

	public String getMessage() {
		return this.message;
	}
}
