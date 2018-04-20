	include "globals.mzn";

num = 4;
cap = 5;
refill = 2;
fun = [4,-1,-2,3];

% model
int: num;
int: cap;
int: refill;
array[1..num] of int: fun;
array[1..num] of int: tokens;
array[1..num] of int: nbr_times;

var int: Total = sum(i in 1..num)(fun[i]*nbr_times[i]);
int: index = arg_min(fun);

constraint
	forall (i in 1..num) (
		nbr_times[i] > 0
	);

constraint
	forall (i in 1..num) (
		tokens[i] - nbr_times[i] >= 0
	);

constraint
	forall (i in 1..num) (
		tokens[i] >= 0
	);
constraint
	forall (i in 1..num) (
		tokens[i] <= cap
	);

constraint
	tokens[1] = cap;

constraint
	forall (i in 2..num) (
		tokens[i] == minimum(tokens[i-1] - nbr_times[i-1] + refill, cap)
	);
solve maximize Total;

output
	[show(Total)]




