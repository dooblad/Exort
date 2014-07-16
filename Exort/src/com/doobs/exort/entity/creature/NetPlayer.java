package com.doobs.exort.entity.creature;

import com.doobs.exort.level.*;
import com.doobs.exort.net.client.*;

public class NetPlayer extends Player {
	private Client client;
	private String username;
	private String address;
	private int port;

	public NetPlayer(Client client, String username, String address, int port, Level level) {
		this.client = client;
		this.username = username;
		this.address = address;
		this.port = port;
	}

	// Getters and setters
	public Client getClient() {
		return client;
	}

	public String getUsername() {
		return username;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String username) {
		this.username = username;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
