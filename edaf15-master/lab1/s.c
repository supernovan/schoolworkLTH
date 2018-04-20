#include <stdio.h>
#include <stdlib.h>

struct frac {
	int nom;
	int dom;
};
typedef struct frac frac;

void print_(frac* f) {
	printf("%d / %d\n", f->nom, f->dom);
}


void reduce_(frac* f) {
	int n = f->nom;
	int d = f->dom;
	int check = 0;

	if (d == 1 || d == 0 || n == 1) {
		return;
	}
	while (1 == 1) {
		int i;
		for (i = 2; i <= d; i++) {
			if (n % i == 0 && d % i == 0) {
				n /= i;
				d /= i;
				check = 1;
				break;
			}
		}
		if (check == 0) {
			f->nom = n;
			f->dom = d;
			return;
		}
		check = 0;
	}
	//Should never come here
}
frac* addq(frac* f1, frac* f2) {
	frac* add = (frac*)malloc(sizeof(frac));
	add->nom = f1->nom * f2->dom + f2->nom*f1->dom;
	add->dom = f1->dom * f2->dom;
	reduce_(add);
	return add;
}

frac* subq(frac* f1, frac* f2) {
	frac* sub = (frac*)malloc(sizeof(frac));
	sub->nom = f1->nom * f2->dom - f2->nom*f1->dom;
	sub->dom = f1->dom * f2->dom;
	reduce_(sub);
	return sub;
}

frac* mulq(frac* f1, frac* f2) {
	frac* mul = (frac*)malloc(sizeof(frac));
	mul->nom = f1->nom * f2->nom;
	mul->dom = f1->dom * f2->dom;
	reduce_(mul);
	return mul;
}

frac* divq(frac* f1, frac* f2) {
	frac* divi = (frac*)malloc(sizeof(frac));
	divi -> nom = f1 -> nom * f2 -> dom;
	divi -> dom = f1 -> dom * f2 -> nom;
	reduce_(divi);
	return divi; 
}



int main(int argc, char* argv[])
{
	frac* f1 = (frac*)malloc(sizeof(frac));
	frac* f2 = (frac*)malloc(sizeof(frac));

	f1 -> nom = 3;
	f1 -> dom = 5;
	f2 -> nom = 5;
	f2 -> dom = 15;

	print_(f1);
	reduce_(f2);
	print_(f2);

	frac* add = addq(f1, f2);
	print_(add);
	frac* sub = subq(f2, f1);
	print_(sub);
	frac* divi = divq(f1, f2);
	print_(divi);
	frac* mul = mulq(f1, f2);
	print_(mul);
	free(f1);
	free(f2);
	free(add);
	free(sub);
	free(divi);
	free(mul);
}