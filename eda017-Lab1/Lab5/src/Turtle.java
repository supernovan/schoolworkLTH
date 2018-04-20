import se.lth.cs.pt.window.SimpleWindow;
import java.math.*;
public class Turtle {
	SimpleWindow w;
	int x, y, dir;
	boolean pen;
	/** Skapar en sköldpadda som ritar i ritfönstret w. Från början 
	    befinner sig sköldpaddan i punkten x, y med pennan lyft och 
	    huvudet pekande rakt uppåt i fönstret (i negativ y-riktning). */
	public Turtle(SimpleWindow w, int x, int y) {
		this.w = w;
		this.x = x;
		this.y = y;
		dir = 90;
		w.moveTo(x, y);
	}

	/** Sänker pennan. */
	public void penDown() {
		pen = true;
	}
	
	/** Lyfter pennan. */
	public void penUp() {
		pen = false;
	}
	
	/** Går rakt framåt n pixlar i den riktning huvudet pekar. */
	public void forward(int n) {
		w.moveTo(x, y);
		x = x+n*((int) Math.cos(dir*Math.PI/180));
		y = y-n*((int) Math.sin(dir*Math.PI/180));
		if(pen) {
			w.lineTo(x,y);
		}
		else {
			w.moveTo(x,y);
		}
	}

	/** Vrider beta grader åt vänster runt pennan. */
	public void left(int beta) {
		dir += beta;
		System.out.println(dir);
	}

	/** Går till punkten newX, newY utan att rita. Pennans läge (sänkt
	    eller lyft) och huvudets riktning påverkas inte. */
	public void jumpTo(int newX, int newY) {
		w.moveTo(newX, newY);
	}

	/** Återställer huvudriktningen till den ursprungliga. */
	public void turnNorth() {
		dir = 90;
	}

	/** Tar reda på x-koordinaten för sköldpaddans aktuella position. */
	public int getX() {
		return x;
	}

 	/** Tar reda på y-koordinaten för sköldpaddans aktuella position. */
	public int getY() {
		return y;
	}
  
	/** Tar reda på sköldpaddans riktning, i grader från den positiva X-axeln. */
 	public int getDirection() {
 		return dir;
	}
}
