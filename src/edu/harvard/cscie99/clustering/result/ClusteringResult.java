package edu.harvard.cscie99.clustering.result;

import java.io.BufferedWriter;
import java.io.FileWriter;
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

	/**
	 * Write the results to an output file with name "outputFilename"
	 * 
	 * @param outputFilename
	 * @throws IOException
	 */
	public void writeClusterLabels(String outputFilename) throws IOException{
		
		try (FileWriter outFile = new FileWriter(outputFilename);  
  
              BufferedWriter bWriter = new BufferedWriter(outFile)) {  
  
			  int idx = 0;
	          for (String label :rowLabels){
	          	bWriter.write(label + " = " +this.getClusterByIdx(idx) + "\n");
	          	//System.out.println(label + " -- > " + this.getClusterByIdx(idx));
	          	idx++;
	          }
              
  
        } catch (IOException e) {  
  
            e.printStackTrace();  
        }    

		
    		
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
