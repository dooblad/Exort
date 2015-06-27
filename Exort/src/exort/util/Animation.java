package exort.util;

public class Animation {
	private int current, max;

	public Animation(int max) {
		this.current = 0;
		this.max = max;
	}

	public void tickUp(int delta) {
		if (this.current < this.max) {
			this.current++;
		}
	}

	public void tickDown(int delta) {
		if ((--this.current) < 0) {
			this.current = 0;
		}
	}

	public void fill() {
		this.current = this.max;
	}

	public void empty() {
		this.current = 0;
	}

	// Getters and setters
	public int getProgress() {
		return this.current;
	}

	public float getPercentage() {
		return (float) this.current / (float) this.max;
	}

	public float getSmoothedPercentage() {
		return (float) Math.sin((((float) this.current / (float) this.max) * Math.PI) / 2);
	}

	public boolean isFull() {
		return this.current >= this.max;
	}

	public boolean isEmpty() {
		return this.current <= 0;
	}
}
