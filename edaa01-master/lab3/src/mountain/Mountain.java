package mountain;

import fractal.*;
import java.util.LinkedList;

public class Mountain extends Fractal {
	private int length;
	private LinkedList<Side> sides;

	/** Creates an object that handles Koch's fractal.
	 * @param length the length of the triangle side
	 */
	public Mountain(int length) {
		super();
		this.length = length;
		sides = new LinkedList<Side>();
	}

	/**
	 * Returns the title.
	 * @return the title
	 */
	public String getTitle() {
		return "Mountain triangel";
	}

	private Point getCenter(Point a, Point b, double dev) {
		if (!sides.contains(new Side(a, b))) {
			Point p = new Point(a.getX() + (b.getX() - a.getX()) / 2, a.getY() + (b.getY() - a.getY())/2 + RandomUtilities.randFunc(dev));
			sides.add(new Side(a, b, p));
			return p;
		}
		Side s = new Side(a, b);
		Point p = sides.get(sides.indexOf(s)).getMid();
		sides.remove(s);
		

		return p;
	}

	/** Draws the fractal.
	 * @param turtle the turtle graphic object
	 */
	public void draw(TurtleGraphics turtle) {
		Point lowerleft  = new Point(turtle.getWidth() / 2.0 - length / 2.0,
			turtle.getHeight() / 2.0 + Math.sqrt(3.0) * length / 4.0);
		Point upper      = new Point(lowerleft.getX() + 150, lowerleft.getY() + Math.sin(-Math.PI/3)*300);
		Point lowerright = new Point(lowerleft.getX() + 300, lowerleft.getY());

		fractalTriangle(turtle, order, 20.0, lowerleft, upper ,lowerright);
	}

	/*
	 * Recursive method: Draws a recursive line of the triangle.
	 */
	private void fractalTriangle(TurtleGraphics turtle, int order, double dev, Point a, Point b, Point c) {
		if (order == 0) {
			turtle.moveTo(a.getX(), a.getY());
			turtle.forwardTo(b.getX(), b.getY());
			turtle.forwardTo(c.getX(), c.getY());
			turtle.forwardTo(a.getX(), a.getY());
		} else {
			Point q = getCenter(a, b, dev);
			Point r = getCenter(b, c, dev);
			Point s = getCenter(c, a, dev);

			fractalTriangle(turtle, order - 1, dev / 2, a, q, s);
			fractalTriangle(turtle, order - 1, dev / 2, q, b, r);
			fractalTriangle(turtle, order - 1, dev / 2, r, c, s);
			fractalTriangle(turtle, order - 1, dev / 2, s, q, r);
		}
	}
}
