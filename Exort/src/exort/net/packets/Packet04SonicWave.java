package exort.net.packets;

import exort.net.client.*;
import exort.net.server.*;

/**
 * A Packet containing information about a conjured SonicWave in the format
 * 
 * 04<username>,<direction>
 */
public class Packet04SonicWave extends Packet {
	private String username;
	private float direction;

	/**
	 * Creates a Packet04SonicWave with "data".
	 */
	public Packet04SonicWave(byte[] data) {
		super(04);
		String[] dataArray = this.readData(data).split(",");
		this.username = dataArray[0];
		this.direction = Float.valueOf(dataArray[1]);
	}

	public Packet04SonicWave(String username, float direction) {
		super(04);
		this.username = username;
		this.direction = direction;
	}

	public void sendData(Client client) {
		super.sendData(client, this.getData());
	}

	public void sendData(Server server) {
		super.sendData(server, this.getData());
	}

	
	public byte[] getData() {
		return ("04" + this.username + "," + this.direction).getBytes();
	}

	// Getters and setters
	public String getUsername() {
		return this.username;
	}

	public float getDirection() {
		return this.direction;
	}
}
