#include <stdio.h>

int main(int argc, char* argv[])
{
	int a;
	int b;
	int c;
	int nbr;
	
	printf("Please enter two numbers: ");
	
	// 4
	fflush(stdout);
	
	nbr = scanf("%d %d", &a, &b);

	//5
	printf("Number of elements is: %d\n", nbr);

	c = a + b;


	printf("%d + %d = %d\n", a, b, c);

	//6
	//printf("Argument number %d is %s\n", argc-1, argv[argc-1]);

	//7
	/*
	int d;
	int e;
	sscanf(argv[1], "%d", &d);
	sscanf(argv[2], "%d", &e);
	printf("%d + %d = %d\n", d,e, d + e);
	*/

	//8
	int sum = 0;
	int temp;
	int i;
	printf("%d\n", argc);
	for (i = 1; i<argc; i++) { 
		if (sscanf(argv[i], "%d", &temp) != 1) {
			exit(1);
		}
		sum += temp;
	}
	printf("Summan av element är: %d\n", sum);

	//9 
	FILE *fp;

	fp = fopen("test.txt", "w+");
	fprintf(fp, "Summan av element är: %d\n", sum);
	fclose(fp);

	return 0;
}