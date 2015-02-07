package edu.harvard.cscie99.clustering.algorithm;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.harvard.cscie99.clustering.result.ClusteringResult;
import edu.harvard.cscie99.clustering.util.InputParamEnum;
import edu.harvard.cscie99.clustering.util.Utility;

public class LeaderAlgoImpl implements IClusterAlgo {

	@Override
	public ClusteringResult cluster(List<String> rowLabels,double[][] data,
			Map<String, Object> clusterParams) {
		
		try {
			final double minDistance = Double.valueOf(clusterParams.get(InputParamEnum.IN_MIN_DIST.value()).toString()); // TODO check for exception
			final int rows = data.length;
			
			ClusteringResult results = new ClusteringResult(rows,rowLabels);
			Map<String, Number> minPair;
			List<List<Integer>> clusters = new ArrayList<List<Integer>>();
			List<Integer> clustersRows = new ArrayList<Integer>();
			
			clustersRows.add(0); 		// add the first row 
			clusters.add(clustersRows);	// create the first Cluster and add the first row to it.
			
			results.addClusterToLabel(1); // first cluster label starts in 1
			
			for (int idx=1; idx<rows;idx++){
				minPair  = LeaderAlgoImpl.calculateDistance(data, idx, clusters);
				if (minPair.get("MIN").doubleValue()  < minDistance ){
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
		
		} catch (Exception e) {
			// TODO log the error
			e.printStackTrace();
			return null;
		}
	}

	
	/**
	 * TODO move to UTIL
	 * Compute the distance for that row to the closest cluster
	 * 
	 * @param data
	 * @param idxRow
	 * @param clusters
	 * @return Map<String, Number>
	 * @throws Exception
	 */
	private static Map<String, Number> calculateDistance(double[][] data, int idxRow, List<List<Integer>> clusters) throws Exception{
	  
		  Map<String, Number> minPair = new HashMap<String,Number>(3);	
		  double[] rowInEvaluation = data[idxRow];
		  
		  double minDist = Double.MAX_VALUE; // biggest number a Double can get in Java
		  double currentMinDist=0;
		  int minRowIdx = -1;
		  int currentRowIdx = 0;
		  int currentClusterIdx = 0;
		  
		  for (int idxCluster = 0; idxCluster < clusters.size(); idxCluster++) {
			  currentRowIdx = clusters.get(idxCluster).get(0);
			  double[] originalRowCluster = data[currentRowIdx]; // gets the first row that was created for this cluster
			  currentMinDist = Utility.distance(originalRowCluster, rowInEvaluation);
			  
			  if (currentMinDist<minDist){
				  minDist = currentMinDist;
				  minRowIdx = currentRowIdx;
				  currentClusterIdx = idxCluster;
			  }
		          
		  }
		  
		  minPair.put("MIN", minDist);
		  minPair.put("MIN_ROW_IDX", minRowIdx);
		  minPair.put("MIN_CLS_IDX", currentClusterIdx);
		  
		  return minPair ;
	                  
	}
	
	
	@Override
	public ClusteringResult cluster(Map<String, BitSet> data,
			Map<String, Object> clusterParams) {
		
		final double minDistance = Double.valueOf(clusterParams.get(InputParamEnum.IN_MIN_DIST.value()).toString()); // TODO check for exception
		final List<String> rowKeys = new ArrayList<String>(data.keySet());
		final int rows = rowKeys.size();
		
		ClusteringResult results = new ClusteringResult(rows,rowKeys);
		Map<String, Number> minPair;
		
		List<Integer> clustersRows = new ArrayList<Integer>();
		List<List<Integer>> clusters = new ArrayList<List<Integer>>();
		
		clustersRows.add(0); 		// add the first row 
		clusters.add(clustersRows);	// create the first Cluster and add the first row to it.
		
		results.addClusterToLabel(1); // first cluster label starts in 1
				
		for (int idx=1; idx<rows;idx++){
			minPair  = LeaderAlgoImpl.calculateDistance(data, idx, clusters);
			if (minPair.get("MIN").doubleValue()  < minDistance ){
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

	
	/**
	 * TODO description -- merge with sibling
	 * @param data
	 * @param idxRow
	 * @param clusters
	 * @return Map<String, Number>
	 */
	private static Map<String, Number> calculateDistance(Map<String, BitSet> data, int idxRow, List<List<Integer>> clusters) {
		
		  Map<String, Number> minPair = new HashMap<String,Number>(3);	
		  final List<String> rowKeys = new ArrayList<String>(data.keySet());
		  final BitSet rowInEvaluation = data.get(rowKeys.get(idxRow));
		  
		  int minDist = Integer.MAX_VALUE; // biggest number a Double can get in Java
		  int currentMinDist=0;
		  int minRowIdx = -1;
		  int currentRowIdx = 0;
		  int currentClusterIdx = 0;
		  
		  for (int idxCluster = 0; idxCluster < clusters.size(); idxCluster++) {
			  currentRowIdx = clusters.get(idxCluster).get(0);
			  BitSet originalRowCluster = data.get(rowKeys.get(currentRowIdx)); // gets the first row that was created for this cluster
			  currentMinDist = Utility.distance(originalRowCluster, rowInEvaluation);
			  
			  if (currentMinDist<minDist){
				  minDist = currentMinDist;
				  minRowIdx = currentRowIdx;
				  currentClusterIdx = idxCluster;
			  }
		          
		  }
		  
		  minPair.put("MIN", minDist);
		  minPair.put("MIN_ROW_IDX", minRowIdx);
		  minPair.put("MIN_CLS_IDX", currentClusterIdx);
		  
		  return minPair ;
		
	}
		
}

	