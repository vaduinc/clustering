package edu.harvard.cscie99.clustering;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import edu.harvard.cscie99.clustering.algorithm.IClusterAlgo;
import edu.harvard.cscie99.clustering.algorithm.JarvisAlgoImpl;
import edu.harvard.cscie99.clustering.algorithm.LeaderAlgoImpl;
import edu.harvard.cscie99.clustering.io.FingerprintReader;
import edu.harvard.cscie99.clustering.io.IReader;
import edu.harvard.cscie99.clustering.io.MatrixReader;
import edu.harvard.cscie99.clustering.result.ClusteringResult;
import edu.harvard.cscie99.clustering.util.InputParamEnum;

public class Cluster {

	private static String PARAM_GENERAL_TYPE = "general";
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
		
			clusterParams = setInputParameters(args);
			IReader reader = getReader(clusterParams);
			IClusterAlgo algorithm = getAlgorithm(clusterParams);
			ClusteringResult results = executeAlgorithm(clusterParams, reader, algorithm);
			
	        System.out.println(results.toString());
            
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
			results = algorithm.cluster(((MatrixReader)reader).getRowHeaders(),((MatrixReader)reader).getRawMatrix() , clusterParams);
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
	
	
	/**
     * Uses the regex to check whether the value is valid or not.
     * 
     * @param constraintRegex
     * @param value
     * @return true/false
     */
	private static boolean validateInputType(String constraintRegex, Object value){
    	
    	try{
    		Pattern pattern = Pattern.compile(constraintRegex);
			Matcher matcher = pattern.matcher(value.toString().trim());
			if(!matcher.matches()){
            	return false;
            }
		}catch (PatternSyntaxException pex){
	        return false;
		}
    	
    	return true;
    }
	

	/**
	 * TODO description
	 * 
	 * @param args
	 * @param inputParam
	 * @return Object[] [0] input parameter name, [1] input parameter value 
	 */
	public static Object[] getValueFromInput(String[] args, InputParamEnum inputParam){
		
		int idxParam = 0;
		for(String inValue : args){
			
			if (inValue.endsWith(inputParam.value()) && inValue.startsWith("-")){
				
				if ((idxParam + 1) < args.length){
				
					if (inputParam.getValidation()!=null){
						if (validateInputType(inputParam.getValidation(),args[idxParam + 1])){
							return new String[] { inValue,args[idxParam + 1]};
						}else{
							return null;
						}
						
					}else{
						return new String[] { inValue,args[idxParam + 1]}; // there is no validation, then just return the value
					}
				}else{
					// There are no more input values
					
					if (inputParam.getReqEnum()){
						return null;
					}else{
						return new String[] { inValue,""};
					}
				}
			}
			idxParam++;
		}
		
		return null;
	}
	

	
	/**
	 * TODO description
	 * 
	 * @param clusterParams
	 * @param args
	 * @param type
	 * @throws Exception
	 */
	public static void setInputParameters(Map<String, Object> clusterParams,String[] args, String type) throws Exception{
		
		Object[] nameValuePair = null;
		
		for (InputParamEnum inputParam :InputParamEnum.values()){
			
			// Make sure the parameters is specific for the selected algorithm or is generic
			if (type.equalsIgnoreCase(inputParam.getAlgo())){
		
				nameValuePair = getValueFromInput(args,inputParam);
				if (nameValuePair!=null){
					clusterParams.put(nameValuePair[0].toString(), nameValuePair[1]);
				}else{
					throw new Exception("Error: Invalid input parameter " + inputParam.name() );
				}
			}
		}
	}
	
	
	/**
	 * TODO description
	 * 
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> setInputParameters(String[] args) throws Exception{
		
		Map<String, Object> clusterParams = new HashMap<String, Object>();
		
		// Need at least 4 input parameters to run Leader algorithm.
		if ( args.length<=3){
			throw new Exception("Error: Missing input parameters.");
		}else{

			// get the general input parameter values
			setInputParameters(clusterParams, args, PARAM_GENERAL_TYPE);
			
			// get the general input parameter values
			setInputParameters(clusterParams, args, (String)clusterParams.get(InputParamEnum.IN_ALGO.value()));
			

		}
		
		return clusterParams;
	}
	
}
