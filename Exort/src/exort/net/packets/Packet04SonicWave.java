package exort.net.packets;

import exort.net.client.*;
import exort.net.server.*;

/**
 * A Packet containing information about a conjured SonicWave in the format
 *
 * 04<username>,<direction>
 */
public class Packet04SonicWave extends Packet {
	private int id;
	private float direction;

	/**
	 * Creates a Packet04SonicWave with "data".
	 */
	public Packet04SonicWave(byte[] data) {
		super(04);
		String[] dataArray = this.readData(data).split(",");
		this.id = Integer.parseInt(dataArray[0]);
		this.direction = Float.valueOf(dataArray[1]);
	}

	public Packet04SonicWave(int id, float direction) {
		super(04);
		this.id = id;
		this.direction = direction;
	}

	public void sendData(Client client) {
		super.sendData(client, this.getData());
	}

	public void sendData(Server server) {
		super.sendData(server, this.getData());
	}

	public byte[] getData() {
		return ("04" + this.id + "," + this.direction).getBytes();
	}

	public int getID() {
		return this.id;
	}

	public float getDirection() {
		return this.direction;
	}
}
