package astar;

import java.util.*;
import java.io.*;

/**
 * The Map Class. Logic and map functions for astar implemented here
 *
 * @author Sertac Gorkem
 * @version 1.0.1
 * @since 2019-07-15
 */

public class Map {

	public Node arrMap[][] = null;
	public Node dummy[][] = null;
	public int randomStart = 101;
	public int begin = 0;
	public int end = 0;
	public long nodeCount = 0;
	public String fileName = "";
	public ArrayList<ArrayList<Node>> pathList = new ArrayList<ArrayList<Node>>();
	public ArrayList<Node> visited = new ArrayList<Node>();
	public boolean isExample = false;


	Comparator<Node> intcomp = new Comparator<Node>() {
		@Override
		public int compare(Node s1, Node s2) {
			if (s1.fvalue == s2.fvalue) {
				return s1.gvalue - s2.gvalue;
			} else {
				return s1.fvalue - s2.fvalue;
			}
		}
	};
	
	Comparator<Node> intcomp2 = new Comparator<Node>() {
		@Override
		public int compare(Node s1, Node s2) {

			return s1.fvalue - s2.fvalue;

		}
	};
	/**
	 * constructor for Map object
	 * 
	 * @param example Boolean
	 */
	public Map(boolean example) {

		if (example == true) {
			isExample = true;
			arrMap = new Node[5][5];
			for (int i = 0; i < arrMap.length; i++) {
				for (int j = 0; j < arrMap[i].length; j++) {
					Node nd = new Node(i, j);
					arrMap[i][j] = nd;
					if (i != 0) {
						arrMap[i - 1][j].south = arrMap[i][j];
						arrMap[i][j].north = arrMap[i - 1][j];
					}

					if (j != 0) {
						arrMap[i][j - 1].west = arrMap[i][j];
						arrMap[i][j].east = arrMap[j][j - 1];
					}

				}
			}
			arrMap[1][2].isOccupied = true;
			arrMap[2][2].isOccupied = true;
			arrMap[2][3].isOccupied = true;
			arrMap[3][2].isOccupied = true;
			arrMap[3][3].isOccupied = true;
			arrMap[4][3].isOccupied = true;

			arrMap[4][2].isStart = true;
			arrMap[4][4].isEnd = true;
			begin = 22;
			end = 24;

			dummy = arrMap.clone();
		} else {
			this.makeNeighbors();
		}
	}

	/**
	 * Generates neighbors for indivual cells
	 * 
	 *
	 */
	private void makeNeighbors() {
		for (int i = 0; i < arrMap.length; i++) {
			for (int j = 0; j < arrMap[i].length; j++) {
				if (i != 0) {
					arrMap[i - 1][j].south = arrMap[i][j];
					arrMap[i][j].north = arrMap[i - 1][j];
				}

				if (j != 0) {
					arrMap[i][j - 1].west = arrMap[i][j];
					arrMap[i][j].east = arrMap[i][j - 1];
				}
			}
		}
	}

