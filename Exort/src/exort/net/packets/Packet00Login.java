package exort.net.packets;

import exort.net.client.*;
import exort.net.server.*;

public class Packet00Login extends Packet {
	private String username;

	// -1 indicates a null ID.
	private int id;

	public Packet00Login(byte[] data) {
		super(00);
		String[] dataArray = this.readData(data).split(",");
		this.username = dataArray[0];
		this.id = Integer.parseInt(dataArray[1]);
	}

	/**
	 * Creates a Packet00Login for a Player who has not been assigned an ID from the
	 * Server yet.
	 */
	public Packet00Login(String username) {
		this(username, -1);
	}

	public Packet00Login(String username, int id) {
		super(00);
		this.username = username;
		this.id = id;
	}

	public void sendData(Client client) {
		super.sendData(client, this.getData());
	}

	public void sendData(Server server) {
		super.sendData(server, this.getData());
	}

	public byte[] getData() {
		return ("00" + this.username + "," + this.id).getBytes();
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getID() {
		return this.id;
	}

	public void setID(int id) {
		this.id = id;
	}
}
