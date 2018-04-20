package lab4;

public class Point {
	double x;
	double y;
	int p = 0;
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double dist(Point p) {
		double x_ = x - p.getX();
		double y_ = y - p.getY();
		return Math.sqrt(x_*x_ + y_*y_);
	}
	public void setP(int n) {
		p = n-1;
	}
	
	public int getP() {
		return p;
	}
}
