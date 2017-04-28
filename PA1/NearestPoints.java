import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

public class NearestPoints {
	ArrayList<Float> points;
	HashTable table;
	
	public NearestPoints(String dataFile) throws FileNotFoundException, IOException{
		File f = new File(dataFile);
		InputStream in = new FileInputStream(f);
		points = new ArrayList<Float>();
		
		int size = in.available();
		String s = "";
        for(int i = 0; i < size; i++) {
        	char next = (char)in.read();
        	
        	if(next == '\n'){
        		float value = Float.parseFloat(s);
        		points.add(new Float(value));
        		s = "";
        	}else{
        		s += next;
        	}// end if next is new line
        
        }// end for loop
        
        buildDataStructure();
        in.close();
	}// end constructor
	
	public NearestPoints(ArrayList<Float> pointSet){
		points = pointSet;
		buildDataStructure();
	}// end constructor
		
	public ArrayList<Float> naiveNearestPoints(float p){
		Iterator<Float> iter = points.iterator();
		ArrayList<Float> list = new ArrayList<Float>();
		
		while(iter.hasNext()){
	    	// loop finding the nearest points
	    	Float q = iter.next();
	    	
	    	if(p != q && Math.abs(p - q) <= 1){
	    		list.add(q);
	    	}// end if absolute (p-q) <= 1

	    }// end while loop
		return list;
	}// end function naiveNearestPoints()
	
	public void buildDataStructure(){
		int size = (int) Math.round((1.5 * points.size()));
		table = new HashTable(size);
		
		for(int i = 0; i < points.size(); i++){
			float point = points.get(i);
			int floor = (int) Math.floor(point); 
			Tuple t = new Tuple(floor, point);
			table.add(t);
		}// end for loop storing points in data structure
		
	}// end function buildDataStructure()
	
	public ArrayList<Float> npHashNearestPoints(float p){
		ArrayList<Float> list = new ArrayList<Float>();
		int[] hashes = new int[3];
		
		// grab all possible hash keys
		hashes[0] = table.hashFunction().hash((int) Math.floor(p-1));
		hashes[1] = table.hashFunction().hash((int) Math.floor(p));
		hashes[2] = table.hashFunction().hash((int) Math.floor(p+1));
		
		// grab all possible neighbor buckets
		Bucket[] buckets = new Bucket[3];
		buckets[0] = table.getBucket(hashes[0]);
		buckets[1] = table.getBucket(hashes[1]);
		buckets[2] = table.getBucket(hashes[2]);
		
		// loop over all neighbor buckets, checking nearest points
		for(int i = 0; i < buckets.length; i++){
			// loop over our 3 buckets
			if(buckets[i] == null) continue;
			
			for(int j = 0; j < buckets[i].size(); j++){
				// loop over each element in the bucket
				Tuple t = buckets[i].get(j);
				Float q = (float) t.getValue();
				
				if(p != q && Math.abs(p - q) <= 1){
		    		list.add(q);
		    	}// end if absolute (p-q) <= 1
				
			}// end inner for loop
			
		}// end outer for loop
		
		return list;
	}// end function npHashNearestPoints
	
	public void allNearestPointsNaive() throws FileNotFoundException {
		Iterator<Float> iter = points.iterator();
		ArrayList<Float> list = new ArrayList<Float>();
		String output = "";
		
		while(iter.hasNext()){
	    	// loop finding the nearest points
	    	Float p = iter.next();
	    	output += p;
	    	list = naiveNearestPoints(p);
	    	output += listToString(list);
	    	if(iter.hasNext())
	    		output += "\n";
	    }// end outer while
		
		try(PrintWriter out = new PrintWriter("NaiveSolution.txt") ){
		   out.print(output);
		}// end try
		
	}// end function allNearestPointsNaive()
	
	public void allNearestPointsHash() throws FileNotFoundException {
		Iterator<Float> iter = points.iterator();
		ArrayList<Float> list = new ArrayList<Float>();
		String output = "";
		
		while(iter.hasNext()){
	    	// loop finding the nearest points
	    	Float p = iter.next();
	    	output += p;
	    	list = npHashNearestPoints(p);
	    	output += listToString(list);
	    	if(iter.hasNext())
	    		output += "\n";
	    }// end outer while
		
		try(PrintWriter out = new PrintWriter("HashSolution.txt") ){
		   out.print(output);
		}// end try
		
	}// end function allNearestPointsHash()
	
	public String listToString(ArrayList<Float> list){
		Iterator<Float> iter = list.iterator();
    	String s = "";
    	
    	while(iter.hasNext()){
    		if(iter.hasNext())
    			s += " ";
    		Float f = iter.next();
    		s += f;
    	}// end inner while loop
    	
    	return s;
	}// end function listToString
	
	public HashTable hashTable(){
		return table;
	}// end function for getting the hashTable
	
	@Override
	public String toString(){
		return table.toString();
	}// end function toString
	
}// end class nearestPoints
