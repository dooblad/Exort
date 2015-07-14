package exort.net.client;

import java.io.*;
import java.net.*;

import org.lwjgl.util.vector.*;

import exort.*;
import exort.gui.*;
import exort.net.*;
import exort.util.*;

/**
 * Handles the low-level components of networking.
 */
public class PacketIO extends Thread {
	private GUI gui;

	// Net variables.
	private DatagramSocket socket;
	private PacketHandler parser;
	private InetAddress address;
	private int port;

	private volatile boolean running;

	/**
	 * Creates a PacketIO connected to "addresss". Uses "gui" for sending raw packets to
	 * chat when debugging.
	 */
	public PacketIO(GUI gui, PacketHandler parser, String address) {
		this.gui = gui;
		this.parser = parser;
		this.port = NetVariables.PORT;
		try {
			this.socket = new DatagramSocket();
			this.address = InetAddress.getByName(address);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
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
				this.gui.addToChat(new Message("[" + packet.getAddress().getHostAddress() + "] " + new String(packet.getData()).trim(), new Vector4f(1f, 1f,
						0f, 1f)));
			}
			this.parser.parsePacket(data, packet.getAddress(), packet.getPort());
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