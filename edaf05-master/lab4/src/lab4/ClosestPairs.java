package lab4;

import java.util.*;

public class ClosestPairs {
	ArrayList<Point> pairs;
	Point[] help;
	Point helper;
	double d;
	int[] pair;
	public ClosestPairs() {
		pairs = new ArrayList<Point>();
		d = Double.MAX_VALUE;
	}
	
	public void add(Point p) {
		pairs.add(p);
	}
	
	public void sort() {
		mSort(0, pairs.size()-1);
		closeP();
	}
	private void mSort(int low, int high) {
		if (low < high) {
			int middle = low + (high - low)/2;
			mSort(low, middle);
			mSort(middle+1, high);
			
			merge(low, middle, high);
		}
	}
	
	private void merge(int low, int middle, int high) {
		
		help = new Point[pairs.size()];
		
		for (int i = low; i<= high; i++) {
			help[i] =  pairs.get(i);
		}
		
		
		int i = low;
		int j = middle + 1;
		int k = low;
		
		while (i <= middle && j <= high) {
			if (help[i].getX() <= help[j].getX()) {
				pairs.set(k, help[i]);
				i++;
			} else {
				pairs.set(k, help[j]);
				j++;
			}
			k++;
		}
		
		while (i <= middle) {
			pairs.set(k, help[i]);
			k++;
			i++;
		}
	}
	
	private void closeP() {
		int middle = 0 + (pairs.size())/2;
		closeP(0, middle);
		closeP(middle+1, pairs.size());
		pointC(d, 0, pairs.size(), middle);
	}
	
	private void closeP(int l, int u) {
		if (u - l <= 3) {
			for (int i = l; i<=u-1; i++) {
				for (int j = i+1; j<=u; j++) {
					if (j == pairs.size()) {
						return;
					}
					double d1 = pairs.get(i).dist(pairs.get(j));
					if (d1 < d) {
						d = d1;
					}
				}
			}
			return;
		}
		int middle = l + (u - l)/2;
		closeP(l, middle);
		closeP(middle+1, u);
		pointC(d, l, u, middle);
	}
	
	private void pointC(double dist, int low, int high, int middle) {
		double l = pairs.get(middle).getX() + (pairs.get(middle+1).getX() - pairs.get(middle).getX())/2;
		ArrayList<Point> finalP = new ArrayList<Point>(16);
		boolean flag = false;
		
		for (int i = low; i<high; i++) {
			if (pairs.get(i).getX() > l - dist && pairs.get(i).getX() < l + dist) {
				for (int j = 0; j<finalP.size(); j++) {
					if (pairs.get(i).getY() < finalP.get(j).getY()) {
						finalP.add(j, pairs.get(i));
						flag = true;
						break;
					}
				}
				if (!flag) {
					finalP.add(pairs.get(i));
				}
				flag = false;
			}
		}
		for (int i = 0; i<finalP.size()-1; i++) {
			for (int j = i+1; j<finalP.size(); j++) {
				
					double tempD = finalP.get(i).dist(finalP.get(j));
					if (d > tempD) {
						d = tempD;
					}
				
			}
		}
	}
	
	public double getD() {
		return d;
	}
	public int getS() {
		return pairs.size();
	}
	
	public void brute() {
		pair = new int[2];
		double dist = Integer.MAX_VALUE;
		for (int i = 0; i<pairs.size()-1; i++) {
			for (int j = i+1; j<pairs.size(); j++) {
				if (pairs.get(i).dist(pairs.get(j)) < dist) {
					dist = pairs.get(i).dist(pairs.get(j));
					pair[0] = i;
					pair[1] = j;
				}
			}
		}
		System.out.println(dist + " Potatis " + pair[0] + " " + pair[1]);
	}
}
