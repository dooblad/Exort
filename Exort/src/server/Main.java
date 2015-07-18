package server;

import server.level.*;
import server.net.*;
import server.ui.*;

public class Main {
	private static final String NAME = "Exort Server";
	private static final int DEFAULT_WIDTH = 640, DEFUALT_HEIGHT = 480;

	public static boolean debug = true;

	private CommandHandler commands;

	private UI ui;
	private Level level;
	private Server server;

	public Main() {
		this.commands = new CommandHandler();

		this.ui = new UI(NAME, DEFAULT_WIDTH, DEFUALT_HEIGHT, this.commands);
		this.level = new Level();
		this.server = new Server(this.ui, this.level);
		this.commands.setServer(this.server);
	}

	public static void main(String[] args) {
		new Main();
	}
}
