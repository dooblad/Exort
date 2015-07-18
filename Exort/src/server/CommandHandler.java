package server;

import java.util.*;

import server.net.*;
import shared.*;

/**
 * Decomposes commands and executes them.
 */
public class CommandHandler {
	@SuppressWarnings("unused")
	private Server server;

	/**
	 * For when there is no available Server yet.
	 */
	public CommandHandler() {
		this(null);
	}

	/**
	 * Creates a CommandHandler for "server".
	 */
	public CommandHandler(Server server) {
		this.server = server;
	}

	/**
	 * Parses "input" into a command and executes it.
	 */
	public String handleInput(String input) {
		Scanner command = new Scanner(input);
		if (command.hasNext()) {
			String name = command.next();
			if (name.equals("get")) {
				return this.handleGet(command);
			} else if (name.equals("set")) {
				return this.handleSet(command);
			}
			command.close();
			return "Unknown command \"" + name + "\"";
		}
		command.close();
		return "";
	}

	/**
	 * Handles a given 'get' "command".
	 */
	public String handleGet(Scanner command) {
		String variable = command.next();
		if (variable.equals("port")) {
			return String.valueOf(NetVariables.PORT);
		} else {
			return "\"" + variable + "\" is not a gettable variable.";
		}
	}

	/**
	 * Handles a given 'set' "command".
	 */
	public String handleSet(Scanner command) {
		String variable = command.next();
		if (variable.equals("debug")) {
			String newValue = command.next();
			if (newValue.equals("true")) {
				Main.debug = true;
				return "\"debug\" set to true";
			} else if (newValue.equals("false")) {
				Main.debug = false;
				return "\"debug\" set to false";
			}
		}
		return "\"" + variable + "\" is not a settable variable.";
	}

	/**
	 * Assigns this CommandHandler to "server".
	 */
	public void setServer(Server server) {
		this.server = server;
	}
}
