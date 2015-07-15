package exort.net.client;

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
	private InetAddress address;
	private int port;

	private volatile boolean running;

	/**
	 * Creates a PacketIO connected to "addresss". Uses "gui" for sending raw packets to
	 * chat when debugging.
	 */
	public PacketIO(Client client, GUI gui, InetAddress address, Level level) {
		this.gui = gui;
		this.handler = new PacketHandler(client, gui, address, level);
		try {
			this.socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		} 
		this.address = address;
		this.port = NetVariables.PORT;
		this.running = false;
		this.start();
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
				this.gui.addToChat(new Message("[" + packet.getAddress().getHostAddress() + "] " + new String(packet.getData()).trim(), new Vector4f(1f, 1f,
						0f, 1f)));
			}
			this.handler.parsePacket(data, packet.getAddress(), packet.getPort());
		}
	}

	/**
	 * Sends the packet contained within "data".
	 */
	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length, this.address, this.port);
		try {
			this.socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Closes all network processes.
	 */
	public synchronized void exit() {
		this.running = false;
		this.socket.close();
	}

	/**
	 * Returns the address this PacketIO is connecting to.
	 */
	public InetAddress getAddress() {
		return this.address;
	}

	/**
	 * Returns the port being used for networking.
	 */
	public int getPort() {
		return this.port;
	}
}