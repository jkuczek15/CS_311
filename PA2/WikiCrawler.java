import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WikiCrawler {
	// crawl Wikipedia pages creating a directed graph with edges stored in WikiCS.txt
	private static final String BASE_URL = "https://en.wikipedia.org";		// main URL to crawl
	private String seedUrl;	 		// original URL to begin crawling
	private int max;  		 		// max number of pages to be crawled
	private String fileName; 		// file name for which graph to be written too
	
	public WikiCrawler(String seedUrl, int max, String fileName){
		this.seedUrl = seedUrl;
		this.max = max;
		this.fileName = fileName;
	}// end WikiCrawler constructor
	
	public void crawl() throws MalformedURLException, IOException, InterruptedException {
		// writes to the file named <fileName>, first line = number of vertices (max)
		// next lines = a directed edge of the web graph
	    ArrayList<Edge> edges = BFSTraversal(seedUrl);
		String fileContents = "";
		
		for(int i = 0; i < edges.size(); i++){
			fileContents = fileContents + edges.get(i).toString() + "\r";
		}// end for loop over all edges
	    
	    // write the response to a file
	    BufferedWriter bw = null;
		FileWriter fw = null;

		try {
			// begin try-catch block for writing to a file
			fw = new FileWriter(fileName);
			bw = new BufferedWriter(fw);
			bw.write(max + "\n");
			bw.write(fileContents);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// begin finally block for closing 
			try {
				// begin try-catch block for closing fileWriter
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}// end try-catch block for closing fileWriter

		}// end try-catch-finally block
				
	}// end function crawl()
	
	public ArrayList<String> extractLinks(String doc) throws IOException {
		// parse the string doc and return a list of links from the document (HTML)
		ArrayList<String> links = new ArrayList<String>();	
		String[] lines = doc.split("\r");
		
		for(int i = 0; i < lines.length; i++){
			// for loop over all our lines
			String regex = "href=\"/wiki/.*?\"";
			Pattern string = Pattern.compile(regex);
		    Matcher m = string.matcher(lines[i]);

		    while (m.find()) {
		    	// while loop over all our link matches
		    	String link = m.group().substring(m.group().indexOf('"')+1, m.group().length()-1);
		    	
		    	if(!link.contains("#") && !link.contains(":") && !links.contains(link)){
		    		 // only add links that don't contain these strings, don't add duplicates
		    		 links.add(link);
		    	}// end if only add certain links
		    	
		    }// end while loop over regex matches
		    
		}// end for loop over all lines in the document
		
		return links;
	}// end function extractLinks(...)
	
	private ArrayList<Edge> BFSTraversal(String seed_url) throws InterruptedException, IOException {
		// traverse the web graph starting at seed_url
		Queue<String> queue = new LinkedList<String>();
		
		// some lists we need here
		ArrayList<String> visited = new ArrayList<String>();
		ArrayList<String> extractedLinks = new ArrayList<String>();
		ArrayList<Edge> edges = new ArrayList<Edge>();
		
		// initialize streams and readers
		InputStream is = null;
		BufferedReader rd = null;
		StringBuffer response = null;
		
		// begin by adding our seedUrl to the queue and visited
		queue.add(seed_url);
		visited.add(seed_url);
		
		// keep us under max requests by counting the number of requests
		int counter = 1;
		
		while(!queue.isEmpty()){
			// while loop over all links in the queue
			String currentPage = queue.poll();
			// boolean for determining if we parse links, set true after first <p> tag
			boolean linkParse = false;
			
			try {
				// open GET request to our URL			    
			    is = new URL(BASE_URL + currentPage).openStream();
			    rd = new BufferedReader(new InputStreamReader(is));
			    response = new StringBuffer();
			    // increment our request counter
			    counter++;
			} catch (Exception e) {
		      e.printStackTrace();
			}// end try-catch block
					
			if(counter % 100 == 0){
				Thread.sleep(3000);
			}// end if we need to sleep
			
		    // append our entire response to a single String
		    String line;
		    while ((line = rd.readLine()) != null) {
		    	
		    	if(line.contains("<p>") || line.contains("<P>")){
		    		// we don't care about links until we reach first <p>
		    		linkParse = true;
		    	}// end if we've reached the first paragraph tag, begin parsing links
		    	
		    	if(linkParse){
		    		response.append(line);
		    		response.append('\r');
		    	}// end if we are parsing links
		     
		    }// end while
		    
		    // extract our links from our currentPage Response
		    extractedLinks = extractLinks(response.toString());
		    
		    for(int i = 0; i < extractedLinks.size(); i++){
		    	// for loop over all our extracted links
		    	String link = extractedLinks.get(i);
		    	
		    	if(!visited.contains(link) && visited.size() < max) {
		    		queue.add(link);
		    		visited.add(link);
	    		}// end if we should visit this link
		    	
		    	Edge e = new Edge(currentPage, link);
		    	
		    	if(visited.contains(link) && !currentPage.equals(link) && !edges.contains(e)){
	    			edges.add(e);
	    		}// end if we dont have a self-loop
		    		
		    }// end for loop over all our extracted links

		   if(rd != null)
			   rd.close();
		   if(is != null)
			   is.close();
		   
		}// end while loop over all items in the queue
		
		return edges;
	}// end function BFSTraversal()
	
	class Edge {
		// create an Edge of a directed graph (<start>, <end>) pair
		String start;
		String end;
		
		public Edge(String start, String end){
			this.start = start;
			this.end = end;
		}// end Edge constructor
			
		@Override
		public String toString(){
			return start + " " + end;
		}// end function toString()
		
		@Override
		public boolean equals(Object o){
			Edge e = (Edge) o;
			return this.start == e.start && this.end == e.end;
		}// end function equals()
		
	}// end class Edge

}// end class WikiCrawler