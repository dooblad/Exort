package shared.net.packets;

/**
 * Used to move the Player with "id" to ("x", 0, "z").
 */
public class Packet02Move extends Packet {
	private float x, z;

	/**
	 * Recreates a Packet02Move from "data".
	 */
	public Packet02Move(byte[] data) {
		this.packetID = PacketType.MOVE.getID();

		String[] dataArray = readData(data).split(",");
		this.playerID = Integer.parseInt(dataArray[0]);
		this.x = Float.parseFloat(dataArray[1]);
		this.z = Float.parseFloat(dataArray[2]);
	}

	/**
	 * Creates a Packet02Move for the Player with "id" to move to ("x", 0, "z").
	 */
	public Packet02Move(int id, float x, float z) {
		super(PacketType.MOVE.getID(), id);
		this.x = x;
		this.z = z;
	}

	public byte[] getData() {
		return (PacketType.MOVE.id + this.playerID + "," + this.x + "," + this.z).getBytes();
	}

	/**
	 * Returns the x-coordinate the Player is moving to.
	 */
	public float getX() {
		return this.x;
	}

	/**
	 * Returns the z-coordinate the Player is moving to.
	 */
	public float getZ() {
		return this.z;
	}
}