package exort.net.packets;

import exort.net.client.*;
import exort.net.server.*;

public class Packet01Disconnect extends Packet {
	private int id;

	public Packet01Disconnect(byte[] data) {
		super(01);
		this.id = Integer.parseInt(this.readData(data));
	}

	public Packet01Disconnect(int id) {
		super(01);
		this.id = id;
	}

	public void sendData(Client client) {
		super.sendData(client, this.getData());
	}

	public void sendData(Server server) {
		super.sendData(server, this.getData());
	}

	public byte[] getData() {
		return ("01" + this.id).getBytes();
	}

	public int getID() {
		return this.id;
	}

}
