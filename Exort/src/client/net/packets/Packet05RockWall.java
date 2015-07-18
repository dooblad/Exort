package client.net.packets;

/**
 * Used to conjure a RockWall with a certain position and orientation (in radians).
 */
public class Packet05RockWall extends Packet {
	// In radians.
	private float orientation;
	private float x, z;

	/**
	 * Recreates a Packet05RockWall from "data".
	 */
	public Packet05RockWall(byte[] data) {
		this.packetID = PacketType.ROCK_WALL.getID();

		String[] dataArray = readData(data).split(",");
		this.playerID = Integer.parseInt(dataArray[0]);
		this.orientation = Float.valueOf(dataArray[1]);
		this.x = Float.parseFloat(dataArray[2]);
		this.z = Float.parseFloat(dataArray[3]);
	}

	/**
	 * Creates a Packet05RockWall conjured by the Player with "id" with "direction" and
	 * position ("x", 0, "z").
	 */
	public Packet05RockWall(int id, float orientation, float x, float z) {
		super(PacketType.ROCK_WALL.getID(), id);
		this.orientation = orientation;
		this.x = x;
		this.z = z;
	}

	public byte[] getData() {
		return (PacketType.ROCK_WALL.id + this.playerID + "," + this.orientation + "," + this.x + "," + this.z).getBytes();
	}

	/**
	 * Returns the orientation (in radians) this Packet05RockWall specifies.
	 */
	public float getOrientation() {
		return this.orientation;
	}

	/**
	 * Returns the x-coordinate this Packet05RockWall specifies.
	 */
	public float getX() {
		return this.x;
	}

	/**
	 * Sets the x-coordinate this Packet05RockWall specifies to "x".
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Returns the z-coordinate this Packet05RockWall specifies.
	 */
	public float getZ() {
		return this.z;
	}

	/**
	 * Sets the z-coordinate this Packet05RockWall specifies to "z".
	 */
	public void setZ(float z) {
		this.z = z;
	}
}