package com.doobs.exort.net.server;

import java.io.*;
import java.net.*;

import com.doobs.exort.gfx.*;
import com.doobs.exort.level.*;
import com.doobs.exort.net.*;

public class PacketIO extends Thread {
	private DatagramSocket socket;
	private PacketParser parser;
	private int port;
	
	public PacketIO(Server server, Level level) {
		port = NetVariables.PORT;
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}

		parser = new PacketParser(server, level);
	}

	public void run() {
		while (true) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
			GUI.addMessage("[" + packet.getAddress().getHostAddress() + "] " + new String(packet.getData()).trim());
			parser.parsePacket(packet.getData(), packet.getAddress().getHostAddress(), packet.getPort());
		}
	}

	public void sendData(byte[] data, String address, int port) {
		try {
			DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName(address), port);
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void exit() {
		socket.close();
	}

	// Getters and setters
	public int getPort() {
		return port;
	}
}