	/**
	 * Generates 100x100 map with close 70% cell occupancy
	 * 
	 * @param filePath String
	 */
	public void generate(String filePath) {

		arrMap = new Node[randomStart][randomStart];

		for (int i = 0; i < arrMap.length; i++) {
			for (int j = 0; j < arrMap[i].length; j++) {
				Node nd = new Node(i, j);
				arrMap[i][j] = nd;
			}
		}

		double randomDouble = Math.random();
		int val = randomStart * randomStart;
		randomDouble = randomDouble * (val);
		int randomStart = (int) randomDouble;
		begin = randomStart;
		int rowS = randomStart / 101;
		int colS = randomStart % 101;
		System.out.println(randomStart + " beginning " + rowS + " " + colS);

		double randomDouble2 = Math.random();
		randomDouble2 = randomDouble2 * (val);
		int randomEnd = (int) randomDouble2;
		end = randomEnd;
		int rowE = randomEnd / 101;
		int colE = randomEnd % 101;
		System.out.println(randomEnd + " ending " + rowE + " " + colE);

		arrMap[rowS][colS].isStart = true;
		arrMap[rowE][colE].isEnd = true;

		try {

			FileWriter writer = new FileWriter(filePath, true);
			for (int i = 0; i < arrMap.length; i++) {
				for (int j = 0; j < arrMap[i].length; j++) {
					if (arrMap[i][j].isStart == false && arrMap[i][j].isEnd == false) {
						// %70 is occupied
						double randomOccupancy = Math.random();
						randomOccupancy = randomOccupancy * 100;
						int randomNumber = (int) randomOccupancy;
						if (randomNumber > 70) {
							arrMap[i][j].isOccupied = true;
						}
					}
					if (arrMap[i][j].isStart == true) {
						writer.write("S");
					} else if (arrMap[i][j].isEnd == true) {
						writer.write("E");
					} else {
						if (arrMap[i][j].isOccupied == true) {
							writer.write("x");
						} else {
							writer.write("-");
						}
					}
				}
				writer.write("\n");
			}
			writer.close();
		} catch (Exception e) {
			System.out.println("Error occurred while writing the array to file " + e.getMessage() + "\n Moving on");
		}

	}

	/**
	 * Prints out the solution for the map
	 * 
	 * @param filePath String
	 */
	public void printSolution(String filePath) {
		try {

			FileWriter writer = new FileWriter(filePath, true);
			for (int i = 0; i < arrMap.length; i++) {
				for (int j = 0; j < arrMap[i].length; j++) {

					if (arrMap[i][j].isStart == true) {
						writer.write("S");
					} else if (arrMap[i][j].isEnd == true) {
						writer.write("E");
					} else {
						if (arrMap[i][j].isOccupied == true) {
							writer.write("x");
						} else if (arrMap[i][j].isVisited == true) {
							writer.write("V");
						} else {
							writer.write("-");
						}
					}
				}
				writer.write("\n");
			}
			writer.close();
		} catch (Exception e) {
			System.out.println("Error occurred while writing the array to file " + e.getMessage() + "\n Moving on");
		}
	}

	/**
	 * Prints alternate paths on to given file
	 * 
	 * @param filePath String
	 */
	public void printPaths(String filePath) {
		for (int p = 0; p < pathList.size(); p++) {
			try {
				String fp = filePath + "/" + p + ".txt";
				Node[][] temp = new Node[randomStart][randomStart];
				FileWriter writer = new FileWriter(fp, true);
				temp = dummy.clone();

				ArrayList<Node> nodeList = pathList.get(p);
				for (int z = 0; z < nodeList.size(); z++) {
					Node nd = nodeList.get(z);
					temp[nd.row][nd.rcol].isVisited = true;
				}

				for (int i = 0; i < temp.length; i++) {
					for (int k = 0; k < temp[i].length; k++) {

						if (temp[i][k].isStart == true) {
							writer.write("S");
						} else if (temp[i][k].isEnd == true) {
							writer.write("E");
						} else {
							if (temp[i][k].isOccupied == true) {
								if (temp[i][k].isVisited == true) {
									writer.write("P");
								} else {
									writer.write("x");
								}
							} else {

								if (temp[i][k].isVisited == true) {
									writer.write("V");
								} else {
									writer.write("-");
								}
							}

						}
					}
					writer.write("\n");
				}
				writer.close();
			} catch (Exception e) {
				System.out.println("Error occurred while writing the array to file " + e.getMessage() + "\n Moving on");
			}
		}
	}

