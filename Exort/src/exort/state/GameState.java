package exort.state;

public interface GameState {
	public void tick(int delta);

	public void render();
}