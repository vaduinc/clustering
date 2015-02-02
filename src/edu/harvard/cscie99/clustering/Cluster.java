package edu.harvard.cscie99.clustering;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.harvard.cscie99.clustering.algorithm.IClusterAlgo;
import edu.harvard.cscie99.clustering.algorithm.JarvisAlgoImpl;
import edu.harvard.cscie99.clustering.io.MatrixReader;
import edu.harvard.cscie99.clustering.result.ClusteringResult;

public class Cluster {

	private static String FILENAME = "./testdata/iris.txt";
	
	/**
	 * java –jar clustering.jar -fpfile [filename] -outpath [outpath] –algorithm kmeans –param1 [param1] –param2 [param2]... 
	 *  
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("loadData");
		Map<String, String> clusterParams = new HashMap<String, String>();
		
		clusterParams.put("minDistance","0.5");
		clusterParams.put("distanceMetric","Euclidian");
		clusterParams.put("numNeighbors","2"); // TODO make sure this and next line are validated commonNeighbors <= numNeighbors
		clusterParams.put("commonNeighbors","1");
		
        MatrixReader reader = new MatrixReader();
        try {
            reader.loadData(FILENAME);
            
            List<String> rowHeaders = reader.getRowHeaders();
            
            //IClusterAlgo algo = new LeaderAlgoImpl();
            IClusterAlgo algo = new JarvisAlgoImpl();
            ClusteringResult results = algo.cluster(reader.getRawMatrix(), clusterParams);
            
            int idx = 0;
            for (String label :rowHeaders){
            	System.out.println(label + " -- > " + results.getClusterByIdx(idx));
            	idx++;
            }
            
            
        } catch(IOException ioe) {
            fail("Failed to load file " + FILENAME);
        }
		
		
	}

}
