package com.doobs.exort.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.doobs.exort.level.*;

public class PacketHandler extends Thread {
	private DatagramSocket socket;
	private PacketParser parser;
	private InetAddress address;
	private int port;

	public PacketHandler(Client client, String address, Level level) {
		port = NetVariables.PORT;
		try {
			socket = new DatagramSocket();
			this.address = InetAddress.getByName(address);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		parser = new PacketParser(client, level);
	}

	public void run() {
		while (true) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			// System.out.println("[" + packet.getAddress().getHostAddress() +
			// "]:" + new String(packet.getData()).trim());
			parser.parsePacket(data, packet.getAddress(), packet.getPort());
		}
	}

	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length, address,
				port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Getters and setters
	public PacketParser getParser() {
		return parser;
	}

	public InetAddress getAddress() {
		return address;
	}

	public void setAddress(InetAddress address) {
		this.address = address;
	}

	public int getPort() {
		return port;
	}
}
