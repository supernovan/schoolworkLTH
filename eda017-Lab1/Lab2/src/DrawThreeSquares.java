import se.lth.cs.pt.window.SimpleWindow;
import se.lth.cs.pt.square.Square;

public class DrawThreeSquares {
	public static void main(String[] args) {
		SimpleWindow w = new SimpleWindow(600, 600, "DrawSquare");
		// Square sq = new Square(250, 250, 100);
		Square sq = new Square(200, 200, 100);
		sq.draw(w);
		sq = new Square(220,280,100);
		sq.draw(w);
		sq = new Square(230,300,100);
		sq.draw(w);
	}
}
