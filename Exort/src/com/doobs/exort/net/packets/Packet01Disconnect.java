package com.doobs.exort.net.packets;

import com.doobs.exort.net.client.*;
import com.doobs.exort.net.server.*;

public class Packet01Disconnect extends Packet {

	private String username;

	public Packet01Disconnect(byte[] data) {
		super(01);
		this.username = readData(data);
	}

	public Packet01Disconnect(String username) {
		super(01);
		this.username = username;
	}

	public void writeData(Client client) {
		super.writeData(client, getData());
	}
	
	public void writeData(Server server) {
		super.writeData(server, getData());
	}

	@Override
	public byte[] getData() {
		return ("01" + this.username).getBytes();
	}

	public String getUsername() {
		return username;
	}

}
