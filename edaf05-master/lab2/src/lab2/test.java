package lab2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class test {
	public static void main(String[] args) {
		Graph words = new Graph();
		Scanner scan = new Scanner(System.in);
		System.out.println("Skriv in dat-filnamnet");
		String str = scan.next(); 
		try (BufferedReader br = new BufferedReader(new FileReader(str))) {
			String line;
			while ((line = br.readLine()) != null) {
				words.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		System.out.println("Skriv in test-filnamnet");
		str = scan.next(); 
		try (BufferedReader br = new BufferedReader(new FileReader(str))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split(" ");
				tokens[0].replaceAll("[^a-zA-Z]", "");
				tokens[1].replaceAll("[^a-zA-Z]", "");
				System.out.println(words.bfs(tokens[0], tokens[1]));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		//manuell testning
		/*
		words.add("other");
		words.add("there");
		words.add("their");
		words.add("could");
		words.add("would");
		words.add("about");
		words.printGraph();
		System.out.println(words.bfs("other", "there"));
		System.out.println(words.bfs("other", "their"));
		System.out.println(words.bfs("could", "would"));
		System.out.println(words.bfs("would", "could"));
		System.out.println(words.bfs("there", "other"));
		System.out.println(words.bfs("about", "there"));
		*/ 
	}
}
