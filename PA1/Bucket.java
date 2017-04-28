import java.util.ArrayList;
import java.util.Iterator;

public class Bucket {
	
	ArrayList<Tuple> tuples;
	int size;
	
	public Bucket(){
		tuples = new ArrayList<Tuple>();
		size = 0;
	}// end Bucket constructor
	
	public void add(Tuple t){
		tuples.add(t);
		size++;
	}// end function add()
	
	public void remove(int k){
		tuples.remove(k);
		size--;
	}// end remove function for Bucket
	
	public void remove(Tuple t){
		tuples.remove(t);
		size--;
	}// end remove function for a tuple
	
	public Tuple get(int index){
		return tuples.get(index);
	}// end function for returning a tuple
	
	public ArrayList<Tuple> getTuples(){
		return tuples;
	}// end function getTuples()
	
	public int size(){
		return size;
	}// end getSize method
	
	public Iterator<Tuple> iterator(){
		return tuples.iterator();
	}// end function for getting a Bucket iterator
	
	public boolean isEmpty(){
		return size == 0;
	}// end function isEmpty()
	
	@Override
	public String toString(){
		String s = "[";
		Iterator<Tuple> iter = tuples.iterator();
		
		while(iter.hasNext()){
		    Tuple t = iter.next();
		    s += t.toString();
		    
		    if(iter.hasNext()){
		    	s += " => ";
		    }// end if iterator has next
		    
	   }// end while over arrayList in HashTable
		
		s += "]";
		return s;
	}// end function toString()
	
}// end class Bucket
