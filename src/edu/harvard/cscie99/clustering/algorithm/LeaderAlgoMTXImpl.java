package edu.harvard.cscie99.clustering.algorithm;

import java.util.List;
import java.util.Map;

import edu.harvard.cscie99.clustering.util.InputParamEnum;
import edu.harvard.cscie99.clustering.util.Utility;

/**
 * Implementation of Leader algorithm using double[][] type
 * for the input data
 *
 */
public class LeaderAlgoMTXImpl extends LeaderAlgorithm<double[][]> {

	@Override
	public  Number getDistance(double[][] data, List<String> rowKeys , int fromIdx, int toIdx){
		
		final double[] rowInEvaluation = data[fromIdx];
		final double[] originalRowCluster = data[toIdx]; // gets the first row that was created for this cluster
 	    
		return Utility.distance(originalRowCluster, rowInEvaluation);
		
	}

	@Override
	public List<String> getRowLabels(double[][] data,Map<String, Object> clusterParams) {
		
		final List<String> rowKeys = (List<String>) clusterParams.get(InputParamEnum.DEF_LABELS.value());
		
		return rowKeys;
	}


}