	/**
	 * reads generated text file from the file and makes sets the occupied locations
	 * 
	 * @param text String
	 * @param row int
	 */
	public void readString(String text, int row) {
		if (arrMap == null) {
			arrMap = new Node[randomStart][randomStart];
		}
		for (int j = 0; j < arrMap[row].length; j++) {
			Node nd = new Node(row, j);
			if (text.charAt(j) == '-') {
				nd.isOccupied = false;
			} else if (text.charAt(j) == 'x') {
				nd.isOccupied = true;
			}

			if (text.charAt(j) == 'S') {
				begin = row * arrMap.length + j % arrMap.length;
				nd.isStart = true;
			} else if (text.charAt(j) == 'E') {
				end = row * arrMap.length + j % arrMap.length;
				nd.isEnd = true;
			}

			arrMap[row][j] = nd;
		}
		dummy = arrMap.clone();
	}

	/**
	 * Resets the Map
	 */
	public void resetArr() {
		for (int i = 0; i < arrMap.length; i++) {
			for (int j = 0; j < arrMap[i].length; j++) {
				arrMap[i][j].isVisited = false;
				arrMap[i][j].isDiscovered = false;
				arrMap[i][j].parent = null;
			}
		}
		nodeCount = 0;
	}

	

	

	/**
	 * Prints out the Map on the console
	 */
	public void print() {
		for (int i = 0; i < arrMap.length; i++) {
			for (int j = 0; j < arrMap[i].length; j++) {
				if (arrMap[i][j].isStart == true) {
					System.out.print(" S ");
				} else if (arrMap[i][j].isEnd == true) {
					System.out.print(" E ");
				} else {
					if (arrMap[i][j].isOccupied == true) {
						System.out.print(" x ");
					} else if (arrMap[i][j].isVisited == true) {
						System.out.print(" v ");
					} else {
						System.out.print(" - ");
					}
				}
			}
			System.out.println();
		}
	}

