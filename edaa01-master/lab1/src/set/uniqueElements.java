package set;
import set.MaxSet;
public class uniqueElements{
	public static int[] uniqueElements(int[] ints) {
	MaxSet<Integer> array = new MaxSet<Integer>();
	int maxim;
	for(int i : ints) {
		array.add(i);
	}
	int[] sort = new int[array.size()];
	for(int i = array.size()-1; i>=0; i--) {
		maxim = array.getMax();
		sort[i] = maxim;
		array.remove(maxim);
		
	}
	return sort;
	}
	public static void main(String[] args) {
		int[] banan = {6,5,5,7,2,8,9,8};
		int[] sorted = uniqueElements(banan);
		for(int e : sorted) {
			System.out.print(e);
		}
	}
}