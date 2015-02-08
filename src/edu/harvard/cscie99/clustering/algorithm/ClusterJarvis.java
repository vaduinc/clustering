package edu.harvard.cscie99.clustering.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Used by the Jarvis algorithm.
 * It holds all the information related for each cluster created (a cluster for each row initially)
 *  
 *
 */
public class ClusterJarvis {

	static String STATUS_PROCESSED = "PROCESSED";		  // finished processing.
	static String STATUS_NOT_PROCESSED = "NOT PROCESSED"; // new cluster
	static String STATUS_MOVED = "MOVED";				  // the data from this cluster was moved to another one	
	
	private int id;
	private List<DistanceRow> closestNeighbors ; 			//<distance, rowId>
	private List<Integer> clusterRows; 						// rows that belong to this cluster
	private String status; 									// to know whether this cluster was already processed.
	
	
	/**
	 * Just for debugging purposes
	 */
	public String toString(){
		
		StringBuffer stb = new StringBuffer("Cluster id:" + id + " status: " + status +"\n");
		
		stb.append("Neighbors :\n");
		for(DistanceRow dstRow: closestNeighbors){
			stb.append(dstRow.toString() +"\n");
		}
		
		stb.append("Rows in this Cluster :\n");
		for(Integer clusterRow: clusterRows){
			stb.append(" Rowid = " + clusterRow +"\n");
		}
		
		return stb.toString();
	}
	
	
	/**
	 * It only keeps the "max" amount of elements in the list.
	 * max = numNeighbors command line input parameter
	 * 
	 * @param max
	 * @param source
	 * @return List<DistanceRow>
	 */
	public static List<DistanceRow> truncateCollection(int max, List<DistanceRow> source) {
		
		if (source.size()>max){
			source.subList(max,source.size()).clear();
			return source;
		}else{
			return source;
		}
	}
	
	/**
	 * Constructor
	 * 
	 * @param clusterId
	 */
	public ClusterJarvis(int clusterId){
		this.id = clusterId;
		this.status = STATUS_NOT_PROCESSED;
		this.clusterRows = new ArrayList<Integer>();
		this.closestNeighbors = new ArrayList<DistanceRow>();
		
	}
	
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Get the rows ids from the closestNeighbors cluster
	 * 
	 * @return List<Integer>
	 */
	public List<Integer> getClosestNeighborsRowsId() {
		
		List<Integer> distRowsIDs = new ArrayList<Integer>(closestNeighbors.size());
		
		for(DistanceRow distanceRow: closestNeighbors){
			distRowsIDs.add(distanceRow.getRowId());
		}
		
		return distRowsIDs;
	}
		
	
	/**
	 * Sort the "closestNeighbors" collection.
	 * It's used after getting all the distances between
	 * one row and the rest of the list.
	 */
	public void sortClosestNeighbors(){
		Collections.sort(closestNeighbors);
	}
	
	
	public List<DistanceRow> getClosestNeighbors() {
		return closestNeighbors;
	}
	
	public void setClosestNeighbors(List<DistanceRow> closestNeighbors) {
		this.closestNeighbors = closestNeighbors;
	}
	
	public void addCloseNeighbor(Number distance, Integer rowId){
		this.closestNeighbors.add(new DistanceRow(distance, rowId));
	}
	
	public void addRow2Cluster(Integer rowIdx){
		clusterRows.add(rowIdx);
	}
	
	public void addRows2Cluster(List<Integer> mergedRows){
		clusterRows.addAll(mergedRows);
	}
	
	public List<Integer> getClusterRows() {
		return clusterRows;
	}
	
	public void setClusterRows(List<Integer> clusterRows) {
		this.clusterRows = clusterRows;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}
	
}
