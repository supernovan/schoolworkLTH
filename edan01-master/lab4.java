
import org.jacop.core.Store;
import org.jacop.Core.IntVar;
import org.jacop.constraint.*;
import org.jacop.search.*;
import java.util.Scanner;

public class lab4 {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		String str = scan.next();
		data d = new data(str);

		long before = System.currentTimeToMillis();
		reg(d);
		long after = System.currentTimeToMillis();
		System.out.println("Executationtime in ms: " + (after - before));
	}

	public static void reg(Data data) {
		int timeAdd = data.timeAdd;
		int timeMul = data.timeMul;
		int nbrAdders = data.nbrAdders;
		int nbrMullers = data.nbrMullers;
		int nbrElements = data.nbrElements;

		int[] add = data.add;
		int[] mul = data.mul;
		int[] last = data.last;

		int[][] dep = data.dep;

		Store store = new Store();

		int maxTime = 50; //should maybe be higher?

		//2 rectangles, first for containing when the
		// operation starts inluding the length.
		// The other one is for containing the index of operations
		IntVar[] startT1 = new IntVar[nbrElements];
		IntVar[] lengthT1 = new IntVar[nbrElements];

		IntVar[] startT2 = new IntVar[nbrElements];
		IntVar[] lengthT2 = new IntVar[nbrElements];




		//Start: start for operation at index i
		//Duration: The duration at index (clockcycle)
		//End: endcycle for operation at index i 
		IntVar[] startA = new IntVar[add.length];
		IntVar[] durA = new IntVar[add.length];
		IntVar[] endA = new IntVar[add.length];

		IntVar[] startM = new IntVar[mul.length];
		IntVar[] durM = new IntVar[mul.length];
		IntVar[] endM = new IntVar[mul.length];



	}
}