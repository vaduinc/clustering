package edu.harvard.cscie99.clustering.util;

import java.util.BitSet;

/**
 *	Class to allocated common (utility) functionality 
 *
 */
public class Utility {

	/**
	 * Returns the distance from row1 to row2
	 * 
	 * @param row1
	 * @param row2
	 * @return int 
	 */
	public static int distance(BitSet row1, BitSet row2){
		
		int length1 = row1.length();
		int length2 = row2.length();
		
		if ( length1>=length2){
			row1.set(length1);
			row2.set(length1);
		}else{
			row1.set(length2);
			row2.set(length2);
		}
			
		row1.xor(row2);	
	      
	    return row1.cardinality();
	}

	/**
	 * TODO move to UTIL static -- throws exception
	 * 
	 * @param point1
	 * @param point2
	 * @return double 
	 */
	public static double distance(double[] point1, double[] point2)	{
		
		// ASSUMPTION: the length of the arrays is the same. 
		Double sum = 0D;
		for (int i = 0; i < point1.length; i++) {
			sum = sum + (point2[i] - point1[i]) * (point2[i] - point1[i]);
		}
		return Math.sqrt(sum);
	}

}
