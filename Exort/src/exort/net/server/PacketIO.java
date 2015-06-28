package exort.net.server;

import java.io.*;
import java.net.*;

import org.lwjgl.util.vector.*;

import exort.*;
import exort.gfx.*;
import exort.level.*;
import exort.net.*;
import exort.util.*;

public class PacketIO extends Thread {
	private GUI gui;

	private DatagramSocket socket;
	private PacketParser parser;
	private int port;

	public PacketIO(GUI gui, Server server, Level level) {
		this.gui = gui;
		this.port = NetVariables.PORT;
		try {
			this.socket = new DatagramSocket(this.port);
		} catch (SocketException e) {
			e.printStackTrace();
		}

		this.parser = new PacketParser(gui, server, level);
	}

	public void run() {
		while (true) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				this.socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
			if (Main.debug) {
				this.gui.addMessage(new Message("[" + packet.getAddress().getHostAddress() + "] " + new String(packet.getData()).trim(), new Vector4f(1f, 0f,
						0f, 1f)));
			}
			this.parser.parsePacket(packet.getData(), packet.getAddress().getHostAddress(), packet.getPort());
		}
	}

	public void sendData(byte[] data, String address, int port) {
		try {
			DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName(address), port);
			this.socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void exit() {
		this.socket.close();
	}

	// Getters and setters
	public int getPort() {
		return this.port;
	}
}
