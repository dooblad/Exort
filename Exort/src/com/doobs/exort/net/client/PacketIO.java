package com.doobs.exort.net.client;

import java.io.*;
import java.net.*;

import org.lwjgl.util.vector.*;

import com.doobs.exort.gfx.*;
import com.doobs.exort.level.*;
import com.doobs.exort.net.*;
import com.doobs.exort.util.*;

public class PacketIO extends Thread {
	private GUI gui;
	
	private DatagramSocket socket;
	private PacketParser parser;
	private InetAddress address;
	private int port;
	
	public PacketIO(Client client, GUI gui, String address, Level level) {
		this.gui = gui;
		port = NetVariables.PORT;
		try {
			socket = new DatagramSocket();
			this.address = InetAddress.getByName(address);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		parser = new PacketParser(gui, client, level);
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
			gui.addMessage(new Message("[" + packet.getAddress().getHostAddress() + "] " + new String(packet.getData()).trim(), new Vector4f(1f, 1f, 0f, 1f)));
			parser.parsePacket(data, packet.getAddress(), packet.getPort());
		}
	}

	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void exit() {
		socket.close();
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