	/**
	 * Begins Forward astar
	 * 
	 * @return Boolean
	 */
	public boolean beginForward() {
		boolean result = true;
		int i = begin / arrMap.length;
		int j = begin % arrMap[0].length;
		int endRow = end / arrMap.length;
		int endCol = end % arrMap[0].length;
		
		
		arrMap[i][j].calculateH(endRow, endCol);
		arrMap[i][j].calculateF();

		ArrayList<Node> path = computePath(endRow, endCol, arrMap[i][j]);

		pathList.add(path);

		if (path.size() >= 2) {
			int counter = path.size() - 1;
			boolean resetPath = false;
			while (counter >= 0 && counter <= path.size() - 1) {

				try {
					if (path.get(counter).isOccupied == false && resetPath == false) {
						path.get(counter).isDiscovered = true;
						// path.get(counter).isVisited = true;

						Node _nd = path.get(counter);
						if (_nd.north != null) {
							if (_nd.north.isOccupied == true) {
								_nd.north.isDiscovered = true;
							}
						}
						if (_nd.east != null) {
							if (_nd.east.isOccupied == true) {
								_nd.east.isDiscovered = true;
							}
						}
						if (_nd.south != null) {
							if (_nd.south.isOccupied == true) {
								_nd.south.isDiscovered = true;
							}
						}
						if (_nd.west != null) {
							if (_nd.west.isOccupied == true) {
								_nd.west.isDiscovered = true;
							}
						}
						
						counter--;
					} else {

						if (path.size() > counter + 1) {

							resetter();
							counter++;
							ArrayList<Node> temp = computePath(endRow, endCol, path.get(counter));

							if (temp.size() == 0) {

								resetPath = true;
							} else {

								int diff = temp.size() - path.size();
								if (diff > 0) {
									counter += diff;
								}
								pathList.add(temp);
								path = temp;
								resetPath = false;
							}

						} else {
							System.out.println("Forward Solution does not exist for " + fileName);
							return result;
						}

					}
				} catch (Exception e) {
					System.out.println("Error" + e.getMessage());
				}
			}

			ArrayList<Node> finalPath = pathList.get(pathList.size() - 1);
			Node ptr = finalPath.get(1);
			if (ptr.isEnd == true) {
				while (ptr != null) {
					ptr.isVisited = true;
					ptr = ptr.parent;
					if (ptr.isStart == true) {
						result = true;
					}
				}
			}
		}
		return result;
	}
	/**
	 * Begins Backward astar
	 * @return
	 */
	public boolean beginBackward() {
		boolean result = true;
		int i = begin / arrMap.length;
		int j = begin % arrMap[0].length;

		int endRow = end / arrMap.length;
		int endCol = end % arrMap[0].length;

		arrMap[endRow][endCol].calculateH(i, j);
		arrMap[endRow][endCol].calculateF();

		ArrayList<Node> path = computePathBackwards(i, j, arrMap[endRow][endCol], arrMap[i][j]);
		pathList.add(path);

		if (path.size() >= 2) {
			int counter = 0;
			boolean resetPath = false;
			while (counter >= 0 && counter <= path.size() - 1) {

				try {
					if (path.get(counter).isOccupied == false && resetPath == false) {
						path.get(counter).isDiscovered = true;
						// path.get(counter).isVisited = true;

						Node _nd = path.get(counter);
						if (_nd.north != null) {
							if (_nd.north.isOccupied == true) {
								_nd.north.isDiscovered = true;
							}
						}
						if (_nd.east != null) {
							if (_nd.east.isOccupied == true) {
								_nd.east.isDiscovered = true;
							}
						}
						if (_nd.south != null) {
							if (_nd.south.isOccupied == true) {
								_nd.south.isDiscovered = true;
							}
						}
						if (_nd.west != null) {
							if (_nd.west.isOccupied == true) {
								_nd.west.isDiscovered = true;
							}
						}

						// visited.add(path.get(counter));
						counter++;
					} else {
						// openList.clear();
						if (path.size() > counter + 1) {

							resetter();
							counter--;
							if (counter < 0) {
								System.out.println("Backwards Solution does not exist for " + fileName);
								return result;
							}
							ArrayList<Node> temp = computePathBackwards(i, j, path.get(path.size() - 1),
									path.get(counter));

							if (temp.size() == 0) {
								// counter = counter+1;
								resetPath = true;
							} else {

								int _tempCount = counter - 2;
								while (_tempCount >= 0) {
									temp.add(0, path.get(_tempCount));
									// temp.add(path.get(_tempCount));
									_tempCount--;
								}
								pathList.add(temp);
								path = temp;
								resetPath = false;
							}

						} else {
							System.out.println("Backwards Solution does not exist for " + fileName);
							return result;
						}

					}
				} catch (Exception e) {
					System.out.println("Error" + e.getMessage());
				}
			}
			// System.out.println("Found it" + visited.size());
			ArrayList<Node> finalPath = pathList.get(pathList.size() - 1);
			Node ptr = finalPath.get(1);
			if (ptr.isEnd == true) {
				while (ptr != null) {
					ptr.isVisited = true;
					ptr = ptr.parent;
					if (ptr.isStart == true) {
						result = true;
					}
				}
			}
		}
		return result;
	}

	/**
	 * resets the Map
	 */
	private void resetter() {
		for (int i = 0; i < arrMap.length; i++) {
			for (int j = 0; j < arrMap[i].length; j++) {
				if (arrMap[i][j].isDiscovered == false) {
					arrMap[i][j].parent = null;
				}
			}
		}
	}

	/**
	 * 
	 * @param compare Node
	 */
	private void resetter(Node compare) {
		for (int i = 0; i < arrMap.length; i++) {
			for (int j = 0; j < arrMap[i].length; j++) {
				Node nd = arrMap[i][j];
				if (nd.isDiscovered == false && nd.equals(compare) == false) {
					arrMap[i][j].parent = null;
				} else {
				}
			}
		}
	}

