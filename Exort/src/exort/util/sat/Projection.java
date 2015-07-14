package exort.util.sat;

public class Projection {
	double min, max;

	public Projection(double min, double max) {
		this.min = min;
		this.max = max;
	}

	public Projection() {
		this(0, 0);
	}

	public boolean overlaps(Projection p) {
		return !((p.getMin() > this.max) || (p.getMax() < this.min));
	}

	public double getOverlap(Projection p) {
		double smallest = this.max - p.getMin();
		if ((this.min - p.getMax()) < smallest) {
			smallest = p.getMax() - this.min;
		}
		
		// If it returns negative values, they're not overlapping
		return smallest;
	}

	// Getters and setters
	public double getMin() {
		return this.min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMax() {
		return this.max;
	}

	public void setMax(double max) {
		this.max = max;
	}
}
