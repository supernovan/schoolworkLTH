import se.lth.cs.pt.window.SimpleWindow;

import java.util.Random;
import java.awt.Color;

public class LineDrawing {
	public static void main(String[] args) {
		int rng;
		Random rand = new Random();
		SimpleWindow w = new SimpleWindow(500, 500, "LineDrawing");
		w.moveTo(250, 250);
		w.setLineWidth(10);
		while (true) {
			w.waitForMouseClick();
			w.lineTo(w.getMouseX(), w.getMouseY());
			rng = rand.nextInt(3);
			if(rng == 0) {
				w.setLineColor(Color.red);
			}
			else if(rng == 1) {
				w.setLineColor(Color.blue);
			}
			else {
				w.setLineColor(Color.green);
			}
		}
	}
}
