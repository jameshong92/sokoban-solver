import java.io.IOException;

/**
 * SokobanTester solves easy to moderate sokoban puzzles using different search algorithms.
 * Search algorithms are based on the textbook pseudocode, and Hungarian Algorithm is from wikipedia.
 * @author Hyun Seung Hong (hh2473)
 *
 */
public class SokobanTester {

	public static void main(String[] args) {
		try {
			MainFrame m = new MainFrame();
		} catch (IOException e) {
			System.out.println("IO Exception occured");
			e.printStackTrace();
		}
	}

}
