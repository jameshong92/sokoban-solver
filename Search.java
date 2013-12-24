import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 * Search class implements breadth-first search, depth-first search, uniform-cost search,
 * greedy search and heuristics search in order to solve the sokoban puzzle
 * @author Hyun Seung Hong (hh2473)
 *
 */
public class Search {

	//search strings
	private static final String BFS = "Breadth-First Search";
    private static final String DFS = "Depth-First Search";
    private static final String UCS = "Uniform-Cost Search";
    private static final String ASTAR = "A* Search";
    private static final String GREEDY = "Greedy Search";
    
    private static Heuristics h;
	
	public Search(Heuristics h) {
		Search.h = h;
	}
	
	/**
	 * Implementation for breadth-first search.
	 * Code is based on textbook pseudocode
	 * @param p
	 * @return
	 */
	public String bfs(Problem p) {
		long startTime = System.currentTimeMillis();
		int totalNode = 1;
		int redundant = 0;
		Node node = new Node(p.initialState, null, 0, ""); //node with state=problem.INITIAL-STATE, PATH-COST = 0
		if (p.goalTest(node.state)) //if problem.GOAL-TEST(node.state) then return SOLUTION(node)
			return getSolution(BFS, node, totalNode, 0, 0, 0, System.currentTimeMillis() - startTime);
		
		Set<State> explored = new HashSet<State>(); //empty set
		Queue<Node> fringe = new LinkedList<Node>();
		fringe.add(node);
		ArrayList<String> actions;
		Node child;
		while (!fringe.isEmpty()) {
			node = fringe.poll(); //node <- POP(frontier)
			//System.out.println("Player at ("+n.state.player.row+","+n.state.player.col+")");
			explored.add(node.state); //add node.STATE to explored
			
			actions = p.actions(node.state); //get actions
			for (int i=0; i<actions.size(); i++) { //for each action in problem.ACTIONS(node.STATE) do
				child = getChild(p, node, actions.get(i), false); //child <- CHILD-NODE(problem,node,action)
				if(child!=null && child.state!=null) {
					totalNode++;
					//if child.STATE is not in explored or fringe, then
					if ((!explored.contains(child.state))&&(!fringe.contains(child))) {
						String solution = getSolution(BFS, child, totalNode, redundant, fringe.size(), explored.size(), System.currentTimeMillis() - startTime);
						if (p.goalTest(child.state))
							return solution;
						if (!p.deadlockTest(child.state)) // check deadlock state
							fringe.add(child);
					}
					else
						redundant++;
				}
			}
		}
		return getSolution(BFS, null, totalNode, redundant, fringe.size(), explored.size(), System.currentTimeMillis() - startTime);
	}

	/**
	 * Implementation for depth-first search.
	 * Code is similar to breadth-first search, but instead of queue, stack is used for implementing LIFO
	 * @param p
	 * @return
	 */
	public String dfs(Problem p) {
		long startTime = System.currentTimeMillis();
		int totalNode = 1;
		int redundant = 0;
		Node node = new Node(p.initialState, null, 0, ""); //node with state=problem.INITIAL-STATE, PATH-COST = 0
		if (p.goalTest(node.state)) //if problem.GOAL-TEST(node.state) then return SOLUTION(node)
			return getSolution(DFS, node, totalNode, 0, 0, 0, System.currentTimeMillis() - startTime);
		
		Set<State> explored = new HashSet<State>(); //empty set
		Stack<Node> fringe = new Stack<Node>();
		fringe.push(node);
		while (!fringe.isEmpty()) {
			node = fringe.pop(); //node <- POP(frontier)
			//System.out.println("Player at ("+n.state.player.row+","+n.state.player.col+")");
			explored.add(node.state); //add node.STATE to explored
			ArrayList<String> actions = p.actions(node.state); //get actions
			for (int i=0; i<actions.size(); i++) { //for each action in problem.ACTIONS(node.STATE) do
				Node child = getChild(p, node, actions.get(i), false); //child <- CHILD-NODE(problem,node,action)
				if(child!=null&&child.state!=null) {
					totalNode++;
					//if child.STATE is not in explored or fringe, then
					if ((!explored.contains(child.state))&&(!fringe.contains(child))) {
						String solution = getSolution(DFS, child, totalNode, redundant, fringe.size(), explored.size(), System.currentTimeMillis() - startTime);
						if (p.goalTest(child.state))
							return solution;
						if (!p.deadlockTest(child.state))
							fringe.push(child);
					}
					else
						redundant++;
				}
			}
		}
		return getSolution(DFS, null, totalNode, redundant, fringe.size(), explored.size(), System.currentTimeMillis() - startTime);
	}

