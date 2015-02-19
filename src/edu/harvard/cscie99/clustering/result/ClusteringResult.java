package edu.harvard.cscie99.clustering.result;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.harvard.cscie99.clustering.util.InputParamEnum;

public class ClusteringResult {

	private List<Integer> rowCluster; 
	private List<String> rowLabels;
	
	public ClusteringResult(int rows,List<String> rowLabels) {
		this.rowCluster = new ArrayList<Integer>(rows);
		this.rowLabels = rowLabels;
	}

	/**
	 * Write the results to an output file with name "outputFilename"
	 * If the input parameter "outputFilename" == "@display" then
	 * it will just printout the results to the screen/display and
	 * will not create a file.
	 * 
	 * @param outputFilename
	 * @throws IOException
	 */
	public void writeClusterLabels(String outputFilename) throws IOException{

		if(outputFilename.equalsIgnoreCase(InputParamEnum.OUTPUT_DISPLAY)){
			// does not create an output file. It just printout the results on screen
			System.out.println(this.toString());	
		}else{
		
			BufferedWriter bWriter =null;
			try {
			   	  FileWriter outFile = new FileWriter(outputFilename);  
			
	              bWriter = new BufferedWriter(outFile);  
	  
				  int idx = 0;
		          for (String label :rowLabels){
		          	bWriter.write(label + " = " +this.getClusterByIdx(idx) + "\n");
		          	idx++;
		          }
	  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        } finally {
	            if (bWriter != null) {
	            	bWriter.close();
	            }
	        }
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

	public List<Integer> getRowCluster() {
		return rowCluster;
	}
	
	/**
	 * Optional output 
	 * Just to help when debugging.
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
