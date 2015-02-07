package edu.harvard.cscie99.clustering.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * TODO
 * description
 *
 */
public class ClusterJarvis {

	static String STATUS_PROCESSED = "PROCESSED";
	static String STATUS_NOT_PROCESSED = "NOT PROCESSED";
	static String STATUS_MOVED = "MOVED";
	
	private int id;
	private List<DistanceRow> closestNeighbors ; 			//<distance, rowId>
	private List<Integer> clusterRows; 						// rows that belong to this cluster
	private String status; 									// to know whether this cluster was already processed
	
	
	/**
	 * TODO move to utilities
	 * 
	 * @param max
	 * @param source
	 * @return
	 */
	public static SortedMap<Double,Integer> putFirstEntries(int max, SortedMap<Double,Integer> source) {
		  int count = 0;
		  TreeMap<Double,Integer> target = new TreeMap<Double,Integer>();
		  for (Map.Entry<Double,Integer> entry:source.entrySet()) {
		     if (count >= max) break;

		     target.put(entry.getKey(), entry.getValue());
		     count++;
		  }
		  return target;
	}
	
	
	/**
	 * TDOO description 
	 * 
	 * @param max
	 * @param source
	 * @return
	 */
	public static List<DistanceRow> truncateCollection(int max, List<DistanceRow> source) {
		
		if (source.size()>max){
			return source.subList(0, max);
		}else{
			return source;
		}
	}
	
	
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
	 * TODO description
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
	 * TODO description
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
