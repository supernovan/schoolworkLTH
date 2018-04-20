package fractal;
import koch.Koch;
import mountain.Mountain;

public class FractalApplication {
	public static void main(String[] args) {
		Fractal[] fractals = new Fractal[1];
		// fractals[0] = new Koch(300);
		fractals[0] = new Mountain(300);
	    new FractalView(fractals, "Fraktaler");
	}

}
