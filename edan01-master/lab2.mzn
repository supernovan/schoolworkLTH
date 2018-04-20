include "globals.mzn";
graph_size = 6;
start = 1;
n_dests = 2;
dest = [5,6];
n_edges = 9;
from = [1,1,1,2,2,3,3,3,4];
to = [2,3,4,3,5,4,5,6,6];
cost = [6,1,5,5,3,5,6,4,2];	


%model

int: graph_size;
int: start;
int: n_dests;
int: n_edges;
array[1..graph_size] of var 1..graph_size: path;
array[1..n_edges] of int: from;
array[1..n_edges] of int: to;
array[1..n_edges] of int: cost;
array[1..n_dests] of int: dest;
array[1..graph_size] of var 0..1: used_v;
array[1..n_edges] of var 0..1: used_e;

var int: total_cost = sum(i in 1..n_edges)(
	if used_e[i] == 1 then cost[i]
	else 0
	endif
);
var 0..graph_size: nbr_v;
var 0..n_edges: nbr_e;

constraint
	nbr_v = sum(i in 1..graph_size where used_v[i] = 1)(used_v[i]); 

constraint
	nbr_e = sum(i in 1..n_edges where used_e[i] = 1)(used_e[i]); 

constraint
	nbr_e = nbr_v - 1;

constraint
	used_v[start] = 1;

constraint
	forall (i in 1..n_dests) (
		used_v[dest[i]] = 1
	);

constraint
	1 <= sum(i in 1..n_edges) (
		if used_e[i] == 1 then
			if from[i] == start \/ to[i] == start then 1
			else 0
			endif
		else 0
		endif 
	);

constraint 
	forall(i in 1..n_dests) (
		1 <= sum(j in 1..n_edges) (
			if used_e[j] == 1 then
				if dest[i] == from[j] \/ dest[i] == to[j] then 1
				else 0
				endif
			else 0
			endif
		)
	);

constraint
	forall (i in 1..n_edges) (
		if used_e[i] == 1 then
			used_v[from[i]] = 1 /\
			used_v[to[i]] = 1
		else true
		endif
	);



solve minimize total_cost;

output
	[show(total_cost) ]
		++
		[show(used_v[i])
		| i in 1..graph_size]

