import se.lth.cs.pt.window.SimpleWindow;
import se.lth.cs.pt.square.Square;

public class DrawSquare {
	public static void main(String[] args) {
		SimpleWindow w = new SimpleWindow(600, 600, "DrawSquare");
		Square sq = new Square(250, 250, 100);
		sq.draw(w);
		int sqX = 250;
		int sqY = 250;
		while(true) {
			w.waitForMouseClick();
			sq.erase(w);
			for(int i = 0; i<10; i++) {
				sq.move((w.getMouseX()-sqX)/10, (w.getMouseY()-sqY)/10);
				sq.draw(w);
				w.delay(10);
				sq.erase(w);
			}
			sq.draw(w);
			sqX = w.getMouseX();
			sqY = w.getMouseY();
		}
	}
}
