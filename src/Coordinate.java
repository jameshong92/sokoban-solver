/**
 * Coordinate class stores row and column values for boxes, player, goals and walls
 * @author Hyun Seung Hong (hh2473)
 *
 */
public class Coordinate {
	
	public int row;
	public int col;
	
	public Coordinate(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	// hashcode for row and column.
	// multiplying 1000 to row and adding col ensures that no same set of coordinates is added
	@Override
	public int hashCode() {
		return row*1000 + col;
	}
	
	// equals method used for contains()
	@Override
	public boolean equals(Object object){
		if (object == null) return false;
	    if (object == this) return true;
	    if (this.getClass() != object.getClass()) return false;
		Coordinate c = (Coordinate) object;
		if(this.hashCode()== c.hashCode()) return true;
	    return ((this.row == c.row) && (this.col == c.col));
	}
	
}
