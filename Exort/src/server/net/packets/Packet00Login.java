package server.net.packets;

/**
 * Used for Client logins and Player ID assignment by the Server.
 */
public class Packet00Login extends Packet {
	private String username;

	/**
	 * Recreates a Packet00Login from "data".
	 */
	public Packet00Login(byte[] data) {
		this.packetID = PacketType.LOGIN.getID();

		String[] dataArray = readData(data).split(",");
		this.username = dataArray[0];
		this.playerID = Integer.parseInt(dataArray[1]);
	}

	/**
	 * Creates a Packet00Login for a Player with "username" who has not been assigned an
	 * ID from the Server yet. This is primarily for Client use.
	 */
	public Packet00Login(String username) {
		this(username, -1);
	}

	/**
	 * Creates a Packet00Login to assign "id" to the Player with "username". This is
	 * primarily for Server use.
	 */
	public Packet00Login(String username, int id) {
		super(PacketType.LOGIN.getID(), id);
		this.username = username;
	}

	public byte[] getData() {
		return (PacketType.LOGIN.id + this.username + "," + this.playerID).getBytes();
	}

	/**
	 * Returns the username of the Player logging in.
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * Sets the username of the Player logging in to "username".
	 */
	public void setUsername(String username) {
		this.username = username;
	}
}