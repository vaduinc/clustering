package edu.harvard.cscie99.clustering.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.harvard.cscie99.clustering.result.ClusteringResult;
import edu.harvard.cscie99.clustering.util.InputParamEnum;

public abstract class LeaderAlgorithm<E extends Object> implements IClusterAlgo<E> {

//	@Override
//	public ClusteringResult cluster(List<String> rowLabels,double[][] data,
//			Map<String, Object> clusterParams) {
//		
//		try {
//			final double minDistance = Double.valueOf(clusterParams.get(InputParamEnum.IN_MIN_DIST.value()).toString()); // TODO check for exception
//			final int rows = data.length;
//			
//			ClusteringResult results = new ClusteringResult(rows,rowLabels);
//			Map<String, Number> minPair;
//			List<List<Integer>> clusters = new ArrayList<List<Integer>>();
//			List<Integer> clustersRows = new ArrayList<Integer>();
//			
//			clustersRows.add(0); 		// add the first row 
//			clusters.add(clustersRows);	// create the first Cluster and add the first row to it.
//			
//			results.addClusterToLabel(1); // first cluster label starts in 1
//			
//			for (int idx=1; idx<rows;idx++){
//				minPair  = LeaderAlgoImpl.calculateDistance(data, idx, clusters);
//				if (minPair.get("MIN").doubleValue()  < minDistance ){
//					clusters.get(minPair.get("MIN_CLS_IDX").intValue()).add(idx);
//					results.addClusterToLabel(minPair.get("MIN_CLS_IDX").intValue()+1);
//				}else{
//					// Create a new cluster and insert the evaluated row into it.
//					List<Integer> newClustersRows = new ArrayList<Integer>();
//					newClustersRows.add(idx);
//					clusters.add(newClustersRows);
//					results.addClusterToLabel(clusters.size()); 
//				}
//			}
//
//			return results;
//		
//		} catch (Exception e) {
//			// TODO log the error
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	
//	/**
//	 * TODO move to UTIL
//	 * Compute the distance for that row to the closest cluster
//	 * 
//	 * @param data
//	 * @param idxRow
//	 * @param clusters
//	 * @return Map<String, Number>
//	 * @throws Exception
//	 */
//	private static Map<String, Number> calculateDistance(double[][] data, int idxRow, List<List<Integer>> clusters) throws Exception{
//	  
//		  Map<String, Number> minPair = new HashMap<String,Number>(3);	
//		  double[] rowInEvaluation = data[idxRow];
//		  
//		  double minDist = Double.MAX_VALUE; // biggest number a Double can get in Java
//		  double currentMinDist=0;
//		  int minRowIdx = -1;
//		  int currentRowIdx = 0;
//		  int currentClusterIdx = 0;
//		  
//		  for (int idxCluster = 0; idxCluster < clusters.size(); idxCluster++) {
//			  currentRowIdx = clusters.get(idxCluster).get(0);
//			  double[] originalRowCluster = data[currentRowIdx]; // gets the first row that was created for this cluster
//			  currentMinDist = Utility.distance(originalRowCluster, rowInEvaluation);
//			  
//			  if (currentMinDist<minDist){
//				  minDist = currentMinDist;
//				  minRowIdx = currentRowIdx;
//				  currentClusterIdx = idxCluster;
//			  }
//		          
//		  }
//		  
//		  minPair.put("MIN", minDist);
//		  minPair.put("MIN_ROW_IDX", minRowIdx);
//		  minPair.put("MIN_CLS_IDX", currentClusterIdx);
//		  
//		  return minPair ;
//	                  
//	}
//	

	
	public abstract List<String> getRowLabels(E data,	Map<String, Object> clusterParams);
	
	
	@Override
	public ClusteringResult cluster(E data,	Map<String, Object> clusterParams) {
		
		final double minDistance = Double.valueOf(clusterParams.get(InputParamEnum.IN_MIN_DIST.value()).toString()); // TODO check for exception
		final List<String> rowKeys = getRowLabels(data, clusterParams);
		final int rows = rowKeys.size();
		
		ClusteringResult results = new ClusteringResult(rows,rowKeys);
		Map<String, Number> minPair;
		
		List<Integer> clustersRows = new ArrayList<Integer>();
		List<List<Integer>> clusters = new ArrayList<List<Integer>>();
		
		clustersRows.add(0); 		// add the first row 
		clusters.add(clustersRows);	// create the first Cluster and add the first row to it.
		
		results.addClusterToLabel(1); // first cluster label starts in 1
				
		for (int idx=1; idx<rows;idx++){
			minPair  = this.calculateDistance(data, idx, clusters,rowKeys);
			if (minPair.get("MIN").doubleValue()  <= minDistance ){
				clusters.get(minPair.get("MIN_CLS_IDX").intValue()).add(idx);
				results.addClusterToLabel(minPair.get("MIN_CLS_IDX").intValue()+1);
			}else{
				// Create a new cluster and insert the evaluated row into it.
				List<Integer> newClustersRows = new ArrayList<Integer>();
				newClustersRows.add(idx);
				clusters.add(newClustersRows);
				results.addClusterToLabel(clusters.size()); 
			}
		}
		
		return results;
	}

	
	public abstract Number getDistance(E data, List<String> rowKeys , int fromIdx, int toIdx);
	
	
	/**
	 * TODO description 
	 * 
	 * @param data
	 * @param fromIdx
	 * @param clusters
	 * @return Map<String, Number>
	 */
	private  Map<String, Number> calculateDistance(E data, int fromIdx, List<List<Integer>> clusters,List<String> rowKeys) {
		
		  Map<String, Number> minPair = new HashMap<String,Number>(3);	
		  
		  Number minDist = Integer.MAX_VALUE; // biggest number a Double can get in Java
		  Number currentMinDist=0;
		  int minRowIdx = -1;
		  int toIdx = 0;
		  int currentClusterIdx = 0;
		  
		  for (int idxCluster = 0; idxCluster < clusters.size(); idxCluster++) {
			  toIdx = clusters.get(idxCluster).get(0);
			  currentMinDist = getDistance(data, rowKeys, fromIdx, toIdx);
			  
			  if (currentMinDist.doubleValue() < minDist.doubleValue()){
				  minDist = currentMinDist;
				  minRowIdx = toIdx;
				  currentClusterIdx = idxCluster;
			  }
		          
		  }
		  
		  minPair.put("MIN", minDist);
		  minPair.put("MIN_ROW_IDX", minRowIdx);
		  minPair.put("MIN_CLS_IDX", currentClusterIdx);
		  
		  return minPair ;
		
	}

		
}

	