package lab2;
import java.util.*;

public class Graph {
	HashSet<Head> graph;
	public Graph() {
		graph = new HashSet<Head>();
	}
	
	public boolean add(String str) {
		if (graph.contains(str)) {
			return false;
		} else {
			Head h = new Head(str);
			for (Head ver : graph) {
				String temp = ver.getStr();
				
				if (strCheck(temp, str)) {
					ver.addEdge(h);
				}
				if (strCheck(str, temp)) {
					h.addEdge(ver);
				}
				
			}
			graph.add(h);
			return true;
		}
	}
	public int bfs(String h, String tar) {
		Head iter = null;
		for (Head ver : graph) {
			if (ver.getStr().equals(h)) {
				iter = ver;
				break;
			}
		}
		if (iter == null) {
			return -1;
		}
		
		LinkedList<Head> q = new LinkedList<Head>();
		q.addFirst(iter);
		iter.visited();
		while (!q.isEmpty()) {
			Head temp = q.removeFirst();
			if (temp.getStr().equals(tar)) {
				int dist = temp.getDist();
				clear();
				return dist-1;
			}
			for (int i = 0; i<temp.getSize(); i++) {
				if (!temp.getNeighbor(i).getVisit()) {
					temp.getNeighbor(i).visited();
					q.addLast(temp.getNeighbor(i));
					temp.getNeighbor(i).addDist(temp.getDist());
				}
			}
			
			
		}
		clear();
		return -1;
	}
	
	private void clear() {
		for (Head ver : graph) {
			ver.clear();
			for (int j = 0; j<ver.getSize(); j++) {
				ver.getNeighbor(j).clear();
			}
		}
	}
	
	public void printGraph() {
		for (Head ver : graph) {
			System.out.print(ver.getStr());
			for (int j = 0; j<ver.getSize(); j++) {
				System.out.print(" " + ver.getNeighbor(j).getStr());
			}
			System.out.print("\n");
		}
	}
	
	private boolean strCheck(String first, String sec) {
		boolean[] check = new boolean[4];
		char ab = first.charAt(1);
		char bb = first.charAt(2);
		char cb = first.charAt(3);
		char db = first.charAt(4);
		int count = 0;
		for (int i = 0; i<sec.length(); i++) {
			char temp = sec.charAt(i);
			if (temp == ab && !check[0]) {
				check[0] = true;
			} else if (temp == bb && !check[1]) {
				check[1] = true;
			} else if (temp == cb && !check[2]) {
				check[2] = true;
			} else if (temp == db && !check[3]) {
				check[3] = true;
			} else {
				count++;
				if (count > 1) {
					return false;
				}
			}
		}
		
		return check[0] && check[1] && check[2] && check[3]; 
	}
}
