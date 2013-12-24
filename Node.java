
/**
 * Node class stores state, parent node, cost, and move (l, r, u, d)
 * @author Hyun Seung Hong (hh2473)
 *
 */
public class Node {

	public Node parent;
	public State state;
	public int cost;
	public String move;
	
	public Node(State state, Node parent, int cost, String move) {
		this.state = state;
		this.parent = parent;
		this.cost = cost;
		this.move = move;
	}
	
	/**
	 * Equals returns true if states in the nodes are equal
	 */
	@Override
	public boolean equals(Object n) {
		return (this.state == ((Node) n).state);
	}

}
