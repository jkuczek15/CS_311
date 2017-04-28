import java.util.concurrent.ThreadLocalRandom;

public class HashFunction {
	// create a HashFunction for our hash table
	int a, b, p;
	
	public HashFunction(int range){
		this.p = Helper.getPrime(range);
		this.a = ThreadLocalRandom.current().nextInt(0, p);
		this.b = ThreadLocalRandom.current().nextInt(0, p);
	}// end constructor for HashFunction
	
	public int hash(int x){
		long val = Helper.mod((a * x + b), p);
		return (int) val;
	}// end function for hashing
	
	public int getA(){
		return a;
	}// end getter method

	public int getB(){
		return b;
	}// end getter method
	
	public int getP(){
		return p;
	}// end getter method
	
	public void setA(int x){
		a = Helper.mod(x, p);
	}// end setter method
	
	public void setB(int y){
		b = Helper.mod(y, p);
	}// end setter method
	
	public void setP(int x){
		p = Helper.getPrime(x);
		setA(ThreadLocalRandom.current().nextInt(0, p));
		setB(ThreadLocalRandom.current().nextInt(0, p));
	}// end setter method
	
}// end class HashFunction
