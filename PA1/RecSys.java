import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class RecSys {
	int numUsers;
	int numMovies;
	int[][] ratings;
	NearestPoints n;
	ArrayList<Float> points;
	
	public RecSys(String mrMatrix) throws FileNotFoundException, IOException {
		File f = new File(mrMatrix);
		Scanner in = new Scanner(f);
		points  = new ArrayList<Float>();

		numUsers = in.nextInt();
		numMovies = in.nextInt();
		ratings = new int[numUsers][numMovies];
		
		for(int i = 0; i < numUsers; i++){
			points.add(in.nextFloat());
			
			for(int j = 0; j < numMovies; j++){
				ratings[i][j] = in.nextInt();
			}// end inner for loop
			
		}// end outer for loop
		
		n = new NearestPoints(points);
		in.close();
	}// end constructor
	
	public float ratingOf(int u, int m){
		ArrayList<Integer> movieRatings = new ArrayList<Integer>();
		
		if(ratings[u-1][m-1] != 0){
			return ratings[u-1][m-1];
		}// end if we have a valid rating, dont predict
		
		// grab all of our nearest points
		ArrayList<Float> nearestPoints = n.npHashNearestPoints(points.get(u-1));
		Iterator<Float> iter = nearestPoints.iterator();
		
		// loop over all our nearest points
		while(iter.hasNext()){
			int key = points.indexOf(iter.next());
			movieRatings.add(ratings[key][m-1]);
		}// end while loop over nearestPoints
		
		// compute the average
		float total = 0;
		for(int i = 0; i < movieRatings.size(); i++){
			total += movieRatings.get(i);
		}// end for loop
		
		return total / movieRatings.size();
	}// end function ratingOf()
	
}// end class RecSys
