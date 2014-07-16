package com.doobs.exort.net.client;

import java.net.*;

import com.doobs.exort.*;
import com.doobs.exort.level.*;
import com.doobs.exort.net.*;
import com.doobs.exort.net.packets.*;
import com.doobs.exort.state.*;

public class Client extends NetComponent {
	private Main main;

	private PacketHandler handler;

	public Client(Main main, Level level, String address) {
		this.main = main;

		handler = new PacketHandler(this, address, level);
		handler.start();
	}

	public void handleMove(Packet02Move packet) {
		if (main.getCurrentState() instanceof DuelState)
			((DuelState) main.getCurrentState()).getLevel().movePlayer(packet.getUsername(), packet.getX(), packet.getZ(), packet.getTime());
	}

	public void sendData(byte[] data) {
		handler.sendData(data);
	}

	// Getters and Setters
	public PacketHandler getHandler() {
		return handler;
	}

	public InetAddress getAddress() {
		return handler.getAddress();
	}

	public void setAddress(InetAddress address) {
		handler.setAddress(address);
	}

	public int getPort() {
		return handler.getPort();
	}
}