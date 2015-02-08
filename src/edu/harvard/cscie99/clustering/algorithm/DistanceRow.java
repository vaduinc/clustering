package edu.harvard.cscie99.clustering.algorithm;

/**
 *	Class to keep the distance to this specific row (rowId) 
 *
 *	Used with the Jarvis algorithm.
 */
public class DistanceRow implements Comparable<DistanceRow> {

	private int rowId;
	private Number distance;
	
	/**
	 * Just for debugging purposes
	 */
	public String toString(){
		return " rowId[" + rowId + "] - distance[" + distance +"]"; 
	}
	
	public DistanceRow(Number distance, int rowId){
		this.rowId = rowId;
		this.distance = distance;
	}
	
	public int getRowId() {
		return rowId;
	}

	public Number getDistance() {
		return distance;
	}

	@Override
	public int compareTo(DistanceRow o) {
		
		if(this.distance.doubleValue()>o.distance.doubleValue() ){
			return 1;
		}else if(this.distance.doubleValue()<o.distance.doubleValue() ){
			return -1;
		}else{
			return 0;
		}	
		
	}

}
