package exort.net.packets;

/**
 * Used to conjure a SonicWave in a certain direction (in radians).
 */
public class Packet04SonicWave extends Packet {
	// In radians.
	private float direction;

	/**
	 * Recreates a Packet04SonicWave from "data".
	 */
	public Packet04SonicWave(byte[] data) {
		this.packetID = PacketType.SONIC_WAVE.getID();

		String[] dataArray = readData(data).split(",");
		this.playerID = Integer.parseInt(dataArray[0]);
		this.direction = Float.valueOf(dataArray[1]);
	}

	/**
	 * Creates a Packet04SonicWave in "direction" (in radians) from the Player with "id".
	 */
	public Packet04SonicWave(int id, float direction) {
		super(PacketType.SONIC_WAVE.getID(), id);
		this.direction = direction;
	}

	public byte[] getData() {
		return (PacketType.SONIC_WAVE.id + this.playerID + "," + this.direction).getBytes();
	}

	/**
	 * Returns the direction (in radians) this Packet04SonicWave specifies.
	 */
	public float getDirection() {
		return this.direction;
	}
}
