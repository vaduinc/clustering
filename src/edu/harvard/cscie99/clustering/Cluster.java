package edu.harvard.cscie99.clustering;

import java.io.IOException;
import java.util.Map;

import edu.harvard.cscie99.clustering.algorithm.IClusterAlgo;
import edu.harvard.cscie99.clustering.algorithm.JarvisAlgoFPImpl;
import edu.harvard.cscie99.clustering.algorithm.JarvisAlgoMTXImpl;
import edu.harvard.cscie99.clustering.algorithm.LeaderAlgoFPImpl;
import edu.harvard.cscie99.clustering.algorithm.LeaderAlgoMTXImpl;
import edu.harvard.cscie99.clustering.io.FingerprintReader;
import edu.harvard.cscie99.clustering.io.IReader;
import edu.harvard.cscie99.clustering.io.MatrixReader;
import edu.harvard.cscie99.clustering.result.ClusteringResult;
import edu.harvard.cscie99.clustering.util.CommandLineParam;
import edu.harvard.cscie99.clustering.util.InputParamEnum;

/**
 *	Main class to execute clustering algorithms
 *
 *	java -jar clustering.jar [-mtxfile | -fpfile] myfile.txt -outpath [./output | @display ] -algorithm [leader | jarvis] -minDistance 0.5 -numNeighbors 2 -commonNeighbors 1
 * 
 * 		-algorithm must be one of the following values; leader or jarvis
 * 		-minDistance must be used with Leader algorithm
 *		-numNeighbors AND -commonNeighbors must be used with Jarvis algorithm, and numNeighbors >= commonNeighbors
 *
 *	java -jar clustering.jar -mtxfile ./HW1CodeData/testdata/iris.txt -outpath @display -algorithm leader -minDistance 0.5 
 * 	java -jar clustering.jar -mtxfile ./HW1CodeData/testdata/iris.txt -outpath @display -algorithm jarvis -numNeighbors 5 -commonNeighbors 1
 *  java -jar clustering.jar -fpfile ./HW1CodeData/testdata/bbb2_daylight.fp.txt -outpath @display -algorithm leader -minDistance 100 
 * 	java -jar clustering.jar -fpfile ./HW1CodeData/testdata/bbb2_daylight.fp.txt -outpath @display -algorithm jarvis -numNeighbors 10 -commonNeighbors 3
 * 
 */
public class Cluster {

	public static void main(String[] args) {

		Map<String, Object> clusterParams =null;

		try {
		
			clusterParams = CommandLineParam.setInputParameters(args);
			IReader reader = getReader(clusterParams);
			IClusterAlgo algorithm = getAlgorithm(clusterParams);
			ClusteringResult results = executeAlgorithm(clusterParams, reader, algorithm);
			
			// output results to a file or screen.
			results.writeClusterLabels(clusterParams.get(InputParamEnum.IN_OUTPATH.value()).toString());
            
        } catch(IOException ioe) {
            System.err.println("Error: Failed to load file " + clusterParams.get(InputParamEnum.IN_FILE.value()));
        } catch (Exception e){
        	System.err.println("Error: Something went terrible wrong!!! \n" + e);
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
			if ( clusterParams.get(InputParamEnum.IN_DATA_TYPE.value()).toString().equalsIgnoreCase(InputParamEnum.FP_TYPE) ){
				return new LeaderAlgoFPImpl();
				//return null;
			}else{
				return new LeaderAlgoMTXImpl();
			}
		}else{
			if ( clusterParams.get(InputParamEnum.IN_DATA_TYPE.value()).toString().equalsIgnoreCase(InputParamEnum.FP_TYPE) ){
				return new JarvisAlgoFPImpl();
			}else{
				return new JarvisAlgoMTXImpl();
			}
			
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
		
		if ( clusterParams.get(InputParamEnum.IN_DATA_TYPE.value()).toString().equalsIgnoreCase(InputParamEnum.FP_TYPE) ){
			results = algorithm.cluster(((FingerprintReader)reader).getFingerprintMap(), clusterParams);
		}else{
			//results = algorithm.cluster(((MatrixReader)reader).getRowHeaders(),((MatrixReader)reader).getRawMatrix() , clusterParams);
			clusterParams.put(InputParamEnum.DEF_LABELS.value(), ((MatrixReader)reader).getRowHeaders());
			results = algorithm.cluster(((MatrixReader)reader).getNormalizedMatrix() , clusterParams);
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
		Object inputFileName =  clusterParams.get(InputParamEnum.MTX_FILE_TYPE);
		
		if (inputFileName==null){
			// if the MTX parameter was not set then it must be a FPFile type
			inputFileName =  clusterParams.get(InputParamEnum.FP_FILE_TYPE);
			reader= new FingerprintReader();
			clusterParams.put(InputParamEnum.IN_DATA_TYPE.value(), InputParamEnum.FP_TYPE);
		}else{
			reader= new MatrixReader();
			clusterParams.put(InputParamEnum.IN_DATA_TYPE.value(), InputParamEnum.MTX_TYPE);
		}
		
		reader.loadData(inputFileName.toString());
		return reader;
	}
	
}