	public ArrayList<Node> computePath(int goalRow, int goalCol, Node beginning) {
		boolean start = true;
		ArrayList<Node> path = new ArrayList<Node>();
		PriorityQueue<Node> openList = new PriorityQueue<Node>(intcomp);

		openList.add(beginning);

		while (openList.size() != 0) {
			Node nd = openList.remove();
			nodeCount++;
			if (nd.isEnd == true) {
				Node ptr = nd;
				while (ptr != null) {
					path.add(ptr);
					Node s = ptr.parent;
					ptr = s;
				}
				break;
			}
			if (openList.size() > 0) {
				// System.out.println(openList.size());
				if (openList.peek().fvalue == nd.fvalue) {

				} else {

				}
			}
			if (start == true) {
				start = false;
				nd.isDiscovered = true;
				if (nd.north != null) {
					if (nd.north.isOccupied == false && nd.north.isDiscovered == false) {
						nd.north.calculateH(goalRow, goalCol);
						nd.north.gvalue = nd.gvalue + 1;
						nd.north.calculateF();
						nd.north.parent = nd;
						openList.add(nd.north);
					} else {
						nd.north.isDiscovered = true;
					}
				}
				if (nd.south != null) {
					if (nd.south.isOccupied == false && nd.south.isDiscovered == false) {

						nd.south.calculateH(goalRow, goalCol);
						nd.south.gvalue = nd.gvalue + 1;
						nd.south.calculateF();
						nd.south.parent = nd;
						openList.add(nd.south);
					} else {
						nd.south.isDiscovered = true;
					}
				}
				if (nd.east != null) {
					if (nd.east.isOccupied == false && nd.east.isDiscovered == false) {
						nd.east.calculateH(goalRow, goalCol);
						nd.east.gvalue = nd.gvalue + 1;
						nd.east.calculateF();
						nd.east.parent = nd;
						openList.add(nd.east);
					} else {
						nd.east.isDiscovered = true;
					}
				}
				if (nd.west != null) {
					if (nd.west.isOccupied == false && nd.west.isDiscovered == false) {
						nd.west.calculateH(goalRow, goalCol);
						nd.west.gvalue = nd.gvalue + 1;
						nd.west.calculateF();
						nd.west.parent = nd;
						openList.add(nd.west);
					} else {
						nd.west.isDiscovered = true;
					}
				}
			} else {
				// after first iteration after discovery
				if (nd.north != null) {
					Node _north = nd.north;
					if (_north.isDiscovered == false && nd.parent.equals(_north) == false) {
						if (_north.parent == null) {
							_north.calculateH(goalRow, goalCol);
							_north.gvalue = nd.gvalue + 1;
							_north.calculateF();
							_north.parent = nd;

							openList.add(_north);
						}

					}
				}
				if (nd.east != null) {
					Node _east = nd.east;
					if (_east.isDiscovered == false && nd.parent.equals(_east) == false) {
						if (_east.parent == null) {
							_east.calculateH(goalRow, goalCol);
							_east.gvalue = nd.gvalue + 1;
							_east.calculateF();
							_east.parent = nd;
							openList.add(_east);
						}

					}
				}
				if (nd.south != null) {
					Node _south = nd.south;
					if (_south.isDiscovered == false && nd.parent.equals(_south) == false) {

						if (_south.parent == null) {
							_south.calculateH(goalRow, goalCol);
							_south.gvalue = nd.gvalue + 1;
							_south.calculateF();
							_south.parent = nd;
							openList.add(_south);
						}

					}
				}
				if (nd.west != null) {
					Node _west = nd.west;
					if (_west.isDiscovered == false && nd.parent.equals(_west) == false) {

						if (_west.parent == null) {
							_west.calculateH(goalRow, goalCol);
							_west.gvalue = nd.gvalue + 1;
							_west.calculateF();
							_west.parent = nd;
							openList.add(_west);
						}

					}
				}
			}
		}
		return path;
	}

