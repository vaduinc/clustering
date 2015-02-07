package edu.harvard.cscie99.clustering.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.harvard.cscie99.clustering.algorithm.JarvisAlgoFPImpl;
import edu.harvard.cscie99.clustering.algorithm.JarvisAlgoMTXImpl;
import edu.harvard.cscie99.clustering.result.ClusteringResult;
import edu.harvard.cscie99.clustering.util.CommandLineParam;
import edu.harvard.cscie99.clustering.util.InputParamEnum;
import edu.harvard.cscie99.clustering.util.Utility;

public class AlgorithmTest {

	public final static double EPSILON = 1E-4;
	
	Map<String, BitSet> dataFP; 
	
	double[][] dataMTX;
	
	List<String> rowLabels;

	public AlgorithmTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
		
		dataFP = new HashMap<String, BitSet>();

		BitSet row1 = new BitSet();
		BitSet row2 = new BitSet();
		BitSet row3 = new BitSet();
		BitSet row4 = new BitSet();

		// assign values to row1
		row1.set(1);
		row1.set(3);
		dataFP.put("ArticleA", row1);

		// assign values to row2
		row2.set(1);
		row2.set(4);
		dataFP.put("ArticleB", row2);

		// assign values to row3
		row3.set(2);
		row3.set(3);
		row3.set(5);
		dataFP.put("ArticleC", row3);

		// assign values to row4
		row4.set(5);
		row4.set(6);
		dataFP.put("ArticleD", row4);
		
		///////////////////////////////////////////////////
		
		dataMTX = new double[8][2];
		
		dataMTX[0] = new double[] {12,6};
		dataMTX[1] = new double[] {15,16};
		dataMTX[2] = new double[] {18,17};
		dataMTX[3] = new double[] {10,8};
		dataMTX[4] = new double[] {8,7};
		dataMTX[5] = new double[] {9,6};
		dataMTX[6] = new double[] {12,9};
		dataMTX[7] = new double[] {20,18};
		
		rowLabels = new ArrayList<String>(8);
		rowLabels.add("row1");
		rowLabels.add("row2");
		rowLabels.add("row3");
		rowLabels.add("row4");
		rowLabels.add("row5");
		rowLabels.add("row6");
		rowLabels.add("row7");
		rowLabels.add("row8");
		
