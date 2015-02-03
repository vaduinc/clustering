package edu.harvard.cscie99.clustering.algorithm;

public class DistanceRow implements Comparable<DistanceRow> {

	//private int precision = 3; // Default value
//	private double point1[][];
//	private double point2[][];
	private int rowId;
	private double distance;
	
	public DistanceRow(double distance, int rowId){
		this.rowId = rowId;
		this.distance = distance;
	}
	
//	public DistanceRow(double[][] point1, double[][] point2){
//		this.point1 = point1;
//		this.point2 = point2;
//	}

//	public DistanceP1P2(double[][] point1, double[][] point2, int precision){
//		this.point1 = point1;
//		this.point2 = point2;
//		this.precision = precision;
//	}

//	public int getPrecision() {
//		return precision;
//	}
//
//	public void setPrecision(int precision) {
//		this.precision = precision;
//	}

//	public double[][] getPoint1() {
//		return point1;
//	}
//	
//	public double[][] getPoint2() {
//		return point2;
//	}

	
	public int getRowId() {
		return rowId;
	}

	public double getDistance() {
		return distance;
	}

//	public void setDistance(double distance) {
//		this.distance = distance;
//	}
	
	@Override
	public int compareTo(DistanceRow o) {
		// TODO Auto-generated method stub
		
		if(this.distance==o.distance ){
			return 0;
		}else if(this.distance>o.distance ){
			return 1;
		}else{
			return -1;
		}
		
	}

}
