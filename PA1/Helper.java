import java.math.BigInteger;

public class Helper {
	// all of our helper methods go here

	public static int getPrime(int n){
		// return the first positive prime of at least n
		boolean found = false;
		
		while(!found){
			// while loop until we find a prime >= n
			if(isPrime(n)){
				// found a prime
				found = true;
			}else{
				// did not find prime
				if(n == 1 || n % 2 == 0){
					n = n + 1;
				}else{
					n = n + 2;
				}// end if we have an even number
				
			}// end if this is a prime
			
		}// end while we haven't found a prime
		
		return n;
	}// end function getPrime()
	
	public static boolean isPrime(int n) {
		if(n < 2) return false;
		if(n == 2) return true;
		BigInteger b = new BigInteger(Integer.toString(n));
		return b.isProbablePrime(100);
	}// end function isPrime
	
	public static int mod(int x, int y){
	    int result = x % y;
	    if (result < 0){
	        result += y;
	    }
	    return result;
	}// end mod function

}// end class helper
