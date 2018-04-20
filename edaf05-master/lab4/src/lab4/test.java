package lab4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class test {
	public static void main(String[] args) {
		
		File folder = new File("input");
		ArrayList<File> listOfFiles = new ArrayList<File>(Arrays.asList(folder.listFiles()));
		Collections.sort(listOfFiles);
		

		for (File file : listOfFiles) {
		    if (file.isFile()) {
		    	ClosestPairs c = new ClosestPairs();
		        System.out.print(file.getName());
		        try (BufferedReader br = new BufferedReader(new FileReader("input/" + file.getName()))) {
					String line;
					while ((line = br.readLine()) != null) {
						if (line.matches(".*[a-zA-Z]+.*") || !line.matches(".*\\d+.*")) {
							if (!line.contains("e+0")) {
								continue;
							}
						}
						line = line.replaceAll("\\s+", " ").trim();
						if (line.startsWith(" ")) {
							line = line.substring(1);
						}
						line.replace("e+0", "*10^");
						String[] tokens = line.split(" ");
						if (tokens.length < 3) {
							continue;
						}
						Point p = new Point(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]));
						p.setP(Integer.parseInt(tokens[0]));
						c.add(p);
						
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} 
				
				c.sort();
				System.out.print(" " + c.getS());
				System.out.println(" " + c.getD());
		    }
		}
	}
}
