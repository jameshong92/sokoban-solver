import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Problem class stores the coordinates for walls and goals, and implements goalTest and deadlockTest
 * to check whether the boxes are in the goal positions or state is in a deadlock state.
 * Problem class also implements actions function, which returns valid action for a player
 * @author Hyun Seung Hong (hh2473)
 *
 */
public class Problem {

	State initialState;
	HashSet<Coordinate> walls;
	HashSet<Coordinate> goals;
	HashMap<Coordinate, Coordinate> blocked;
	
	/**
	 * receives initial state, walls, and goals from the SokobanSolver class
	 * @param walls
	 * @param initialState
	 * @param goals
	 */
	public Problem(HashSet<Coordinate> walls, State initialState, HashSet<Coordinate> goals) {
		this.initialState = initialState;
		this.walls = walls;
		this.goals = goals;
	}

	/**
	 * checks whether the boxes in the current state are in the goal position
	 * returns true if goal is found, and returns false otherwise
	 */
	public boolean goalTest(State state) {
		for(Coordinate box : state.boxes)
			if (!goals.contains(box))
				return false;
		return true;
	}
	
	/**
	 * simple check to see if any of the boxes are in a deadlock state:
	 *  #  or  #####  or  ##
	 * #$      #X$X#      $$
	 * where X represents anything but a goal
	 * @param state
	 * @return
	 */
	public boolean deadlockTest(State state) {
		for (Coordinate box : state.boxes) {
			int row = box.row;
			int col = box.col;
			if (!setContains(goals, row, col)) {
				if (setContains(walls, row-1, col)&&setContains(walls, row, col-1))
					return true; //top & left
				if (setContains(walls, row-1, col)&&setContains(walls, row, col+1))
					return true; //top & right
				if (setContains(walls, row+1, col)&&setContains(walls, row, col-1))
					return true; //bottom & left
				if (setContains(walls, row+1, col)&&setContains(walls, row, col+1))
					return true; //bottom & right

				if (setContains(walls, row-1, col-1)&&setContains(walls, row-1, col)&&
						setContains(walls, row-1, col+1)&&setContains(walls, row, col-2)&&
						setContains(walls, row, col+2)&&(!setContains(goals, row, col-1))&&
								!setContains(goals, row, col+1))
					return true; //top & sides
				if (setContains(walls, row+1, col-1)&&setContains(walls, row+1, col)&&
						setContains(walls, row+1, col+1)&&setContains(walls, row, col-2)&&
						setContains(walls, row, col+2)&&(!setContains(goals, row, col-1))&&
								(!setContains(goals, row, col+1)))
					return true; //bottom & sides
				if (setContains(walls, row-1, col-1)&&setContains(walls, row, col-1)&&
						setContains(walls, row+1, col-1)&&setContains(walls, row-2, col)&&
						setContains(walls, row+2, col)&&(!setContains(goals, row-1, col))&&
								(!setContains(goals, row+1, col)))
					return true; //left & vertical
				if (setContains(walls, row-1, col+1)&&setContains(walls, row, col+1)&&
						setContains(walls, row+1, col+1)&&setContains(walls, row-2, col)&&
						setContains(walls, row+2, col)&&(!setContains(goals, row-1, col))&&
								(!setContains(goals, row+1, col)))
					return true; //right & top/bottom
			}
		}
		return false;
	}

	/**
	 * checks the available actions for the player
	 * @param state
	 * @return arraylist of strings (u d l r)
	 */
	public ArrayList<String> actions(State state) {
		ArrayList<String> actionList = new ArrayList<String>();
		int row = state.player.row;
		int col = state.player.col;
		HashSet<Coordinate> boxes = state.boxes;
		
		//checking if moving up, right, down, left is valid
		//for each, check if next player move is a wall
		//if next move has a box, check next box move does not overlap with wall or another box
		Coordinate newPlayer = new Coordinate(row-1,col);
		Coordinate newBox = new Coordinate(row-2, col);
		if (!walls.contains(newPlayer))
			if (boxes.contains(newPlayer)&&(boxes.contains(newBox)||walls.contains(newBox)))
				;
			else
				actionList.add("u");
		newPlayer = new Coordinate(row,col+1);
		newBox = new Coordinate(row, col+2);
		if (!walls.contains(newPlayer))
			if (boxes.contains(newPlayer)&&(boxes.contains(newBox)||walls.contains(newBox)))
				;
			else
				actionList.add("r");
		newPlayer = new Coordinate(row+1,col);
		newBox = new Coordinate(row+2, col);
		if (!walls.contains(newPlayer))
			if (boxes.contains(newPlayer)&&(boxes.contains(newBox)||walls.contains(newBox)))
				;
			else
				actionList.add("d");
		newPlayer = new Coordinate(row,col-1);
		newBox = new Coordinate(row, col-2);
		if (!walls.contains(newPlayer))
			if (boxes.contains(newPlayer)&&(boxes.contains(newBox)||walls.contains(newBox)))
				;
			else
				actionList.add("l");
		return actionList;
	}

	private boolean setContains(HashSet<Coordinate> set, int row, int col) {
		if (set.contains(new Coordinate(row, col)))
			return true;
		return false;
	}
	
}
