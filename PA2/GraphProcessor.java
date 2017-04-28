import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class GraphProcessor {
	// process a graph from the text file graphData, find SCC's
	private HashMap<String, Vertex> vertices;				// vertices in our graph
	private HashMap<Vertex, Boolean> marked;				// store whether the vertices are marked
	private HashMap<String, ArrayList<String>> outgoing;	// outgoing lists for each vertex
	private HashMap<String, ArrayList<String>> outgoingR;	// outgoing lists for each vertex in  direction
	private HashMap<String, Integer> finishTimes;			// finishTimes for DFS traversal
	private HashMap<String, Integer> distances;				// distances for BFS shortest path
	private HashMap<Integer, ArrayList<String>> sccs;		// our strongly connected components
	private Integer counter;								// counter for computing finish times
	private int maxSCCsize = 0;
	
	public GraphProcessor(String graphData) throws IOException {
		counter = 0;
		// read graph data file
		vertices = new HashMap<String, Vertex>();
		initMarked();
		
		// create hashMap containing our outgoing edges
		outgoing = new HashMap<String, ArrayList<String>>();
		outgoingR = new HashMap<String, ArrayList<String>>();
		
		// read from graph data file
		readFile(graphData);    
		
		// form our SCC data structure
		finishTimes = new HashMap<String, Integer>();
		sccs = new HashMap<Integer, ArrayList<String>>();
		distances = new HashMap<String, Integer>();
		computeSCC();
	}// end constructor GraphProcessor
	
	public int outDegree(String v) {
		// returns the outdegree of v	
		ArrayList<String> list = outgoing.get(v);
		
		if(list == null){
			return 0;
		}// end if there is no outdegree for this vertex
		
		return list.size();
	}// end function outDegree(...)
	
	public boolean sameComponent(String u, String v) {
		// returns true if u and v are in the same SCC, otherwise false
		Vertex uu = vertices.get(u);
		Vertex vv = vertices.get(v);
		
		// return false if either vertex is not in our graph
		if(uu == null || vv == null) return false;
		
		// simply compare the hashCodes
		return uu.sccKey == vv.sccKey;
	}// end function sameCompoenent(...)
	
	public ArrayList<String> componentVertices(String v) {
		// return all the vertices belonging to the SCC containing v
		Vertex u = vertices.get(v);
		if(u == null) return null;
		
		ArrayList<String> returnList = sccs.get(u.sccKey);
		
		if(returnList == null){
			returnList = new ArrayList<String>();
			returnList.add(v);
		}// end if returnList is null
		
		return returnList;
	}// end function componenetVertices(...)
	
	public int largestComponent() {
		// returns the size of the largest component
		return maxSCCsize;
	}// end function largestComponen()t
	
	public int numComponents() {
		// returns the number of strongly connected components
		return sccs.size();
	}// end function numComponents()
	
	public ArrayList<String> bfsPath(String u, String v) {
		// returns arrayList of strings representing BFS path from u to v
		ArrayList<String> path = new ArrayList<String>();
		// some lists we need here
		Queue<String> queue = new LinkedList<String>();
		ArrayList<String> visited = new ArrayList<String>();
		HashMap<String, String> parents = new HashMap<String, String>();		
		
		// begin by adding our seedUrl to the queue and visited
		queue.add(u);
		visited.add(u);
		Vertex k = null;
		
		// loop through our HashMap of vertices using an iterator
		Iterator<Map.Entry<String, Vertex>> it = vertices.entrySet().iterator();
	  
		while (it.hasNext()) {
	        Map.Entry<String, Vertex> pair = (Map.Entry<String, Vertex>) it.next();
	        k = (Vertex) pair.getValue();
	        distances.put(k.toString(), Integer.MAX_VALUE);
	        parents.put(k.toString(), null);
	    }// end while
			
		distances.put(u, 0);
		
		while(!queue.isEmpty()){
			String next = queue.remove();
			
			if(next.equals(v)){
				// we have found a path from v to u
				break;
			}else{
				// we are still searching for a path
				ArrayList<String> outEdges = outgoing.get(next);
				
				if(outEdges != null){
					// we have out edges
					for(int i = 0; i < outEdges.size(); i++){
						String e = outEdges.get(i);
						
						if(distances.get(e) > distances.get(next)+1) {
							distances.put(e, distances.get(next)+1);
							parents.put(e, next);
						}// end if we found a new shortest path, update distances
						
						if(!visited.contains(e)) {
							queue.add(e);
							visited.add(e);
						}// end if visited contains the node
						
					}// end for loop over outgoing edges
					
				}// end if we have outgoing edges
				
			}// end if we have found a path from v to u
			
		}// end while loop over our whole queue
	
		path.add(v);
		String temp = parents.get(v);
		
		if(temp == null){
			path.add(v);
			return path;
		}// end if there is no parent, means we didnt find a shortest path to our node
		
		while(!temp.equals(u)){
			path.add(temp);
			temp = parents.get(temp);
		}// create our shortest path list
		
		// add our starting vertex
		path.add(u);
		// reverse the shortest path
		Collections.reverse(path);
		return path;
	}// end function bfsPath(...)
	
	private void readFile(String fileName) throws IOException {
		String start = "";
		String end = "";
		String line = "";
		ArrayList<String> currentList = null;
		ArrayList<String> currentListR = null;
		
		try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            bufferedReader.readLine();		// read the first line to get to the edges
            
            while((line = bufferedReader.readLine()) != null) {
            	String[] input = line.split("\\s");
            	start = input[0];
            	end = input[1];
            	
            	Vertex v = new Vertex(start);
            	if(vertices.get(start) == null){
            		vertices.put(start, v);
            	}// end if vertices does not contain start vertex
            	
            	Vertex u = new Vertex(end);
            	if(vertices.get(end) == null){
            		vertices.put(end, u);
            	}// end if vertices does not contain end vertex
            	            	
            	currentList = outgoing.get(start);
				if(currentList == null){
					currentList = new ArrayList<String>();
				}// end if currentList hasnt been created
				
				if(!currentList.contains(end)){
					currentList.add(end);
				}// end if the list doesnt contain this vertex
				
				outgoing.put(start, currentList);
				
				currentListR = outgoingR.get(end);
				if(currentListR == null){
					currentListR = new ArrayList<String>();
				}// end if currentList hasnt been created
				
				if(!currentListR.contains(start)){
					currentListR.add(start);
				}// end if the list doesnt contain this vertex
				
				outgoingR.put(end, currentListR);
            }// end while reading the file

            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            ex.printStackTrace();             
        }// end try catch block
		
	}// end function readFile(...)
	
	private void computeOrder() {
		// compute a finish time ordering for SCC algorithm
		initMarked();
		counter = 0;
		Vertex v = null;
		
		// loop through our HashMap of vertices using an iterator
		Iterator<Map.Entry<String, Vertex>> it = vertices.entrySet().iterator();
	  
		while (it.hasNext()) {
	        Map.Entry<String, Vertex> pair = (Map.Entry<String, Vertex>) it.next();
	        v = (Vertex) pair.getValue();
	        // perform depth-first search on the graph
	        Boolean mark = marked.get(v.toString());
	        
	        if(mark == null || !mark){
	        	finishDFS(v);
	        }// end if v is not marked
	        
	    }// end while
		
	}// end function computeOrder()
	
	private void finishDFS(Vertex v) {
		// perform recursive DFS on the reversed graph
		marked.put(v, true);
		ArrayList<String> list = outgoingR.get(v);
		
		if(list != null) {
			// outgoing edges are not null
			for(int i = 0; i < list.size(); i++) {
				String w = list.get(i);
				Vertex u = vertices.get(w);
				
				if(!marked.get(u)) {
					finishDFS(u);
				}// end if u is not marked
					
			}// end for loop over the list
			
		}// end if list is not null
		
		counter++;
		finishTimes.put(v.toString(), counter);
	}// end function finishDFS(...)
	
	private void computeSCC() {
		// compute the strongly connected components of our graph
		computeOrder();
		counter = 0;
		// sort our finishTimes in descending order
		finishTimes = (HashMap<String, Integer>) sortByValue((Map<String, Integer>) finishTimes);

		// unmark all vertices
		initMarked();
				
		Iterator<Map.Entry<String, Integer>> it = finishTimes.entrySet().iterator();
	    String w = null;
	    
		while (it.hasNext()) {
			// while loop over all finishTimes
	        Map.Entry<String, Integer> pair = (Map.Entry<String, Integer>) it.next();
	        w = (String) pair.getKey();
	        Vertex v = vertices.get(w);
	        
	        Boolean mark = marked.get(v);
	        
	        if(mark == null || !mark) {
	        	// set initial SCC set to be empty
	        	ArrayList<String> list = new ArrayList<String>();
	        	
        		// recursive DFS searching for strongly connected components 
	        	list = sccDFS(v, list, v);
	        	
   	        	// form our lists of SCCS
   	        	for(int i = 0; i < list.size(); i++) {
   	        		// retreive the string for the vertex
   	        		String t = list.get(i);
   	        		// retreive our vertex
        			Vertex k = vertices.get(t);
        			
        			// set the SCC key for these vertices to be the same as the root of the component
        			k.sccKey = vertices.get(list.get(0)).sccKey;
        			
        			// add the component to our component listing
        			sccs.put(k.sccKey, list);
        			
    				if(list.size() > maxSCCsize){
    					maxSCCsize = list.size();
    				}// end if list.size() > maxSCCsize
        				
        		}// end for loop adding vertex - scc combos to our data structure
	   	        
	        }// end if v is not marked
	       
		}// end while loop over all vertices
		
	}// end function computeSCC()
	
	private ArrayList<String> sccDFS(Vertex v, ArrayList<String> list, Vertex root) {
		// perform recursive DFS over the finish time ordering
		marked.put(v, true);
		
		// get all outgoing edges from v (normal graph)
		ArrayList<String> outEdges = outgoing.get(v.toString());
		Vertex u = null;
		
		// add this vertex to strongly connected list
		if(v != root && !list.contains(v.toString())){
			list.add(v.toString());
		}// end if we don't add duplicates
		
		if(outEdges != null) {
			// we have outgoing edges to loop over
			for(int i = 0; i < outEdges.size(); i++){
					
				// for loop over all outgoing edges
				String w = outEdges.get(i);
				u = vertices.get(w);
	
				Boolean mark = marked.get(u);
						
				if(mark == null || !mark) {
					sccDFS(u, list, root);
				}// end if u is not marked
				
			}// end for loop over all outgoing edges from this vertex

			if(!list.contains(root.toString())){
				list.add(root.toString());
			}// end if we havent added the root
			
		}// end if we have outgoing edges
		return list;
	}// end function sccDFS(...)
	
	private <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map ) {
		// sort our finishTimes in descending order for easy iteration
		List<Map.Entry<K, V>> list = new LinkedList<>( map.entrySet() );
		
		Collections.sort( list, new Comparator<Map.Entry<K, V>>() {
	        @Override
	        public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 ) {
	            return ( o2.getValue() ).compareTo( o1.getValue() );
	        }// end compare function
		});

	    Map<K, V> result = new LinkedHashMap<>();
	    
	    for (Map.Entry<K, V> entry : list) {
	        result.put( entry.getKey(), entry.getValue() );
	    }// end for
	    
	    return result;
	}// end sortByValue function(...)
	
	private void initMarked(){
		marked = new HashMap<Vertex, Boolean>();
		
		for (Map.Entry<Vertex, Boolean> entry : marked.entrySet()) {
		    entry.setValue(false);
		}// end for loop initializing hashmap to unmarked values
		
	}// end function for initializing the marked variable
	
	class Vertex {
		// create a vertex for GraphProcesser functions
		String vertex;
		int sccKey;
		
		public Vertex(String vertex){
			this.vertex = vertex;
			this.sccKey = this.hashCode();
		}// end Vertex constructor
		
		@Override
		public String toString(){
			return vertex;
		}// end function toString()
		
		@Override
		public boolean equals(Object o){
			Vertex v = (Vertex) o;
			return this.vertex.equals(v.vertex);
		}// end function equals()
		
	}// end class Vertex
	
}// end class GraphProcessor