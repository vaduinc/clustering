package edu.harvard.cscie99.clustering.util;

import java.util.BitSet;

public class Utility {

	/**
	 * Returns the distance from point1 to point2
	 * 
	 * @param point1
	 * @param point2
	 * @return int 
	 */
	public static int distance(BitSet point1, BitSet point2){
		
		int length1 = point1.length();
		int length2 = point2.length();
		
		if ( length1>=length2){
			point1.set(length1);
			point2.set(length1);
		}else{
			point1.set(length2);
			point2.set(length2);
		}
			
		point1.xor(point2);	
		//System.out.println(point1.cardinality() + " : " + point1);
	      
	    return point1.cardinality();
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