		///////////////////////////////////////////////////
	}

	@After
	public void tearDown() {
	}


	/**
	 * Test the distance from 2 bitsets.
	 * It is called from cluster algorithm when 
	 * fingerprint input is used. 
	 */
	@Test
	public void testFingerprintDistance(){

		// create 2 rows
		BitSet row1 = new BitSet();
		BitSet row2 = new BitSet();

		// assign values to point1
		row1.set(2);
		row1.set(4);
		row1.set(5);
		row1.set(10);
		row1.set(12);
		row1.set(19);
		row1.set(26);

		// assign values to point2
		row2.set(2);
		row2.set(4);
		row2.set(9);
		row2.set(12);
		row2.set(13);
		row2.set(20);
		row2.set(25);
		row2.set(26);

		int distance = Utility.distance(row1,row2);

		assertEquals(distance,7);
	}



	/**
	 * Test the distance between 2 points.
	 * It is called from cluster algorithm when 
	 * Matrix input is used. 
	 * @throws Exception 
	 */
	@Test
	public void testMatrixDistance() throws Exception{

		// create 2 points
		double[] point1 = { 5.1 , 3.5 , 1.4 , 0.2 };
		double[] point2 = { 4.9 , 3 , 1.4 , 0.2  };

		double distance = Utility.distance(point1,point2);

		assertTrue(Math.abs(distance - 0.5385164807134502 ) < EPSILON);

	}


	/**
	 * Test the following fingerprint data using the following 
	 * input parameters:
	 * 
	 * "-fpfile", "DATA_HARD_CODED", "-outpath", "@display", "-algorithm", "jarvis",	"-numNeighbors", "3", "-commonNeighbors", "2" 
	 *
	 * 	DATA
	 * 		ArticleA	1,3
	 *		ArticleB	1,4
	 *		ArticleC	2,3,5
	 *		ArticleD	5,6
	 *
	 *	Validates the end results is:
	 * 		ArticleA -- > 1
	 *		ArticleB -- > 1
	 *		ArticleC -- > 3
	 *		ArticleD -- > 3
	 *
	 * @throws Exception 
	 */
	@Test
	public void testJarvisFingerprintOuput1() throws Exception {

		JarvisAlgoFPImpl algo = new JarvisAlgoFPImpl();
		
		// Default/initial input parameters set.
		String[] args = new String[] { "-fpfile", "./testdata/fp_test.txt",
				"-outpath", "@display", "-algorithm", "jarvis",
				"-numNeighbors", "2", "-commonNeighbors", "1" };
		Map<String, Object> clusterParams = CommandLineParam.setInputParameters(args);
		clusterParams.put("-dataType", "fp");

		ClusteringResult results = algo.cluster(dataFP, clusterParams);
		
		// Optional print results to console.
		results.writeClusterLabels(clusterParams.get(InputParamEnum.IN_OUTPATH.value()).toString());
		
		Set<Integer> finalClusters = new HashSet<Integer>(results.getRowCluster());

		assertTrue(finalClusters.size()==2);
		assertTrue(finalClusters.contains(new Integer(1)));
		assertTrue(finalClusters.contains(new Integer(3)));

	}

	
	/**
	 * Test the following fingerprint data using the following 
	 * input parameters:
	 * 
	 * "-fpfile", "DATA_HARD_CODED", "-outpath", "@display", "-algorithm", "jarvis",	"-numNeighbors", "4", "-commonNeighbors", "3" 
	 *
	 * 	DATA
	 * 		ArticleA	1,3
	 *		ArticleB	1,4
	 *		ArticleC	2,3,5
	 *		ArticleD	5,6
	 *
	 *	Validates the end results is:
	 * 		ArticleA -- > 1
	 *		ArticleB -- > 2
	 *		ArticleC -- > 3
	 *		ArticleD -- > 4
	 *
	 * @throws Exception 
	 */
	@Test
	public void testJarvisFingerprintOuput2() throws Exception {

		JarvisAlgoFPImpl algo = new JarvisAlgoFPImpl();

		// Default/initial input parameters set.
		String[] args = new String[] { "-fpfile", "./testdata/fp_test.txt",
				"-outpath", "@display", "-algorithm", "jarvis",
				"-numNeighbors", "4", "-commonNeighbors", "3" };
		
		Map<String, Object> clusterParams = CommandLineParam.setInputParameters(args);
		clusterParams.put("-dataType", "fp");

		ClusteringResult results = algo.cluster(dataFP, clusterParams);
		
		// Optional print results to console.
		results.writeClusterLabels(clusterParams.get(InputParamEnum.IN_OUTPATH.value()).toString());
		
		Set<Integer> finalClusters = new HashSet<Integer>(results.getRowCluster());

		assertTrue(finalClusters.size()==4);
		assertTrue(finalClusters.contains(new Integer(1)));
		assertTrue(finalClusters.contains(new Integer(2)));
		assertTrue(finalClusters.contains(new Integer(3)));
		assertTrue(finalClusters.contains(new Integer(4)));

	}
	
	
	/**
	 * Test the following matrix data using the following 
	 * input parameters:
	 * 
	 * "-mtxfile", "DATA_HARD_CODED", "-outpath", "@display", "-algorithm", "jarvis","-numNeighbors", "5", "-commonNeighbors", "3" 
	 *
	 * 	DATA
	 * 				   C    V
	 * 		row1 -- > 12 ,  6
	 *		row2 -- > 15 , 16
	 *		row3 -- > 18 , 17
	 *		row4 -- > 10 ,  8
	 *		row5 -- >  8 ,  7
	 *		row6 -- >  9 ,  6
	 *		row7 -- > 12 ,  9
	 *		row8 -- > 20 , 18
	 *
	 *	Validates the end results is:
	 * 		row1 -- > 1
	 *		row2 -- > 2
	 *		row3 -- > 2
	 *		row4 -- > 1
	 *		row5 -- > 1
	 *		row6 -- > 1
	 *		row7 -- > 1
	 *		row8 -- > 2
	 *
	 * @throws Exception 
	 */
	@Test
	public void testJarvisMatrixOuput1() throws Exception {

		JarvisAlgoMTXImpl algo = new JarvisAlgoMTXImpl();

		// Default/initial input parameters set.
		String[] args = new String[] { "-mtxfile", "DATA_HARD_CODED",
				"-outpath", "@display", "-algorithm", "jarvis",
				"-numNeighbors", "5", "-commonNeighbors", "3" };
		
		Map<String, Object> clusterParams = CommandLineParam.setInputParameters(args);
		clusterParams.put("-dataType", "mtx");
		clusterParams.put("rowlabels",rowLabels);

		ClusteringResult results = algo.cluster(dataMTX, clusterParams);
		
		// Optional print results to console.
		results.writeClusterLabels(clusterParams.get(InputParamEnum.IN_OUTPATH.value()).toString());
		
		Set<Integer> finalClusters = new HashSet<Integer>(results.getRowCluster());

		assertTrue(finalClusters.size()==2);
		
	}

}