	public ArrayList<Node> computePathBackwards(int goalRow, int goalCol, Node beginning, Node goalNode) {
		boolean start = true;
		ArrayList<Node> path = new ArrayList<Node>();
		PriorityQueue<Node> openList = new PriorityQueue<Node>(intcomp);

		openList.add(beginning);

		while (openList.size() != 0) {
			Node nd = openList.remove();
			nodeCount++;
			if (nd.equals(goalNode)) {
				Node ptr = nd;
				while (ptr != null) {
					path.add(ptr);
					Node s = ptr.parent;
					ptr = s;
				}
				break;
			}
			if (openList.size() > 0) {
				// System.out.println(openList.size());
				if (openList.peek().fvalue == nd.fvalue) {

				} else {

				}
			}
			if (start == true) {
				start = false;
				nd.isDiscovered = true;
				if (nd.north != null) {
					if (nd.north.isOccupied == false && nd.north.isDiscovered == false) {
						nd.north.calculateH(goalRow, goalCol);
						nd.north.gvalue = nd.gvalue + 1;
						nd.north.calculateF();
						nd.north.parent = nd;
						openList.add(nd.north);
					} else {
						nd.north.isDiscovered = true;
					}
				}
				if (nd.south != null) {
					if (nd.south.isOccupied == false && nd.south.isDiscovered == false) {

						nd.south.calculateH(goalRow, goalCol);
						nd.south.gvalue = nd.gvalue + 1;
						nd.south.calculateF();
						nd.south.parent = nd;
						openList.add(nd.south);
					} else {
						nd.south.isDiscovered = true;
					}
				}
				if (nd.east != null) {
					if (nd.east.isOccupied == false && nd.east.isDiscovered == false) {
						nd.east.calculateH(goalRow, goalCol);
						nd.east.gvalue = nd.gvalue + 1;
						nd.east.calculateF();
						nd.east.parent = nd;
						openList.add(nd.east);
					} else {
						nd.east.isDiscovered = true;
					}
				}
				if (nd.west != null) {
					if (nd.west.isOccupied == false && nd.west.isDiscovered == false) {
						nd.west.calculateH(goalRow, goalCol);
						nd.west.gvalue = nd.gvalue + 1;
						nd.west.calculateF();
						nd.west.parent = nd;
						openList.add(nd.west);
					} else {
						nd.west.isDiscovered = true;
					}
				}
			} else {
				// after first iteration after discovery
				if (nd.north != null) {
					Node _north = nd.north;
					if (_north.isDiscovered == false && nd.parent.equals(_north) == false) {
						if (_north.parent == null) {
							_north.calculateH(goalRow, goalCol);
							_north.gvalue = nd.gvalue + 1;
							_north.calculateF();
							_north.parent = nd;

							openList.add(_north);
						}
					} else if (_north.equals(goalNode)) {
						_north.calculateH(goalRow, goalCol);
						_north.gvalue = nd.gvalue + 1;
						_north.calculateF();
						_north.parent = nd;

						openList.add(_north);
					}
				}
				if (nd.east != null) {
					Node _east = nd.east;
					if (_east.isDiscovered == false && nd.parent.equals(_east) == false) {
						if (_east.parent == null) {
							_east.calculateH(goalRow, goalCol);
							_east.gvalue = nd.gvalue + 1;
							_east.calculateF();
							_east.parent = nd;
							openList.add(_east);
						}
					} else if (_east.equals(goalNode)) {
						_east.calculateH(goalRow, goalCol);
						_east.gvalue = nd.gvalue + 1;
						_east.calculateF();
						_east.parent = nd;
						openList.add(_east);
					}
				}
				if (nd.south != null) {
					Node _south = nd.south;
					if (_south.isDiscovered == false && nd.parent.equals(_south) == false) {

						if (_south.parent == null) {
							_south.calculateH(goalRow, goalCol);
							_south.gvalue = nd.gvalue + 1;
							_south.calculateF();
							_south.parent = nd;
							openList.add(_south);
						}
					} else if (_south.equals(goalNode)) {
						_south.calculateH(goalRow, goalCol);
						_south.gvalue = nd.gvalue + 1;
						_south.calculateF();
						_south.parent = nd;
						openList.add(_south);
					}
				}
				if (nd.west != null) {
					Node _west = nd.west;
					if (_west.isDiscovered == false && nd.parent.equals(_west) == false) {

						if (_west.parent == null) {
							_west.calculateH(goalRow, goalCol);
							_west.gvalue = nd.gvalue + 1;
							_west.calculateF();
							_west.parent = nd;
							openList.add(_west);
						}
					} else if (_west.equals(goalNode)) {
						_west.calculateH(goalRow, goalCol);
						_west.gvalue = nd.gvalue + 1;
						_west.calculateF();
						_west.parent = nd;
						openList.add(_west);
					}
				}
			}
		}
		return path;
	}

