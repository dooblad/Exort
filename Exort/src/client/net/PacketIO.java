package client.net;

import java.io.*;
import java.net.*;

import org.lwjgl.util.vector.*;

import shared.level.*;
import shared.net.*;
import client.*;
import client.gui.*;
import client.util.*;

/**
 * Handles the low-level components of networking.
 */
public class PacketIO implements Runnable {
	private GUI gui;

	private PacketHandler handler;

	// Net variables.
	private DatagramSocket socket;
	private InetAddress address;

	private volatile boolean running;

	/**
	 * Creates a PacketIO connected to "addresss". Uses "gui" for sending raw Packets to
	 * chat when debugging. Automatically starts itself on a new Thread when created.
	 */
	public PacketIO(Client client, GUI gui, InetAddress address, Level level) {
		this.gui = gui;
		this.handler = new PacketHandler(client, level);
		try {
			this.socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		this.address = address;
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
				this.gui.addToChat(new Message("[" + packet.getAddress().getHostAddress() + "] " + new String(packet.getData()).trim(), new Vector4f(1f, 1f,
						0f, 1f)));
			}
			this.handler.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
		}
	}

	/**
	 * Sends the packet contained within "data".
	 */
	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length, this.address, NetVariables.PORT);
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
}