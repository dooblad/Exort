package com.doobs.exort.net;

import java.io.*;
import java.net.*;

import com.doobs.exort.level.*;
import com.doobs.exort.net.client.*;
import com.doobs.exort.net.server.*;

public class PacketHandler extends Thread {
	private DatagramSocket socket;
	private PacketParser parser;
	private InetAddress address;
	private int port;

	public PacketHandler(NetComponent component, String address, Level level) {
		port = NetVariables.PORT;
		try {
			if (component instanceof Client) {
				socket = new DatagramSocket();
				this.address = InetAddress.getByName(address);
			} else if (component instanceof Server) {
				socket = new DatagramSocket(port);
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		parser = new PacketParser(component, level);
	}

	@Override
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
			parser.parsePacket(data, packet.getAddress().toString(), packet.getPort());
		}
	}

	public void sendData(byte[] data) {
		sendData(data, null);
	}

	public void sendData(byte[] data, String address) {
		try {
			DatagramPacket packet;

			if (address != null)
				packet = new DatagramPacket(data, data.length, InetAddress.getByName(address), port);
			else
				packet = new DatagramPacket(data, data.length, this.address, port);

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
