sokoban-solver
==============
COMS W4701 Artificial Intelligence
<br>Fall 2013
<br>Assignment 2: Sokoban Search Algorithm
<br>James Hyun Seung Hong (hh2473)

Purpose
-------

The purpose of this project is to solve a Sokoban puzzle using different search methods.
<p>More information on Sokoban puzzle: http://www.sokobano.de/wiki/index.php?title=Main_Page</p>

Implementation
--------------
Search methods that I have implemented are:
- Breadth-first search (http://en.wikipedia.org/wiki/Breadth-first_search)
- Depth-first search (http://en.wikipedia.org/wiki/Depth-first_search)
- Uniform-cost search (http://en.wikipedia.org/wiki/Uniform-cost_search)
- Greedy best-first search (http://en.wikipedia.org/wiki/Best-first_search#Greedy_BFS)
- A* search (http://en.wikipedia.org/wiki/A*_search)

<br>
For greedy and A* search algorithms, I used 4 different heuristic functions:
- Manhattan distance (http://en.wikipedia.org/wiki/Taxicab_geometry)
- Euclidean distance (http://en.wikipedia.org/wiki/Euclidean_distance)
- Hungarian algorithm (http://en.wikipedia.org/wiki/Hungarian_algorithm)
- Max{manhattan, euclidean, hungarian}

Hungarian algorithm is adapted from https://github.com/KevinStern/software-and-algorithms/blob/master/src/main/java/blogspot/software_and_algorithms/stern_library/optimization/HungarianAlgorithm.java

Instructions
------------
<b> .java files should be placed in same directory as img folder and examples folder for the program to locate the images properly.</b>

<h3>Usage</h3>

To compile files:
<pre><code>cd src
javac *.java
</code></pre>

To run the program:

<pre><code>java SokobanTester
</code></pre>

<h2>Input</h2>

The Sokoban files must be in the following format:
<pre>[Number of columns]
[Number of rows]
[Rest of the puzzle]
</pre>

Puzzle should be expressed using the following ASCII characters:
<pre>#   (hash)      Wall 
.	(period)	Empty goal 
@	(at)    	Player on floor 
+	(plus)		Player on goal 
$	(dollar)	Box on floor 
*	(asterisk)	Box on goal 
</pre>

Output
------

The output is in the following format:
<pre>1. Search method used to solve the problem
2. Move solution (or fail message if no solution found)
3. Total number of steps taken
4. Number of nodes generated
5. Number of nodes containing states that were generated previously
6. Number of nodes on the explored list (if there is one) when termination occurs
7. The actual run time of the algorithm, expressed in milliseconds
</pre>
