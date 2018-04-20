import java.util.*;
import java.io.*;
import java.util.regex.*;

public class gs {
	public static void main(String[] args) {
		//read in file and stuff
		Scanner scan = new Scanner(System.in);
		System.out.println("Skriv in filnamnet");
		String str;

		if (args.length > 0) {
			str = args[0];
		} else {
			str = scan.next(); 
		}
		
		String[] names = new String[1];
		boolean nameInFile = false;
		boolean checkSize = false;
		int size = 0;
		Person[] current = new Person[1];
		int curInd = 1;
		LinkedList<Person> men = new LinkedList<Person>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(str))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains("#")) {
					continue;
				}
				if (line.contains("n") && !checkSize) {
					checkSize = true;
					line = line.replaceAll("[^0-9]","");
					size = Integer.parseInt(line);
					current = new Person[size*2];
				}
				else if (line.contains(":")) {
					line.replaceAll("[^\\d]", "");
					String[] tokens = line.split(" ");
					tokens[0] = tokens[0].substring(0, tokens[0].length()-1);
					Person p = new Person(Integer.parseInt(tokens[0]), size);
					if (p.getNbr()%2 == 0) {
						men.add(p);
					} else {
						current[curInd] = p;
						curInd+= 2;
					}
					for (int i = 1; i<tokens.length; i++) {
						p.setPref(Integer.parseInt(tokens[i]));
					}
					
				} else if (Pattern.matches(".*[a-zA-Z]+.*", line)) {
					
					if (!nameInFile) {
						nameInFile = true;
						names = new String[500];
					}
					
					String[] tokens = line.split(" ");
					names[Integer.parseInt(tokens[0]) - 1] = tokens[1];
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} 
		
		/*
		for (int i = 0; i<men.size(); i++) {
			System.out.println(men.get(i).getNbr());
		}
		*/
		
		//test case 1
		/*
		Person ro = new Person(1, 3);
		ro.setPref(6);
		ro.setPref(4);
		ro.setPref(2);
		
		Person mo = new Person(2, 3);
		mo.setPref(3);
		mo.setPref(5);
		mo.setPref(1);
		
		Person ch = new Person(3, 3);
		ch.setPref(2);
		ch.setPref(6);
		ch.setPref(4);
		
		Person ph = new Person(4, 3);
		ph.setPref(5);
		ph.setPref(1);
		ph.setPref(3);
		
		Person jo = new Person(5, 3);
		jo.setPref(6);
		jo.setPref(4);
		jo.setPref(2);
		
		Person ra = new Person(6, 3);
		ra.setPref(1);
		ra.setPref(5);
		ra.setPref(3);
		
		
		
		men.push(ro);
		men.push(ch);
		men.push(jo);
		current[mo.getNbr()] = mo;
		current[ph.getNbr()] = ph;
		current[ra.getNbr()] = ra;
		*/
		
		while (!men.isEmpty()) {
			Person man = men.peek();
			int woman = man.askW();
			if (current[woman].isFree()) {
				current[woman].setPar(man);
				men.remove();
			}
			else {
				Person w = current[woman];
				if (w.getPref(man.getNbr())) {
					Person temp = w.curMan();
					w.setPar(man);
					men.remove();
					men.push(temp);
				}
			}
			
		}
//		for (int i = 0; i<names.length; i++) {
//			System.out.println(names[i]);
//		}
//		
//		
		for (int i = 1; i< current.length; i+=2) {
			if (nameInFile) {
				System.out.println(names[(current[i].getNbr())] + " is with " + names[(current[i].curMan().getNbr())]);
			} else {
				System.out.println((current[i].getNbr()+1) + " is with " + (current[i].curMan().getNbr()+1));
			}
		}
	}
}