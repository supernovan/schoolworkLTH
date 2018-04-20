package lab3;

import java.util.*;

public class Graph {
	ArrayList<Vertex> cities;
	public Graph() {
		cities = new ArrayList<Vertex>();
	}
	
	public boolean add(Vertex city) {
		return cities.add(city);
	}
	
	public void addEdge(Vertex v1, Vertex v2, int w) {
		for (int i = 0; i<cities.size(); i++) {
			if (v1.getStr().equals(cities.get(i).getStr())) {
				Edge e = new Edge(v1, v2, w);
				cities.get(i).addEdge(e);
				break;
			}
		}
	}
	
	public Set<Edge> getEdgeTree(int index) {
		return cities.get(index).getL();
	}
	
	public int krus() {
		List<Edge> allSets = new ArrayList<Edge>();
		for (int i = 0; i<cities.size(); i++) {
			allSets.addAll(cities.get(i).getL());
		}
		Collections.sort(allSets);
		ArrayList<HashSet<Vertex>> result = new ArrayList<HashSet<Vertex>>();
		int dist = 0;
		boolean flag = false;
		int lastSet = 0;
		for (int i = 0; i<allSets.size(); i++) {
			Edge e = allSets.get(i);

			for (int j = 0; j<result.size(); j++) {
				if (result.get(j).contains(e.beg) && result.get(j).contains(e.end)) {
					flag = true;
					break;
				} else if (result.get(j).contains(e.beg) || result.get(j).contains(e.end)) {
					if (flag) {
						result.get(lastSet).addAll(result.get(j));
						result.remove(j);
						break;
					} else {
						result.get(j).add(e.beg);
						result.get(j).add(e.end);
						dist += e.getW();
						flag = true;
						lastSet = j;
					}
				}
			}
			if (!flag) {
				result.add(new HashSet<Vertex>());
				result.get(result.size()-1).add(e.beg);
				result.get(result.size()-1).add(e.end);
				dist += e.getW();
			}
			flag = false;
			lastSet = 0;
		}
		for (Vertex v : result.get(0)) {
			System.out.println(v.getStr());
		}
		//merge edgelists
		//sort in 1..2.. osv
		//bygg en ny lista där man undviker cirkulationer
		// Lägg in i HashSet och kolla om båda finns med andra ord.
		//skriv ut längden 
		
		return dist;
	}
}
