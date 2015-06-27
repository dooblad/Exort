package exort.net.packets;

import exort.net.client.*;
import exort.net.server.*;

public class Packet01Disconnect extends Packet {

	private String username;

	public Packet01Disconnect(byte[] data) {
		super(01);
		this.username = this.readData(data);
	}

	public Packet01Disconnect(String username) {
		super(01);
		this.username = username;
	}

	public void sendData(Client client) {
		super.sendData(client, this.getData());
	}

	public void sendData(Server server) {
		super.sendData(server, this.getData());
	}

	@Override
	public byte[] getData() {
		return ("01" + this.username).getBytes();
	}

	// Getters and setters
	public String getUsername() {
		return this.username;
	}

}
