package com.doobs.exort.net;

import java.net.InetAddress;

import com.doobs.exort.*;
import com.doobs.exort.level.*;
import com.doobs.exort.net.packets.*;

public class Client extends Thread implements Runnable{
	private Main main;

	private PacketHandler handler;
	
	// Packet timing
	private long lastTime;
	private long delta;
	private long time;

	public Client(Main main, Level level, String address) {
		this.main = main;

		handler = new PacketHandler(this, address, level);
		handler.start();
		
		lastTime = System.currentTimeMillis();
		delta = 0;
		time = 0;
	}
	
	public void run() {
		while(true) {
			delta = System.currentTimeMillis() - lastTime;
			lastTime = System.currentTimeMillis();
			time += delta;
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void handleMove(Packet02Move packet) {
		main.getLevel().movePlayer(packet.getUsername(), packet.getX(),
				packet.getZ(), packet.getTime());
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

	public int getPacketTime() {
		return (int) (time % 1000);
	}
	
	public void setTime(int time) {
		this.time = time;
	}
}