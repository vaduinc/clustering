package edu.harvard.cscie99.clustering.algorithm;

import java.util.Map;

import edu.harvard.cscie99.clustering.result.ClusteringResult;

/**
 * Interface the must be implemented by all the clustering algorithms
 *
 * @param <E> is used for the data input type. Either <double[][]> or Map<String, BitSet>
 */
public interface IClusterAlgo<E> {

	public ClusteringResult cluster(E data, Map<String,Object> clusterParams);
	
}
