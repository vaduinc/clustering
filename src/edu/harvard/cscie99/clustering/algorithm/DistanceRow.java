package edu.harvard.cscie99.clustering.algorithm;

public class DistanceRow implements Comparable<DistanceRow> {

	private int rowId;
	private Number distance;
	
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
		// TODO Auto-generated method stub
		
		if(this.distance==o.distance ){
			return 0;
		}else if(this.distance.doubleValue()>o.distance.doubleValue() ){
			return 1;
		}else{
			return -1;
		}
		
	}

}
