package edu.harvard.cscie99.clustering.algorithm;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import edu.harvard.cscie99.clustering.util.Utility;

public class JarvisAlgoFPImpl extends JarvisAlgorithm<Map<String, BitSet>> {

	@Override
	public  Number getDistance(Map<String, BitSet> data, List<String> rowKeys , int fromIdx, int toIdx){
		
		return Utility.distance(data.get(rowKeys.get(fromIdx)), data.get(rowKeys.get(toIdx)));
		
	}

	@Override
	public List<String> getRowLabels(Map<String, BitSet> data,
			Map<String, Object> clusterParams) {
		
		List<String> rowKeys = new ArrayList<String>(data.keySet());
		Collections.sort(rowKeys); // Sorting is a MUST. Otherwise, the results will come in different order.
		
		return rowKeys;
	}

}