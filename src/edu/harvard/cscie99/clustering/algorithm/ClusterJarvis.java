package edu.harvard.cscie99.clustering.algorithm;

import java.util.ArrayList;
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
	private SortedMap<Double,Integer> closestNeighbors ; 	//<distance, rowId>
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
	
	
	public ClusterJarvis(int clusterId){
		this.id = clusterId;
		this.status = STATUS_NOT_PROCESSED;
		this.clusterRows = new ArrayList<Integer>();
		this.closestNeighbors = new TreeMap<Double,Integer>();
		
	}
	
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public SortedMap<Double,Integer> getClosestNeighbors() {
		return closestNeighbors;
	}
	
	public void setClosestNeighbors(SortedMap<Double,Integer> closestNeighbors) {
		this.closestNeighbors = closestNeighbors;
	}
	
	public void addNeighbor(Double distance, Integer rowId){
		this.closestNeighbors.put(distance, rowId);
	}
	
	public void addRow2Cluster(Integer rowIdx){
		clusterRows.add(rowIdx);
	}
	
	// TODO change to TreeSet.
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
