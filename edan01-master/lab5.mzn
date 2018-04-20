include "globals.mzn";
% DATA
n = 5;
n_commercial = 13;
n_residential = 12; 
point_distribution = [-5,-4,-3,3,4,5];

%
int: n;
int: n_commercial; 
int: n_residential;

var int: total = sum(row_cost) + sum(col_cost);

array[1..n+1] of int: point_distribution;
array[1..n,1..n] of var 0..1: matrix; %matrix for the grid
array[1..n] of var int: row_cost;
array[1..n] of var int: col_cost;


%commercial is 0, residental is 1
constraint 
	sum(i in 1..n)(
		sum(j in 1..n)(
			matrix[i,j]
		)) = n_residential;


%contstrain symmetri stuff?
constraint lex2(matrix);

constraint
	forall(i in 1..n) (
		col_cost[i] = point_distribution[sum(j in 1..n)(matrix[i,j]) + 1]
	);

constraint
	forall(i in 1..n) (
		row_cost[i] = point_distribution[sum(j in 1..n)(matrix[j,i]) + 1]
	);

solve:: int_search([matrix[i,j] | i, j in 1..n], first_fail, indomain_min, complete) maximize(total);

output
	[show(total)]

