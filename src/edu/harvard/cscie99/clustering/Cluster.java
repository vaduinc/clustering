package edu.harvard.cscie99.clustering;

import java.io.IOException;
import java.util.Map;

import edu.harvard.cscie99.clustering.algorithm.IClusterAlgo;
import edu.harvard.cscie99.clustering.algorithm.JarvisAlgoImpl;
import edu.harvard.cscie99.clustering.algorithm.LeaderAlgoImpl;
import edu.harvard.cscie99.clustering.io.FingerprintReader;
import edu.harvard.cscie99.clustering.io.IReader;
import edu.harvard.cscie99.clustering.io.MatrixReader;
import edu.harvard.cscie99.clustering.result.ClusteringResult;
import edu.harvard.cscie99.clustering.util.CommandLineParam;
import edu.harvard.cscie99.clustering.util.InputParamEnum;

public class Cluster {

	public static String PARAM_GENERAL_TYPE = "general";
	private static String MTX_FILE_TYPE = "-mtxfile";
	private static String FP_FILE_TYPE = "-fpfile";

	/**
	 * java -jar -clustering.jar [-mtxfile|-fpfile] myfile.txt -outpath ./output -algorithm kmeans -k 3 -distanceMetric Euclidian -initialMethod initialIndices -initindices 2,3,5
	 *  
	 * @param args
	 */
	public static void main(String[] args) {

		Map<String, Object> clusterParams =null;

		try {
		
			clusterParams = CommandLineParam.setInputParameters(args);
			IReader reader = getReader(clusterParams);
			IClusterAlgo algorithm = getAlgorithm(clusterParams);
			ClusteringResult results = executeAlgorithm(clusterParams, reader, algorithm);
			
			results.writeClusterLabels(clusterParams.get(InputParamEnum.IN_OUTPATH.value()).toString());
			
	        //System.out.println(results.toString());
            
        } catch(IOException ioe) {
            System.err.println("Error: Failed to load file " + clusterParams.get(InputParamEnum.IN_FILE.value()));
        } catch (Exception ex){
        	System.err.println("Error: Something went terrible wrong!!! " + ex.toString());
        }
		
		
	}
	

	/**
	 * Factory method that creates the algorithm to be executed based on
	 * the input parmeters.
	 * 
	 * @param clusterParams
	 * @return
	 * @throws IOException
	 */
	public static IClusterAlgo getAlgorithm(Map<String, Object> clusterParams) {
		
		if ( clusterParams.get(InputParamEnum.IN_ALGO.value()).toString().equalsIgnoreCase("leader") ){
			return new LeaderAlgoImpl();
		}else{
			return new JarvisAlgoImpl();
		}
	}
	
	
	/**
	 * Execute the selected IClusterAlgo
	 * 
	 * @param clusterParams
	 * @param reader
	 * @param algorithm
	 * @return ClusteringResult
	 */
	public static ClusteringResult executeAlgorithm(Map<String, Object> clusterParams,IReader reader,IClusterAlgo algorithm) {
		
		ClusteringResult results = null;
		
		if ( clusterParams.get("-dataType").toString().equalsIgnoreCase("fp") ){
			results = algorithm.cluster(((FingerprintReader)reader).getFingerprintMap(), clusterParams);
		}else{
			//results = algorithm.cluster(((MatrixReader)reader).getRowHeaders(),((MatrixReader)reader).getRawMatrix() , clusterParams);
			results = algorithm.cluster(((MatrixReader)reader).getRowHeaders(),((MatrixReader)reader).getNormalizedMatrix() , clusterParams);
		}
		
        return results;
	}
	
	
	/**
	 * Load data form file based on the input parameter -mtxfile or fpfile.
	 * Sets the type of data to be used, either; matrix or fingerprint
	 * 
	 * @param clusterParams
	 * @return IReader 
	 * @throws IOException
	 */
	public static IReader getReader(Map<String, Object> clusterParams) throws IOException{
		
		IReader reader = null;
		Object inputFileName =  clusterParams.get(MTX_FILE_TYPE);
		
		if (inputFileName==null){
			// if the MTX parameter was not set then it must be a FPFile type
			inputFileName =  clusterParams.get(FP_FILE_TYPE);
			reader= new FingerprintReader();
			clusterParams.put("-dataType", "fp");
		}else{
			reader= new MatrixReader();
			clusterParams.put("-dataType", "mtx");
		}
		
		reader.loadData(inputFileName.toString());
		return reader;
	}
	
}
