package client.state;

/**
 * Used for controlling the game's state easily... duh.
 */
public interface GameState {
	/**
	 * Handles the behavior of this GameState, adjusting time-reliant variables using
	 * "delta".
	 */
	public void tick(int delta);

	/**
	 * Renders this GameState.
	 */
	public void render();
}
