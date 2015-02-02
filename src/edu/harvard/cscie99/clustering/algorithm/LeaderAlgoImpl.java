package edu.harvard.cscie99.clustering.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.harvard.cscie99.clustering.result.ClusteringResult;

public class LeaderAlgoImpl implements IClusterAlgo {

	@Override
	public ClusteringResult cluster(double[][] data,
			Map<String, String> clusterParams) {
		// TODO Auto-generated method stub
		
		try {
			final double minDistance = Double.valueOf(clusterParams.get("minDistance")); // TODO check for exception
			final String distanceMetric = clusterParams.get("distanceMetric"); //{"Euclidian"}
			final int rows = data.length;
			
			ClusteringResult results = new ClusteringResult(rows);
			Map<String, Number> minPair;
			List<List<Integer>> clusters = new ArrayList<List<Integer>>();
			List<Integer> clustersRows = new ArrayList<Integer>();
			//double current[] = data[0]; 
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
			  currentMinDist = LeaderAlgoImpl.distance(originalRowCluster, rowInEvaluation);
			  
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
	
	
	/**
	 * TODO move to UTIL static -- throws exception
	 * 
	 * @param point1
	 * @param point2
	 * @return double 
	 */
	private static double distance(double[] point1, double[] point2)
			throws Exception {
		
		// Make sure both have the same length 
		if (point1.length == point2.length) {
			Double sum = 0D;
			for (int i = 0; i < point1.length; i++) {
				sum = sum + (point2[i] - point1[i]) * (point2[i] - point1[i]);
			}
			return Math.sqrt(sum);
		} else {
			throw new Exception("Error: array lengths are not equal");
		}
	}
	

	@Override
	public ClusteringResult cluster(Map<String, List<Integer>> data,
			Map<String, Object> clusterParams) {
		// TODO Auto-generated method stub
		return null;
	}

}

	