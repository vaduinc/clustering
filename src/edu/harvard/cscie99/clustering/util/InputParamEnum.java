package edu.harvard.cscie99.clustering.util;

public enum InputParamEnum {

//	public static String IN_MTX_FILE = "-mtxfile";
//	public static String IN_FP_FILE = "-fpfile";
//	public static String IN_OUTPATH = "-outpath";
//	public static String IN_ALGO = "-algorithm";
//	public static String IN_DIST_METRIC = "-distanceMetric";
//	public static String IN_MIN_DIST = "-minDistance";
//	public static String IN_NUM_NEIGH = "-numNeighbors";
//	public static String IN_COMM_NEIGH = "-commonNeighbors";
	
	IN_ALGO("-algorithm",true,"(leader|jarvis)","general" ),
	IN_FILE("file",true,null, "general" ),
	IN_OUTPATH("-outpath",true,null, "general" ),
	IN_DIST_METRIC("-distanceMetric",true,null, "general" ),
	IN_NUM_NEIGH("-numNeighbors",true,"\\d{2}", "jarvis" ),
	IN_COMM_NEIGH("-commonNeighbors", true,"\\d{2}", "jarvis" ),
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