package edu.harvard.cscie99.clustering.algorithm;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import edu.harvard.cscie99.clustering.util.Utility;

public class JarvisAlgoMTXImpl extends JarvisAlgorithm<double[][] > {

	@Override
	public  Number getDistance(double[][] data, List<String> rowKeys , int fromIdx, int toIdx){
		
		
		Double toFrom = Utility.distance(data[fromIdx], data[toIdx]);
		
		return new BigDecimal(toFrom).setScale(PRECISION,BigDecimal.ROUND_HALF_UP).doubleValue();
		
	}

	@Override
	public List<String> getRowLabels(double[][] data, Map<String, Object> clusterParams) {
		
		List<String> rowKeys = (List<String>) clusterParams.get("rowlabels");
		
		return rowKeys;
	}

}