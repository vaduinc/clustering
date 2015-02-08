package edu.harvard.cscie99.clustering.algorithm;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import edu.harvard.cscie99.clustering.util.Utility;

/**
 * Implementation of Leader algorithm using Map<String, BitSet> type
 * for the input data
 *
 */
public class LeaderAlgoFPImpl extends LeaderAlgorithm<Map<String, BitSet>> {

	@Override
	public  Number getDistance(Map<String, BitSet> data, List<String> rowKeys , int fromIdx, int toIdx){
		
		final BitSet rowInEvaluation = data.get(rowKeys.get(fromIdx));
		final BitSet originalRowCluster = data.get(rowKeys.get(toIdx)); // gets the first row that was created for this cluster
		
		return Utility.distance(originalRowCluster, rowInEvaluation);
		
	}

	@Override
	public List<String> getRowLabels(Map<String, BitSet> data,Map<String, Object> clusterParams) {
		
		List<String> rowKeys = new ArrayList<String>(data.keySet());
		Collections.sort(rowKeys); // Sorting is a MUST. Otherwise, the results will come in different order.
		
		return rowKeys;
	}


}