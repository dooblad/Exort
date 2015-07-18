package server.net;

import java.io.*;
import java.net.*;

import server.*;
import server.level.*;
import server.ui.*;
import shared.*;

/**
 * Handles the low-level components of networking.
 */
public class PacketIO implements Runnable {
	private UI ui;

	private PacketHandler handler;

	// Net variables.
	private DatagramSocket socket;

	private volatile boolean running;

	/**
	 * Creates a PacketIO connected to "addresss". Uses "ui" for sending raw packets to
	 * chat when debugging. Automatically starts itself on a new Thread when created.
	 */
	public PacketIO(UI ui, Server server, Level level) {
		this.ui = ui;
		this.handler = new PacketHandler(server);
		try {
			this.socket = new DatagramSocket(NetVariables.PORT);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		new Thread(this).start();
	}

	/**
	 * Makes this PacketIO begin receiving packets.
	 */
	public void run() {
		this.running = true;
		while (this.running) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				this.socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
			if (Main.debug) {
				this.ui.addMessage("[" + packet.getAddress().getHostAddress() + "] " + new String(packet.getData()).trim());
			}
			this.handler.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
		}
	}

	/**
	 * Sends the packet contained within "data" to "address":"port".
	 */
	public void sendData(byte[] data, InetAddress address, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
		try {
			this.socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Closes all network processes.
	 */
	public void exit() {
		this.running = false;
		this.socket.close();
	}
}