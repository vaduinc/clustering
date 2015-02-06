package edu.harvard.cscie99.clustering;

import java.util.BitSet;

public class POC {

	public static void main(String[] args) {
		
		// create 2 bitsets
	      BitSet bitset1 = new BitSet(8);
	      BitSet bitset2 = new BitSet(8);
		
	   // assign values to bitset1
	      bitset1.set(2);
	      bitset1.set(4);
	      bitset1.set(5);
	      bitset1.set(10);
	      bitset1.set(12);
	      bitset1.set(19);
	      bitset1.set(26);

	      // assign values to bitset2
	      bitset2.set(2);
	      bitset2.set(4);
	      bitset2.set(9);
	      bitset2.set(12);
	      bitset2.set(13);
	      bitset2.set(20);
	      bitset2.set(25);
	      bitset2.set(26);
	      
	      BitSet bitset1c = (BitSet) bitset1.clone();
	      BitSet bitset2c = (BitSet) bitset2.clone();
	      bitset1c.flip(0,bitset1c.length());
	      bitset2c.flip(0,bitset2c.length());

	      // print the sets
	      System.out.println("Bitset1:" + bitset1);
	      System.out.println("Bitset2:" + bitset2);
	      System.out.println("Bitset1c:" + bitset1c);
	      System.out.println("Bitset2c:" + bitset2c);

	      // perform a logical or between the two bitsets
	      bitset1.or(bitset2);
	      bitset1c.or(bitset2c);
	      bitset1.and(bitset1c);
	      System.out.println("" + bitset1);

	}

}
