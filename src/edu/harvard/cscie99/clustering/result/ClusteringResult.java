package edu.harvard.cscie99.clustering.result;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClusteringResult {

	private List<Integer> rowCluster; 
	
	public ClusteringResult(int rows) {
		rowCluster = new ArrayList<Integer>(rows);
	}

	public void writeClusterLabels(String outputFilename) throws IOException{
		
	}
	
	public void addClusterToLabel(Integer clusterId){
		rowCluster.add(clusterId);
	}
	
	public void addClusterToLabelByIndex(Integer clusterId, int idx){
		rowCluster.add(idx, clusterId);
	}

	public Integer getClusterByIdx(int idx){
		return rowCluster.get(idx);
	}
	
}
