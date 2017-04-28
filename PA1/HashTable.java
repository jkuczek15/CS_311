import java.util.ArrayList;
import java.util.Iterator;

public class HashTable {
	// implementation of a hash table
	int p;
	HashFunction h;
	Bucket[] table;
	
	public HashTable(int size){
		this.p = Helper.getPrime(size);
		this.table = new Bucket[p];
		initializeTable();
		this.h = new HashFunction(p);
	}// end constructor HashTable
	
	public int maxLoad(){
		int max = 0;
		
		for(int i = 0; i < size(); i++){
			
			if(table[i] != null && table[i].size() > max){
				max = table[i].size();
			}// end if this is bigger than max
			
		}// end for loop
		
		return max;
	}// end maxLoad function
	
	public double averageLoad(){
		int non_empty_buckets = 0;
		
		for(int i = 0; i < size(); i++){
			
			if(table[i] != null){
				non_empty_buckets++;
			}// end if this cell is not null
			
		}// end for loop
		if(non_empty_buckets == 0) return 0;
		return numElements() / (double) non_empty_buckets;
	}// end function averageLoad()
	
	public int size(){
		return p;
	}// end function for getting the size
	
	public int numElements(){
		int count = 0;
		
		for(int i = 0; i < size(); i++){
			
			if(table[i] != null){
				count += table[i].size();
			}// end if table cell is not null
			
		}// end for loop
		
		return count;
	}// end numElements function
	
	public double loadFactor(){
		return (double) numElements() / (double) size();
	}// end function loadFactor()
	
	public void add(Tuple t){
		
		if(loadFactor() > 0.7){
			Bucket[] tmp_table = table;
			// resize the current table
			resize();
			// rehash old values into the new table
			rehash(tmp_table);
		}// end if loadFactor is greater than 0.7
		
		int hash_key = h.hash(t.getKey());
		
		if(table[hash_key] == null){
			// if our value is null, we need to create a new Bucket
			table[hash_key] = new Bucket();
		}// end if table value is null
		
		table[hash_key].add(t);
	}// end function for adding a tuple to the hashTable
	
	public ArrayList<Tuple> search(int k){
		int hash_key = h.hash(k);
		ArrayList<Tuple> list = new ArrayList<Tuple>();
		
		if(table[hash_key] == null){
			return list;
		}// end if bucket is null, just return an empty list
		
		Bucket bucket = table[hash_key];
		Tuple t;
	
		for(int i = 0; i < bucket.size(); i++){
			t = bucket.get(i);
			
			if(t.getKey() == k){
				list.add(t);
			}// end if key != k
			
		}// end for loop
		
		return list;
	}// end function search
	
	public void remove(Tuple t){
		int hash_key = h.hash(t.getKey());
		Bucket bucket = table[hash_key];
		bucket.remove(t);
		
		if(bucket.size() == 0){
			table[hash_key] = null;
		}// end if bucket size is 0
		
	}// end function remove
	
	public void rehash(Bucket[] buckets){
		
		for(int i = 0; i < buckets.length; i++){
			if(buckets[i] == null) continue;
			Iterator<Tuple> iter = buckets[i].iterator();
		    
			while(iter.hasNext()){
		    	// loop over tmp buckets, rehashing
		    	Tuple t = iter.next();
		    	add(t);
		    }// end while loop
			
		}// end for loop
		
	}// end function rehash
	
	public void resize(){
		// find prime at least twice current size
		p = Helper.getPrime(2 * p);
		// create the new table
		table = new Bucket[p];
		initializeTable();
		// recreate our hash function
		h.setP(p);
	}// end function resize()
	
	public void initializeTable(){
		for(int i = 0; i < p; i++){
			this.table[i] = null;
		}// end for loop setting null values in hash table
	}// end function initTable()
	
	public Bucket getBucket(int index){
		return table[index];
	}// end function for returning a Bucket at an index
	
	@Override
    public String toString() {
	   String s = "";
	   int key = 0;
       for(int i = 0; i < size(); i++){
    	   
    	   if(table[i] != null){
    		   s += key + ": " + table[i].toString();
    		   s += "\n";
    	   }else{
    		   s += i + ": {}\n"; 
    	   }// end if table cell is null
    	   key++;
       }// end for loop
       return s;
    }// end function toString
	
	public HashFunction hashFunction(){
		return h;
	}// end function getHashFunction
	
}// end class HashTable
