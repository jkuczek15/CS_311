import java.util.ArrayList;

public class DynamicProgramming {
	// provide the methods minCostVC and StringAlignment
	
	public static ArrayList<Integer> minCostVC(int[][] M){
		// function to return a min-cost vertical cut given a 2D matrix 
		int rows = M.length;
		int columns = M[0].length;
		
		// initialize minCostVCPath to store our minimum path
		ArrayList<Integer> minCostVCPath = new ArrayList<Integer>();
		
		// store the minDirections for cuts dynamically
		// let 0 = null, 1 = left, 2 = middle, 3 = right
		int[][][] minDirections = new int[rows][columns][1];
		
		int minCost = Integer.MAX_VALUE;
		for(int k = 0; k < columns; k++){
			// for loop selecting a starting column
			int cost = 0;
			
			// use mutable variable j for inside the row loop
			int j = k;
			
			// add on our cost for the first row
			cost = cost + M[0][j];
			
			// initialize a vertical cut ArrayList to store this cut and add the first element
			ArrayList<Integer> verticalCut = new ArrayList<Integer>();
			verticalCut.add(0);
			verticalCut.add(j);
			
			// we are traversing down through matrix
			for(int i = 0; i < rows-1; i++){
				// for loop over rows traversing to the bottom
				if(minDirections[i][j][0] == 0) {
					// we don't have a pre-computed minimum path for this point, compute the minimum direction by comparing
					// the left, middle, and right points directly in the row directly below
					int l, r, m;
					
					// grab the left, middle, and right cuts
					l = (j-1 >= 0) ? M[i+1][j-1] : -1;
					m = M[i+1][j];
					r = (j+1 < columns) ? M[i+1][j+1] : -1;
					
					// compute the minimum direction from the point
					minDirections[i][j][0] = computeMinDirection(l, m, r);
				}// end if minimum direction has not been computed
				
				if(minDirections[i][j][0] == 1){
					// left cut
					j = j-1;
				}else if(minDirections[i][j][0] == 3){
					// right cut
					j = j+1;
				}// end if left or right cut
				
				// increment our cost for current vertical cut
				cost += M[i+1][j];
				
				// add the minimum point to our path
				verticalCut.add(i+1);
				verticalCut.add(j);
			}// end row for loop traversing down
				
			if(cost < minCost){
				minCost = cost;
				minCostVCPath = verticalCut;
			}// end if we found a new minimum cost vertical cut
			
		}// end loop selecting a starting column 
		
		return minCostVCPath;
	}// end function minCostVC(...)
	
	public static String stringAlignment(String x, String y){
		//compute the minCostAlignment String for Strings x and y
		int i, j, k, m, n, cost, parent;
		Character tmp;
		// set the insert penalty
		int insertPenalty = 4;
		
		n = x.length();
		m = y.length();
		
		if(m > n){
			return "Error: String y must have a smaller length than string x";
		}// end if we have an error
		
		// initialize our different cost options and the matrix
		int opt[] = new int[3];
		Cell matrix[][] = new Cell[n+1][n+1];
		
		// initialize the first cell
		matrix[0][0] = new Cell(0, -1, ' ');
		for(i = 1; i < n+1; i++){
			// for loop initializing the matrix
			
			if(i == n){
				tmp = x.charAt(n-1);
			}else{
				tmp = x.charAt(i-1);
			}// end if
			
			matrix[0][i] = new Cell(i, 1, tmp);
			
			if(i == m){
				tmp = y.charAt(m-1);
			}else if(i > m){
				tmp = null;
			}else{
				tmp = y.charAt(i-1);
			}// end if
			
			matrix[i][0] = new Cell(i, 2, tmp);
		}// end for loop initializing matrix
		
		for(i = 1; i < n; i++){
			// for loop over String x
			char c = x.charAt(i);
			
			for(j = 1; j < m; j++){
				// for loop over String y
				char v = y.charAt(j);
				// determine our three different cost options
				// cost for not making a change
				opt[0] = matrix[i-1][j-1].cost + match(c, v);
				// cost for inserting to right of character
				opt[1] = matrix[i][j-1].cost + insertPenalty;
				// cost for inserting to left of character
				opt[2] = matrix[i-1][j].cost + insertPenalty;
				
				cost = opt[0];
				parent = 0;
								
				for(k = 1; k <= 2; k++){
					// for loop over options
					if(opt[k] < cost){
						cost = opt[k];
						parent = k;
					}// end if we found a new min cost
					
				}// end for loop over our options
				
				matrix[i][j] = new Cell(cost, parent, v);
			}// end for loop over String y
		
		}// end for loop over String x
		
		return reconstructPath(x, y, x.length()-1, y.length()-1, new StringBuilder(y), matrix);
	}// end function StringAlignment(...)
	
	private static String reconstructPath(String x, String y, int i, int j, StringBuilder build, Cell[][] matrix){
		// get the minimum path from our computed matrix for use with StringAlignment method
		
		if(matrix[i][j].parent == -1){
			// base case for recursive call
			return build.toString();
		}else if(matrix[i][j].parent == 0){
			// match
			reconstructPath(x, y, i-1, j-1, build, matrix);
		}else if(matrix[i][j].parent == 1){
			// insert right?			
			// make recursive call to construct min path from matrix
			reconstructPath(x, y, i, j-1, build, matrix);
			build.insert(i, '$');
		}else if(matrix[i][j].parent == 2){
			// insert left?			
			// make recursive call to construct min path from matrix
			reconstructPath(x, y, i-1, j, build, matrix);
			build.insert(i-1, "$");
		}// end if parent == 2
		
		return build.toString();
	}// end function reconstructPath(...)
	
	private static int computeMinDirection(int l, int m, int r) {
		int min = 0;
		
		if(l == -1){
			min = Math.min(r, m);
			return (min == r) ? 3 : 2;
		}else if(r == -1){
			min = Math.min(l, m);
			return (min == l) ? 1 : 2;
		}else{
			min = Math.min(Math.min(l, m), r);
			
			if(min == l)
				return 1;
			else if(min == m)
				return 2;
			else
				return 3;
		}// end if we need to compute the minimum of the three values
			
	}// end function computeMinDirection(...)
	
	private static int match(char c, char d){
		if(c == d) 
			return 0;
		// penalty is 2 if two characters are not equal
		return 2;
	}// end function match(...)
		
	private static class Cell {
		// Cell for use with StringAlignment
		int cost;
		int parent;
		Character character;
		
		public Cell(int cost, int parent, Character c){
			this.cost = cost;
			this.parent = parent;
			this.character = c;
		}// end cell class
		
		@Override
		public String toString(){
			return "(C: " + cost + ", P: " + parent + " Ch + " + character + ")"; 
		}// end function toString
		
	}// end private class Cell
		
}// end class DynamicProgramming