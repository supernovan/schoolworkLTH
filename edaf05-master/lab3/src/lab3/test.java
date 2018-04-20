package lab3;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class test {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		System.out.println("Skriv in filnamnet");
		String str;
		str = scan.next();
		
		Graph g = new Graph();
		try (BufferedReader br = new BufferedReader(new FileReader(str))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains("\"") && !line.contains("--")) {
					line = line.replace(" ", "");
					line = line.replace("\"", "");
				} else if (line.contains("\"") || line.contains("--")) {
					line = line.replace(" ", "");
					line = line.replace("\"", "");
					line = line.replace("[", " ");
					line = line.replace("]", "");
				}
				if (!line.contains("--")) {
					line = line.replace(" ", "");
					Vertex v = new Vertex(line);
					g.add(v);
				} else if (line.contains("--")) {
					line = line.replace("--", " ");
					String[] tokens = line.split(" ");
					Vertex v1 = new Vertex(tokens[0]);
					Vertex v2 = new Vertex(tokens[1]);
					int w = Integer.parseInt(tokens[2]);
					g.addEdge(v1, v2, w);
				}
				
			}

			System.out.println("Den kortaste samt girigaste vägen är " + g.krus());
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
