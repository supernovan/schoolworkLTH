package mountain;

public class Side {
	private Point a;
	private Point b;
	private Point mid;

	/** Constructs and initializes a side between the specified point, point location. */
	public Side(Point a, Point b) {
		this.a = a;
		this.b = b;
	}

	public Side(Point a, Point b, Point mid) {
		this.a = a;
		this.b = b;
		this.mid = mid;
	}

	/**
	 * Returns the a point.
	 * @return the b point
	 */
	public Point getA() {
		return a;
	}

	/**
	 * Returns the b point.
	 * @return the b point
	 */
	public Point getB() {
		return b;
	}

	public Point getMid() {
		return mid;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Side) {
			Side other = (Side)o;
			return a == other.getA() && b == other.getB() || b == other.getA() && a == other.getB();
		}
		return false;
	}
}