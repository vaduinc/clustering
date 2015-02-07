package edu.harvard.cscie99.clustering.io;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.harvard.cscie99.clustering.util.Utility;


public class AlgorithmTest {
    
	 public final static double EPSILON = 1E-4;
	private String FILENAME = "./testdata/iris.txt";
    
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
    	
    	// create 2 bitsets
	      BitSet point1 = new BitSet();
	      BitSet point2 = new BitSet();
		
	   // assign values to bitset1
	      point1.set(2);
	      point1.set(4);
	      point1.set(5);
	      point1.set(10);
	      point1.set(12);
	      point1.set(19);
	      point1.set(26);

	      // assign values to bitset2
	      point2.set(2);
	      point2.set(4);
	      point2.set(9);
	      point2.set(12);
	      point2.set(13);
	      point2.set(20);
	      point2.set(25);
	      point2.set(26);
    	
	      int distance = Utility.distance(point1,point2);
    	
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
    	
    	// create 2 bitsets
	      double[] point1 = { 5.1 , 3.5 , 1.4 , 0.2 };
	      double[] point2 = { 4.9 , 3 , 1.4 , 0.2  };
		
	      double distance = Utility.distance(point1,point2);
    	
	      assertTrue(Math.abs(distance - 0.5385164807134502 ) < EPSILON);
	      
    }
    
    
   
    
}
