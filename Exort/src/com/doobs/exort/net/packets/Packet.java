package com.doobs.exort.net.packets;

import com.doobs.exort.net.*;

public abstract class Packet {

	public static enum PacketType {
		INVALID(-1), LOGIN(00), DISCONNECT(01), MOVE(02);

		private int id;

		private PacketType(int packetId) {
			this.id = packetId;
		}

		public int getId() {
			return id;
		}
	}

	public byte packetId;

	public Packet(int packetId) {
		this.packetId = (byte) packetId;
	}

	public abstract void writeData(Client client);

	public String readData(byte[] data) {
		String message = new String(data).trim();
		return message.substring(2);
	}

	public abstract byte[] getData();

	public static PacketType lookupPacket(String id) {
		try {
			return lookupPacket(Integer.parseInt(id));
		} catch (NumberFormatException e) {
			return PacketType.INVALID;
		}
	}

	public static PacketType lookupPacket(int id) {
		for (PacketType p : PacketType.values()) {
			if (p.getId() == id) {
				return p;
			}
		}
		return PacketType.INVALID;
	}
}
