sokoban-solver
==============
James Hyun Seung Hong (hh2473)
Fall 2013 Artificial Intelligence Assignment 2

Purpose
-------

The purpose of this project is to solve a Sokoban puzzle using different search methods.

Search methods that I have implemented are:
  -Breadth-first search (http://en.wikipedia.org/wiki/Breadth-first_search)
  -Depth-first search (http://en.wikipedia.org/wiki/Depth-first_search)
  -Uniform-cost search (http://en.wikipedia.org/wiki/Uniform-cost_search)
  -Greedy best-first search (http://en.wikipedia.org/wiki/Best-first_search#Greedy_BFS)
  -A* search (http://en.wikipedia.org/wiki/A*_search)

For greedy and A* search algorithms, I used 4 different heuristic functions:
  -Manhattan distance (http://en.wikipedia.org/wiki/Taxicab_geometry)
  -Euclidean distance (http://en.wikipedia.org/wiki/Euclidean_distance)
  -Hungarian algorithm (http://en.wikipedia.org/wiki/Hungarian_algorithm)
  -Max{manhattan, euclidean, hungarian}

Hungarian algorithm is adapted from adapted from:
https://github.com/KevinStern/software-and-algorithms/blob/master/src/main/java/blogspot/software_and_algorithms/stern_library/optimization/HungarianAlgorithm.java

Usage
-----

Compile files:
<pre><code>
cd src
javac *.java
</code></pre>

The main method is SokobanTester.

<pre><code>
java SokobanTester
</code></pre>

Input
-----

The Sokoban files must be in the following format:

> [Number of columns]
> [Number of rows]
> [Rest of the puzzle]

Puzzle should be expressed using the following ASCII characters:
> #	(hash)  	Wall 
> .	(period)	Empty goal 
> @	(at)    	Player on floor 
> +	(plus)		Player on goal 
> $	(dollar)	Box on floor 
> *	(asterisk)	Box on goal 

Output
------

The output is in the following format:
1. Search method used to solve the problem
2. Move solution (or fail message if no solution found)
3. Total number of steps taken
4. Number of nodes generated
5. Number of nodes containing states that were generated previously
6. Number of nodes on the explored list (if there is one) when termination occurs
7. The actual run time of the algorithm, expressed in milliseconds
