package edu.harvard.cscie99.clustering.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.harvard.cscie99.clustering.result.ClusteringResult;
import edu.harvard.cscie99.clustering.util.InputParamEnum;

/**
 * Class to support the Leader clustering algorithm.
 * It supports both Matrix and Fingerprint input data. 
 *
 *	Clusters = {}
 *		For each data row
 *			Compute the distance for that row to the closest cluster
 *			If distance < minDistance then
 *				Assign the row to the given cluster
 *			else
 *				Create a new cluster centered at the current row
 *				Assign the row to the given cluster
 *		
 *
 * @param <E> is either <double[][]> or Map<String, BitSet>
 */
public abstract class LeaderAlgorithm<E extends Object> implements IClusterAlgo<E> {

	final static int MIN_DISTANCE = 0; //array position where the min. distance is stored.
	final static int CLUSTER_ID_MIN_DISTANCE = 1;  //array position where the Cluster ID with the min. distance is stored.
	
	public abstract List<String> getRowLabels(E data,	Map<String, Object> clusterParams);
	public abstract Number getDistance(E data, List<String> rowKeys , int fromIdx, int toIdx);
	
	@Override
	public ClusteringResult cluster(E data,	Map<String, Object> clusterParams) {
		
		final double minDistance = Double.valueOf(clusterParams.get(InputParamEnum.IN_MIN_DIST.value()).toString()); 
		final List<String> rowKeys = getRowLabels(data, clusterParams);
		final int rows = rowKeys.size();
		Number[] minPair;
		
		ClusteringResult results = new ClusteringResult(rows,rowKeys);
		
		List<Integer> clustersRows = new ArrayList<Integer>();
		List<List<Integer>> clusters = new ArrayList<List<Integer>>();
		
		clustersRows.add(0); 		// add the first row 
		clusters.add(clustersRows);	// create the first Cluster and add the first row to it.
		
		results.addClusterToLabel(1); // first cluster label starts in 1
				
		// for each row in the data
		for (int idx=1; idx<rows;idx++){
			// compute the distance for that row to the closest cluster
			minPair  = this.calculateDistance(data, idx, clusters,rowKeys);
			if (minPair[MIN_DISTANCE].doubleValue()  <= minDistance ){
				// Assign the row to the given cluster
				clusters.get(minPair[MIN_CLS_IDX].intValue()).add(idx);
				results.addClusterToLabel(minPair[MIN_CLS_IDX].intValue()+1);
			}else{
				// Create a new cluster centered at the current row
				List<Integer> newClustersRows = new ArrayList<Integer>();
				newClustersRows.add(idx);
				clusters.add(newClustersRows);
				results.addClusterToLabel(clusters.size()); 
			}
		}
		
		return results;
	}

	
	/**
	 * Compute the distance for row (fromIdx) to the closest cluster 
	 * 
	 * @param data
	 * @param fromIdx	current row
	 * @param clusters
	 * @return Number[]	position[0] = minimum distance value, position[1] = cluster id 
	 */
	private  Number[]  calculateDistance(E data, int fromIdx, List<List<Integer>> clusters,List<String> rowKeys) {
		
		  Number[] minPair = new Number[2] ;	
		  
		  Number minDist = Integer.MAX_VALUE; // biggest number a Integer can get in Java
		  Number currentMinDist=0;
		  int toIdx = 0;
		  int currentClusterIdx = 0;
		  
		  for (int idxCluster = 0; idxCluster < clusters.size(); idxCluster++) {
			  toIdx = clusters.get(idxCluster).get(0);
			  currentMinDist = getDistance(data, rowKeys, fromIdx, toIdx);
			  
			  if (currentMinDist.doubleValue() < minDist.doubleValue()){
				  minDist = currentMinDist;
				  currentClusterIdx = idxCluster;
			  }
		          
		  }
		  
		  minPair[MIN_DISTANCE] = minDist;
		  minPair[MIN_CLS_IDX] = currentClusterIdx;
		  
		  return minPair ;
		
	}

		
}

	