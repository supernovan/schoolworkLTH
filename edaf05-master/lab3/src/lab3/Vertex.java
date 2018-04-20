package lab3;
import java.util.*;


public class Vertex implements Comparable<Vertex> {
	HashMap<Edge, Integer> edges;
	String city;
	int weight;
	
	public Vertex(String city) {
		edges = new HashMap<Edge, Integer>();
		this.city = city;
	}
	
	public void addEdge(Edge e) {
		edges.put(e, e.getW());
	}
	
	public int getDist(Edge e) {
		return edges.get(e);
	}
	
	
	public Set<Edge> getL() {
		Set<Edge> temp = edges.keySet();
		return temp;
	}
	
	public String getStr() {
		return city;
	}
	
	public void eToStr() {
		System.out.println(edges.toString());
	}
	
	@Override
	public String toString() {
		return city;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Vertex) {
			Vertex v = (Vertex) o;
			return getStr().equals(v.getStr());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return getStr().hashCode();
	}
	
	@Override 
	public int compareTo(Vertex ob) {
		Vertex w = (Vertex) ob;
		if (city.compareTo(w.getStr()) > 0) {
			return 1;
		} else if (city.compareTo(w.getStr()) < 0) {
			return -1;
		}
		return 0;
	}
}
