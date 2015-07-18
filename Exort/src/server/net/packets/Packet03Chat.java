package server.net.packets;

/**
 * Used to put messages in the chat for other Players to see.
 */
public class Packet03Chat extends Packet {
	private String message;

	/**
	 * Recreates a Packet03Chat from "data".
	 */
	public Packet03Chat(byte[] data) {
		this.packetID = PacketType.CHAT.getID();

		String[] dataArray = readData(data).split(",");
		this.playerID = Integer.parseInt(dataArray[0]);
		this.message = dataArray[1];
	}

	/**
	 * Creates a Packet03Chat carrying "message" from the Player with "id".
	 */
	public Packet03Chat(int id, String message) {
		super(PacketType.CHAT.getID(), id);
		this.message = message;
	}

	public byte[] getData() {
		return (PacketType.CHAT.id + this.playerID + "," + this.message).getBytes();
	}

	/**
	 * Returns the message being sent.
	 */
	public String getMessage() {
		return this.message;
	}
}