	/**
	 * Starts adaptive astar
	 * @return Boolean if solution exists
	 */
	public boolean beginAdaptive() {
		boolean result = false;
		int i = begin / arrMap.length;
		int j = begin % arrMap[0].length;

		int endRow = end / arrMap.length;
		int endCol = end % arrMap[0].length;

		
		arrMap[i][j].calculateH(endRow, endCol);
		arrMap[i][j].calculateF();

		ArrayList<Node> path = computeAdaptive(endRow, endCol, arrMap[i][j]);

		pathList.add(path);

		if (path.size() >= 2) {
			boolean resetPath = false;
			while (path.size() > 0) {
				int _counter = path.size() - 1;
				try {
					if (path.get(_counter).isOccupied == false && resetPath == false) {
						path.get(_counter).isDiscovered = true;


						Node _nd = path.get(_counter);
						if (_nd.north != null) {
							if (_nd.north.isOccupied == true) {
								_nd.north.isDiscovered = true;
							}
						}
						if (_nd.east != null) {
							if (_nd.east.isOccupied == true) {
								_nd.east.isDiscovered = true;
							}
						}
						if (_nd.south != null) {
							if (_nd.south.isOccupied == true) {
								_nd.south.isDiscovered = true;
							}
						}
						if (_nd.west != null) {
							if (_nd.west.isOccupied == true) {
								_nd.west.isDiscovered = true;
							}
						}
						if (_nd.isEnd == false) {
							resetter(path.get(_counter - 1));
							ArrayList<Node> temp = computeAdaptive(endRow, endCol, path.get(_counter));
							path = temp;
							pathList.add(path);
						} else {
							break;
						}

					}

				} catch (Exception e) {
					System.out.println("Error" + e.getMessage());
					result = false;
				}
			}
			if (pathList.size() > 0) {
				ArrayList<Node> finalPath = new ArrayList<Node>();
				int c = 1;
				while (finalPath.size() == 0 && c < pathList.size()) {
					finalPath = pathList.get(pathList.size() - c);
					c++;
				}
				if (finalPath.size() > 0) {
					Node ptr = finalPath.get(finalPath.size() - 1);
					if (ptr.isEnd == true) {
						while (ptr != null) {
							ptr.isVisited = true;
							if (ptr.isStart == true) {
								result = true;
							}
							ptr = ptr.parent;

						}

					} else {
						result = false;
					}
				} else {
					System.out.println("Adaptive Solution does not exist for " + fileName);
					result = false;
				}
			} else {
				System.out.println("Adaptive Solution does not exist for " + fileName);
				result = false;
			}
		}
		return result;
	}

