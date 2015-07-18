package server;

import server.net.*;
import server.ui.*;
import shared.level.*;

public class Main {
	private static final String NAME = "Exort Server";
	private static final int DEFAULT_WIDTH = 640, DEFAULT_HEIGHT = 480;

	private CommandHandler commands;

	private ServerUI ui;
	private Level level;
	private Server server;

	public Main() {
		this.commands = new CommandHandler();

		this.ui = new ServerUI(NAME, DEFAULT_WIDTH, DEFAULT_HEIGHT, this.commands);
		this.level = new Level();
		this.server = new Server(this.ui, this.level);
		this.commands.setServer(this.server);
	}

	public static void main(String[] args) {
		new Main();
	}
}
