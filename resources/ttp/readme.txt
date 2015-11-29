=== Traveling Thief Problem (TTP) instance files ===

== More Instances ==

All 9,720 instances can be downloaded from the following page:

http://cs.adelaide.edu.au/~optlog/research/ttp.php

== Reference ==

The article in which the instances are described is the following:

"A Comprehensive Benchmark Set and Heuristics for the Traveling Thief Problem"
Sergey Polyakovskiy, Mohammad Reza Bonyadi, Markus Wagner, Frank Neumann, and 
Zbigniew Michalewicz
Genetic and Evolutionary Computation Conference 2014
http://dl.acm.org/citation.cfm?id=2598249

Abstract: Real-world optimization problems often consist of several NP-hard optimization 
problems that interact with each other. The goal of this paper is to provide a benchmark 
suite that promotes a research of the interaction between problems and their mutual 
influence. We establish a comprehensive benchmark suite for the traveling thief problem 
(TTP) which combines the traveling salesman problem and the knapsack problem. Our 
benchmark suite builds on common benchmarks for the two sub-problems which grant a basis 
to examine the potential hardness imposed by combining the two classical problems. 
Furthermore, we present some simple heuristics for TTP and their results on our benchmark 
suite.

If you have any questions regarding the TTP instances, 
please do not hesitate to contact Markus Wagner (wagner@acrocon.com)

== tspTours ==

This folder contains binaries and some TSP tours:
- the binaries are "Chained Lin-Kernighan" (linkern) heuristic solvers taken from 
  http://www.math.uwaterloo.ca/tsp/concorde/downloads/downloads.htm
- the *.linkern.tour files contain one solution per TSBlib file
Note that multiple runs of linkern result typically result in different tours (for 
details see Section 6 of the above-mentioned article).