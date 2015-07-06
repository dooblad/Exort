package exort.net.client;

import java.io.*;
import java.net.*;

import org.lwjgl.util.vector.*;

import exort.*;
import exort.gui.*;
import exort.level.*;
import exort.net.*;
import exort.util.*;

public class PacketIO extends Thread {
	private GUI gui;

	private DatagramSocket socket;
	private PacketParser parser;
	private InetAddress address;
	private int port;

	public PacketIO(Client client, GUI gui, String address, Level level) {
		this.gui = gui;
		this.port = NetVariables.PORT;
		try {
			this.socket = new DatagramSocket();
			this.address = InetAddress.getByName(address);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		this.parser = new PacketParser(client, level);
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
				this.gui.addToChat(new Message("[" + packet.getAddress().getHostAddress() + "] " + new String(packet.getData()).trim(), new Vector4f(1f, 1f,
						0f, 1f)));
			}
			this.parser.parsePacket(data, packet.getAddress(), packet.getPort());
		}
	}

	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length, this.address, this.port);
		try {
			this.socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void exit() {
		this.socket.close();
	}

	public PacketParser getParser() {
		return this.parser;
	}

	public InetAddress getAddress() {
		return this.address;
	}

	public int getPort() {
		return this.port;
	}
}
