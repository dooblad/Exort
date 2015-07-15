package exort.net.server;

import java.io.*;
import java.net.*;

import org.lwjgl.util.vector.*;

import exort.*;
import exort.gui.*;
import exort.level.*;
import exort.net.*;
import exort.util.*;

/**
 * Handles the low-level components of networking.
 */
public class PacketIO extends Thread {
	private GUI gui;

	private PacketHandler handler;

	// Net variables.
	private DatagramSocket socket;
	private int port;

	private volatile boolean running;

	/**
	 * Creates a PacketIO connected to "addresss". Uses "gui" for sending raw packets to
	 * chat when debugging.
	 */
	public PacketIO(GUI gui, Server server, Level level) {
		this.gui = gui;
		this.port = NetVariables.PORT;
		try {
			this.socket = new DatagramSocket(this.port);
		} catch (SocketException e) {
			e.printStackTrace();
		}

		this.handler = new PacketHandler(server);
		this.running = false;
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
				this.gui.addToChat(new Message("[" + packet.getAddress().getHostAddress() + "] " + new String(packet.getData()).trim(), new Vector4f(1f, 0f,
						0f, 1f)));
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
	 * Returns the port being used for networking.
	 */
	public int getPort() {
		return this.port;
	}
}
