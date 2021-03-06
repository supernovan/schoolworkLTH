package lab2;
import java.util.*;

public class Head {
	ArrayList<Head> neighbor;
	private String word;
	private int size;
	private int dist;
	boolean visit;
	
	public Head(String str) {
		word = str;
		visit = false;
		size = 0;
		dist = 1;
		neighbor = new ArrayList<Head>();
	}
	
	public String getStr() {
		return word;
	}
	
	public void addEdge(Head e) {
		if (neighbor.contains(e)) {
			return;
		} else {
			neighbor.add(e);
			size++;
			
		}
	}
	@Override
	public boolean equals(Object v) {
		boolean retVal = false;
		
		if (v instanceof Head) {
			Head temp = (Head) v;
			retVal = temp.getStr() == word;
		}
		
		return retVal;
	}
	
	@Override
	public int hashCode() {
		return word.hashCode();
	}
	public boolean haveNeighbor(Head h) {
		return neighbor.contains(h);
	}
	public Head getNeighbor(int i) {
		if (i >= size) {
			return null;
		} else {
			return neighbor.get(i);
		}
	}
	
	public void visited() {
		visit = true;
	}
	
	public boolean getVisit() {
		return visit;
	}
	
	public void clear() {
		visit = false;
		dist = 1;
	}
	
	public int getSize() {
		return size;
	}
	
	public int getDist() {
		return dist;
	}
	
	public void addDist(int d) {
		dist += d;
	}
}
