package exort.net.packets;

/**
 * Used to disconnect the Player with "id" from the game.
 */
public class Packet01Disconnect extends Packet {
	private int id;

	/**
	 * Recreates a Packet01Disconnect from "data".
	 */
	public Packet01Disconnect(byte[] data) {
		super(PacketType.DISCONNECT.getID(), Integer.parseInt(readData(data)));
	}

	/**
	 * Creates a Packet01Disconnect for the Player with "id".
	 */
	public Packet01Disconnect(int id) {
		super(PacketType.DISCONNECT.getID(), id);
		this.id = id;
	}

	public byte[] getData() {
		return (PacketType.DISCONNECT.id + this.id).getBytes();
	}

	/**
	 * Returns the ID of the Player who is disconnecting.
	 */
	public int getID() {
		return this.id;
	}
}