	/**
	 * Implementation for uniform-cost search, greedy search, and A* search.
	 * These search method uses same code, except for sorting for priorityQueue.
	 * @param p
	 * @param choice
	 * @return
	 */
	public String prioritySearch(Problem p, char choice) {
		String method = UCS;
		boolean isUCS = true;
		long startTime = System.currentTimeMillis();
		int totalNode = 1;
		int redundant = 0;
		Node initial = new Node(p.initialState, null, 0, "");
		Set<State> explored = new HashSet<State>();
		//check search method to see if greedy or A* is chosen
		Queue<Node> fringe = new PriorityQueue<Node>(11, costComparator);
		if (choice == 'g') {
			fringe = new PriorityQueue<Node>(11, greedyComparator);
			method = GREEDY;
			isUCS = false;
		}
		if(choice == 'a') {
			fringe = new PriorityQueue<Node>(11, astarComparator);
			method = ASTAR;
			isUCS = false;
		}
		fringe.add(initial);
		while (!fringe.isEmpty()) {
			Node n = fringe.remove();
			if (p.goalTest(n.state))
				return getSolution(method, n, totalNode, redundant, fringe.size(), explored.size(), System.currentTimeMillis() - startTime);
			if (!p.deadlockTest(n.state)) { //check for deadlock
				explored.add(n.state);
				ArrayList<String> actions = p.actions(n.state);
				for (int i=0; i<actions.size(); i++) {
					Node child = getChild(p, n, actions.get(i), isUCS);
					if((child!=null) && (child.state!=null)) {
						totalNode++;
						if ((!explored.contains(child.state))&&(!fringe.contains(child)))
								fringe.add(child);
						else {
							redundant++;
							//check if fringe contains current state and compare the cost
							for (Node next : fringe) {
								if (next == child) 
									if(child.cost < next.cost) {
										next = child;
									}
							}
						}
					}
				}
			}
		}
		return getSolution(method, null, totalNode, redundant, fringe.size(), explored.size(), System.currentTimeMillis() - startTime);
	}
	
	/**
	 * When solution is found, or when a program fails to solve the puzzle, getSolution returns the solution
	 * @param method
	 * @param n
	 * @param totalNode
	 * @param redundant
	 * @param fringeSize
	 * @param exploredSize
	 * @param totalTime
	 * @return
	 */
	private String getSolution(String method, Node n, int totalNode, int redundant, int fringeSize, int exploredSize, long totalTime) {
		String result = "";
		int steps = 0;
		if (n == null)
			result = "Failed to solve the puzzle";
		else
			while (n.parent!=null) {
				result = n.move + " " + result;
				n = n.parent;
				steps++;
			}
		result = "Using " + method + ":\n" + result + "\n(total of " + steps + " steps)" +
				"\na) Number of nodes generated: " + totalNode + 
				"\nb) Number of nodes containing states that were generated previously: " + redundant + 
				"\nc) Number of nodes on the fringe when termination occurs: " + fringeSize + 
				"\nd) Number of nodes on the explored list (if there is one) when termination occurs: " + exploredSize +
				"\ne) The actual run time of the algorithm, expressed in actual time units: " + totalTime + "ms";
		return result;
	}
	
	/**
	 * getChild method returns the child node according to the action performed (l, r, u, d)
	 * if isUcs is true, add 1 to newCost whenever box is pushed
	 * @param p
	 * @param n
	 * @param action
	 * @param isUcs
	 * @return
	 */
	private Node getChild(Problem p, Node n, String action, boolean isUcs) {
		@SuppressWarnings("unchecked")
		HashSet<Coordinate> boxes = (HashSet<Coordinate>) n.state.boxes.clone();
		int row = n.state.player.row;
		int col = n.state.player.col;
		int newCost = n.cost+1;
		Coordinate newPlayer = n.state.player;
		char choice = action.charAt(0);
		switch(choice) {
			case 'u':
				//update player coordinate
				newPlayer = new Coordinate(row-1, col);
				//check if player is pushing a box
				if (boxes.contains(newPlayer)) {
					Coordinate newBox = new Coordinate(row-2, col);
					//update box coordinate
					boxes.remove(newPlayer);
					boxes.add(newBox);
					if (isUcs)
						newCost++;
				}
				break;
			case 'd':
				//update player coordinate
				newPlayer = new Coordinate(row+1, col);
				//check if player is pushing a box
				if (boxes.contains(newPlayer)) {
					Coordinate newBox = new Coordinate(row+2, col);
					//update box coordinate
					boxes.remove(newPlayer);
					boxes.add(newBox);
					if (isUcs)
						newCost++;
				}
				break;
			case 'l':
				//update player coordinate
				newPlayer = new Coordinate(row, col-1);
				//check if player is pushing a box
				if (boxes.contains(newPlayer)) {
					Coordinate newBox = new Coordinate(row, col-2);
					//update box coordinate
					boxes.remove(newPlayer);
					boxes.add(newBox);
					if (isUcs)
						newCost++;
				}
				break;
			case 'r':
				//update player coordinate
				newPlayer = new Coordinate(row, col+1);
				//check if player is pushing a box
				if (boxes.contains(newPlayer)) {
					Coordinate newBox = new Coordinate(row, col+2);
					//update box coordinate
					boxes.remove(newPlayer);
					boxes.add(newBox);
					if (isUcs)
						newCost++;
				}
				break;
		}
		return new Node(new State(boxes, newPlayer), n, newCost, Character.toString(choice));
	}
	
	/**
	 * Comparator for A*
	 */
	public static Comparator<Node> astarComparator = new Comparator<Node>() {
		@Override
		public int compare(Node n1, Node n2) {
            return (int) ((n1.cost + h.getHeuristic(n1.state)) - (n2.cost + h.getHeuristic(n2.state)));
        }
	};
	
	/**
	 * Comparator for UCS
	 */
	public static Comparator<Node> costComparator = new Comparator<Node>(){
		@Override
		public int compare(Node n1, Node n2) {
            return (int) (n1.cost - n2.cost);
        }
	};
	
	/**
	 * Comparator for greedy search
	 */
	public static Comparator<Node> greedyComparator = new Comparator<Node>() {
		@Override
		public int compare(Node n1, Node n2) {
            return (int) ((h.getHeuristic(n1.state)) - (h.getHeuristic(n2.state)));
        }
	};
	
}



