import java.util.HashSet;

/**
 * State class stores set of boxes and player
 * @author Hyun Seung Hong (hh2473)
 *
 */
public class State {

	HashSet<Coordinate> boxes;
	Coordinate player;
	
	public State(HashSet<Coordinate> boxes, Coordinate player) {
		this.boxes = boxes;
		this.player = player;
	}
	
	/**
	 * Implemented hashcode to check if state is already explored
	 */
	@Override
	public int hashCode() {
		int result = 17;
		for (Coordinate b : boxes) {
			result = 37 * result + b.hashCode();
		}
		result = 37 * result + player.hashCode();
		return result;
	}
	
	/**
	 * Overriding equals for contains() method
	 */
	@Override
	public boolean equals(Object object){
		
	    if (object == null) return false;
	    if (object == this) return true;
	    if (this.getClass() != object.getClass()) return false;
	    State s = (State)object;
	    if(this.hashCode()== s.hashCode()) return true;
	    if((this.boxes == s.boxes) && (this.player == s.player)) return true;
	    return false;
	}
	
}