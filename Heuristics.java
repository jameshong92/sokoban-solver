import java.util.HashSet;

/**
 * Heuristics class calculates the cost from current state to the goal, and returns the optimal cost.
 * It implements three different heuristics - Manhattan Distance, Euclidean Distance, and Hungarian Algorithm
 * @author Hyun Seung Hong (hh2473)
 *
 */
public class Heuristics {
	
	private HashSet<Coordinate> goals;
	double[][] cost;
	HungarianAlgorithm h;
	char hChoice;
	
	/**
	 * Stores coordinates for goals, and initializes array for cost.
	 * @param goals
	 */
	public Heuristics(HashSet<Coordinate> goals, char hChoice) {
		this.goals = goals;
		this.hChoice = hChoice;
		this.cost = new double[goals.size()][goals.size()];
		h = new HungarianAlgorithm(cost.length);
	}
	
	/**
	 * Gets two coordinates as variables, and adds the sum of differences of rows and columns
	 * to get distance between two coordinates
	 * @param c1 coodinate 1
	 * @param c2 coordinate 2
	 * @return distance from c1 to c2
	 */
	private int manhattan(Coordinate c1, Coordinate c2) {
		return Math.abs(c1.row-c2.row) + Math.abs(c1.col-c2.col);
	}
	
	/**
	 * Gets two coordinates as variables, and calculates straight line distance by squaring row and column
	 * values and taking the square root of them.
	 * @param c1 coordinate 1
	 * @param c2 coordinate 2
	 * @return distance from c1 to c2
	 */
	private double euclidean(Coordinate c1, Coordinate c2) {
		return Math.sqrt((double)((c1.row-c2.row)*(c1.row-c2.row)+(c1.col-c2.col)*(c1.col-c2.col)));
	}

	/**
	 * Receives state and heuristic choice as parameters and calculates the distance for each box and player
	 * @param s state
	 * @param str heuristics choice
	 * @return sum of distances for player and boxes
	 */
	public double calculate(State s, String method) {
		HashSet<Coordinate> boxes = s.boxes;
		double sum = 0;
		
		//get distance from player to boxes, and add to sum
		Coordinate player = s.player;
		double playerMin = getDist(player, boxes, method);
		sum+= playerMin;
		
		//get distance from boxes to goals, add each minimum distance to the sum
		for (Coordinate b : boxes) {
			double boxMin = getDist(b, goals, method);
			sum += boxMin;
		}
		
		return sum;
	}

	/**
	 * Calculates minimum distance from given object to each set of coordinates
	 * @param obj
	 * @param sets
	 * @param method
	 * @return distance from obj to closest coordinate in the hashset
	 */
	private double getDist(Coordinate obj, HashSet<Coordinate> sets, String method) {
		double minDist = 1000000;
		
		//For each coordinate in a set, calculate the distance according to given heuristic choice
		for (Coordinate c : sets) {
			double dist;
			if (method.equals("m"))
				dist = manhattan(obj, c);
			else
				dist = euclidean(obj, c);
			if (dist < minDist)
				minDist = dist;
		}
		
		return minDist;
	}
	
	/**
	 * Gets state as a parameter, and calculates cost from current state to goal state 
	 * using the input heuristics choice (hChoice)
	 * @param state
	 * @return
	 */
	public double getHeuristic(State state) {
		
		if (hChoice == 'm')
			return calculate(state, "m");
		if (hChoice == 'e')
			return calculate(state, "e");
		
		int i=0;
		for (Coordinate box : state.boxes) {
			int j=0;
			double playerCost = manhattan(state.player, box);
			for (Coordinate goal : goals) {
				cost[i][j] = manhattan(box, goal);
				cost[i][j] += playerCost;
				j++;
			}
			i++;
		}
		
		int[] result = h.execute(cost);
		double max = 0;
		for (int k=0; k<goals.size(); k++) {
			int goalCol = result[k];
			if (goalCol>-1)
				max += cost[k][goalCol];
		}
		if (hChoice == 'h')
			return max;
		
		return Math.max(Math.max(calculate(state, "m"), calculate(state, "e")), max);
	}

}