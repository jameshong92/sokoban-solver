import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;


/**
 * SokobanTester solves easy to moderate sokoban puzzles using different search algorithms.
 * Search algorithms are based on the textbook pseudocode, and Hungarian Algorithm is from wikipedia.
 * @author Hyun Seung Hong (hh2473)
 *
 */
public class SokobanTester {

	public static void main(String[] args) {
		// receive file input and search method, and if method is greedy or A*, receive input for choice of heuristics
		Scanner in = new Scanner(System.in);
		System.out.println("Enter filename:");
		String filename = in.nextLine();
		System.out.println("Enter b for breath-first, d for depth-first, u for uniform-cost, g for greedy, a for A* search: ");
		String method = in.nextLine();
		String hChoice = " ";
		// if search method is greedy or A* search, get heuristics choice
		if (method.charAt(0) == 'a' || method.charAt(0) == 'g' || method.charAt(0) == 'u') {
			System.out.println("Enter m - Manhattan Distance, e - Euclidean Distance, h - Hungarian Algorithm, x - max{h1, h2, h3}");
			hChoice = in.nextLine();
		}
		
		// load file, and check for number of players
		// if number of player is 1, solve the puzzle using chosen search method.
		if (hChoice.length()==0)
			hChoice = "x";
		SokobanSolver s = new SokobanSolver(hChoice.charAt(0));
		try {
			int numPlayer = s.loadFile(filename);
			if (numPlayer!=1)
				System.out.println("Please have only 1 player in the puzzle");
			else {
				System.out.println("File read successfully!");
				System.out.println("Solving...");
				// print the result
				System.out.println(s.solve(method.charAt(0)));
			}
		} catch (FileNotFoundException e) {
			System.out.println("File: \"" + filename + "\" not found!"); 
		} catch (NumberFormatException e) {
			System.out.println("File incorrectly formatted! First line needs to " +
					"contain total number of rows in the puzzle");
		} catch (NoSuchElementException e) {
			System.out.println("File incorrectly formatted! First line needs to " +
					"contain total number of rows in the puzzle");
		} // catch appropriate exceptions
	}

}
