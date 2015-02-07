package edu.harvard.cscie99.clustering.util;

/**
 *	Enum to parse the command line input parameters
 *
 *	pos[0] 	Name of the input parameter
 *	pos[1] 	Whether the parameter is required or not
 *	pos[2] 	Regex to validate value of the input param.
 *	pos[3] 	Defines whether the parameter makes reference to 
 *			a specific algorithm (jarvis | leader) or if general (general)  
 *
 */

public enum InputParamEnum {

	IN_ALGO("-algorithm",true,"(leader|jarvis)","general" ),
	IN_FILE("file",true,null, "general" ),
	IN_OUTPATH("-outpath",true,null, "general" ),
	//IN_DIST_METRIC("-distanceMetric",false,null, "general" ),  // not in used yet
	IN_NUM_NEIGH("-numNeighbors",true,"\\d{1,2}", "jarvis" ),
	IN_COMM_NEIGH("-commonNeighbors", true,"\\d{1,2}", "jarvis" ),
	IN_MIN_DIST("-minDistance",true,"^[0-9]\\d*(\\.\\d+)?$", "leader" ), 
	;
  
    private final String value;
    private final Boolean required;
    private final String validation;
    private final String algo;

    InputParamEnum(String v, Boolean reqEnum, String validEnum, String algoEnum ) {
        value = v;
        required = reqEnum;
        validation = validEnum;
        algo = algoEnum;
    }

    public String getAlgo(){
    	return algo;
    }
    
    public String getValidation(){
    	return validation;
    }
    
    public String value() {
        return value;
    }

    public Boolean getReqEnum(){
    	return required;
    }
	
}