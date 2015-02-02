package edu.harvard.cscie99.clustering.algorithm;

import java.util.List;
import java.util.Map;

import edu.harvard.cscie99.clustering.result.ClusteringResult;

public interface IClusterAlgo {

	ClusteringResult cluster(double[][] data, Map<String,String> clusterParams);
	
	ClusteringResult cluster(Map<String, List<Integer>> data, Map<String,Object> clusterParams);
	
}
