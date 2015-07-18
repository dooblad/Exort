package shared.net;

import java.io.*;
import java.net.*;

import server.net.*;
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
	public PacketIO(UI ui, Networker networker) {
		this.ui = ui;
		this.handler = new PacketHandler(networker);
		try {
			if (networker instanceof Server) {
				this.socket = new DatagramSocket(NetVariables.PORT);
			} else {
				this.socket = new DatagramSocket();
			}
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
			byte[] data = new byte[NetVariables.BYTES_PER_PACKET];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				this.socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
			if (GlobalVariables.debug) {
				this.ui.addMessage("[" + packet.getAddress().getHostAddress() + ":" + packet.getPort() + "] " + new String(packet.getData()).trim());
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

	/**
	 * Returns the port this PacketIO is bound to.
	 */
	public int getPort() {
		// TODO: Figure out what this means.
		return this.socket.getLocalPort();
	}
}