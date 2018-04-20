package lab2;

public class Edge {
	
	private Head first, second;
	
	public Edge(Head first, Head second) {
		this.first = first;
		this.second = second;
	}
	
	@Override
	public boolean equals(Object ob) {
		if (ob instanceof Edge) {
			Edge e = (Edge) ob;
			return e.first.equals(first) && e.second.equals(second);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return first.getStr().hashCode() ^ second.getStr().hashCode();
	}
	
}
