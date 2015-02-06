package edu.harvard.cscie99.clustering.result;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClusteringResult {

	private List<Integer> rowCluster; 
	private List<String> rowLabels;
	
	public ClusteringResult(int rows,List<String> rowLabels) {
		this.rowCluster = new ArrayList<Integer>(rows);
		this.rowLabels = rowLabels;
	}

	public void writeClusterLabels(String outputFilename) throws IOException{
		
	}
	
	public void addClusterToLabel(Integer clusterId){
		rowCluster.add(clusterId);
	}
	
	public void setClusterToLabelByIndex(int idx, Integer clusterId ){
		rowCluster.set(idx, clusterId);
	}

	public Integer getClusterByIdx(int idx){
		return rowCluster.get(idx);
	}
	
	/**
	 * To help when debugging.
	 */
	public String toString(){
		
		StringBuffer sbf= new StringBuffer("");
		int idx = 0;
        for (String label :rowLabels){
        	sbf.append(label + " -- > " + this.getClusterByIdx(idx) + "\n");
        	idx++;
        }
		
		return sbf.toString();
		
	}
	
}
