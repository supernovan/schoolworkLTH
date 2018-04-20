import java.util.Scanner;

public class Calculator {
	public static void main(String[] args) {
		System.out.println("Skriv två tal");
		Scanner scan = new Scanner(System.in);
		double nbr1 = scan.nextDouble();
		double nbr2 = scan.nextDouble();
		double sum = nbr1 + nbr2;
		double sub = nbr1 - nbr2;
		double prod = nbr1*nbr2;
		double kvot = nbr1/nbr2;
		System.out.println("Summan av talen är " + sum);
		System.out.println("Skillnaden av talen är" + sub);
		System.out.println("produkten av talen är" + prod);
		System.out.println("Kvoten av talen är" + kvot);
		scan.close();
	}
}
