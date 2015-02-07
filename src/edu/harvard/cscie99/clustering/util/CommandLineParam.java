package edu.harvard.cscie99.clustering.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import edu.harvard.cscie99.clustering.Cluster;

/**
 * Simple helper to validate the command line input parameters and 
 * insert them into a Map. 
 *
 */
public class CommandLineParam {

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
			CommandLineParam.setInputParameters(clusterParams, args, Cluster.PARAM_GENERAL_TYPE);
			
			// get the general input parameter values
			CommandLineParam.setInputParameters(clusterParams, args, (String)clusterParams.get(InputParamEnum.IN_ALGO.value()));
	
		}
		
		return clusterParams;
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
		
				nameValuePair = CommandLineParam.getValueFromInput(args,inputParam);
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
	 * @param inputParam
	 * @return Object[] [0] input parameter name, [1] input parameter value 
	 */
	public static Object[] getValueFromInput(String[] args, InputParamEnum inputParam){
		
		int idxParam = 0;
		for(String inValue : args){
			
			if (inValue.endsWith(inputParam.value()) && inValue.startsWith("-")){
				
				if ((idxParam + 1) < args.length){
				
					if (inputParam.getValidation()!=null){
						if (CommandLineParam.validateInputType(inputParam.getValidation(),args[idxParam + 1])){
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
	 * Uses the regex to check whether the value is valid or not.
	 * 
	 * @param constraintRegex
	 * @param value
	 * @return true/false
	 */
	public static boolean validateInputType(String constraintRegex, Object value){
		
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

}
