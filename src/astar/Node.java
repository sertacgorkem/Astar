package astar;
import java.util.*;


/**
 * The Node Class. This models the single tile inside of a map.
 *
 * @author Sertac Gorkem
 * @version 1.0.1
 * @since 2019-07-15
 */

public class Node {

	//the neighbors for the a single node
	public Node north = null;
	public Node east = null;
	public Node west = null;
	public Node south = null;
	
	public boolean isOccupied = false;
	public boolean isVisited = false;
	
	public int gvalue = 0;
	public int fvalue = 0;
	public int hvalue = 0;
	
	public boolean isStart = false;
	public boolean isEnd = false;
	
	public ArrayList<Node> childeren = new ArrayList<Node> ();
	
	public int row = 0;
	public int rcol = 0;
	
	public boolean isDiscovered = false;
	
	public Node parent = null;
	
	/**
	 * Constructor for Node object
	 * @param _row int
	 * @param _col int
	 */
	public Node(int _row, int _col) {
		row = _row;
		rcol = _col;
	}
	
	/**
	 * Calculate Manhattan heuristic for current cell
	 * @param endRow int
	 * @param endCol int
	 */
	public void calculateH( int endRow, int endCol) {
			int height = (endCol - rcol);
			int width = (row - endRow);
			
			double value = Math.hypot(width, height);
			value =  Math.ceil(value);
			int distance = (int)value;
			hvalue = distance;
	}
	
	/**
	 * Calculate f value for current cell
	 */
	public void calculateF() {
		fvalue = gvalue + hvalue;

	}
	
	/**
	 * Checks if Nodes are equal
	 * @param o Object
	 * @return Boolean
	 */
	@Override
    public boolean equals(Object o) {
		if ( o == null) {
			return false;
		}
        if (this == o) {
            return true;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        Node s = (Node) o;
        return rcol == s.rcol && row == s.row;
    }
	
}