	/**
	 * computes the best path to goal from current state
	 * 
	 * 
	 * @param goalRow
	 * @param goalCol
	 * @param beginning
	 * @return ArrayList<Node>
	 */
	public ArrayList<Node> computeAdaptive(int goalRow, int goalCol, Node beginning) {
		boolean start = true;
		ArrayList<Node> path = new ArrayList<Node>();
		PriorityQueue<Node> openList = new PriorityQueue<Node>(intcomp);

		openList.add(beginning);

		while (openList.size() != 0) {
			Node nd = openList.remove();
			nodeCount++;
			if (nd.isEnd == true) {
				Node ptr = nd;
				while (ptr != null && ptr.equals(beginning) == false) {
					path.add(ptr);
					Node s = ptr.parent;
					ptr = s;
				}
				break;
			}
//			if (openList.size() > 0) {
//				// System.out.println(openList.size());
//				if (openList.peek().fvalue == nd.fvalue) {
//
//				} else {
//
//				}
//			}
			if (start == true) {
				start = false;
				nd.isDiscovered = true;
				if (nd.north != null) {
					if (nd.north.isOccupied == false && nd.north.isDiscovered == false) {
						nd.north.calculateH(goalRow, goalCol);
						nd.north.gvalue = nd.gvalue + 1;
						nd.north.calculateF();
						nd.north.parent = nd;
						openList.add(nd.north);
					} else {
						nd.north.isDiscovered = true;
					}
				}
				if (nd.south != null) {
					if (nd.south.isOccupied == false && nd.south.isDiscovered == false) {

						nd.south.calculateH(goalRow, goalCol);
						nd.south.gvalue = nd.gvalue + 1;
						nd.south.calculateF();
						nd.south.parent = nd;
						openList.add(nd.south);
					} else {
						nd.south.isDiscovered = true;
					}
				}
				if (nd.east != null) {
					if (nd.east.isOccupied == false && nd.east.isDiscovered == false) {
						nd.east.calculateH(goalRow, goalCol);
						nd.east.gvalue = nd.gvalue + 1;
						nd.east.calculateF();
						nd.east.parent = nd;
						openList.add(nd.east);
					} else {
						nd.east.isDiscovered = true;
					}
				}
				if (nd.west != null) {
					if (nd.west.isOccupied == false && nd.west.isDiscovered == false) {
						nd.west.calculateH(goalRow, goalCol);
						nd.west.gvalue = nd.gvalue + 1;
						nd.west.calculateF();
						nd.west.parent = nd;
						openList.add(nd.west);
					} else {
						nd.west.isDiscovered = true;
					}
				}
			} else {
				// after first iteration after discovery
				if (nd.north != null) {
					Node _north = nd.north;
					if (_north.isDiscovered == false && nd.parent.equals(_north) == false) {
						if (_north.parent == null) {
							_north.calculateH(goalRow, goalCol);
							_north.gvalue = nd.gvalue + 1;
							_north.calculateF();
							_north.parent = nd;

							openList.add(_north);
						}

					}
				}
				if (nd.east != null) {
					Node _east = nd.east;
					if (_east.isDiscovered == false && nd.parent.equals(_east) == false) {
						if (_east.parent == null) {
							_east.calculateH(goalRow, goalCol);
							_east.gvalue = nd.gvalue + 1;
							_east.calculateF();
							_east.parent = nd;
							openList.add(_east);
						}

					}
				}
				if (nd.south != null) {
					Node _south = nd.south;
					if (_south.isDiscovered == false && nd.parent.equals(_south) == false) {

						if (_south.parent == null) {
							_south.calculateH(goalRow, goalCol);
							_south.gvalue = nd.gvalue + 1;
							_south.calculateF();
							_south.parent = nd;
							openList.add(_south);
						}

					}
				}
				if (nd.west != null) {
					Node _west = nd.west;
					if (_west.isDiscovered == false && nd.parent.equals(_west) == false) {

						if (_west.parent == null) {
							_west.calculateH(goalRow, goalCol);
							_west.gvalue = nd.gvalue + 1;
							_west.calculateF();
							_west.parent = nd;
							openList.add(_west);
						}

					}
				}
			}
		}
		return path;
	}

}
