package edu.harvard.cscie99.clustering.algorithm;

import java.util.BitSet;
import java.util.List;
import java.util.Map;

import edu.harvard.cscie99.clustering.result.ClusteringResult;

public interface IClusterAlgo {

	ClusteringResult cluster(List<String> rowLabels,double[][] data, Map<String,Object> clusterParams);
	
	ClusteringResult cluster(Map<String, BitSet> data, Map<String,Object> clusterParams);
	
}
