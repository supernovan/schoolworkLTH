include "globals.mzn";

%%%%%%%%%%%%%%%%%%%%%DATA%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Add and multiply duration
int : dAdd;
int : dMul;

%number of adders and mullers
int : nAdd;
int : nMul;

% values from data
int: maxTime;
int : nodes;
set of int : add;
set of int : mul;

% to store the ending time
var 1..maxTime: cycles;

%the dependencie graph
array[1..nodes] of set of int : dependencies;

%Arrays to keep track of durations, finishtime and startingtimes
array[1..nodes] of var int: sTimes;
array[1..nodes] of var int: durations;
array[1..nodes] of var int: finish_times;

%Add vectors
array[1..length(add)] of var 0..maxTime: startTAdd;
array[1..length(add)] of var dAdd..dAdd: add_durations;
array[1..length(add)] of var 1..1: add_resources;
array[1..length(add)] of var 1..nAdd: addSchedule;


%Mul vectors
array[1..length(mul)] of var 0..maxTime: startTMul;
array[1..length(mul)] of var dMul..dMul: mul_durations;
array[1..length(mul)] of var 1..1: mul_resources;
array[1..length(mul)] of var 1..nMul: mulSchedule;




%%%%%%%%%%%%%%%%%%%%%CONSTRAINTS%%%%%%%%%%%%%%%%%%


%every start time should be above 0
constraint forall (i in 1..nodes) (sTimes[i] >= 0);

%and bellow maxTime
constraint forall (i in 1..nodes) (sTimes[i] <= maxTime);

%every duration which is a part of the addset should be dAdd long
constraint forall (i in add) (durations[i] = dAdd);

%same with multimers
constraint forall (i in mul) (durations[i] = dMul);

%to make sure that the starting times of the nodes after doesn't start before the dependencies are done
constraint forall (i in 1..nodes, j in dependencies[i]) 
	(sTimes[j] >= sTimes[i]+durations[i]);

% The finish time of i i always the starttime and the duration of that operation
constraint forall (i in 1..nodes) 
	(finish_times[i] = sTimes[i]+durations[i]);


%Set the starttime vector for add to be inserted here for cumulative constraint
constraint forall (i in 1..length(add))
	(startTAdd[i] = sTimes[add[i]]);

%same with all the multiplying sarting times
constraint forall (i in 1..length(mul)) 
	(startTMul[i] = sTimes[mul[i]]);


%set the maximum finishtime to nbr of cycles it took
constraint cycles = max(finish_times);

%To constrain the program so it only can do nAdd at a time
%constraint cumulative(startTAdd, add_durations, add_resources, nAdd);

%same thing but with multiply operations
%constraint cumulative(startTMul, mul_durations, mul_resources, nMul);

%set up stuff
%constraint
%	diffn(startTAdd, addSchedule, add_durations, add_resources);

%constraint
%	diffn(startTMul, mulSchedule, mul_durations, mul_resources);


solve minimize cycles;
