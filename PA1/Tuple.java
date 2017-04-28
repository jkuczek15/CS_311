public class Tuple {
	// create a tuple (<key>,<value>) pair
	int key;
	double value;
	
	public Tuple(int keyP, float valueP) {
	    key = keyP;
	    value = valueP;
	}// end constructor for Tuple
	
	public int getKey(){
		return key;
	}// end function getKey()
	
	public double getValue(){
		return value;
	}// end function getValue()
	
	@Override
	public boolean equals(Object o){
		Tuple t = (Tuple) o;
		return this.key == t.key && this.value == t.value;
	}// end function equals()
	
	@Override
	public String toString(){
		return "(" + key + "," + value + ")";
	}// end function toString()
	
}// end class Tuple
